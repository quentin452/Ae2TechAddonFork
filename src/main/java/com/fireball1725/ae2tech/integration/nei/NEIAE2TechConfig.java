package com.fireball1725.ae2tech.integration.nei;

import codechicken.nei.api.IConfigureNEI;
import com.fireball1725.ae2tech.lib.Reference;

public class NEIAE2TechConfig implements IConfigureNEI {
    @Override
    public void loadConfig() {
        //API.registerRecipeHandler(new EnergeticInceneratorRecipeHandler());
        //API.registerUsageHandler(new EnergeticInceneratorRecipeHandler());

        //API.hideItem(new ItemStack(Blocks.MACHINE_ENERGETICINCINERATOR.block));
    }

    @Override
    public String getName() {
        return Reference.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return Reference.VERSION_BUILD;
    }


}
