package com.eyren24.screenshare.tools;

import com.eyren24.screenshare.Screenshare;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager {

    private static File file;
    private static FileConfiguration fileConfiguration;


    public static void setUp() {
        file = new File(Screenshare.getInstance().getDataFolder(), "Screenshare.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                Screenshare.getInstance().saveResource("Screenshare.yml", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration get() {
        return fileConfiguration;
    }

    public static void save() {
        try {
            fileConfiguration.save(file);
        } catch (Exception e) {
            System.out.println(Color.chatColor("&e[&6Screenshare&e] &cCan't save file!"));
        }
    }

    public static void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public static Location getLoc(String loc) {
        String getWorld = FileManager.get().getString("control.world");
        if (getWorld == null) return null;
        World world = Bukkit.getWorld(getWorld);
        if (loc.equalsIgnoreCase("admin")) {
            double x = FileManager.get().getDouble("control.admin.x");
            double y = FileManager.get().getDouble("control.admin.y");
            double z = FileManager.get().getDouble("control.admin.z");
            float pitch = (float) FileManager.get().getDouble("control.admin.pitch");
            float yaw = (float) FileManager.get().getDouble("control.admin.yaw");
            return new Location(world, x, y, z, pitch, yaw);
        }
        if (loc.equalsIgnoreCase("hacker")) {
            double x = FileManager.get().getDouble("control.hacker.x");
            double y = FileManager.get().getDouble("control.hacker.y");
            double z = FileManager.get().getDouble("control.hacker.z");
            float pitch = (float) FileManager.get().getDouble("control.hacker.pitch");
            float yaw = (float) FileManager.get().getDouble("control.hacker.yaw");
            return new Location(world, x, y, z, pitch, yaw);
        }
        if (loc.equalsIgnoreCase("finish")) {
            double x = FileManager.get().getDouble("control.finish.x");
            double y = FileManager.get().getDouble("control.finish.y");
            double z = FileManager.get().getDouble("control.finish.z");
            float pitch = (float) FileManager.get().getDouble("control.finish.pitch");
            float yaw = (float) FileManager.get().getDouble("control.finish.yaw");
            return new Location(world, x, y, z, pitch, yaw);
        }
        return null;
    }
}
