package me.alphamode.nebula.content;

import me.alphamode.nebula.content.rocket.RocketEntity;
import me.alphamode.nebula.utils.AutoRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class AllEntities extends AutoRegistry<EntityType<?>> {
    public AllEntities() {
        super(Registry.ENTITY_TYPE);
    }

    public static EntityType<RocketEntity> ROCKET = FabricEntityTypeBuilder.create(SpawnGroup.MISC, RocketEntity::new).build();
}
