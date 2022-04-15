package me.alphamode.nebula;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;

public enum NebulaTabs {
    MISC(FabricItemGroupBuilder.create(Nebula.asResource("misc")).build());

    ItemGroup tab;

    NebulaTabs(ItemGroup group) {
        tab = group;
    }

    public ItemGroup getTab() {
        return tab;
    }
}
