package com.eyren24.screenshare.commands;

import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.Color;
import com.eyren24.screenshare.tools.FileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ScreenshareCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("screenshare.admin")){
            if (!Screenshare.getConfigFile().getString("no-perm").isEmpty()){
                sender.sendMessage(Color.chatColor(Screenshare.getConfigFile().getString("no-perm")));
                return false;
            }
            return false;
        }
        if (args.length > 1) return false;
        if (args.length == 0){
            sender.sendMessage(Color.chatColor("&4&l-----------------------------"));
            sender.sendMessage(Color.chatColor("&7» &cUse &f/screenshare reload"));
            sender.sendMessage(Color.chatColor("&7» &cSetControlPos &f/controlset <admin/hacker/finish>"));
            sender.sendMessage(Color.chatColor("&7» &cUse &f/control <playerName>"));
            sender.sendMessage(Color.chatColor("&7» &cUse &f/finish <playerName>"));
            sender.sendMessage(Color.chatColor("&4&l-----------------------------"));
            return true;
        }
        if (args[0].equals("reload")) {
            FileManager.reload();
            Screenshare.getInstance().reloadConfig();
            sender.sendMessage(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + "&aReload complete"));
            return true;
        }
        return false;
    }
}
