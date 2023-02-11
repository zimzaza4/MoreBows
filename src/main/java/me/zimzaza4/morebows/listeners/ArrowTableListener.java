package me.zimzaza4.morebows.listeners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryCloseEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import me.zimzaza4.morebows.item.ui.ArrowTableUI;

public class ArrowTableListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onClickTable(PlayerInteractEvent e) {
        if (e.getBlock() != null && e.getBlock().getId() == Block.FLETCHING_TABLE && e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            e.setCancelled();
            ArrowTableUI.send(e.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (ArrowTableUI.inventoriesInUse.contains(e.getInventory())) {
            Inventory displayInventory = e.getInventory();
            Player p = e.getPlayer();
            Item input1 = displayInventory.getItem(10);
            Item input2 = displayInventory.getItem(13);
            Item output = displayInventory.getItem(16);

            if (!input1.isNull()) {
                p.dropItem(input1);
            }
            if (!input2.isNull()) {
                p.dropItem(input2);
            }
            if (!output.isNull()) {
                p.dropItem(output);
            }



            ArrowTableUI.inventoriesInUse.remove(displayInventory);
        }
    }
}
