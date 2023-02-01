package ml.pkom.pipeplus.items;

import ml.pkom.mcpitanlibarch.api.item.ExtendSettings;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import ml.pkom.pipeplus.PipePlus;
import ml.pkom.pipeplus.blocks.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class TinFluidPipe extends Item {
    public static Settings itemSettings = new ExtendSettings().addGroup(PipePlus.PIPEPLUS_GROUP, PipePlus.id("tin_fluid_pipe"));


    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(TextUtil.translatable("tip.pipeplus.auto_extract_pipe"));
    }

    public TinFluidPipe(Settings settings) {
        super(settings);
    }

    public static Settings getSettings() {
        return itemSettings;
    }

    public static Item newItem() {
        return new BlockItem(Blocks.TIN_FLUID_PIPE, getSettings());
    }
}
