package me.lloyd26.teleportnotify.utils;

import me.lloyd26.teleportnotify.TeleportNotify;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    public static void broadcastToConsole(String message) {
        if (plugin.getConfig().getBoolean("config.broadcast-to-console")) System.out.println(message);
    }

    public static String setUsage(String usage) {
        Validate.notNull(usage);
        Validate.notEmpty(usage);

        Pattern pattern = Pattern.compile("(?=<)|(?=\\[)");
        Matcher matcher = pattern.matcher(usage);
        if (matcher.find()) {
            String[] args = usage.split("(?=<)|(?=\\[)", 2);
            return getPrimaryColor() + "Usage: " + getAccentColor() + args[0] + ChatColor.RESET + args[1];
        } else {
            return getPrimaryColor() + "Usage: " + getAccentColor() + usage;
        }
    }

    public static ChatColor getPrimaryColor() {
        Validate.notNull(plugin.getConfig().getString("messages.theme.primary-color"));
        return ChatColor.valueOf(plugin.getConfig().getString("messages.theme.primary-color"));
    }

    public static ChatColor getAccentColor() {
        Validate.notNull(plugin.getConfig().getString("messages.theme.accent-color"));
        return ChatColor.valueOf(plugin.getConfig().getString("messages.theme.accent-color"));
    }

    public static String getErrorMessage(Error error) {
        switch (error) {
            case PLAYERNOTFOUND:
                return plugin.getConfig().getString("messages.errors.PlayerNotFound");
            case NOPERMISSION:
                return plugin.getConfig().getString("messages.errors.NoPermission");
            case PLAYERSONLY:
                return plugin.getConfig().getString("messages.errors.PlayersOnly");
            default:
                return null;
        }
    }
}
