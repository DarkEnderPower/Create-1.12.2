package darkenderhilda.create.foundation.tileEntity;

import darkenderhilda.create.foundation.utility.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public abstract class SyncedTileEntity extends TileEntity {


    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    public void sendData() {
        world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2 | 4 | 16);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 1, writeToClient(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readClientUpdate(pkt.getNbtCompound());
    }

    public void readClientUpdate(NBTTagCompound tag) {
        readFromNBT(tag);
    }

    public NBTTagCompound writeToClient(NBTTagCompound tag) {
        return writeToNBT(tag);
    }

    public IBlockState getBlockState() {
        return WorldUtils.stateFormTE(this);
    }
}
