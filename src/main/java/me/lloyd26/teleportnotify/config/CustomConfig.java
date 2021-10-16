package me.lloyd26.teleportnotify.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private final JavaPlugin plugin;
    public CustomConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration createConfig(String name) {
        File file = new File(plugin.getDataFolder(), name);

        if (!name.endsWith(".yml")) {
            name = name + ".yml";
        }

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            //plugin.saveResource(name, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig(String name) {
        File file = new File(plugin.getDataFolder(), name);
        if (!file.exists()) {
            createConfig(name);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveDefaultConfig(String name) {
        File file = new File (plugin.getDataFolder(), name);

        if (!name.endsWith(".yml")) {
            name = name + ".yml";
        }

        if (!file.exists()) {
            plugin.saveResource(name, false);
        }
    }

    public void saveConfig(String name, FileConfiguration config) {
        File file = new File (plugin.getDataFolder(), name);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
