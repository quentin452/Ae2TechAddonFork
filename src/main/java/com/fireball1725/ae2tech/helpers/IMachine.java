package com.fireball1725.ae2tech.helpers;

import appeng.api.networking.IGridHost;

public abstract interface IMachine extends IGridHost {
    //public abstract boolean isPowered();
    public abstract boolean isProcessing();

    public abstract int getState();
}
