package me.lloyd26.teleportnotify.utils;

import me.lloyd26.teleportnotify.TeleportNotify;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Collection;

public class TeleportUtil {

    private final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    CommandSender executor;
    Player player;
    String x;
    String y;
    String z;
    String yaw;
    String pitch;
    Location location;
    String usage;
    String playerMessage;
    String targetMessage;
    String staffMessage;
    Collection<? extends Player> onlinePlayers;

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

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public void setUsage(String usage) { this.usage = usage; }

    public String getPlayerMessage() { return playerMessage; }

    public void setPlayerMessage(String playerMessage) { this.playerMessage = playerMessage; }

    public String getTargetMessage() { return targetMessage; }

    public void setTargetMessage(String targetMessage) { this.targetMessage = targetMessage; }

    public String getStaffMessage() { return staffMessage; }

    public void setStaffMessage(String staffMessage) { this.staffMessage = staffMessage; }

    public Collection<? extends Player> getOnlinePlayers() { return onlinePlayers; }

    public TeleportUtil(CommandSender executor, Player player, String x, String y, String z) {
        this(executor, x, y, z);
        this.player = player;
    }

    public TeleportUtil(CommandSender executor, Player player, String x, String y, String z, String yaw, String pitch) {
        this(executor, player, x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public TeleportUtil(CommandSender executor, String x, String y, String z) {
        this.executor = executor;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public TeleportUtil(CommandSender executor, String x, String y, String z, String yaw, String pitch) {
        this(executor, x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public TeleportUtil(CommandSender executor, Player player, Location location) {
        this.executor = executor;
        this.player = player;
        this.location = location;
    }

    public TeleportUtil(CommandSender executor, Collection<? extends Player> onlinePlayers, Location location) {
        this.executor = executor;
        this.onlinePlayers = onlinePlayers;
        this.location = location;
    }

    public TeleportUtil(CommandSender executor, Collection<? extends Player> onlinePlayers, String x, String y, String z) {
        this(executor, x, y, z);
        this.onlinePlayers = onlinePlayers;
    }

    public void teleportPlayer() {
        if (getLocation() == null) {
            if (Utils.isValidCoord(getX()) && Utils.isValidCoord(getY()) && Utils.isValidCoord(getZ())) {
                final double coordX = getX().startsWith("~") ? ((getPlayer() != null ? getPlayer() : (Player) getExecutor()).getLocation().getX()) + (getX().length() > 1 ? Double.parseDouble(getX().substring(1)) : 0) : Double.parseDouble(getX());
                final double coordY = getY().startsWith("~") ? ((getPlayer() != null ? getPlayer() : (Player) getExecutor()).getLocation().getY()) + (getY().length() > 1 ? Double.parseDouble(getY().substring(1)) : 0) : Double.parseDouble(getY());
                final double coordZ = getZ().startsWith("~") ? ((getPlayer() != null ? getPlayer() : (Player) getExecutor()).getLocation().getZ()) + (getZ().length() > 1 ? Double.parseDouble(getZ().substring(1)) : 0) : Double.parseDouble(getZ());
                Location loc = new Location((getPlayer() != null ? getPlayer() : (Player) getExecutor()).getWorld(), coordX, coordY, coordZ, (getPlayer() != null ? getPlayer() : (Player) getExecutor()).getLocation().getYaw(), (getPlayer() != null ? getPlayer() : (Player) getExecutor()).getLocation().getPitch());
                if (getYaw() != null && getPitch() != null && Utils.isValidCoord(getYaw()) && Utils.isValidCoord(getPitch())) {
                    final float coordYaw = getYaw().startsWith("~") ? (getPlayer() != null ? getPlayer() : (Player) getExecutor()).getLocation().getYaw() + (getYaw().length() > 1 ? Float.parseFloat(getYaw().substring(1)) : 0) : Float.parseFloat(getYaw());
                    final float coordPitch = getPitch().startsWith("~") ? (getPlayer() != null ? getPlayer() : (Player) getExecutor()).getLocation().getPitch() + (getPitch().length() > 1 ? Float.parseFloat(getPitch().substring(1)) : 0) : Float.parseFloat(getPitch());
                    loc.setYaw(coordYaw);
                    loc.setPitch(coordPitch);
                }
                String coordsX, coordsY, coordsZ;
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                coordsX = decimalFormat.format(coordX);
                coordsY = decimalFormat.format(coordY);
                coordsZ = decimalFormat.format(coordZ);
                String coords = coordsX + " " + coordsY + " " + coordsZ;
                if (getPlayer() != null) {
                    getPlayer().teleport(loc);
                } else if (getOnlinePlayers() != null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.teleport(loc);
                    }
                }
                if (getExecutor().hasPermission("tpnotify.notify.admin")) {
                    if (getPlayerMessage() != null) getExecutor().sendMessage(ChatColor.translateAlternateColorCodes('&', getPlayerMessage().replace("%coords%", coords)));
                    if (getPlayer() != null) {
                        if (getTargetMessage() != null && getPlayer().hasPermission("tpnotify.notify.admin")) {
                            getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', getTargetMessage().replace("%coords%", coords)));
                        }
                    }
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.hasPermission("tpnotify.notify.admin")) {
                            if (!p.getName().equals(getExecutor().getName())) p.sendMessage(ChatColor.translateAlternateColorCodes('&', getTargetMessage().replace("%coords%", coords)));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getStaffMessage().replace("%coords%", coords)));
                        }
                    }
                    Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', getStaffMessage().replace("%coords%", coords)));
                }
            } else {
                getExecutor().sendMessage(Utils.setUsage("/teleport [player] <x> <y> <z> [yaw] [pitch]"));
            }
        } else {
            if (getPlayer() != null) {
                getPlayer().teleport(getLocation());
            } else if (getOnlinePlayers() != null) {
                for (Player p : getOnlinePlayers()) {
                    p.teleport(getLocation());
                }
            }
            if (getExecutor().hasPermission("tpnotify.notify.admin")) {
                if (getPlayerMessage() != null) getExecutor().sendMessage(ChatColor.translateAlternateColorCodes('&', getPlayerMessage()));
                if (getPlayer() != null) {
                    if (getTargetMessage() != null && getPlayer().hasPermission("tpnotify.notify.admin")) {
                        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', getTargetMessage()));
                    }
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.hasPermission("tpnotify.notify.admin")) {
                        if (!p.getName().equals(getExecutor().getName())) p.sendMessage(ChatColor.translateAlternateColorCodes('&', getTargetMessage()));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getStaffMessage()));
                    }
                }
                Utils.broadcastToConsole(ChatColor.translateAlternateColorCodes('&', getStaffMessage()));
            }
        }
    }
}
