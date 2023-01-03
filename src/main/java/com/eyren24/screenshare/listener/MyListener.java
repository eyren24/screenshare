package com.eyren24.screenshare.listener;

import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.commands.Control;
import com.eyren24.screenshare.tools.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) throws SQLException {
        Screenshare.getSql().initPlayer(event.getPlayer());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player){
            Player player = Bukkit.getPlayer(event.getEntity().getName());
            try {
                if (Screenshare.getSql().checkControl(player)) {
                    assert player != null;
                    if (player.getName().equalsIgnoreCase(Control.getTarget().getName())){
                        event.setDamage(0.0);
                        event.setCancelled(true);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equalsIgnoreCase(Color.chatColor(Screenshare.getConfigFile().getString("menu.title")))) {
            if (event.getCurrentItem() != null) {
                for (int i = 0; i < Screenshare.getConfigFile().getConfigurationSection("items").getKeys(false).size(); i++) {
                    if (event.getSlot() == Screenshare.getConfigFile().getInt("items." + i + ".position")) {
                        for (String command : Screenshare.getConfigFile().getStringList("items." + i + ".commands")) {
                            List<String> tokens = Arrays.asList(command.split("\\s+"));
                            if (tokens.get(0).equalsIgnoreCase("[CLOSE]")){
                                player.closeInventory();
                            } else if (tokens.get(0).equalsIgnoreCase("[MESSAGE]")) {
                                String message = command.substring(10);
                                player.sendMessage(Color.chatColor(message.replaceAll("%target%", Control.getTarget().getName())));
                            }else {
                                player.performCommand(command.replaceAll("%target%", Control.getTarget().getName()));
                            }
                        }
                        player.closeInventory();
                    }
                }
                event.setCancelled(true);
            }
        }
    }

}
