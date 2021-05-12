package me.lloyd26.teleportnotify.commands;

import me.lloyd26.teleportnotify.TeleportNotify;
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
                try {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.DARK_RED + "Error: " + ChatColor.RED + "Too few arguments!");
                        player.sendMessage(ChatColor.RED + "Usages:");
                        player.sendMessage(ChatColor.RED + " - /tphere <player>");
                    } else if (args.length == 1) {
                        Player target = Bukkit.getPlayer(args[0]);
                        String targetName = target.getName();
                        target.teleport(player.getLocation());
                        //player.sendMessage(ChatColor.YELLOW + "You've teleported " + ChatColor.DARK_AQUA + args[0] + ChatColor.YELLOW + " to you");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tphere.player").replaceAll("%target%", targetName)));
                        //target.sendMessage(ChatColor.YELLOW + "You were teleported to " + ChatColor.DARK_AQUA + args[0]);
                        if (target.hasPermission("tpnotify.notify.admin")) {
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tphere.target").replaceAll("%player%", playerName)));
                        }
                        if (player.hasPermission("tpnotify.notify.admin")) {
                            //if (player.isOp()) {
                            //Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', "&7&o[" + player.getName() + ": &eteleported " + args[0] + " to " + player.getName() + "&7&o]"), "tpnotify.notify.admin");
                            Bukkit.broadcast(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tphere.staff").replaceAll("%player%", playerName).replaceAll("%target%", targetName)), "tpnotify.notify.admin");
                            System.out.println((ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tphere.staff").replaceAll("%player%", playerName).replaceAll("%target%", targetName))));
                            //Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&7&o[" + player.getName() + ": &e&oteleported " + args[0] + " to " + args[1] + "&7&o]"));
                        }
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
