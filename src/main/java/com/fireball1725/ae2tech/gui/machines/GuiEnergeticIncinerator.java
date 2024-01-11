package com.fireball1725.ae2tech.gui.machines;

import com.fireball1725.ae2tech.container.machines.ContainerEnergeticIncinerator;
import com.fireball1725.ae2tech.core.localization.GuiText;
import com.fireball1725.ae2tech.gui.BaseGui;
import com.fireball1725.ae2tech.gui.widgets.GuiProgressBar;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticIncinerator;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiEnergeticIncinerator extends BaseGui {
    GuiProgressBar guiProgressBar;
    ContainerEnergeticIncinerator containerEnergeticIncinerator;

    public GuiEnergeticIncinerator(InventoryPlayer inventoryPlayer, TileEntityEnergeticIncinerator tileEntityEnergeticIncinerator) {
        super(new ContainerEnergeticIncinerator(inventoryPlayer, tileEntityEnergeticIncinerator));
        containerEnergeticIncinerator = ((ContainerEnergeticIncinerator) this.inventorySlots);
        this.xSize = 211;
        this.ySize = 152;
    }

    @Override
    public void initGui() {
        super.initGui();
        guiProgressBar = new GuiProgressBar("gui/energeticincinerator.png", this.guiLeft + 156, this.guiTop + 27, 214, 27, 6, 18, GuiProgressBar.Direction.VERTICAL);
        this.buttonList.add(this.guiProgressBar);
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        bindTexture("gui/energeticincinerator.png");
        drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        double progress = (this.containerEnergeticIncinerator.progress + 1);
        int maxFurnaceCookTime = 150 - ((this.containerEnergeticIncinerator.upgradeTier + 1) * 25);

        progress = (progress / maxFurnaceCookTime);
        progress = progress * 100;
        this.guiProgressBar.current = (int) progress;

        this.fontRendererObj.drawString(getGuiDisplayName(GuiText.EnergeticIncinerator.getLocal()), 8, 8, 4210752);
        this.fontRendererObj.drawString(GuiText.inventory.getLocal(), 8, this.ySize - 96 + 2, 4210752);
    }
}
