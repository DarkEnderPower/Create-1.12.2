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
import darkenderhilda.create.content.kinetics.saw.SawRenderer;
import darkenderhilda.create.content.kinetics.saw.SawTileEntity;
import darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import darkenderhilda.create.content.kinetics.simpleRelays.BracketedKineticTileEntity;
import darkenderhilda.create.content.kinetics.speedController.SpeedControllerRenderer;
import darkenderhilda.create.content.kinetics.speedController.SpeedControllerTileEntity;
import darkenderhilda.create.content.kinetics.transmission.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

import static darkenderhilda.create.Create.REGISTER;

public class AllTileEntity {

    public static void initTileEntities() {
        registerTile(BracketedKineticTileEntity.class, "bracketedKineticTileEntity");
        registerTile(GearboxTileEntity.class, "gearboxTileEntity");
        registerTile(ClutchTileEntity.class, "clutchTileEntity");
        registerTile(GearshiftTileEntity.class, "gearshiftTileEntity");
        registerTile(MillstoneTileEntity.class, "millstoneTileEntity");
        registerTile(DrillTileEntity.class, "drillTileEntity");
        registerTile(SawTileEntity.class, "sawTileEntity");
        registerTile(CreativeMotorTileEntity.class, "creativeMotorTileEntity");
        registerTile(CreativeGearBoxTileEntity.class, "creativeGearBoxTileEntity");
        registerTile(HandCrankTileEntity.class, "handCrankTileEntity");
        registerTile(SpeedControllerTileEntity.class, "speedControllerTileEntity");
        registerTile(BeltTileEntity.class, "beltTileEntity");
    }

    public static void initTileEntityRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(BracketedKineticTileEntity.class, new BracketedKineticBlockEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GearboxTileEntity.class, new GearboxRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ClutchTileEntity.class, new SplitShaftRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(GearshiftTileEntity.class, new SplitShaftRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(MillstoneTileEntity.class, new MillstoneRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DrillTileEntity.class, new DrillTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(SawTileEntity.class, new SawRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CreativeMotorTileEntity.class, new CreativeMotorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(CreativeGearBoxTileEntity.class, new CreativeGearBoxTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(HandCrankTileEntity.class, new HandCrankTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(SpeedControllerTileEntity.class, new SpeedControllerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(BeltTileEntity.class, new BeltTESR());
    }

    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, new ResourceLocation(Create.ID + ":" + key));
    }
}
