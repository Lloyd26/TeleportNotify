package me.lloyd26.teleportnotify.commands;

import me.lloyd26.teleportnotify.TeleportNotify;
import me.lloyd26.teleportnotify.utils.Error;
import me.lloyd26.teleportnotify.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPNotifyCommand implements CommandExecutor {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

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
            } else {
                sender.sendMessage(Utils.setUsage("/tpnotify reload"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOPERMISSION)));
        }
        return true;
    }
}
