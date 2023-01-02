package com.eyren24.screenshare.tools;

import org.bukkit.ChatColor;

public class Color {
    public static String chatColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String replaceVariable(String str, String what) {
        str.replace("%position%", what).replace("%playername%", what);
        return str;
    }

}

