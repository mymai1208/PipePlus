package ml.pkom.pipeplus.items;

import ml.pkom.pipeplus.PipePlus;
import ml.pkom.pipeplus.blocks.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import ml.pkom.mcpitanlibarch.api.util.TextUtil;
import net.minecraft.world.World;

import java.util.List;

public class ObsidianPipe extends Item {
    public static Settings itemSettings = new Settings();


    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(TextUtil.translatable("tooltip.pipeplus.obsidian_pipe"));
    }

    public ObsidianPipe(Settings settings) {
        super(settings);
    }

    public static Settings getSettings() {
        return itemSettings;
    }

    public static Item newItem() {
        return new BlockItem(Blocks.OBSIDIAN_PIPE, getSettings());
    }
}
