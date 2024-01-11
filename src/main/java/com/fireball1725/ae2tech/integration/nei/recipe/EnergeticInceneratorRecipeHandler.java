package com.fireball1725.ae2tech.integration.nei.recipe;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.fireball1725.ae2tech.gui.machines.GuiEnergeticIncinerator;
import com.fireball1725.ae2tech.lib.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EnergeticInceneratorRecipeHandler extends TemplateRecipeHandler {
    public static final String displayNameRecipe = "container" + "." + Reference.MOD_ID + "." + "recipe" + "." + "energeticincenerator";

    @Override
    public String getGuiTexture() {
        return "textures/gui/energeticincinerator.png";
    }

    @Override
    public String getRecipeName() {
        return NEIClientUtils.translate(displayNameRecipe);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiEnergeticIncinerator.class;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), Reference.MOD_ID + "energeticinceneratorSmelting"));
    }

    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        super.loadUsageRecipes(inputId, ingredients);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList();
        for (Map.Entry<ItemStack, ItemStack> recipe : recipes.entrySet())
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getKey(), ingredient)) {
                SmeltingPair arecipe = new SmeltingPair(recipe.getKey(), recipe.getValue());
                arecipe.setIngredientPermutation(Arrays.asList(arecipe.ingred), ingredient);
                arecipes.add(arecipe);
            }
    }

    public class SmeltingPair extends CachedRecipe {
        PositionedStack ingred;
        PositionedStack result;

        public SmeltingPair(ItemStack ingred, ItemStack result) {
            ingred.stackSize = 1;
            this.ingred = new PositionedStack(ingred, 51, 6);
            this.result = new PositionedStack(result, 111, 24);
        }

        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 48, Arrays.asList(ingred));
        }

        public PositionedStack getResult() {
            return result;
        }
    }
}
