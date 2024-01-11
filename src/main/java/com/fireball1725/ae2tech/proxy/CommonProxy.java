package com.fireball1725.ae2tech.proxy;

import appeng.api.AEApi;
import appeng.api.recipes.IRecipeHandler;
import appeng.api.recipes.IRecipeLoader;
import com.fireball1725.ae2tech.blocks.Blocks;
import com.fireball1725.ae2tech.events.PlayerEvents;
import com.fireball1725.ae2tech.events.TickHandler;
import com.fireball1725.ae2tech.items.Items;
import com.fireball1725.ae2tech.tileentity.Tiles;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

import java.io.*;

public abstract class CommonProxy implements IProxy {
    // Register Blocks
    public void registerBlocks() {
        Blocks.registerAll();
    }

    // Register Tile Entities
    public void registerTileEntities() {
        Tiles.registerAll();
    }

    // Register Items
    public void registerItems() {
        Items.registerAll();
    }

    // Register Recipes
    public void registerRecipes(File configFolder) {
        IRecipeHandler recipeHandler = AEApi.instance().registries().recipes().createNewRecipehandler();
        //File externalRecipe = new File(configFolder.getPath() + File.separator + Reference.MOD_ID + File.separator + "machines.recipe");
        //if (externalRecipe.exists()) {
        //    recipeHandler.parseRecipes(new ExternalRecipeLoader(), externalRecipe.getPath());
        //} else {
        recipeHandler.parseRecipes(new InternalRecipeLoader(), "main.recipe");
        //}
        recipeHandler.injectRecipes();
    }

    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());
        FMLCommonHandler.instance().bus().register(TickHandler.instance);
        MinecraftForge.EVENT_BUS.register(new TickHandler());
    }

    private class InternalRecipeLoader implements IRecipeLoader {

        @Override
        public BufferedReader getFile(String path) throws Exception {
            InputStream resourceAsStream = getClass().getResourceAsStream("/assets/ae2tech/recipes/" + path);
            InputStreamReader reader = new InputStreamReader(resourceAsStream, "UTF-8");
            return new BufferedReader(reader);
        }
    }

    private class ExternalRecipeLoader implements IRecipeLoader {

        @Override
        public BufferedReader getFile(String path) throws Exception {
            return new BufferedReader(new FileReader(new File(path)));
        }
    }
}
