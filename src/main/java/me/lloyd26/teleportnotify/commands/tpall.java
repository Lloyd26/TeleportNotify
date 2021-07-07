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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class tpall implements CommandExecutor {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender.hasPermission("tpnotify.tpall.use")) {
                if (sender instanceof Player && args.length == 0) {
                    Player player = (Player) sender;
                    TeleportUtil teleportUtil = new TeleportUtil(sender, Bukkit.getOnlinePlayers(), player.getLocation());
                    teleportUtil.setTargetMessage(plugin.getConfig().getString("messages.commands.tpall.self.target").replace("%player%", player.getName()));
                    teleportUtil.setStaffMessage(plugin.getConfig().getString("messages.commands.tpall.self.staff").replace("%player%", player.getName()));
                    teleportUtil.teleportPlayer();
                } else if (args.length == 0) {
                    sender.sendMessage(Utils.setUsage("/teleportall <player> / <x> <y> <z> [<yaw> <pitch>]"));
                } else if (args.length == 1) {
                    if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                        Player player = Bukkit.getPlayer(args[0]);
                        TeleportUtil teleportUtil = new TeleportUtil(sender, Bukkit.getOnlinePlayers(), player.getLocation());
                        teleportUtil.setPlayerMessage(plugin.getConfig().getString("messages.commands.tpall.other.player").replace("%target%", player.getName()));
                        teleportUtil.setTargetMessage(plugin.getConfig().getString("messages.commands.tpall.other.target").replace("%player%", player.getName()));
                        teleportUtil.setStaffMessage(plugin.getConfig().getString("messages.commands.tpall.other.staff").replace("%player%", (sender instanceof ConsoleCommandSender ? Utils.getConsoleName() : sender.getName())).replace("%target%", player.getName()));
                        teleportUtil.teleportPlayer();
                    } else {
                        sender.sendMessage(Utils.getErrorMessage(Error.PLAYERNOTFOUND, args[0]));
                    }
                } else if (sender instanceof Player && (args.length == 3 || args.length == 5)) {
                    if (Utils.isValidCoord(args[0]) && Utils.isValidCoord(args[1]) && Utils.isValidCoord(args[2])) {
                        TeleportUtil teleportUtil = new TeleportUtil(sender, Bukkit.getOnlinePlayers(), args[0], args[1], args[2]);
                        if (args.length == 5 && Utils.isValidCoord(args[3]) && Utils.isValidCoord(args[4])) {
                            teleportUtil.setYaw(args[3]);
                            teleportUtil.setPitch(args[4]);
                        }
                        teleportUtil.setPlayerMessage(plugin.getConfig().getString("messages.commands.tpall.coords.player"));
                        teleportUtil.setTargetMessage(plugin.getConfig().getString("messages.commands.tpall.coords.target"));
                        teleportUtil.setStaffMessage(plugin.getConfig().getString("messages.commands.tpall.coords.staff").replace("%player%", sender.getName()));
                        teleportUtil.teleportPlayer();
                    }
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOPERMISSION)));
            }
        return true;
    }
}
