package me.lloyd26.teleportnotify.commands;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;
import me.lloyd26.teleportnotify.TeleportNotify;
import me.lloyd26.teleportnotify.utils.Error;
import me.lloyd26.teleportnotify.utils.Utils;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final TeleportNotify plugin;

    public WarpCommand(TeleportNotify plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("tpnotify.warp.use")) {
            Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            if (args.length == 0) {
                //sender.sendMessage(Utils.setUsage("/warp <warp> " + (sender instanceof Player ? "[player]" : "<player>")));
                if (essentials.getWarps().getCount() != 0) {
                    StringBuilder sb = new StringBuilder();
                    boolean isWarpPermissionEnabled = plugin.getConfig().getBoolean("config.permission-based-warps");
                    for (String s : essentials.getWarps().getList()) {
                        if (!isWarpPermissionEnabled) {
                            sender.hasPermission("tpnotify.warps." + s);
                        }
                        if (plugin.getConfig().getBoolean("config.permission-based-warps") == sender.hasPermission("tpnotify.warps." + s)) {
                            sb.append(Utils.getAccentColor()).append(s).append(ChatColor.GRAY).append(", ");
                        }
                    }
                    if (sb.length() != 0) {
                        sb.deleteCharAt(sb.length() - 1);
                        sb.deleteCharAt(sb.length() - 1);

                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.warp.list")
                                .replace("%warps%", sb.toString().trim())));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOWARPSAVAILABLE)));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOWARPSDEFINED)));
                }
            } else if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (essentials.getWarps().getList().contains(args[0])) {
                        try {
                            if ((!plugin.getConfig().getBoolean("config.permission-based-warps") && player.hasPermission("tpnotify.warps." + args[0])) || (plugin.getConfig().getBoolean("config.permission-based-warps") == player.hasPermission("tpnotify.warps." + args[0]))) {
                                player.teleport(essentials.getWarps().getWarp(args[0]));
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.warp.player")
                                    .replace("%warp%", args[0])));
                                if (player.hasPermission("tpnotify.notify.notify")) {
                                    String message = plugin.getConfig().getString("messages.commands.warp.staff")
                                            .replace("%player%", player.getName())
                                            .replace("%warp%", args[0]);
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        if (p.hasPermission("tpnotify.notify.receive")) {
                                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                        }
                                    }
                                    Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', message));
                                }
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOWARPPERMISSION)));
                            }
                        } catch (WarpNotFoundException e) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.WARPNOTFOUND).replace("%warp%", args[0])));
                        } catch (InvalidWorldException e) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.INVALIDWORLD)));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.WARPNOTFOUND).replace("%warp%", args[0])));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.PLAYERSONLY)));
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Utils.getErrorMessage(Error.NOPERMISSION)));
        }
        return true;
    }
}
