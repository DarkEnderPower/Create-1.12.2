package darkenderhilda.create;

import darkenderhilda.create.content.kinetics.belt.BeltTESR;
import darkenderhilda.create.content.kinetics.belt.BeltTileEntity;
import darkenderhilda.create.content.kinetics.crank.HandCrankTESR;
import darkenderhilda.create.content.kinetics.crank.HandCrankTileEntity;
import darkenderhilda.create.content.kinetics.creative.creative_gearbox.CreativeGearBoxTESR;
import darkenderhilda.create.content.kinetics.creative.creative_gearbox.CreativeGearBoxTileEntity;
import darkenderhilda.create.content.kinetics.creative.creative_motor.CreativeMotorRenderer;
import darkenderhilda.create.content.kinetics.creative.creative_motor.CreativeMotorTileEntity;
import darkenderhilda.create.content.kinetics.drill.DrillTileEntityRenderer;
import darkenderhilda.create.content.kinetics.drill.DrillTileEntity;
import darkenderhilda.create.content.kinetics.gearbox.GearboxRenderer;
import darkenderhilda.create.content.kinetics.gearbox.GearboxTileEntity;
import darkenderhilda.create.content.kinetics.millstone.MillstoneRenderer;
import darkenderhilda.create.content.kinetics.millstone.MillstoneTileEntity;
import darkenderhilda.create.content.kinetics.saw.SawTESR;
import darkenderhilda.create.content.kinetics.saw.SawTileEntity;
import darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticTileEntity;
import darkenderhilda.create.content.kinetics.speedController.SpeedControllerTESR;
import darkenderhilda.create.content.kinetics.speedController.SpeedControllerTileEntity;
import darkenderhilda.create.content.kinetics.transmission.ClutchTESR;
import darkenderhilda.create.content.kinetics.transmission.ClutchTileEntity;
import darkenderhilda.create.content.kinetics.transmission.GearshiftTileEntity;
import darkenderhilda.create.content.kinetics.transmission.GearShiftTESR;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import java.util.HashMap;
import java.util.Map;

import static darkenderhilda.create.Create.REGISTER;

public class AllTileEntity {

    public static final Map<Block, Class<? extends TileEntity>> TILE_ENTITIES = new HashMap<>();

    public static TileEntity getTEForBlock(Block block) throws InstantiationException, IllegalAccessException {
        return TILE_ENTITIES.get(block).newInstance();
    }

    static {
        REGISTER.tileEntity(BracketedKineticTileEntity.class, "bracketedKineticTileEntity")
                .validBlocks(AllBlocks.SHAFT, AllBlocks.COGWHEEL, AllBlocks.LARGE_COGWHEEL)
                .visual(new BracketedKineticBlockEntityRenderer())
                .register();

        REGISTER.tileEntity(GearboxTileEntity.class, "gearboxTileEntity")
                .validBlocks(AllBlocks.GEARBOX, AllBlocks.GEARBOX_VERTICAL)
                .visual(new GearboxRenderer())
                .register();

        REGISTER.tileEntity(ClutchTileEntity.class, "clutchTileEntity")
                .validBlocks(AllBlocks.CLUTCH)
                .visual(new ClutchTESR())
                .register();

        REGISTER.tileEntity(GearshiftTileEntity.class, "gearshiftTileEntity")
                .validBlocks(AllBlocks.GEARSHIFT)
                .visual(new GearShiftTESR())
                .register();

        REGISTER.tileEntity(MillstoneTileEntity.class, "millstoneTileEntity")
                .validBlocks(AllBlocks.MILLSTONE)
                .visual(new MillstoneRenderer())
                .register();

        REGISTER.tileEntity(DrillTileEntity.class, "drillTileEntity")
                .validBlocks(AllBlocks.MECHANICAL_DRILL)
                .visual(new DrillTileEntityRenderer())
                .register();

        REGISTER.tileEntity(SawTileEntity.class, "sawTileEntity")
                .validBlocks(AllBlocks.MECHANICAL_SAW)
                .visual(new SawTESR())
                .register();

        REGISTER.tileEntity(CreativeMotorTileEntity.class, "creativeMotorTileEntity")
                .validBlocks(AllBlocks.CREATIVE_MOTOR)
                .visual(new CreativeMotorRenderer())
                .register();

        REGISTER.tileEntity(CreativeGearBoxTileEntity.class, "creativeGearBoxTileEntity")
                .validBlocks(AllBlocks.CREATIVE_GEARBOX)
                .visual(new CreativeGearBoxTESR())
                .register();

        REGISTER.tileEntity(HandCrankTileEntity.class, "handCrankTileEntity")
                .validBlocks(AllBlocks.HAND_CRANK)
                .visual(new HandCrankTESR())
                .register();

        REGISTER.tileEntity(SpeedControllerTileEntity.class, "speedControllerTileEntity")
                .validBlocks(AllBlocks.ROTATION_SPEED_CONTROLLER)
                .visual(new SpeedControllerTESR())
                .register();

        REGISTER.tileEntity(BeltTileEntity.class, "beltTileEntity")
                .validBlocks(AllBlocks.BELT)
                .visual(new BeltTESR())
                .register();
    }
}
