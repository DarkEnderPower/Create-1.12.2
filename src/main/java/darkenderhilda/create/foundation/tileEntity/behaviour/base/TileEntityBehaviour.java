package darkenderhilda.create.foundation.tileEntity.behaviour.base;

import darkenderhilda.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class TileEntityBehaviour {

	public SmartTileEntity tileEntity;
	private boolean paused;
	private int lazyTickRate;
	private int lazyTickCounter;

	public TileEntityBehaviour(SmartTileEntity te) {
		tileEntity = te;
		paused = false;
		setLazyTickRate(10);
	}

	public abstract IBehaviourType<?> getType();

	public void initialize() {

	}

	public void tick() {
		if (lazyTickCounter-- <= 0) {
			lazyTickCounter = lazyTickRate;
			lazyTick();
		}
			
	}

	public void readNBT(NBTTagCompound nbt) {

	}

	public void updateClient(NBTTagCompound nbt) {

	}

	public void writeNBT(NBTTagCompound nbt) {

	}

	public void onBlockChanged(IBlockState oldState) {

	}

	public void onNeighborChanged(EnumFacing direction) {

	}

	public void remove() {

	}
	
	public void destroy() {
		
	}

	public boolean isPaused() {
		return paused;
	}

	public void setLazyTickRate(int slowTickRate) {
		this.lazyTickRate = slowTickRate;
		this.lazyTickCounter = slowTickRate;
	}

	public void lazyTick() {

	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public BlockPos getPos() {
		return tileEntity.getPos();
	}

	public World getWorld() {
		return tileEntity.getWorld();
	}

	public static <T extends TileEntityBehaviour> T get(IBlockAccess reader, BlockPos pos,
														IBehaviourType<T> type) {
		return get(reader.getTileEntity(pos), type);
	}
	
	public static <T extends TileEntityBehaviour> void destroy(IBlockAccess reader, BlockPos pos,
			IBehaviourType<T> type) {
		T behaviour = get(reader.getTileEntity(pos), type);
		if (behaviour != null)
			behaviour.destroy();
	}

	public static <T extends TileEntityBehaviour> T get(TileEntity te, IBehaviourType<T> type) {
		if (te == null)
			return null;
		if (!(te instanceof SmartTileEntity))
			return null;
		SmartTileEntity ste = (SmartTileEntity) te;
		return ste.getBehaviour(type);
	}

	public NBTTagCompound writeToClient(NBTTagCompound compound) {
		return compound;
	}

}
