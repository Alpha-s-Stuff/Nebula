package me.alphamode.nebula.client.gui.widgets;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public abstract class Widget extends DrawableHelper {

    protected List<Widget> nestedWidgets;
    protected int startX, startY;
    protected static final MinecraftClient client = MinecraftClient.getInstance();

    public Widget(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void render(MatrixStack matrixStack) {
        for (Widget nested : nestedWidgets) {
            nested.render(matrixStack);
        }
    }

    public void render(MatrixStack matrixStack, int x, int y, float delta) {
        for (Widget nested : nestedWidgets) {
            nested.render(matrixStack);
        }
    }

    public abstract void renderTooltip(MatrixStack matrixStack, int mouseX, int mouseY);
}