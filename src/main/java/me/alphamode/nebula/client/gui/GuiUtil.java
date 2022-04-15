package me.alphamode.nebula.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;

//Helper methods for gui
public class GuiUtil {
    private static final int TEX_WIDTH = 16;
    private static final int TEX_HEIGHT = 16;

    public static void setColorARGB(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    public static void renderTiledTextureAtlas(MatrixStack matrices, Sprite sprite, int x, int y, int width, int height, int depth) {
        // start drawing sprites
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
        //screen.client.getTextureManager().bindTexture(sprite.getAtlas().getId());
        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        // tile vertically
        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();
        int spriteHeight = sprite.getHeight();
        int spriteWidth = sprite.getWidth();
        int startY = y;
        do {

            int renderHeight = Math.min(spriteHeight, height);
            height -= renderHeight;
            float v2 = sprite.getFrameV((16f * renderHeight) / spriteHeight);

            // we need to draw the quads per width too
            int x2 = x;
            int widthLeft = width;
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            // tile horizontally
            do {
                int renderWidth = Math.min(spriteWidth, widthLeft);
                widthLeft -= renderWidth;

                float u2 = sprite.getFrameU((16f * renderWidth) / spriteWidth);
                buildSquare(matrix, builder, x2, x2 + renderWidth, startY, startY + renderHeight, depth, u1, u2, v1, v2);
                x2 += renderWidth;
            } while (widthLeft > 0);

            startY += renderHeight;
        } while (height > 0);

        // finish drawing sprites
        builder.end();
        //RenderSystem
        RenderSystem.enableDepthTest();
        BufferRenderer.draw(builder);
    }

    public static void buildSquare(Matrix4f matrix, BufferBuilder builder, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2) {
        builder.vertex(matrix, x1, y2, z).texture(u1, v2).next();
        builder.vertex(matrix, x2, y2, z).texture(u2, v2).next();
        builder.vertex(matrix, x2, y1, z).texture(u2, v1).next();
        builder.vertex(matrix, x1, y1, z).texture(u1, v1).next();
    }

    public static void drawFluid(FluidVariant fluidVariant, MatrixStack poseStack, final int xPosition, final int yPosition, int height) {
        Sprite fluidStillSprite = FluidVariantRendering.getSprite(fluidVariant);

        int fluidColor = FluidVariantRendering.getColor(fluidVariant);

        long amount = height;
        long scaledAmount = (amount * TEX_HEIGHT) / FluidConstants.BUCKET;
//        if (amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
//            scaledAmount = MIN_FLUID_HEIGHT;
//        }
        if (scaledAmount > TEX_HEIGHT) {
            scaledAmount = TEX_HEIGHT;
        }

        drawTiledSprite(poseStack, xPosition, yPosition, TEX_WIDTH, TEX_HEIGHT, fluidColor, scaledAmount, fluidStillSprite);
    }

    public static void drawTiledSprite(MatrixStack poseStack, final int xPosition, final int yPosition, final int tiledWidth, final int tiledHeight, int color, long scaledAmount, Sprite sprite) {
        RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        Matrix4f matrix = poseStack.peek().getPositionMatrix();
        setColorARGB(color);

        final int xTileCount = tiledWidth / TEX_WIDTH;
        final int xRemainder = tiledWidth - (xTileCount * TEX_WIDTH);
        final long yTileCount = scaledAmount / TEX_HEIGHT;
        final long yRemainder = scaledAmount - (yTileCount * TEX_HEIGHT);

        final int yStart = yPosition + tiledHeight;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : TEX_WIDTH;
                long height = (yTile == yTileCount) ? yRemainder : TEX_HEIGHT;
                int x = xPosition + (xTile * TEX_WIDTH);
                int y = yStart - ((yTile + 1) * TEX_HEIGHT);
                if (width > 0 && height > 0) {
                    long maskTop = TEX_HEIGHT - height;
                    int maskRight = TEX_WIDTH - width;

                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, Sprite textureSprite, long maskTop, int maskRight, float zLevel) {
        float uMin = textureSprite.getMinU();
        float uMax = textureSprite.getMaxU();
        float vMin = textureSprite.getMinV();
        float vMax = textureSprite.getMaxV();
        uMax = uMax - (maskRight / 16F * (uMax - uMin));
        vMax = vMax - (maskTop / 16F * (vMax - vMin));

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, xCoord, yCoord + 16, zLevel).texture(uMin, vMax).next();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).texture(uMax, vMax).next();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).texture(uMax, vMin).next();
        bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, zLevel).texture(uMin, vMin).next();
        tessellator.draw();
    }
}