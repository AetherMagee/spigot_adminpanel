package aether.utils;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class UltimateAdminPanel extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("adminpanel")).setExecutor(new openerCommandHandler());
        getServer().getPluginManager().registerEvents(new menuClickHandler(), this);
    }
}
// I love how the main class in this plugin is literally one of the smallest ones XD
// OOP being OOP