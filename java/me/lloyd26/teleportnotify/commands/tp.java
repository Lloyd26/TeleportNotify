package me.lloyd26.teleportnotify.commands;

import me.lloyd26.teleportnotify.TeleportNotify;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class tp implements CommandExecutor {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerName = player.getName();
            if (player.hasPermission("tpnotify.tp.use")) {
                try {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Too few arguments!");
                        player.sendMessage(ChatColor.RED + "Usages:");
                        player.sendMessage(ChatColor.RED + " - /tp <player>");
                        player.sendMessage(ChatColor.RED + " - /tp <player1> <player2>");
                    } else if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        String targetName = target.getName();
                        player.teleport(target.getLocation());
                        //player.sendMessage(ChatColor.YELLOW + "You've teleported to " + ChatColor.DARK_AQUA + targetName);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.player").replaceAll("%target%", targetName)));
                        //target.sendMessage(ChatColor.DARK_AQUA + playerName + ChatColor.YELLOW + " teleported to you");
                        if (target.hasPermission("tpnotify.notify.admin")) {
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.target").replaceAll("%player%", playerName)));
                        }
                        if (player.hasPermission("tpnotify.notify.admin")) {
                            //if (player.isOp()) {
                            //Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&7&o[" + playerName + ": &eteleported to " + targetName + "&7&o]"), "tpnotify.notify.admin");
                            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.staff").replaceAll("%player%", playerName).replaceAll("%target%", targetName)), "tpnotify.notify.admin");
                            System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.1player.staff").replaceAll("%player%", playerName).replaceAll("%target%", targetName)));
                            //Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&7&o[" + playerName + ": &e&oteleported to " + args[0] + "&7&o]"));
                        }
                    } else if (args.length == 2) {
                        Player playerToSend = Bukkit.getPlayer(args[0]);
                        Player target = Bukkit.getPlayer(args[1]);
                        String player1Name = playerToSend.getName();
                        String player2Name = target.getName();
                        playerToSend.teleport(target.getLocation());
                        //player.sendMessage(ChatColor.YELLOW + "You've teleported " + ChatColor.DARK_AQUA + player1Name + ChatColor.YELLOW + " to " + ChatColor.DARK_AQUA + player2Name);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player").replaceAll("%player1%", player1Name).replaceAll("%player2%", player2Name)));
                        //playerToSend.sendMessage(ChatColor.YELLOW + "You were teleported to " + ChatColor.DARK_AQUA + player2Name + ChatColor.YELLOW + " by " + ChatColor.DARK_AQUA + playerName);
                        playerToSend.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player1").replaceAll("%player2%", player2Name).replaceAll("%player%", playerName)));
                        //target.sendMessage(ChatColor.DARK_AQUA + player1Name + ChatColor.YELLOW + " was teleported to you by " + ChatColor.DARK_AQUA + playerName);
                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.player2").replaceAll("%player1%", player1Name).replaceAll("%player%", playerName)));
                        if (player.hasPermission("tpnotify.notify.admin")) {
                            //if (player.isOp()) {
                            //Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&7&o[" + playerName + ": &e&oteleported " + player1Name + " to " + player2Name + "&7&o]"), "tpnotify.notify.admin");
                            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.staff").replaceAll("%player%", playerName).replaceAll("%player1%", player1Name).replaceAll("%player2%", player2Name)), "tpnotify.notify.admin");
                            System.out.println((ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.2player.staff").replaceAll("%player%", playerName).replaceAll("%player1%", player1Name).replaceAll("%player2%", player2Name))));
                            //Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&7&o[" + playerName + ": &e&oteleported " + args[0] + " to " + args[1] + "&7&o]"));
                        }
                    } else if (args.length == 3) {
                        final double x = args[0].startsWith("~") ? player.getLocation().getX() + (args[0].length() > 1 ? Double.parseDouble(args[0].substring(1)) : 0) : Double.parseDouble(args[0]);
                        final double y = args[1].startsWith("~") ? player.getLocation().getY() + (args[1].length() > 1 ? Double.parseDouble(args[1].substring(1)) : 0) : Double.parseDouble(args[1]);
                        final double z = args[2].startsWith("~") ? player.getLocation().getZ() + (args[2].length() > 1 ? Double.parseDouble(args[2].substring(1)) : 0) : Double.parseDouble(args[2]);
                        Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
                        player.teleport(loc);
                        //player.sendMessage(ChatColor.GOLD + "Teleporting...");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.player")));
                    }
                } catch (NullPointerException e) {
                    player.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Player not found!");
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
