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
        if (!sender.hasPermission("screenshare.admin")) return false;
        if (args.length != 1) return false;
        if (args[0].equals("reload")) {
            sender.sendMessage(Color.chatColor(Screenshare.getPrefix() + "&aReload complete"));
            FileManager.reload();
            Screenshare.getInstance().reloadConfig();
            return true;
        }
        return false;
    }
}
