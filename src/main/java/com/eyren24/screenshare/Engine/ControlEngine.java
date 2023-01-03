package com.eyren24.screenshare.Engine;

import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.BanPlayer;
import com.eyren24.screenshare.tools.Color;
import com.eyren24.screenshare.tools.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Objects;

public class ControlEngine implements Runnable {

    private final Screenshare plugin;
    private Player admin;
    private Player hacker;

    public ControlEngine(Screenshare plugin, Player admin, Player hacker) {
        this.plugin = plugin;
        this.admin = admin;
        this.hacker = hacker;
        for (String string : Screenshare.getConfigFile().getStringList("control-start")) {
            admin.sendMessage(Color.chatColor(string).replaceAll("%player%", admin.getName()).replaceAll("%cheater%", hacker.getName()));
        }
        for (String string : Screenshare.getConfigFile().getStringList("control-start-cheater")) {
            hacker.sendMessage(Color.chatColor(string).replaceAll("%player%", admin.getName()));
        }
        // Teleport player to position
        admin.teleport(Objects.requireNonNull(FileManager.getLoc("admin")));
        hacker.teleport(Objects.requireNonNull(FileManager.getLoc("hacker")));
    }

    @Override
    public void run() {
        if (FileManager.get().getBoolean("freeze-player")){
            hacker.teleport(Objects.requireNonNull(FileManager.getLoc("hacker")));
        }
        if (!hacker.isOnline()) {
            BanPlayer.banPlayer(hacker.getName(), "Quit-during-control", -1, admin.getName());
            admin.teleport(Objects.requireNonNull(FileManager.getLoc("finish")));
            for (String string : Screenshare.getConfigFile().getStringList("control-finish")) {
                admin.sendMessage(Color.chatColor(string).replaceAll("%punishment%", Color.chatColor(Screenshare.getConfigFile().getString("punishment.quit"))));
            }
            try {
                Screenshare.getSql().setControl(hacker, false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Bukkit.getServer().getScheduler().cancelTask(Screenshare.taskID.get(hacker.getName()));
        }
    }


}

