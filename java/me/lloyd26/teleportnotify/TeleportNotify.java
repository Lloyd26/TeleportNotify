package me.lloyd26.teleportnotify;

import me.lloyd26.teleportnotify.commands.tp;
import me.lloyd26.teleportnotify.commands.tphere;
import me.lloyd26.teleportnotify.commands.tpall;
import me.lloyd26.teleportnotify.commands.tpnotify;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeleportNotify extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("TeleportNotify enabled!");
        //Bukkit.broadcastMessage(ChatColor.GREEN + "Plugin loaded!");

        // Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Commands
        getCommand("tpnotify").setExecutor(new tpnotify());
        getCommand("tp").setExecutor(new tp());
        getCommand("tphere").setExecutor(new tphere());
        getCommand("tpall").setExecutor(new tpall());

        // Listeners
        //getServer().getPluginManager().registerEvents(new StaffWorldChangeEvent(), this);

        // Metrics
        int pluginId = 11255;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("TeleportNotify disabled!");
    }
}
