package me.lloyd26.teleportnotify.commands;

import me.lloyd26.teleportnotify.TeleportNotify;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpnotify implements CommandExecutor {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("tpnotify.reload")) {
                plugin.reloadConfig();
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                    plugin.getLogger().info(ChatColor.GREEN + "Config reloaded by " + player.getName());
                }
            }
        }
        return true;
    }
}
