package me.lloyd26.teleportnotify.commands;

import me.lloyd26.teleportnotify.TeleportNotify;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tpall implements CommandExecutor {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getName();
            if (player.hasPermission("tpnotify.tpall.use")) {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    p.teleport(player.getLocation());
                    if (p.hasPermission("tpnotfy.notify.admin")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tpall.target").replaceAll("%player%", playerName)));
                    }
                }
                if (player.hasPermission("tpnotify.notify.admin")) {
                    Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tpall.staff").replaceAll("%player%", playerName)), "tpnotify.notify.admin");
                    System.out.println((ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tpall.staff").replaceAll("%player%", playerName))));
                }
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Insufficient permission!");
            }
        } else {
            sender.sendMessage("You need to be a player to execute this command!");
        }
        return true;
    }
}
