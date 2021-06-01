package me.lloyd26.teleportnotify.utils;

import me.lloyd26.teleportnotify.TeleportNotify;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class TeleportUtil {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    CommandSender executor;
    Player player;
    String x;
    String y;
    String z;
    String yaw;
    String pitch;

    public CommandSender getExecutor() {
        return executor;
    }

    public void setExecutor(CommandSender executor) {
        this.executor = executor;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getYaw() {
        return yaw;
    }

    public void setYaw(String yaw) {
        this.yaw = yaw;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public TeleportUtil(CommandSender executor, Player player, String x, String y, String z) {
        this.executor = executor;
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        //this.yaw = String.valueOf(getPlayer().getLocation().getYaw());
        //this.pitch = String.valueOf(getPlayer().getLocation().getPitch());
    }

    public TeleportUtil(CommandSender executor, Player player, String x, String y, String z, String yaw, String pitch) {
        this(executor, player, x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void teleportPlayer() {
        if (Utils.isValidCoord(getX()) && Utils.isValidCoord(getY()) && Utils.isValidCoord(getZ())) {
            final double coordX = getX().startsWith("~") ? getPlayer().getLocation().getX() + (getX().length() > 1 ? Double.parseDouble(getX().substring(1)) : 0) : Double.parseDouble(getX());
            final double coordY = getY().startsWith("~") ? getPlayer().getLocation().getY() + (getY().length() > 1 ? Double.parseDouble(getY().substring(1)) : 0) : Double.parseDouble(getY());
            final double coordZ = getZ().startsWith("~") ? getPlayer().getLocation().getZ() + (getZ().length() > 1 ? Double.parseDouble(getZ().substring(1)) : 0) : Double.parseDouble(getZ());
            Location loc = new Location(getPlayer().getWorld(), coordX, coordY, coordZ, getPlayer().getLocation().getYaw(), getPlayer().getLocation().getPitch());
            if (getYaw() != null && getPitch() != null && Utils.isValidCoord(getYaw()) && Utils.isValidCoord(getPitch())) {
                final float coordYaw = getYaw().startsWith("~") ? getPlayer().getLocation().getYaw() + (getYaw().length() > 1 ? Float.parseFloat(getYaw().substring(1)) : 0) : Float.parseFloat(getYaw());
                final float coordPitch = getPitch().startsWith("~") ? getPlayer().getLocation().getPitch() + (getPitch().length() > 1 ? Float.parseFloat(getPitch().substring(1)) : 0) : Float.parseFloat(getPitch());
                loc.setYaw(coordYaw);
                loc.setPitch(coordPitch);
            }
            String coordsX, coordsY, coordsZ;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            coordsX = decimalFormat.format(coordX);
            coordsY = decimalFormat.format(coordY);
            coordsZ = decimalFormat.format(coordZ);
            String coords = coordsX + " " + coordsY + " " + coordsZ;
            getPlayer().teleport(loc);
            String playerMessage, targetMessage, staffMessage;
            if (getPlayer().getName().equalsIgnoreCase(getExecutor().getName())) {
                playerMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.self.player").replace("%coords%", coords));
                staffMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.self.staff").replace("%player%", getPlayer().getName()).replace("%coords%", coords));
            } else {
                playerMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.other.player").replace("%player%", getPlayer().getName()).replace("%coords%", coords));
                targetMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.other.target")).replace("%player%", getExecutor().getName()).replace("%coords%", coords);
                staffMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.commands.tp.coords.other.staff").replace("%player%", getExecutor().getName()).replace("%target%", getPlayer().getName()).replace("%coords%", coords));
                getPlayer().sendMessage(targetMessage);
            }
            getExecutor().sendMessage(playerMessage);
            if (getPlayer().hasPermission("tpnotify.notify.admin")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("tpnotify.notify.admin")) {
                        p.sendMessage(staffMessage);
                    }
                }
                Utils.broadcastToConsole(staffMessage);
            }
        } else {
            getExecutor().sendMessage(Utils.setUsage("/teleport [player] <x> <y> <z> [yaw] [pitch]"));
        }
    }
}
