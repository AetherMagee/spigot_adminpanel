package aether.utils;

import aether.utils.InventoryHolders.MainMenuHolder;
import aether.utils.InventoryHolders.PlayerControlPanelMenuHolder;
import aether.utils.InventoryHolders.PlayerListMenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class menuClickHandler implements Listener {
    @EventHandler
    public void onClickInMainMenu(InventoryClickEvent event) {
        if (!event.getWhoClicked().hasPermission("adminpanel.adminpanel")) {
            return;
        }
        MenuBuilder mb = new MenuBuilder();
        InventoryHolder invHolder = event.getInventory().getHolder();
        if (invHolder instanceof MainMenuHolder) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                return;
            }
            ItemStack currentItem = event.getCurrentItem();
            switch (currentItem.getType()) { // I dont give a single fuck that this switch is pointless rn, this is for extendability
                case PLAYER_HEAD: {
                    mb.buildPlayerListMenu((Player) event.getWhoClicked(), 1);
                    break;
                }
            }
        } else if (invHolder instanceof PlayerListMenuHolder) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) {
                return;
            }
            ItemStack currentItem = event.getCurrentItem();
            switch (currentItem.getType()) {
                case LIME_STAINED_GLASS_PANE:
                    int nextPage = Integer.parseInt(currentItem.getItemMeta().getDisplayName().replace("Go to page ", ""));
                    mb.buildPlayerListMenu((Player) event.getWhoClicked(), nextPage);
                    break;
                case RED_STAINED_GLASS_PANE:
                    mb.buildMainMenu((Player) event.getWhoClicked());
                    break;
                case PLAYER_HEAD:
                    Player targetPlayer = Bukkit.getPlayer(currentItem.getItemMeta().getDisplayName());
                    if (targetPlayer == null) {
                        return;
                    }
                    mb.buildPlayerControlMenu((Player) event.getWhoClicked(), targetPlayer);
                    break;
            }
        } else if (invHolder instanceof PlayerControlPanelMenuHolder) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null) {
                return;
            }
            Player targetPlayer = Bukkit.getPlayer(currentItem.getItemMeta().getDisplayName().replace("Kill player: ", ""));
            if (targetPlayer == null) {
                return; // Plenty of returns, huh?
            }
            switch (currentItem.getType()) {
                case IRON_SWORD: {
                    targetPlayer.setHealth(0.0f);
                }
                case REDSTONE_BLOCK: {
                    targetPlayer.banPlayer("Bye!");
                }
            }
        }
    }
}