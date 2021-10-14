package me.lloyd26.teleportnotify.dependencies;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import me.lloyd26.teleportnotify.TeleportNotify;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Objects;

public class EssentialsConvertWarps {

    static TeleportNotify plugin = TeleportNotify.getPlugin();

    public static void convertWarps(CommandSender sender) {
        Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.warp.convert.attempting")));
        for (int i = 0; i < essentials.getWarps().getCount(); i++) {
            String warpName = (String) essentials.getWarps().getList().toArray()[i];
            Location warp = null;
            FileConfiguration config = plugin.customConfig.getConfig("/warps/" + warpName + ".yml");
            try {
                warp = essentials.getWarps().getWarp(warpName);
            } catch (WarpNotFoundException | InvalidWorldException e) {
                e.printStackTrace();
            }
            plugin.customConfig.createConfig("/warps/" + warpName + ".yml");
            config.set("world", warp.getWorld().getName());
            config.set("x", warp.getX());
            config.set("y", warp.getY());
            config.set("z", warp.getZ());
            config.set("yaw", warp.getYaw());
            config.set("pitch", warp.getPitch());
            config.set("name", warpName);
            try {
                config.set("lastowner", essentials.getWarps().getLastOwner(warpName).toString());
            } catch (WarpNotFoundException e) {
                e.printStackTrace();
            }
            plugin.customConfig.saveConfig("/warps/" + warpName + ".yml", config);
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.warp.convert.converted")
            .replace("%converted%", String.valueOf(Objects.requireNonNull(new File(plugin.getDataFolder() + "/warps/").list()).length))
            .replace("%total%", String.valueOf(essentials.getWarps().getCount()))));
    }
}
