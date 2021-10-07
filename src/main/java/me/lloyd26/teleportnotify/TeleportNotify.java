package me.lloyd26.teleportnotify;

import me.lloyd26.teleportnotify.commands.TeleportCommand;
import me.lloyd26.teleportnotify.commands.TeleportHereCommand;
import me.lloyd26.teleportnotify.commands.TeleportAllCommand;
import me.lloyd26.teleportnotify.commands.TPNotifyCommand;
import me.lloyd26.teleportnotify.utils.UpdateChecker;
import me.lloyd26.teleportnotify.utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeleportNotify extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("TeleportNotify enabled!");

        // Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Commands
        getCommand("tpnotify").setExecutor(new TPNotifyCommand(this));
        getCommand("tp").setExecutor(new TeleportCommand(this));
        getCommand("tphere").setExecutor(new TeleportHereCommand(this));
        getCommand("tpall").setExecutor(new TeleportAllCommand(this));

        // Metrics
        int pluginId = 11255;
        Metrics metrics = new Metrics(this, pluginId);

        new UpdateChecker(this, 90803).getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage("[TeleportNotify] " + Utils.getAccentColor() + "TeleportNotify " + Utils.getPrimaryColor() + "has an update available!");
            } else {
                Bukkit.getConsoleSender().sendMessage("[TeleportNotify] " + Utils.getPrimaryColor() + "You are running the " + Utils.getAccentColor() + "latest " + Utils.getPrimaryColor() + "version!");
            }
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("TeleportNotify disabled!");
    }
}
