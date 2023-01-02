package com.eyren24.screenshare.Engine;

import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.BanPlayer;
import com.eyren24.screenshare.tools.Color;
import com.eyren24.screenshare.tools.FileManager;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.sql.SQLException;

public class ControlEngine extends BukkitRunnable {
    private Player admin;
    private Player hacker;
    public static int taskID;

    public ControlEngine(Player admin, Player hacker) {
        taskID = getTaskId();
        this.admin = admin;
        this.hacker = hacker;

        String getWorld = FileManager.get().getString("control.world");

        if (getWorld == null) {
            admin.sendMessage(Color.chatColor(Screenshare.getPrefix() + "&cPosition not found. please set a position /controlset <admin/hacker/finish>"));
            return;
        }

        World world = Bukkit.getWorld(getWorld);
        double x = FileManager.get().getDouble("control.admin.x");
        double y = FileManager.get().getDouble("control.admin.y");
        double z = FileManager.get().getDouble("control.admin.z");
        float pitch = (float) FileManager.get().getDouble("control.admin.pitch");
        float yaw = (float) FileManager.get().getDouble("control.admin.yaw");
        Location adminLocation = new Location(world, x, y, z, pitch, yaw);
        admin.teleport(adminLocation);
        x = FileManager.get().getDouble("control.hacker.x");
        y = FileManager.get().getDouble("control.hacker.y");
        z = FileManager.get().getDouble("control.hacker.z");
        pitch = (float) FileManager.get().getDouble("control.hacker.pitch");
        yaw = (float) FileManager.get().getDouble("control.hacker.yaw");
        Location targetLocation = new Location(world, x, y, z, pitch, yaw);
        hacker.teleport(targetLocation);

        try {
            Screenshare.getSql().setControl(hacker, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true){
            if (isCancelled()){
                World world = Bukkit.getWorld(FileManager.get().getString("control.world"));
                double x = FileManager.get().getDouble("control.finish.x");
                double y = FileManager.get().getDouble("control.finish.y");
                double z = FileManager.get().getDouble("control.finish.z");
                float pitch = (float) FileManager.get().getDouble("control.finish.pitch");
                float yaw = (float) FileManager.get().getDouble("control.finish.yaw");
                Location finishLoc = new Location(world, x,y,z,pitch,yaw);
                hacker.teleport(finishLoc);
                admin.teleport(finishLoc);
                return;
            }
            if (!hacker.isOnline()){
                try {
                    Screenshare.getSql().setControl(hacker, false);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                World world = Bukkit.getWorld(FileManager.get().getString("control.world"));
                double x = FileManager.get().getDouble("control.finish.x");
                double y = FileManager.get().getDouble("control.finish.y");
                double z = FileManager.get().getDouble("control.finish.z");
                float pitch = (float) FileManager.get().getDouble("control.finish.pitch");
                float yaw = (float) FileManager.get().getDouble("control.finish.yaw");
                Location finishLoc = new Location(world, x,y,z,pitch,yaw);
                admin.teleport(finishLoc);
                BanPlayer.banPlayer(hacker.getName(), Color.chatColor(Screenshare.getPrefix() + FileManager.get().getString("ban-message")),Screenshare.getInstance().getConfig().getInt("ban-quit"), admin.getName());
                return;
            }
        }
    }
}
