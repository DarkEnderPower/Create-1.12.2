package darkenderhilda.create;

import darkenderhilda.create.content.kinetics.belt.BeltBlock;
import darkenderhilda.create.content.kinetics.belt.item.BeltConnectorItem;
import darkenderhilda.create.content.kinetics.crank.HandCrankBlock;
import darkenderhilda.create.content.kinetics.creative.creative_gearbox.CreativeGearBoxBlock;
import darkenderhilda.create.content.kinetics.creative.creative_gearbox.CreativeGearBoxItemBlock;
import darkenderhilda.create.content.kinetics.creative.creative_motor.CreativeMotorBlock;
import darkenderhilda.create.content.kinetics.creative.creative_motor.CreativeMotorItemBlock;
import darkenderhilda.create.content.kinetics.drill.DrillBlock;
import darkenderhilda.create.content.kinetics.gearbox.GearboxBlock;
import darkenderhilda.create.content.kinetics.millstone.MillstoneBlock;
import darkenderhilda.create.content.kinetics.saw.SawBlock;
import darkenderhilda.create.content.kinetics.simpleRelays.CogWheelBlock;
import darkenderhilda.create.content.kinetics.simpleRelays.ShaftBlock;
import darkenderhilda.create.content.MOVEFOLDER.DepotBlock;
import darkenderhilda.create.content.kinetics.speedController.SpeedControllerBlock;
import darkenderhilda.create.content.kinetics.transmission.ClutchBlock;
import darkenderhilda.create.content.kinetics.transmission.GearshiftBlock;
import darkenderhilda.create.foundation.block.SharedProperties;
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
    public static final Block SHAFT = REGISTER.block("shaft", ShaftBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                      .mapColor(MapColor.GRAY))
            .item()
            .register();

    public static final Block COGWHEEL = REGISTER.block("cogwheel", CogWheelBlock::small)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.WOOD))
            .item()
            .register();

    public static final Block LARGE_COGWHEEL = REGISTER.block("large_cogwheel", CogWheelBlock::large)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.WOOD))
            .item()
            .register();

    public static final Block GEARBOX = REGISTER.block("gearbox", GearboxBlock::gearbox)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block GEARBOX_VERTICAL = REGISTER.block("gearbox_vertical", GearboxBlock::gearboxVertical)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block CLUTCH = REGISTER.block("clutch", ClutchBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block GEARSHIFT = REGISTER.block("gearshift", GearshiftBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block MILLSTONE = REGISTER.block("millstone", MillstoneBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.GRAY))
            .item()
            .register();

    public static final Block MECHANICAL_DRILL = REGISTER.block("mechanical_drill", DrillBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block MECHANICAL_SAW = REGISTER.block("mechanical_saw", SawBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block CREATIVE_MOTOR = REGISTER.block("creative_motor", CreativeMotorBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.PURPLE))
            .item(CreativeMotorItemBlock::new)
            .register();

    public static final Block CREATIVE_GEARBOX = REGISTER.block("creative_gearbox", CreativeGearBoxBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.PURPLE))
            .item(CreativeGearBoxItemBlock::new)
            .register();

    public static final Block HAND_CRANK = REGISTER.block("hand_crank", HandCrankBlock::new)
            .initialProperties(SharedProperties.wooden())
            .properties(p -> p
                    .mapColor(MapColor.OBSIDIAN))
            .item()
            .register();

    public static final Block ROTATION_SPEED_CONTROLLER = REGISTER.block("rotation_speed_controller", SpeedControllerBlock::new)
            .initialProperties(SharedProperties.softMetal())
            .properties(p -> p
                    .mapColor(MapColor.YELLOW))
            .item()
            .register();


    //---Blocks---
    public static final Block DEPOT = REGISTER.block("depot", DepotBlock::new)
            .initialProperties(SharedProperties.stone())
            .properties(p -> p
                    .mapColor(MapColor.GRAY))
            .item()
            .register();

    public static final Block BELT = REGISTER.block("belt", BeltBlock::new)
            .properties(p -> p
                    .material(Material.CLOTH)
                    .mapColor(MapColor.GRAY)
                    .soundType(SoundType.CLOTH))
            .item(BeltConnectorItem::new)
            .register();

    //---Partials---
    public static final Block RENDER = REGISTER.block("render", AllPartialModels::new)
            .properties(p -> p
                    .material(Material.ROCK)
                    .hardness(-1.0f)
                    .resistance(36000000.0f))
            .register();

}
