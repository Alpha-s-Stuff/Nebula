package me.alphamode.nebula.content;

import me.alphamode.nebula.Nebula;
import me.alphamode.nebula.utils.AutoRegistry;
import me.alphamode.nebula.utils.RegistryInfo;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.stat.Stats.CUSTOM;

public class AllStats extends AutoRegistry<Identifier> {

    public static Identifier CONDENSER_INTERACT = Nebula.asResource("Condenser Interact");

    public AllStats() {
        super(Registry.CUSTOM_STAT);
    }

    @Override
    public void afterRegister(Identifier registered, RegistryInfo registryInfo) {
        CUSTOM.getOrCreateStat(registered, StatFormatter.DEFAULT);
    }
}
