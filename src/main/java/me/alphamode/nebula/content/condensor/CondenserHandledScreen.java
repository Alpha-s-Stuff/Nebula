package me.alphamode.nebula.content.condensor;

import com.mojang.blaze3d.systems.RenderSystem;
import me.alphamode.nebula.Nebula;
import me.alphamode.nebula.client.gui.widgets.TankWidget;
import me.alphamode.star.transfer.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class CondenserHandledScreen extends HandledScreen<CondenserScreenHandler> {

    private TankWidget atmosphere;
    private TankWidget tank;

    public CondenserHandledScreen(CondenserScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.atmosphere = new TankWidget(this, 39, 15, handler.atmosphere, -1);
        this.tank = new TankWidget(this, 11, 15, handler.tank, ((FluidTank)handler.tank).getCapacity());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        tank.setGases(List.of(new ResourceAmount<>(((FluidTank)handler.tank).getResource(), ((FluidTank)handler.tank).getAmount())));
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        atmosphere.render(matrices);
        tank.render(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, Nebula.asResource("textures/gui/condenser.png"));
        drawTexture(matrices, this.x, this.y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
        super.drawMouseoverTooltip(matrices, x, y);
        atmosphere.renderTooltip(matrices, x, y, (resourceAmount) -> {
            List<Text> tooltip = FluidVariantRendering.getTooltip(resourceAmount.resource());
            tooltip.add(new LiteralText("Concentration: " + resourceAmount.amount() + "%").formatted(Formatting.GOLD));
            if(!MinecraftClient.getInstance().options.advancedItemTooltips)
                tooltip.add(new LiteralText(FabricLoader.getInstance().getModContainer(Registry.FLUID.getId(resourceAmount.resource().getFluid()).getNamespace()).get().getMetadata().getName()).formatted(Formatting.BLUE));

            return tooltip;
        });
        tank.renderTooltip(matrices, x, y, (resourceAmount) -> {
            List<Text> tooltip = FluidVariantRendering.getTooltip(resourceAmount.resource());
            tooltip.add(new LiteralText(((FluidTank)handler.tank).getAmount() + "/" + ((FluidTank)handler.tank).getCapacity() + " Droplets").formatted(Formatting.GOLD));
            if(!MinecraftClient.getInstance().options.advancedItemTooltips)
                tooltip.add(new LiteralText(FabricLoader.getInstance().getModContainer(Registry.FLUID.getId(resourceAmount.resource().getFluid()).getNamespace()).get().getMetadata().getName()).formatted(Formatting.BLUE));

            return tooltip;
        });
    }
}
