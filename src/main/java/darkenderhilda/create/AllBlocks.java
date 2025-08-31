package darkenderhilda.create;

import darkenderhilda.create.content.kinetics.creative.creative_gearbox.CreativeGearBoxBlock;
import darkenderhilda.create.content.kinetics.creative.creative_gearbox.CreativeGearBoxItemBlock;
import darkenderhilda.create.content.kinetics.creative.creative_motor.CreativeMotorBlock;
import darkenderhilda.create.content.kinetics.creative.creative_motor.CreativeMotorItemBlock;
import darkenderhilda.create.content.kinetics.drill.DrillBlock;
import darkenderhilda.create.content.kinetics.gearbox.GearboxBlock;
import darkenderhilda.create.content.kinetics.millstone.MillstoneBlock;
import darkenderhilda.create.content.kinetics.simpleRelays.CogWheelBlock;
import darkenderhilda.create.content.kinetics.simpleRelays.ShaftBlock;
import darkenderhilda.create.content.MOVEFOLDER.DepotBlock;
import darkenderhilda.create.foundation.block.BlockData;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import java.util.ArrayList;
import java.util.List;

import static darkenderhilda.create.Create.REGISTER;

public class AllBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();

    //---Kinetics---
    public static final Block SHAFT = REGISTER.block(ShaftBlock::new)
            .properties(p -> p
                    .name("shaft")
                    .tool("pickaxe", 1)
                    .material(Material.ROCK)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.GRAY))
            .item()
            .register();

    public static final Block COGWHEEL = REGISTER.block(CogWheelBlock::small)
            .properties(p -> p
                    .name("cogwheel")
                    .tool("axe", 1)
                    .tool("pickaxe", 1)
                    .material(Material.WOOD)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.WOOD))
            .item()
            .register();

    public static final Block LARGE_COGWHEEL = REGISTER.block(CogWheelBlock::large)
            .properties(p -> p
                    .name("large_cogwheel")
                    .tool("axe", 1)
                    .tool("pickaxe", 1)
                    .material(Material.WOOD)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.WOOD))
            .item()
            .register();

    public static final Block GEARBOX = REGISTER.block(GearboxBlock::gearbox)
            .properties(p -> p
                    .name("gearbox")
                    .tool("pickaxe", 1)
                    .material(Material.ROCK)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block GEARBOX_VERTICAL = REGISTER.block(GearboxBlock::gearboxVertical)
            .properties(p -> p
                    .name("gearbox_vertical")
                    .tool("pickaxe", 1)
                    .material(Material.ROCK)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block MILLSTONE = REGISTER.block(MillstoneBlock::new)
            .properties(p -> p
                    .name("millstone")
                    .tool("pickaxe", 1)
                    .material(Material.ROCK)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.IRON))
            .item()
            .register();

    public static final Block CREATIVE_GEARBOX = REGISTER.block(CreativeGearBoxBlock::new)
            .properties(p -> p
                    .name("creative_gearbox")
                    .tool("pickaxe", 1)
                    .material(Material.ROCK)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.PURPLE))
            .item(CreativeGearBoxItemBlock::new)
            .register();

    public static final Block CREATIVE_MOTOR = REGISTER.block(CreativeMotorBlock::new)
            .properties(p -> p
                    .name("creative_motor")
                    .tool("pickaxe", 1)
                    .material(Material.ROCK)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.PURPLE))
            .item(CreativeMotorItemBlock::new)
            .register();

    public static final Block MECHANICAL_DRILL = REGISTER.block(DrillBlock::new)
            .properties(p -> p
                    .name("mechanical_drill")
                    .tool("pickaxe", 1)
                    .material(Material.ROCK)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();


    //---Blocks---
    public static final Block DEPOT = REGISTER.block(DepotBlock::new)
            .properties(p -> p
                    .name("depot")
                    .tool("axe", 1)
                    .tool("pickaxe", 1)
                    .material(Material.WOOD)
                    .hardness(BlockData.STONE_HARDNESS)
                    .resistance(BlockData.STONE_RESISTANCE)
                    .soundType(SoundType.STONE)
                    .mapColor(MapColor.GRAY))
            .item()
            .register();

    //---Partials---
    public static final Block RENDER = REGISTER.block(AllPartialModels::new)
            .properties(p -> p
                    .material(Material.ROCK)
                    .name("render")
                    .hardness(-1.0f)
                    .resistance(36000000.0f))
            .register();
}
