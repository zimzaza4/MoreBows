package me.zimzaza4.morebows;

import cn.nukkit.Server;
import cn.nukkit.inventory.CraftingManager;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import me.zimzaza4.morebows.item.bows.*;
import me.zimzaza4.morebows.listeners.ProjectileListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoreBows extends PluginBase {
    private static MoreBows instance;

    private static final String[] RECIPE = new String[] {
            "mmm",
            "mbm",
            "mmm"
    };
    @Override
    public void onEnable() {
        instance = this;
        Server.getInstance().getPluginManager().registerEvents(new ProjectileListener(), this);

        try {
            Item.registerCustomItem(List.of(IcyBow.class, TeleportBow.class, TNTBow.class, AutomaticBow.class, ThunderBow.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        CraftingManager craftingManager = Server.getInstance().getCraftingManager();


        craftingManager.registerRecipe(new ShapedRecipe(new IcyBow(), RECIPE, getBowRecipeMap(Item.get(Item.ICE)), new ArrayList<>()));
        craftingManager.registerRecipe(new ShapedRecipe(new TNTBow(), RECIPE, getBowRecipeMap(Item.get(Item.TNT)), new ArrayList<>()));
        craftingManager.registerRecipe(new ShapedRecipe(new TeleportBow(), RECIPE, getBowRecipeMap(Item.get(Item.ENDER_PEARL)), new ArrayList<>()));
        craftingManager.registerRecipe(new ShapedRecipe(new AutomaticBow(), RECIPE, getBowRecipeMap(Item.get(Item.REDSTONE_BLOCK)), new ArrayList<>()));
        craftingManager.registerRecipe(new ShapedRecipe(new ThunderBow(), RECIPE, getBowRecipeMap(Item.get(Item.GOLD_BLOCK)), new ArrayList<>()));
        craftingManager.rebuildPacket();
    }



    public Map<Character, Item> getBowRecipeMap(Item material) {
        Map<Character, Item> map = new HashMap<>();
        map.put('b', Item.get(Item.BOW));
        map.put('m', material);
        return map;
    }

    @Override
    public void onDisable() {

    }

    public static MoreBows getInstance() {
        return instance;
    }
}
