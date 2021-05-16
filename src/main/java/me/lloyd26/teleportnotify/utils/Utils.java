package me.lloyd26.teleportnotify.utils;

import me.lloyd26.teleportnotify.TeleportNotify;

public class Utils {

    private static final TeleportNotify plugin = TeleportNotify.getPlugin(TeleportNotify.class);

    public static void broadcastToConsole(String message) {
        if (plugin.getConfig().getBoolean("config.broadcast-to-console")) System.out.println(message);
    }
}
