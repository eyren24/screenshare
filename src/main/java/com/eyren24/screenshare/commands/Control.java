package com.eyren24.screenshare.commands;

import com.eyren24.screenshare.Engine.ControlEngine;
import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Control implements CommandExecutor {
    private static Player target;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("screenshare.control")){
            if (!Screenshare.getConfigFile().getString("no-perm").isEmpty()){
                player.sendMessage(Color.chatColor(Screenshare.getConfigFile().getString("no-perm")));
                return false;
            }
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + "&cUsage /control <player>"));
            return false;
        }

        target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + Screenshare.getInstance().getConfig().getString("player-not-found").replaceAll("%playername%", args[0])));
            return false;
        }
        if (target.getUniqueId() == player.getUniqueId()) return false;

        // check if player already checked db
        try {
            if (Screenshare.getSql().checkControl(target)) {
                player.sendMessage(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + "&cThe player is already in control!"));
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Screenshare.getSql().setControl(target, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Screenshare.getInstance(), new ControlEngine(Screenshare.getInstance(), player, target), 0L, 20L);
        Screenshare.taskID.put(target.getName(), taskId);

        return false;
    }
    public static Player getTarget(){
        return target;
    }

}
