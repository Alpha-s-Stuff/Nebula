package me.alphamode.nebula.content.rocket;

import me.alphamode.nebula.Nebula;
import me.alphamode.nebula.client.NebulaClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RocketEntityRenderer extends EntityRenderer<RocketEntity> {

    private final ModelPart part;

    public RocketEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.part = ctx.getPart(NebulaClient.MODEL_CUBE_LAYER);
    }

    @Override
    public void render(RocketEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        part.render(matrices, vertexConsumers.getBuffer(RenderLayer.LINES), light, 0);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-6F, 12F, -6F, 12F, 12F, 12F), ModelTransform.pivot(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public Identifier getTexture(RocketEntity entity) {
        return Nebula.asResource("");
    }
}
