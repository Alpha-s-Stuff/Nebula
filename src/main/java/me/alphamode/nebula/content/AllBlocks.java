package me.alphamode.nebula.content;

import me.alphamode.nebula.NebulaTabs;
import me.alphamode.nebula.content.cable.GasCable;
import me.alphamode.nebula.content.condensor.CondenserBlock;
import me.alphamode.nebula.content.rocket.LaunchPad;
import me.alphamode.nebula.utils.AutoRegistry;
import me.alphamode.nebula.utils.RegistryInfo;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;

public class AllBlocks extends AutoRegistry<Block> {
    public AllBlocks() {
        super(Registry.BLOCK);
    }

    public static final Block CONDENSER = new CondenserBlock();
    public static final Block BASIC_GAS_CABLE = new GasCable();
    public static final Block HULL_GLASS = new GlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block WHITE_HULL_GLASS = new GlassBlock(FabricBlockSettings.copy(Blocks.GLASS));
    public static final Block LAUNCH_PAD = new LaunchPad(FabricBlockSettings.copy(Blocks.IRON_BLOCK));

    @Override
    public void afterRegister(Block registered, RegistryInfo registryInfo) {
        if(registryInfo != null)
            Registry.register(Registry.ITEM, Registry.BLOCK.getId(registered), new BlockItem(registered, new FabricItemSettings().group(registryInfo.tab().getTab())));
        else
            Registry.register(Registry.ITEM, Registry.BLOCK.getId(registered), new BlockItem(registered, new FabricItemSettings().group(NebulaTabs.MISC.getTab())));
    }
}
