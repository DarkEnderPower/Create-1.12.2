package darkenderhilda.create.content.kinetics.saw;

import darkenderhilda.create.foundation.utility.AbstractBlockBreakQueue;
import darkenderhilda.create.foundation.utility.Iterate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TreeCutter {

    public static final Tree NO_TREE =
            new Tree(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());


    @Nonnull
    public static Tree findTree(@Nullable World reader, BlockPos pos, IBlockState brokenState) {
        if (reader == null) {
            return NO_TREE;
        }

        List<BlockPos> logs = new ArrayList<>();
        List<BlockPos> leaves = new ArrayList<>();
        List<BlockPos> attachments = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        List<BlockPos> frontier = new LinkedList<>();

        IBlockState stateAbove = reader.getBlockState(pos.up());

        // Sugar Cane, Cactus
        if (isVerticalPlant(brokenState)) {
            if (!isVerticalPlant(stateAbove))
                return NO_TREE;

            logs.add(pos.up());
            for (int i = 1; i < reader.getHeight(); i++) {
                BlockPos current = pos.up(i);
                if (!isVerticalPlant(reader.getBlockState(current)))
                    break;
                logs.add(current);
            }
            Collections.reverse(logs);
            return new Tree(logs, leaves, attachments);
        }

        // Chorus
        if (isChorus(brokenState)) {
            if (!isChorus(stateAbove))
                return NO_TREE;

            frontier.add(pos.up());
            while (!frontier.isEmpty()) {
                BlockPos current = frontier.remove(0);
                visited.add(current);
                logs.add(current);
                for (EnumFacing direction : Iterate.directions) {
                    BlockPos offset = current.offset(direction);
                    if (visited.contains(offset))
                        continue;
                    if (!isChorus(reader.getBlockState(offset)))
                        continue;
                    frontier.add(offset);
                }
            }
            Collections.reverse(logs);
            return new Tree(logs, leaves, attachments);
        }

        // Regular Tree
        if (!validateCut(reader, pos))
            return NO_TREE;

        visited.add(pos);
        BlockPos.getAllInBox(pos.add(-1, 0, -1), pos.add(1, 1, 1))
                .forEach(p -> frontier.add(new BlockPos(p)));

        // Find all logs & roots
        boolean hasRoots = false;
        while (!frontier.isEmpty()) {
            BlockPos currentPos = frontier.remove(0);
            if (!visited.add(currentPos))
                continue;

            IBlockState currentState = reader.getBlockState(currentPos);
            if (!isLog(currentState))
                continue;
            logs.add(currentPos);
            forNeighbours(currentPos, visited, SearchDirection.UP, p -> frontier.add(new BlockPos(p)));
        }

        visited.clear();
        visited.addAll(logs);
        frontier.addAll(logs);

        // Find all leaves
        while (!frontier.isEmpty()) {
            BlockPos currentPos = frontier.remove(0);
            if (!logs.contains(currentPos))
                if (visited.contains(currentPos))
                    continue;
            visited.add(currentPos);

            IBlockState blockState = reader.getBlockState(currentPos);
            boolean isLog = isLog(blockState);
            boolean isLeaf = isLeaf(blockState);

            if (!isLog && !isLeaf)
                continue;
            if (isLeaf)
                leaves.add(currentPos);

            for (EnumFacing direction : EnumFacing.values()) {
                BlockPos offset = currentPos.offset(direction);
                if (visited.contains(offset))
                    continue;
                IBlockState state = reader.getBlockState(offset);
                if (isLeaf(state))
                    frontier.add(offset);
            }
        }

        return new Tree(logs, leaves, attachments);

    }

    public static boolean isChorus(IBlockState stateAbove) {
        return stateAbove.getBlock() == Blocks.CHORUS_PLANT || stateAbove.getBlock() == Blocks.CHORUS_FLOWER;
    }

    public static boolean isVerticalPlant(IBlockState stateAbove) {
        Block block = stateAbove.getBlock();
        if (block == Blocks.CACTUS)
            return true;
        return block == Blocks.REEDS;
    }

    private static boolean validateCut(World reader, BlockPos pos) {
        Set<BlockPos> visited = new HashSet<>();
        List<BlockPos> frontier = new LinkedList<>();
        frontier.add(pos);
        frontier.add(pos.up());
        int posY = pos.getY();

        while (!frontier.isEmpty()) {
            BlockPos currentPos = frontier.remove(0);
            BlockPos belowPos = currentPos.down();

            visited.add(currentPos);
            boolean lowerLayer = currentPos.getY() == posY;

            IBlockState currentState = reader.getBlockState(currentPos);
            IBlockState belowState = reader.getBlockState(belowPos);

            if (!isLog(currentState))
                continue;
            if (!lowerLayer && !pos.equals(belowPos) && (isLog(belowState)))
                return false;

            for (EnumFacing direction : Iterate.directions) {
                if (direction == EnumFacing.DOWN)
                    continue;
                if (direction == EnumFacing.UP && !lowerLayer)
                    continue;
                BlockPos offset = currentPos.offset(direction);
                if (visited.contains(offset))
                    continue;
                frontier.add(offset);
            }
        }

        return true;
    }

    private static void forNeighbours(BlockPos pos, Set<BlockPos> visited, SearchDirection direction,
                                      Consumer<BlockPos> acceptor) {
        List <BlockPos> blockPos = new ArrayList<>();
        BlockPos.getAllInBox(pos.add(-1, direction.minY, -1), pos.add(1, direction.maxY, 1))
                .forEach(blockPos::add);
        blockPos.stream().filter(((Predicate<BlockPos>) visited::contains).negate())
                .forEach(acceptor);
    }

    private static boolean isLog(IBlockState state) {
        return state.getBlock() == Blocks.LOG;
    }

    private static boolean isLeaf(IBlockState state) {
        return state.getBlock() == Blocks.LEAVES;
    }

    private enum SearchDirection {
        UP(0, 1), DOWN(-1, 0), BOTH(-1, 1);

        int minY;
        int maxY;

        private SearchDirection(int minY, int maxY) {
            this.minY = minY;
            this.maxY = maxY;
        }
    }

    public static class Tree extends AbstractBlockBreakQueue {
        private final List<BlockPos> logs;
        private final List<BlockPos> leaves;
        private final List<BlockPos> attachments;

        public Tree(List<BlockPos> logs, List<BlockPos> leaves, List<BlockPos> attachments) {
            this.logs = logs;
            this.leaves = leaves;
            this.attachments = attachments;
        }

        @Override
        public void destroyBlocks(World world, ItemStack toDamage, @Nullable EntityPlayer playerEntity,
                                  BiConsumer<BlockPos, ItemStack> drop) {
            attachments.forEach(makeCallbackFor(world, 1 / 32f, toDamage, playerEntity, drop));
            logs.forEach(makeCallbackFor(world, 1 / 2f, toDamage, playerEntity, drop));
            leaves.forEach(makeCallbackFor(world, 1 / 8f, toDamage, playerEntity, drop));
        }
    }
}
