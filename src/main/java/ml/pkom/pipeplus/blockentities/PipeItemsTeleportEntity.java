package ml.pkom.pipeplus.blockentities;

import alexiil.mc.mod.pipes.blocks.PipeFlow;
import alexiil.mc.mod.pipes.blocks.TilePipe;
import ml.pkom.pipeplus.PipePlus;
import ml.pkom.pipeplus.TeleportManager;
import ml.pkom.pipeplus.blocks.Blocks;
import ml.pkom.pipeplus.blocks.PipeItemsTeleport;
import ml.pkom.pipeplus.classes.TeleportPipeType;
import ml.pkom.pipeplus.pipeflow.TeleportPipeFlow;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class PipeItemsTeleportEntity extends TilePipe implements IPipeTeleportTileEntity {
    public UUID owner = null;
    public String ownerName = null;
    public Boolean modeIsPublic = null;
    public Integer pipeModeInt = null; // 0=Send Only, 1=Receive Only, 2=Send & Receive 3=Disabled
    public Integer frequency = null;
    public PipeFlow iFlow = null;

    @Override
    public boolean isPublic() {
        return modeIsPublic;
    }

    @Override
    public UUID getOwnerUUID() {
        return owner;
    }

    public boolean canReceive()
    {
        if (pipeModeInt == 0 || pipeModeInt == 3) {
            return false;
        }
        return true;
    }

    public boolean canSend()
    {
        if (pipeModeInt == 1 || pipeModeInt == 3) {
            return false;
        }
        return true;
    }

    @Override
    public Integer getFrequency() {
        return frequency;
    }

    public boolean canPlayerModifyPipe(PlayerEntity player)
    {
        if (owner == null ) return true;
        if(modeIsPublic || owner.equals(player.getUuid()) || player.abilities.creativeMode || owner.equals(UUID.fromString("00000000-0000-0000-0000-000000000000")))
            return true;
        return false;
    }

    public static Map<String, PipeItemsTeleportEntity> tileMap = new LinkedHashMap<>();

    @Override
    public void tick() {
        super.tick();
    }

    public PipeItemsTeleportEntity() {
        super(BlockEntities.PIPE_ITEMS_TELEPORT_ENTITY, Blocks.PIPE_ITEMS_TELEPORT, TeleportPipeFlow::new);
        iFlow = flow;
        if (owner == null) owner = ((PipeItemsTeleport) pipeBlock).owner;

        if (ownerName == null) {
            try {
                ownerName = getWorld().getPlayerByUuid(owner).getName().getString();
            } catch (NullPointerException e) {
            }
        }
        if (modeIsPublic == null) modeIsPublic = false;
        if (pipeModeInt == null) pipeModeInt = 0;
        if (frequency == null) frequency = 0;
        TeleportManager.instance.add(this, frequency);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        //debug();
        if (!tileMap.containsKey(PipePlus.pos2str(getPos()))) {
            tileMap.put(PipePlus.pos2str(getPos()), this);
        }
        PipeItemsTeleportEntity tile = tileMap.get(PipePlus.pos2str(getPos()));
        try {
            tile.owner = tag.getUuid("owner");
        } catch (NullPointerException e) {
            tile.owner = UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
        try {
            tile.ownerName = getWorld().getPlayerByUuid(tile.owner).getName().getString();
        } catch (NullPointerException e) {
            tile.ownerName = tag.getString("ownerName");
        }
        tile.modeIsPublic = tag.getBoolean("isPublic");
        tile.pipeModeInt = tag.getInt("modeInt");
        tile.frequency = tag.getInt("frequency");
        TeleportManager.instance.add(tile, tile.frequency);

        //debug();
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag = super.toTag(tag);
        PipeItemsTeleportEntity tile = tileMap.get(PipePlus.pos2str(getPos()));
        try {
            tag.putUuid("owner", tile.owner);
        } catch (NullPointerException e) {
            tag.putUuid("owner", UUID.fromString("00000000-0000-0000-0000-000000000000"));
        }
        if (tile.ownerName != null)
            tag.putString("ownerName", tile.ownerName);
        if (tile.modeIsPublic != null)
            tag.putBoolean("isPublic", tile.modeIsPublic);
        if (tile.pipeModeInt != null)
            tag.putInt("modeInt", tile.pipeModeInt);
        if (tile.frequency != null)
            tag.putInt("frequency", tile.frequency);
        //debug();
        return tag;
    }

    public void debug() {
        System.out.println("owner: " + owner + ", isPublic: " + modeIsPublic + ", modeInt: " + pipeModeInt + ", frequency: " + frequency);
    }

    @Override
    public TeleportPipeType getPipeType() {
        return TeleportPipeType.ITEMS;
    }
}
