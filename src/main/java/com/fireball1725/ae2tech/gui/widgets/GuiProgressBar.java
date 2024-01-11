package com.fireball1725.ae2tech.gui.widgets;

import com.fireball1725.ae2tech.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiProgressBar extends GuiButton {

    public int current;
    public int max;
    private ResourceLocation texture;
    private int fill_u;
    private int fill_v;
    private int width;
    private int height;
    private Direction direction;

    public GuiProgressBar(String string, int posX, int posY, int u, int y, int _width, int _height, Direction dir) {
        super(posX, posY, _width, "");
        this.xPosition = posX;
        this.yPosition = posY;
        this.texture = new ResourceLocation(Reference.MOD_ID, "textures/" + string);
        this.width = _width;
        this.height = _height;
        this.fill_u = u;
        this.fill_v = y;
        this.current = 0;
        this.max = 100;
        this.direction = dir;
    }

    @Override
    public void drawButton(Minecraft minecraft, int x, int y) {
        if (this.visible) {
            minecraft.getTextureManager().bindTexture(this.texture);
            if (this.direction == Direction.VERTICAL) {
                int diff = this.height - (this.max > 0 ? this.height * this.current / this.max : 0);
                drawTexturedModalRect(this.xPosition, this.yPosition + diff, this.fill_u, this.fill_v + diff, this.width, this.height - diff);
            }
        } else {
            // TODO: Horizontal Progress Bar...
        }
        mouseDragged(minecraft, x, y);
    }

    public int xPos() {
        return this.xPosition - 2;
    }

    public int yPos() {
        return this.yPosition - 2;
    }

    public int getWidth() {
        return this.width + 4;
    }

    public int getHeight() {
        return this.height + 4;
    }

    public boolean isVisible() {
        return true;
    }

    public static enum Direction {
        HORIZONTAL, VERTICAL;
    }
}
