package com.eyren24.screenshare.commands;

import com.eyren24.screenshare.Engine.ControlEngine;
import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.C;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class Control implements CommandExecutor {
    private static Player target;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(Color.chatColor(Screenshare.getPrefix() + "&cUsage /control <player>"));
            return false;
        }

        target = (Player) Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Color.chatColor(Screenshare.getPrefix() + Screenshare.getInstance().getConfig().getString("player-not-found")));
            return false;
        }
        if (target.getUniqueId() == player.getUniqueId()) return false;
        // check if player already checked db
        try {
            if(Screenshare.getSql().checkControl(target)){
                player.sendMessage(Color.chatColor(Screenshare.getPrefix() + "&cThe player is already in control!"));
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        BukkitTask task = new ControlEngine(player, target).runTaskLater(Screenshare.getInstance(), 20L);

        return false;
    }
    public static Player getTarget(){
        return target;
    }

}
