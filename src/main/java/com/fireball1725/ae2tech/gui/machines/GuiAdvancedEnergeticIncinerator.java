package com.fireball1725.ae2tech.gui.machines;

import com.fireball1725.ae2tech.container.machines.ContainerAdvancedEnergeticIncinerator;
import com.fireball1725.ae2tech.gui.BaseGui;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityAdvancedEnergeticIncinerator;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiAdvancedEnergeticIncinerator extends BaseGui {
    public GuiAdvancedEnergeticIncinerator(InventoryPlayer inventoryPlayer, TileEntityAdvancedEnergeticIncinerator tileEntityAdvancedEnergeticIncinerator) {
        super(new ContainerAdvancedEnergeticIncinerator(inventoryPlayer, tileEntityAdvancedEnergeticIncinerator));
        this.xSize = 211;
        this.ySize = 184;
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        bindTexture("gui/advancedenergeticincinerator.png");
        drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {

    }
}
