package com.fireball1725.ae2tech.core.localization;

import net.minecraft.util.StatCollector;

public enum GuiText {
    inventory("container"),
    EnergeticIncinerator,
    AdvancedEnergeticIncinerator,
    EnergeticCrumbler,;

    String root;

    private GuiText() {
        this.root = "gui.ae2tech";
    }

    private GuiText(String r) {
        this.root = r;
    }

    public String getUnlocalized() {
        return this.root + "." + toString();
    }

    public String getLocal() {
        return StatCollector.translateToLocal(getUnlocalized());
    }
}
