package aether.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class openerCommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender comsender, @NotNull Command command, @NotNull String label, String[] args) {
        if (comsender instanceof Player) {
            Player sender = (Player) comsender;
            MenuBuilder mb = new MenuBuilder();
            mb.buildMainMenu(sender);
        }
        return true;
    }
}
