package com.fireball1725.ae2tech.gui.machines;

import com.fireball1725.ae2tech.container.machines.ContainerEnergeticCrumbler;
import com.fireball1725.ae2tech.core.localization.GuiText;
import com.fireball1725.ae2tech.gui.BaseGui;
import com.fireball1725.ae2tech.gui.widgets.GuiProgressBar;
import com.fireball1725.ae2tech.tileentity.machines.TileEntityEnergeticCrumbler;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiEnergeticCrumbler extends BaseGui {
    GuiProgressBar guiProgressBar;
    ContainerEnergeticCrumbler containerEnergeticCrumbler;

    public GuiEnergeticCrumbler(InventoryPlayer inventoryPlayer, TileEntityEnergeticCrumbler tileEntityEnergeticCrumbler) {
        super(new ContainerEnergeticCrumbler(inventoryPlayer, tileEntityEnergeticCrumbler));
        containerEnergeticCrumbler = ((ContainerEnergeticCrumbler) this.inventorySlots);
        this.xSize = 176;
        this.ySize = 152;
    }

    @Override
    public void initGui() {
        super.initGui();
        guiProgressBar = new GuiProgressBar("gui/energeticcrumbler.png", this.guiLeft + 156, this.guiTop + 27, 180, 27, 6, 18, GuiProgressBar.Direction.VERTICAL);
        this.buttonList.add(this.guiProgressBar);
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        bindTexture("gui/energeticcrumbler.png");
        drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
    }

    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.guiProgressBar.current = this.containerEnergeticCrumbler.progress / 2;
        this.fontRendererObj.drawString(getGuiDisplayName(GuiText.EnergeticCrumbler.getLocal()), 8, 8, 4210752);
        this.fontRendererObj.drawString(GuiText.inventory.getLocal(), 8, this.ySize - 96 + 2, 4210752);
    }
}
