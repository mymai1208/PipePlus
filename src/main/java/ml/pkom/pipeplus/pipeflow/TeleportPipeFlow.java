package ml.pkom.pipeplus.pipeflow;

import alexiil.mc.mod.pipes.blocks.TilePipe;
import alexiil.mc.mod.pipes.pipe.PipeSpFlowItem;
import alexiil.mc.mod.pipes.pipe.TravellingItem;
import ml.pkom.pipeplus.TeleportManager;
import ml.pkom.pipeplus.blockentities.IPipeTeleportTileEntity;
import ml.pkom.pipeplus.blockentities.PipeItemsTeleportEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class TeleportPipeFlow extends PipeSpFlowItem {

    public PipeItemsTeleportEntity tileEntity;
    private ReentrantLock mutex = new ReentrantLock();


    public TeleportPipeFlow(TilePipe pipe) {
        super(pipe);
        tileEntity = (PipeItemsTeleportEntity) pipe;
    }

    @Override
    public ItemStack injectItem(ItemStack stack, boolean doAdd, Direction from, DyeColor colour, double speed) {
        if(this.world().isClient) {
            throw new IllegalStateException("Cannot inject items on the client side!");
        }

        List<IPipeTeleportTileEntity> pipes = TeleportManager.instance.getPipes(tileEntity.getFrequency(), tileEntity.getOwnerUUID());

        for (IPipeTeleportTileEntity pipe : pipes) {
            if(!(pipe instanceof PipeItemsTeleportEntity pipeTile)) {
                continue;
            }

            if(pipeTile.pipeUUID == tileEntity.pipeUUID) {
                continue;
            }

            lock();

            //転送中にパイプが破壊された場合は中断
            if(world().getBlockEntity(pipeTile.getPos()) == null || world().getBlockEntity(tileEntity.getPos()) == null) {
                unlock();

                return stack;
            }

            try {
                if (tileEntity.canSend() && pipeTile.canReceive()) {
                    ItemStack copy = stack.copy();

                    copy.setSubNbt("pipeplus-teleporting", new NbtCompound());

                    insertItemsForce(copy, from, colour, speed);

                    pipeTile.getFlow().insertItemsForce(stack, from, colour, speed);

                    return ItemStack.EMPTY;
                }
            } finally {
                unlock();
            }
        }

        return stack;
    }

    public void lock() {
        mutex.lock();
    }

    public void unlock() {
        mutex.unlock();
    }

    @Override
    protected boolean canBounce() {
        return TeleportManager.instance.getPipes(tileEntity.getFrequency(), tileEntity.getOwnerUUID()).size() < 2;
    }

    @Override
    protected List<EnumSet<Direction>> getOrderForItem(TravellingItem item, EnumSet<Direction> validDirections) {
        return super.getOrderForItem(item, validDirections);
    }
}