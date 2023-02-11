package me.zimzaza4.morebows.util;

import cn.nukkit.Player;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.inventory.BaseInventory.AIR_ITEM;

public final class InventoryUtil {
    private static final Long2LongMap lastClickTime = new Long2LongOpenHashMap();

    private InventoryUtil() {
        throw new UnsupportedOperationException();
    }


    @NotNull
    public static Item getSlotTransactionResult(Inventory inventory, @NotNull InventoryTransaction transaction) {
        Item item = AIR_ITEM;
        for (var each : transaction.getActionList()) {
            if (each instanceof SlotChangeAction action) {
                if (action.getInventory() == inventory) {
                    item = action.getTargetItem();
                }
            }
        }
        return item;
    }

    @NotNull
    public static Item getSlotTransactionResult(Inventory inventory, @NotNull InventoryTransactionEvent event) {
        return getSlotTransactionResult(inventory, event.getTransaction());
    }

    public static boolean ensurePlayerSafeForCustomInv(@NotNull Player player) {
        var lastClick = lastClickTime.getOrDefault(player.getLoaderId(), 0L);
        var currentClick = System.currentTimeMillis();
        if (currentClick - lastClick < 100) {
            return false;
        }
        lastClickTime.put(player.getLoaderId(), currentClick);
        return true;
    }
}