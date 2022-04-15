package me.alphamode.nebula.client.gui.widgets;

import me.alphamode.nebula.client.gui.GuiUtil;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class TankWidget extends Widget {

    private static Sprite fluidSprite = null;

    private final int x, y;
    private List<ResourceAmount<FluidVariant>> gases;
    private long maxVolume;
    private final HandledScreen<?> screen;
    private final List<Pair<ResourceAmount<FluidVariant>, Integer>> renderCache = new ArrayList<>();

    public TankWidget(HandledScreen<?> screen, int x, int y, Storage<FluidVariant> storage, long maxVolume) {
        super(screen.x + x, screen.y + y);
        this.screen = screen;
        this.x = screen.x + x;
        this.y = screen.y + y;

        List<ResourceAmount<FluidVariant>> gases = new ArrayList<>();
        try(Transaction t = Transaction.openOuter()) {
            storage.iterator(t).forEachRemaining(fluidVariantStorageView -> {
                gases.add(new ResourceAmount<>(fluidVariantStorageView.getResource(), fluidVariantStorageView.getAmount()));
            });
        }
        this.gases = gases;
        this.maxVolume = maxVolume;

        update();

        if (fluidSprite == null)
            fluidSprite = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER.getStill())
                    .getFluidSprites(client.player.world, BlockPos.ORIGIN, Fluids.WATER.getStill().getDefaultState())[0];
    }

    public void render(MatrixStack matrixStack) {
        int offset = y + 52; //67;
        for (var entry : renderCache) {
            int height = entry.getRight();
            GuiUtil.setColorARGB(FluidVariantRendering.getColor(entry.getLeft().resource()));
            GuiUtil.renderTiledTextureAtlas(matrixStack, fluidSprite, x, offset - height, 20, height, 0);
            offset -= height;
        }
    }

    @Override
    public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        renderTooltip(matrixStack, mouseX, mouseY, null);
    }

    public void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY, Function<ResourceAmount<FluidVariant>, List<Text>> lines) {
        int offset = y + 52;
        for (var entry : renderCache) {
            if (mouseX >= x && mouseX <= x + 20 && mouseY >= offset - entry.getRight() && mouseY < offset)
                screen.renderTooltip(matrixStack, lines.apply(entry.getLeft()), mouseX, mouseY);
            offset -= entry.getRight();
        }

    }

    public void setGases(List<ResourceAmount<FluidVariant>> gases) {
        if (!this.gases.equals(gases)) {
            this.gases = gases;
            update();
        }
    }

    public void setMaxVolume(long maxVolume) {
        if (this.maxVolume != maxVolume) {
            this.maxVolume = maxVolume;
            update();
        }
    }

    private void update() {
        renderCache.clear();
        if (maxVolume == -1) {
            double sumVolume = gases.stream().mapToLong(ResourceAmount::amount).sum();
            int pixelsAvailable = 52 - gases.size() * 2;

            for (ResourceAmount<FluidVariant> gas : gases)
                renderCache.add(new Pair<>(gas, (int) (gas.amount() / sumVolume * pixelsAvailable + 2)));
        } else {
            for (ResourceAmount<FluidVariant> gas : gases)
                renderCache.add(new Pair<>(gas, Math.max(2, (int) (52.0 * gas.amount() / maxVolume))));
        }
    }
}