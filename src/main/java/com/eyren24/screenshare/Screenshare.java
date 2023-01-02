package com.eyren24.screenshare;

import com.eyren24.screenshare.commands.Control;
import com.eyren24.screenshare.commands.FinishControl;
import com.eyren24.screenshare.commands.ScreenshareCommands;
import com.eyren24.screenshare.commands.SetControl;
import com.eyren24.screenshare.datamanager.DataBase;
import com.eyren24.screenshare.listener.MyListener;
import com.eyren24.screenshare.tools.Color;
import com.eyren24.screenshare.tools.FileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.sql.SQLException;

public final class Screenshare extends JavaPlugin {
    private static String prefix;

    private static Screenshare instance;

    private static DataBase sql;

    public static Screenshare getInstance() {
        return instance;
    }

    public static String getPrefix() {
        return prefix;
    }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        System.out.println(Color.chatColor("&aScreenshare enabled"));

        instance = this;

        FileManager.setUp();
        FileManager.get().options().copyDefaults(true);
        FileManager.save();

        prefix = getConfig().getString("prefix");


        File file = new File(getDataFolder()+"/database.db");
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        try {
            sql = new DataBase(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }

    @Override
    public void onDisable() {
        System.out.println(Color.chatColor("&cScreenshare disabled"));
        sql.closeConnection();
    }

    private void init() {
        getCommand("control").setExecutor(new Control());
        getCommand("finish").setExecutor(new FinishControl());
        getCommand("screenshare").setExecutor(new ScreenshareCommands());
        getCommand("controlset").setExecutor(new SetControl());
        getServer().getPluginManager().registerEvents(new MyListener(), this);

    }

    public static FileConfiguration getConfigFile(){
        return instance.getConfig();
    }

    public static DataBase getSql(){
        return sql;
    }
}
