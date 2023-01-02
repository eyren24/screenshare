package com.eyren24.screenshare.commands;

import com.eyren24.screenshare.Engine.ControlEngine;
import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FinishControl implements CommandExecutor {

    private Inventory inventory;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length != 1) return false;
        Player target = (Player) Bukkit.getPlayerExact(args[0]);
        if (target == null){
            player.sendMessage(Color.chatColor(Screenshare.getPrefix() + Screenshare.getInstance().getConfig().getString("player-not-found")));
            return false;
        }
        if (target.getUniqueId() == player.getUniqueId()) return false;
        createInventory();
        try {
            if(!Screenshare.getSql().checkControl(target)){
                player.sendMessage(Color.chatColor(Color.chatColor(Screenshare.getPrefix() + "&cThe player is not in control")));
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        player.openInventory(inventory);

        Bukkit.getScheduler().cancelTask(ControlEngine.taskID);

        try {
            Screenshare.getSql().setControl(target, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public void createInventory(){
        inventory = Bukkit.createInventory(null, Screenshare.getConfigFile().getInt("menu.size"), Color.chatColor(Screenshare.getConfigFile().getString("menu.title")));
        int items = Screenshare.getConfigFile().getInt("menu.items");
        ItemStack itemStack = new ItemStack(Material.BLUE_CONCRETE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Screenshare.getConfigFile().getString("0.name"));
        List<String> lores = Screenshare.getConfigFile().getStringList("0.lore");
        List<String> itemLore = new ArrayList<String>();
        for (String lore : lores){
            itemLore.add(Color.chatColor(lore));
        }
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(Screenshare.getConfigFile().getInt("0.position"), itemStack);
        /*for (int i=0; i<=items; i++){
            ItemStack itemStack = new ItemStack(Material.BLUE_CONCRETE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(Color.chatColor(Screenshare.getConfigFile().getString(i+".name")));
            List<String> lores = Screenshare.getConfigFile().getStringList(i+".lore");
            List<String> itemLore = new ArrayList<String>();
            for (String lore : lores){
                itemLore.add(Color.chatColor(lore));
            }
            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(Screenshare.getConfigFile().getInt(i+".position"), itemStack);
        }*/
    }

}
