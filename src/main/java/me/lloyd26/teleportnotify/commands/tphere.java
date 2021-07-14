package me.lloyd26.teleportnotify.commands;

import me.lloyd26.teleportnotify.TeleportNotify;
import me.lloyd26.teleportnotify.utils.Error;
import me.lloyd26.teleportnotify.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tphere implements CommandExecutor {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getName();
            if (player.hasPermission("tpnotify.tphere.use")) {
                if (args.length == 0) {
                    player.sendMessage(Utils.setUsage("/teleporthere <player>"));
                } else if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                    if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        String targetName = target.getName();
                        target.teleport(player.getLocation());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tphere.player").replaceAll("%target%", targetName)));
                        if (target.hasPermission("tpnotify.notify.receive")) {
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tphere.target").replaceAll("%player%", playerName)));
                        }
                        if (player.hasPermission("tpnotify.notify.notify")) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.hasPermission("tpnotify.notify.receive")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tphere.staff").replaceAll("%player%", playerName).replaceAll("%target%", targetName)));
                                }
                            }
                            Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tphere.staff").replaceAll("%player%", playerName).replaceAll("%target%", targetName)));
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERNOTFOUND).replace("%player%", args[0])));
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOPERMISSION)));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERSONLY)));
        }
        return true;
    }
}
