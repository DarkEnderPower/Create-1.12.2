package darkenderhilda.create;

import darkenderhilda.create.content.kinetics.creative.creative_gearbox.CreativeGearBoxTESR;
import darkenderhilda.create.content.kinetics.creative.creative_gearbox.CreativeGearBoxTileEntity;
import darkenderhilda.create.content.kinetics.creative.creative_motor.CreativeMotorTESR;
import darkenderhilda.create.content.kinetics.creative.creative_motor.CreativeMotorTileEntity;
import darkenderhilda.create.content.kinetics.drill.DrillTESR;
import darkenderhilda.create.content.kinetics.drill.DrillTileEntity;
import darkenderhilda.create.content.kinetics.gearbox.GearboxTESR;
import darkenderhilda.create.content.kinetics.gearbox.GearboxTileEntity;
import darkenderhilda.create.content.kinetics.millstone.MillstoneTESR;
import darkenderhilda.create.content.kinetics.millstone.MillstoneTileEntity;
import darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticTESR;
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

    static {
        REGISTER.tileEntity(BracketedKineticTileEntity.class, "bracketedKineticTileEntity")
                .validBlocks(AllBlocks.SHAFT, AllBlocks.COGWHEEL, AllBlocks.LARGE_COGWHEEL)
                .visual(new BracketedKineticTESR())
                .register();

        REGISTER.tileEntity(GearboxTileEntity.class, "gearboxTileEntity")
                .validBlocks(AllBlocks.GEARBOX, AllBlocks.GEARBOX_VERTICAL)
                .visual(new GearboxTESR())
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
                .visual(new MillstoneTESR())
                .register();

        REGISTER.tileEntity(DrillTileEntity.class, "drillTileEntity")
                .validBlocks(AllBlocks.MECHANICAL_DRILL)
                .visual(new DrillTESR())
                .register();

        REGISTER.tileEntity(CreativeMotorTileEntity.class, "creativeMotorTileEntity")
                .validBlocks(AllBlocks.CREATIVE_MOTOR)
                .visual(new CreativeMotorTESR())
                .register();

        REGISTER.tileEntity(CreativeGearBoxTileEntity.class, "creativeGearBoxTileEntity")
                .validBlocks(AllBlocks.CREATIVE_GEARBOX)
                .visual(new CreativeGearBoxTESR())
                .register();

        REGISTER.tileEntity(SpeedControllerTileEntity.class, "speedControllerTileEntity")
                .validBlocks(AllBlocks.ROTATION_SPEED_CONTROLLER)
                .visual(new SpeedControllerTESR())
                .register();
    }

    public static TileEntity getTEForBlock(Block block) throws InstantiationException, IllegalAccessException {
        return TILE_ENTITIES.get(block).newInstance();
    }
}
