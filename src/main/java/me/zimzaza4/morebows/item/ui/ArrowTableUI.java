package me.zimzaza4.morebows.item.ui;

import cn.nukkit.Player;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.BaseInventory;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import me.iwareq.fakeinventories.CustomInventory;
import me.iwareq.fakeinventories.FakeInventories;
import me.iwareq.fakeinventories.block.SingleFakeBlock;
import me.iwareq.fakeinventories.util.ItemHandler;
import me.zimzaza4.morebows.item.CustomArrowBase;
import me.zimzaza4.morebows.item.MenuButton;
import me.zimzaza4.morebows.util.InventoryUtil;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ArrowTableUI {

    private static Map<Item[], Item> recipes = new HashMap<>();
    public static Set<Inventory> inventoriesInUse = new HashSet<>();

    public static void addRecipe(Item[] input, Item output) {
        recipes.put(input, output);
    }

    public static Map<Item[], Item> getRecipes() {
        return recipes;
    }

    public static void send(Player player) {
        if (player != null && InventoryUtil.ensurePlayerSafeForCustomInv(player)) {

            var displayInventory = new CustomInventory(InventoryType.CHEST, "morebows.ui.arrow_table");

            displayInventory.setDefaultItemHandler(((item, event) -> {
                event.setCancelled();

            }));
            displayInventory.setItem(10, BaseInventory.AIR_ITEM, (item, event) -> {
                Item result = InventoryUtil.getSlotTransactionResult(displayInventory, event);
                if (result.getId() == Item.ARROW) {
                    return;
                }
                event.setCancelled(true);
            });
            displayInventory.setItem(13, BaseInventory.AIR_ITEM, (item, event) -> { });
            displayInventory.setItem(16, BaseInventory.AIR_ITEM, (item, event) -> { });
            ItemHandler bottomHandler = (item, event) -> {
                event.setCancelled();
                for (Map.Entry<Item[], Item> recipe : recipes.entrySet()) {
                    Item input1 = displayInventory.getItem(10);
                    Item input2 = displayInventory.getItem(13);
                    Item output = displayInventory.getItem(16);
                    Item recipe1 = recipe.getKey()[0];
                    Item recipe2 = recipe.getKey()[1];
                    if (input1.getCount() >= recipe1.getCount()
                            && input2.getCount() >= recipe2.getCount()
                            && input1.equalsIgnoringEnchantmentOrder(recipe1, false)
                            && input2.equalsIgnoringEnchantmentOrder(recipe2, false)) {
                        if (!output.isNull()) {
                            if (!output.equalsIgnoringEnchantmentOrder(recipe.getValue(), false)) {
                                continue;
                            }
                            if (output.getCount() + recipe.getValue().getCount() > output.getMaxStackSize()) {
                                continue;
                            }
                        }

                        displayInventory.setItem(10, input1.decrement(recipe1.getCount()));
                        displayInventory.setItem(13, input2.decrement(recipe2.getCount()));
                        if (output.isNull()) {
                            displayInventory.setItem(16, recipe.getValue());
                        } else {
                            displayInventory.setItem(16, output.increment(recipe.getValue().getCount()));
                        }

                        break;
                    }

                }
            };
            displayInventory.setItem(14, new MenuButton(), bottomHandler);
            displayInventory.setItem(15, new MenuButton(), bottomHandler);
            inventoriesInUse.add(displayInventory);
            player.addWindow(displayInventory);

        }

    }
}
