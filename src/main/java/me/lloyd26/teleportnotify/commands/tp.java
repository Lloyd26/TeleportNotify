package me.lloyd26.teleportnotify.commands;

import me.lloyd26.teleportnotify.TeleportNotify;
import me.lloyd26.teleportnotify.utils.Error;
import me.lloyd26.teleportnotify.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class tp implements CommandExecutor {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getName();
            if (player.hasPermission("tpnotify.tp.use")) {
                if (args.length == 0) {
                    player.sendMessage(Utils.setUsage("/teleport <player> [player] / <x> <y> <z> [yaw] [pitch]"));
                } else if (args.length == 1 || args.length == 2) {
                    if (args.length == 1 && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                        Player target = Bukkit.getPlayer(args[0]);
                        String targetName = target.getName();
                        player.teleport(target.getLocation());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.player").replace("%target%", targetName)));
                        if (target.hasPermission("tpnotify.notify.admin")) {
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.target").replace("%player%", playerName)));
                        }
                        if (player.hasPermission("tpnotify.notify.admin")) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.hasPermission("tpnotify.notify.admin")) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.staff").replace("%player%", playerName).replace("%target%", targetName)));
                                }
                            }
                            Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.staff").replace("%player%", playerName).replace("%target%", targetName)));
                        }
                    } else if (args.length == 2 && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                        Player playerToSend = Bukkit.getPlayer(args[0]);
                        if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
                            Player target = Bukkit.getPlayer(args[1]);
                            String player1Name = playerToSend.getName();
                            String player2Name = target.getName();
                            playerToSend.teleport(target.getLocation());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player").replace("%player1%", player1Name).replace("%player2%", player2Name)));
                            playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player1").replace("%player2%", player2Name).replace("%player%", playerName)));
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player2").replace("%player1%", player1Name).replace("%player%", playerName)));
                            if (player.hasPermission("tpnotify.notify.admin")) {
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (p.hasPermission("tpnotify.notify.admin")) {
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.staff").replace("%player%", playerName).replace("%player1%", player1Name).replace("%player2%", player2Name)));
                                    }
                                }
                                Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.staff").replace("%player%", playerName).replace("%player1%", player1Name).replace("%player2%", player2Name)));
                            }
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERNOTFOUND).replace("%player%", args[1])));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERNOTFOUND).replace("%player%", args[0])));
                    }
                } else if (args.length == 3 || args.length == 5) {
                    if (Utils.isValidCoord(args[0]) && Utils.isValidCoord(args[1]) && Utils.isValidCoord(args[2])) {
                        final double x = args[0].startsWith("~") ? player.getLocation().getX() + (args[0].length() > 1 ? Double.parseDouble(args[0].substring(1)) : 0) : Double.parseDouble(args[0]);
                        final double y = args[1].startsWith("~") ? player.getLocation().getY() + (args[1].length() > 1 ? Double.parseDouble(args[1].substring(1)) : 0) : Double.parseDouble(args[1]);
                        final double z = args[2].startsWith("~") ? player.getLocation().getZ() + (args[2].length() > 1 ? Double.parseDouble(args[2].substring(1)) : 0) : Double.parseDouble(args[2]);
                        final float yaw;
                        final float pitch;
                        Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
                        if (args.length == 5) {
                            if (Utils.isValidCoord(args[3]) && Utils.isValidCoord(args[4])) {
                                yaw = args[3].startsWith("~") ? player.getLocation().getYaw() + (args[3].length() > 1 ? Float.parseFloat(args[3].substring(1)) : 0) : Float.parseFloat(args[3]);
                                pitch = args[4].startsWith("~") ? player.getLocation().getPitch() + (args[4].length() > 1 ? Float.parseFloat(args[4].substring(1)) : 0) : Float.parseFloat(args[4]);
                                loc.setYaw(yaw);
                                loc.setPitch(pitch);
                            } else {
                                player.sendMessage("/teleport <x> <y> <z> [yaw] [pitch]");
                            }
                        }
                        String coordsX, coordsY, coordsZ;
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        coordsX = decimalFormat.format(x);
                        coordsY = decimalFormat.format(y);
                        coordsZ = decimalFormat.format(z);
                        String coords = coordsX + " " + coordsY + " " + coordsZ;
                        player.teleport(loc);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.player").replace("%coords%", coords)));
                        if (player.hasPermission("tpnotify.notify.admin")) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.hasPermission("tpnotify.notify.admin")) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.staff").replace("%player%", playerName).replace("%coords%", coords)));
                                }
                            }
                            Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.staff").replace("%player%", playerName).replace("%coords%", coords)));
                        }
                    } else {
                        player.sendMessage(Utils.setUsage("/teleport <x> <y> <z> [yaw] [pitch]"));
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
