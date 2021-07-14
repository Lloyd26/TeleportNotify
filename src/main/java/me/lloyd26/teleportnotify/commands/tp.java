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

public class tp implements CommandExecutor {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender.hasPermission("tpnotify.tp.use")) {
                if (args.length == 0) {
                    sender.sendMessage(Utils.setUsage("/teleport <player> " + (sender instanceof Player ? "[player] / [player]" : "<target> / <player>") + " <x> <y> <z> [<yaw> <pitch>]"));
                } else if (args.length == 1 || args.length == 2) {
                    if (args.length == 1 && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0])) && sender instanceof Player) {
                        Player player = (Player) sender;
                        Player target = Bukkit.getPlayer(args[0]);
                        String targetName = target.getName();
                        player.teleport(target.getLocation());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.player").replace("%target%", targetName)));
                        if (target.hasPermission("tpnotify.notify.receive")) {
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.target").replace("%player%", sender.getName())));
                        }
                        if (player.hasPermission("tpnotify.notify.notify")) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.hasPermission("tpnotify.notify.receive")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.staff").replace("%player%", sender.getName()).replace("%target%", targetName)));
                                }
                            }
                            Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.staff").replace("%player%", sender.getName()).replace("%target%", targetName)));
                        }
                    } else if (args.length == 1 && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERSONLY)));
                    } else if (args.length == 2 && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                        Player playerToSend = Bukkit.getPlayer(args[0]);
                        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
                            Player target = Bukkit.getPlayer(args[1]);
                            String player1Name = playerToSend.getName();
                            String player2Name = target.getName();
                            playerToSend.teleport(target.getLocation());
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player").replace("%player1%", player1Name).replace("%player2%", player2Name)));
                            playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player1").replace("%player2%", player2Name).replace("%player%", (sender instanceof ConsoleCommandSender ? Utils.getConsoleName() : sender.getName()))));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player2").replace("%player1%", player1Name).replace("%player%", (sender instanceof ConsoleCommandSender ? Utils.getConsoleName() : sender.getName()))));
                            if (sender.hasPermission("tpnotify.notify.notify")) {
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (p.hasPermission("tpnotify.notify.receive")) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.staff").replace("%player%", (sender instanceof ConsoleCommandSender ? Utils.getConsoleName() : sender.getName())).replace("%player1%", player1Name).replace("%player2%", player2Name)));
                                    }
                                }
                                Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.staff").replace("%player%", (sender instanceof ConsoleCommandSender ? Utils.getConsoleName() : sender.getName())).replace("%player1%", player1Name).replace("%player2%", player2Name)));
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERNOTFOUND).replace("%player%", args[1])));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERNOTFOUND).replace("%player%", args[0])));
                    }
                } else if (args.length == 3 || args.length == 5 || args.length == 4 || args.length == 6) {
                    TeleportUtil teleportUtil = new TeleportUtil(sender, args[0], args[1], args[2]);
                    teleportUtil.setPlayerMessage(plugin.getConfig().getString("messages.commands.tp.coords.self.player").replace("%player%", sender.getName()));
                    teleportUtil.setStaffMessage(plugin.getConfig().getString("messages.commands.tp.coords.self.staff").replace("%player%", sender.getName()));
                    if (sender instanceof Player) teleportUtil.setPlayer((Player) sender);
                    if (args.length == 5) {
                        teleportUtil.setYaw(args[3]);
                        teleportUtil.setPitch(args[4]);
                    } else if (args.length == 4 || args.length == 6) {
                        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                            teleportUtil.setPlayer(Bukkit.getPlayer(args[0]));
                            teleportUtil.setX(args[1]);
                            teleportUtil.setY(args[2]);
                            teleportUtil.setZ(args[3]);
                            if (Bukkit.getPlayer(args[0]).getName().equals(sender.getName())) {
                                teleportUtil.setPlayerMessage(plugin.getConfig().getString("messages.commands.tp.coords.self.player").replace("%player%", sender.getName()));
                                teleportUtil.setStaffMessage(plugin.getConfig().getString("messages.commands.tp.coords.self.staff").replace("%player%", sender.getName()));
                            } else {
                                teleportUtil.setPlayerMessage(plugin.getConfig().getString("messages.commands.tp.coords.other.player").replace("%player%", Bukkit.getPlayer(args[0]).getName()));
                                teleportUtil.setTargetMessage(plugin.getConfig().getString("messages.commands.tp.coords.other.target").replace("%player%", (sender instanceof ConsoleCommandSender ? Utils.getConsoleName() : sender.getName())));
                                teleportUtil.setStaffMessage(plugin.getConfig().getString("messages.commands.tp.coords.other.staff").replace("%player%", (sender instanceof ConsoleCommandSender ? Utils.getConsoleName() : sender.getName())).replace("%target%", Bukkit.getPlayer(args[0]).getName()));
                            }
                            if (args.length == 6) {
                                teleportUtil.setYaw(args[4]);
                                teleportUtil.setPitch(args[5]);
                            }
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERNOTFOUND).replace("%player%", args[0])));
                            return true;
                        }
                    }
                    if ((args.length == 3 || args.length == 5) && !(sender instanceof Player)) {
                        sender.sendMessage(Utils.setUsage("/teleport <player> <x> <y> <z> [<yaw> <pitch>]"));
                    } else {
                        teleportUtil.teleportPlayer();
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERNOTFOUND).replace("%player%", args[0])));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOPERMISSION)));
            }
        return true;
    }
}
