package me.alphamode.nebula.content;

import me.alphamode.nebula.content.condensor.CondenserBlockEntity;
import me.alphamode.nebula.content.rocket.LaunchPadBlockEntity;
import me.alphamode.nebula.utils.AutoRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class AllBlockEntities extends AutoRegistry<BlockEntityType<?>> {
    public AllBlockEntities() {
        super(Registry.BLOCK_ENTITY_TYPE);
    }

    public static BlockEntityType<CondenserBlockEntity> CONDENSER = FabricBlockEntityTypeBuilder.create(CondenserBlockEntity::new, AllBlocks.CONDENSER).build();
    public static BlockEntityType<LaunchPadBlockEntity> LAUNCH_PAD = FabricBlockEntityTypeBuilder.create(LaunchPadBlockEntity::new, AllBlocks.LAUNCH_PAD).build();
}
