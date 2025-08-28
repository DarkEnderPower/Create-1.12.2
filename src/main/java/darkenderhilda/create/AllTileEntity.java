package darkenderhilda.create;

import darkenderhilda.create.content.kinetics.creative_gearbox.CreativeGearBoxTESR;
import darkenderhilda.create.content.kinetics.creative_gearbox.CreativeGearBoxTileEntity;
import darkenderhilda.create.content.kinetics.creative_motor.CreativeMotorTESR;
import darkenderhilda.create.content.kinetics.creative_motor.CreativeMotorTileEntity;
import darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticTESR;
import darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticTileEntity;
import darkenderhilda.create.foundation.tileEntity.TileEntityRegister;
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

        REGISTER.tileEntity(CreativeGearBoxTileEntity.class, "creativeGearBoxTileEntity")
                .validBlocks(AllBlocks.CREATIVE_GEARBOX)
                .visual(new CreativeGearBoxTESR())
                .register();

        REGISTER.tileEntity(CreativeMotorTileEntity.class, "creativeMotorTileEntity")
                .validBlocks(AllBlocks.CREATIVE_MOTOR)
                .visual(new CreativeMotorTESR())
                .register();

    }

    public static TileEntity getTEForBlock(Block block) throws InstantiationException, IllegalAccessException {
        return TILE_ENTITIES.get(block).newInstance();
    }
}
