package com.fireball1725.ae2tech.helpers;

import appeng.api.networking.IGridHost;
import appeng.api.util.DimensionalCoord;

public abstract interface IGridProxyable extends IGridHost {
    public abstract AENetworkHelper getProxy();

    public abstract DimensionalCoord getLocation();

    public abstract void gridChanged();
}
