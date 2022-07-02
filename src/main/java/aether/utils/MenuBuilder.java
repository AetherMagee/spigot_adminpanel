package aether.utils;

import aether.utils.InventoryHolders.MainMenuHolder;
import aether.utils.InventoryHolders.PlayerControlPanelMenuHolder;
import aether.utils.InventoryHolders.PlayerListMenuHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collection;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class MenuBuilder {
    public ItemStack setItemStackName(ItemStack itemStackToRename, String newName) {
        ItemMeta ISmeta = itemStackToRename.getItemMeta();
        ISmeta.setDisplayName(ChatColor.RESET + newName);
        itemStackToRename.setItemMeta(ISmeta);
        return itemStackToRename;
    }


    public void buildMainMenu(Player targetPlayer) {
        Inventory mainInventory = Bukkit.createInventory(new MainMenuHolder(), 27, "Admin panel - Main menu");

        ItemStack playerSkull = new ItemStack(Material.PLAYER_HEAD);
        playerSkull = setItemStackName(playerSkull, "List all players");
        mainInventory.setItem(13, playerSkull);

        targetPlayer.openInventory(mainInventory);
    }


    public void buildPlayerListMenu(Player targetPLayer, int page) {
        // Avoiding errors
        if (page < 1) {
            page = 1;
        }

        // Creating constants
        int playerCountLimit = 26;

        // Creating basic inventory and listing all players online
        Inventory newInventory = Bukkit.createInventory(new PlayerListMenuHolder(), 27, "Player list");
        targetPLayer.openInventory(newInventory);
        Collection<? extends Player> onlinePlayersCollection = getServer().getOnlinePlayers();
        Object[] onlinePlayers = onlinePlayersCollection.toArray();

        // Creating "exit" and "next" items
        ItemStack exitItem = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        exitItem = setItemStackName(exitItem, "Exit");
        newInventory.setItem(22, exitItem);

        if (page > 1) {
            ItemStack menuBackItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            menuBackItem = setItemStackName(menuBackItem, "Go to page " + (page - 1));
            newInventory.setItem(18, menuBackItem);
            playerCountLimit = playerCountLimit - 1;
        }
        if (onlinePlayersCollection.size() - (playerCountLimit * (page - 1))> playerCountLimit) {
            ItemStack menuNextItem = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            menuNextItem = setItemStackName(menuNextItem, "Go to page " + (page + 1));
            newInventory.setItem(26, menuNextItem);
            playerCountLimit = playerCountLimit - 1;
        }

        int playerOffset = playerCountLimit * (page - 1);
        // Iterating through players and displaying them in inventory
        try {
            for (int playerIndexInArray = 0; playerIndexInArray < playerCountLimit; playerIndexInArray++) {
                if (playerIndexInArray + playerOffset == onlinePlayersCollection.size()) {
                    break;
                }
                // Selecting a player from list with page offset
                Player lookedPlayer = (Player) onlinePlayers[playerIndexInArray + playerOffset];
                // Creating player head
                ItemStack targetPlayerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta tphMeta = (SkullMeta) targetPlayerHead.getItemMeta();
                tphMeta.setDisplayName(ChatColor.RESET + lookedPlayer.getName());
                tphMeta.setOwningPlayer(lookedPlayer);
                targetPlayerHead.setItemMeta(tphMeta);
                // Adding head to inventory
                newInventory.addItem(targetPlayerHead);
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            buildPlayerListMenu(targetPLayer, page - 1);
        }
    }



    public void buildPlayerControlMenu(Player playerToControl, Player playerToBeControlled) {
        Inventory inventory = Bukkit.createInventory(new PlayerControlPanelMenuHolder(), InventoryType.CHEST, "Selected player: " + playerToBeControlled.getName());
        playerToControl.openInventory(inventory);
        ItemStack killCommandItem = new ItemStack(Material.IRON_SWORD, 1);
        killCommandItem = setItemStackName(killCommandItem, "Kill player: " + playerToBeControlled.getName());
        inventory.setItem(11, killCommandItem);
        ItemStack banCommandItem = new ItemStack(Material.REDSTONE_BLOCK, 1);
        banCommandItem = setItemStackName(banCommandItem, "Ban player: " + playerToBeControlled.getName());
        banCommandItem.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        inventory.setItem(12, banCommandItem);
    }
}
