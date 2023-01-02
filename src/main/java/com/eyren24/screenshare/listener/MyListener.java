package com.eyren24.screenshare.listener;

import com.eyren24.screenshare.Engine.ControlEngine;
import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.commands.Control;
import com.eyren24.screenshare.tools.BanPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) throws SQLException {
        Screenshare.getSql().initPlayer(event.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (!event.getView().getTitle().equalsIgnoreCase("Title")) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == 1){
            BanPlayer.banPlayer(Control.getTarget().getName(), Screenshare.getInstance().getConfig().getString("ban-first-option-reason"),Screenshare.getInstance().getConfig().getInt("ban-first-option-exp"), player.getName());
            Control.getTarget().kickPlayer(Screenshare.getInstance().getConfig().getString("ban-first-option-reason"));
        }
        if (event.getSlot() == 4){
            BanPlayer.banPlayer(Control.getTarget().getName(), Screenshare.getInstance().getConfig().getString("ban-second-option-reason"),Screenshare.getInstance().getConfig().getInt("ban-second-option-exp"), player.getName());
            Control.getTarget().kickPlayer(Screenshare.getInstance().getConfig().getString("ban-second-option-reason"));
        }
        event.getWhoClicked().closeInventory();
    }

}
