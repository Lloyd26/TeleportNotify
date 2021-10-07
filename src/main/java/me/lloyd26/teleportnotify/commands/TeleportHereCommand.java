package me.lloyd26.teleportnotify.commands;

import me.lloyd26.teleportnotify.TeleportNotify;
import me.lloyd26.teleportnotify.utils.Error;
import me.lloyd26.teleportnotify.utils.TeleportUtil;
import me.lloyd26.teleportnotify.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportHereCommand implements CommandExecutor {

    private final TeleportNotify plugin;

    public TeleportHereCommand(TeleportNotify plugin) {
        this.plugin = plugin;
    }

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
                        TeleportUtil teleportUtil = new TeleportUtil(sender, player, target);
                        teleportUtil.setExecutorMessage(plugin.getConfig().getString("messages.commands.tphere.player").replaceAll("%target%", target.getName()));
                        teleportUtil.setTargetMessage(plugin.getConfig().getString("messages.commands.tphere.target").replaceAll("%player%", playerName));
                        teleportUtil.setStaffMessage(plugin.getConfig().getString("messages.commands.tphere.staff").replaceAll("%player%", playerName).replaceAll("%target%", target.getName()));
                        teleportUtil.teleportPlayer();
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
