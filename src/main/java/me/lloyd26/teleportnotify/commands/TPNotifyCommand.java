package me.lloyd26.teleportnotify.commands;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import me.lloyd26.teleportnotify.TeleportNotify;
import me.lloyd26.teleportnotify.dependencies.EssentialsConvertWarps;
import me.lloyd26.teleportnotify.utils.Error;
import me.lloyd26.teleportnotify.utils.Utils;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TPNotifyCommand implements CommandExecutor {

    private final TeleportNotify plugin;

    public TPNotifyCommand(TeleportNotify plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("tpnotify.reload")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                sender.sendMessage(Utils.getAccentColor() + "TeleportNotify" + Utils.getPrimaryColor() + " reloaded!");
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    plugin.getLogger().info(Utils.getAccentColor() + "TeleportNotify" + Utils.getPrimaryColor() + " reloaded by " + Utils.getAccentColor() + player.getName() + Utils.getPrimaryColor() + "!");
                }
            } else if (args.length == 1 && args[0].equalsIgnoreCase("convert-warps")) {
                if (Bukkit.getServer().getPluginManager().getPlugin("Essentials") != null) {
                    EssentialsConvertWarps.convertWarps(sender);
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.ESSENTIALSNOTFOUND)));
                }
            } else {
                sender.sendMessage(Utils.setUsage("/tpnotify reload"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOPERMISSION)));
        }
        return true;
    }
}
