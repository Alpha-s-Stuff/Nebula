package me.alphamode.nebula.data.providers;

import me.alphamode.nebula.Nebula;
import me.alphamode.nebula.content.AllBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class NebulaModelProvider extends FabricModelProvider {

    public NebulaModelProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerMachineModel(blockStateModelGenerator, AllBlocks.CONDENSER);
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.HULL_GLASS);
        blockStateModelGenerator.registerParentedItemModel(AllBlocks.HULL_GLASS, Nebula.asResource("block/hull_glass"));
        blockStateModelGenerator.registerSimpleCubeAll(AllBlocks.WHITE_HULL_GLASS);
        blockStateModelGenerator.registerParentedItemModel(AllBlocks.WHITE_HULL_GLASS, Nebula.asResource("block/white_hull_glass"));
    }

    public void registerMachineModel(BlockStateModelGenerator blockStateModelGenerator, Block machine) {
        var map = new TextureMap();
        map.put(TextureKey.EAST, TextureMap.getSubId(machine, "_front"));
        map.put(TextureKey.WEST, TextureMap.getSubId(machine, "_back"));
        map.put(TextureKey.NORTH, TextureMap.getSubId(machine, "_side"));
        map.put(TextureKey.SOUTH, TextureMap.getSubId(machine, "_side"));
        map.put(TextureKey.UP, TextureMap.getSubId(machine, "_end"));
        map.put(TextureKey.DOWN, TextureMap.getSubId(machine, "_end"));
        map.put(TextureKey.PARTICLE, TextureMap.getSubId(machine, "_front"));
        Identifier identifier = Models.CUBE_DIRECTIONAL.upload(machine, map, blockStateModelGenerator.modelCollector);
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(machine)
                .coordinate(
                        BlockStateVariantMap.create(Properties.FACING)
                                .register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.X, VariantSettings.Rotation.R180))
                                .register(Direction.UP, BlockStateVariant.create().put(VariantSettings.MODEL, identifier))
                                .register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, identifier))
                                .register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                .register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R270))
                ));
        Identifier id = new Identifier(Registry.BLOCK.getId(machine).getNamespace(), "block/" + Registry.BLOCK.getId(machine).getPath());
        blockStateModelGenerator.registerParentedItemModel(machine, id);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
