package com.eyren24.screenshare.tools;

import com.eyren24.screenshare.Screenshare;
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
}
