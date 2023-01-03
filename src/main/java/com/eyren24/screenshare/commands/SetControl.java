package com.eyren24.screenshare.commands;

import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.Color;
import com.eyren24.screenshare.tools.FileManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetControl implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if (!player.hasPermission("screenshare.control.set")){
            if (!Screenshare.getConfigFile().getString("no-perm").isEmpty()){
                player.sendMessage(Color.chatColor(Screenshare.getConfigFile().getString("no-perm")));
                return false;
            }
            return false;
        }
        String world = player.getWorld().getName();
        double X = player.getLocation().getX();
        double Y = player.getLocation().getY();
        double Z = player.getLocation().getZ();
        double pitch = player.getLocation().getPitch();
        double yaw = player.getLocation().getYaw();

        if (args.length != 1) return false;

        if (args[0].equalsIgnoreCase("hacker")) {
            FileManager.get().set("control.world", world);
            FileManager.get().set("control.hacker.x", X);
            FileManager.get().set("control.hacker.y", Y);
            FileManager.get().set("control.hacker.z", Z);
            FileManager.get().set("control.hacker.pitch", pitch);
            FileManager.get().set("control.hacker.yaw", yaw);
            player.sendMessage(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + Screenshare.getInstance().getConfig().getString("control-pos").replaceAll("%position%", "Hacker")));
            FileManager.save();
            FileManager.reload();
            return true;
        }
        if (args[0].equalsIgnoreCase("admin")) {
            FileManager.get().set("control.world", world);
            FileManager.get().set("control.admin.x", X);
            FileManager.get().set("control.admin.y", Y);
            FileManager.get().set("control.admin.z", Z);
            FileManager.get().set("control.admin.pitch", pitch);
            FileManager.get().set("control.admin.yaw", yaw);
            player.sendMessage(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + Screenshare.getInstance().getConfig().getString("control-pos").replaceAll("%position%", "Admin")));
            FileManager.save();
            FileManager.reload();
            return true;
        }
        if (args[0].equalsIgnoreCase("finish")) {
            FileManager.get().set("control.world", world);
            FileManager.get().set("control.finish.x", X);
            FileManager.get().set("control.finish.y", Y);
            FileManager.get().set("control.finish.z", Z);
            FileManager.get().set("control.finish.pitch", pitch);
            FileManager.get().set("control.finish.yaw", yaw);
            player.sendMessage(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + Screenshare.getInstance().getConfig().getString("control-pos").replaceAll("%position%", "Finish")));
            FileManager.save();
            FileManager.reload();
            return true;
        }

        return false;
    }
}
