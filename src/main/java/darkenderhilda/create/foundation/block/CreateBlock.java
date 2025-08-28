package darkenderhilda.create.foundation.block;

import com.mojang.realmsclient.util.Pair;
import darkenderhilda.create.AllShapes;
import darkenderhilda.create.Create;
import darkenderhilda.create.foundation.utils.WorldUtils;
import darkenderhilda.create.foundation.shapes.ExtendedShape;
import darkenderhilda.create.foundation.shapes.IHasExtendedShape;
import darkenderhilda.create.foundation.register.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;


public class CreateBlock extends Block implements IHasModel, IHasExtendedShape {

    public CreateBlock(BlockProperties properties) {
        super(properties.getMaterial(), properties.getMapColor());
        setRegistryName(Create.ID, properties.getName());
        setTranslationKey(getRegistryName().toString());

        for(Pair<String, Integer> harvestLevel : properties.getHarvestLevels()) {
            setHarvestLevel(harvestLevel.first(), harvestLevel.second());
        }

        setHardness(properties.getHardness());
        setResistance(properties.getResistance());
        setSoundType(properties.getSoundType());
        setCreativeTab(properties.getCreativeTab());
        setLightLevel(properties.getLightLevel());
    }



    @Override
    public ExtendedShape getShape(IBlockState state) {
        return AllShapes.FULL_BLOCK;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        getShape(state).getShapes()
                .forEach(s -> addCollisionBoxToList(pos, entityBox, collidingBoxes, s));
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return WorldUtils.raytraceMultiAABB(getShape(state).getShapes(), pos, start, end);
    }

    @Override
    public void registerModel() {
        if(registerItem())
            Create.proxy.registerItemRenderer(ItemBlock.getItemFromBlock(this), 0, "inventory");
    }

    protected boolean registerItem() {
        return true;
    }
}
