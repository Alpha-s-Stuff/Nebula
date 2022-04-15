package me.alphamode.nebula.client;

import me.alphamode.nebula.Nebula;
import me.alphamode.nebula.content.AllBlocks;
import me.alphamode.nebula.content.AllEntities;
import me.alphamode.nebula.content.dimensions.AllDimensions;
import me.alphamode.nebula.content.fluids.AllFluids;
import me.alphamode.nebula.content.rocket.RocketEntityRenderer;
import me.alphamode.star.client.models.CTModelRegistry;
import me.alphamode.star.client.renderers.UpsideDownFluidRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class NebulaClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(Nebula.asResource("cube"), "main");

    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry.INSTANCE.register(AllFluids.HYDROGEN.still(), AllFluids.HYDROGEN.flowing(), new UpsideDownFluidRenderer());
        FluidRenderHandlerRegistry.INSTANCE.register(AllFluids.OXYGEN.still(), AllFluids.OXYGEN.flowing(), new UpsideDownFluidRenderer());

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), AllFluids.HYDROGEN.still(), AllFluids.HYDROGEN.flowing());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), AllBlocks.HULL_GLASS, AllBlocks.WHITE_HULL_GLASS);

        DimensionRenderingRegistry.registerCloudRenderer(AllDimensions.SPACE, (context -> {})); // Prevent cloud rendering
        DimensionRenderingRegistry.registerSkyRenderer(AllDimensions.SPACE, new SpaceRenderer());
        DimensionRenderingRegistry.registerDimensionEffects(AllDimensions.SPACE.getValue(), new SpaceEffects());
        CTModelRegistry.registerCTModel(AllBlocks.HULL_GLASS);
        CTModelRegistry.registerCTModel(AllBlocks.WHITE_HULL_GLASS);
        EntityRendererRegistry.register(AllEntities.ROCKET, RocketEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, RocketEntityRenderer::getTexturedModelData);
    }


}
