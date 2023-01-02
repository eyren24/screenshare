package com.eyren24.screenshare.tools;

import org.bukkit.BanList;
import org.bukkit.Bukkit;

import java.util.Date;

public class BanPlayer {

    public static void banPlayer(String target, String reason, long expires, String source) {
        if (expires == -1) {
            Bukkit.getBanList(BanList.Type.NAME).addBan(target, reason, null, source);
            return;
        }
        Date date = new Date(System.currentTimeMillis() + 60 * expires * 1000);
        Bukkit.getBanList(BanList.Type.NAME).addBan(target, reason, date, source);
    }
}
