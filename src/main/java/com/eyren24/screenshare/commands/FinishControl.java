package com.eyren24.screenshare.commands;

import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.Color;
import com.eyren24.screenshare.tools.FileManager;
import com.sun.org.apache.bcel.internal.generic.RET;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


public class FinishControl implements CommandExecutor {

    private Inventory inventory;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (!player.hasPermission("screenshare.control.finish")){
            if (!getConfig().getString("no-perm").isEmpty()){
                player.sendMessage(Color.chatColor(getConfig().getString("no-perm")));
                return false;
            }
            return false;
        }
        createInventory();
        if (args.length != 1) return false;
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + Screenshare.getInstance().getConfig().getString("player-not-found")));
            return false;
        }
        if (target.getUniqueId() == player.getUniqueId()) return false;

        try {
            if (!Screenshare.getSql().checkControl(target)) {
                player.sendMessage(Color.chatColor(Color.chatColor(Screenshare.getInstance().getConfig().getString("prefix") + "&cThe player is not in control")));
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        Bukkit.getServer().getScheduler().cancelTask(Screenshare.taskID.get(target.getName()));
        player.teleport(Objects.requireNonNull(FileManager.getLoc("finish")));
        target.teleport(Objects.requireNonNull(FileManager.getLoc("finish")));
        try {
            Screenshare.getSql().setControl(target, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        player.openInventory(inventory);
        return false;
    }

    private FileConfiguration getConfig() {
        return Screenshare.getConfigFile();
    }

    public void createInventory() {
        inventory = Bukkit.createInventory(null, getConfig().getInt("menu.size"), Color.chatColor(getConfig().getString("menu.title")));
        initializeItems();
    }

    private void initializeItems() {
        for (int i = 0; i < getConfig().getConfigurationSection("items").getKeys(false).size(); i++) {
            inventory.setItem(getConfig().getInt("items." + i + ".position"), createGuiItem(getConfig().getString("items." + i + ".item"), getConfig().getString("items." + i + ".name"), getConfig().getStringList("items." + i + ".lore")));
        }
    }

    protected ItemStack createGuiItem(final String material, final String name, final List<String> lores) {
        final ItemStack item = new ItemStack(Material.getMaterial(material), 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(Color.chatColor(name));

        meta.setLore(lores);

        item.setItemMeta(meta);

        return item;
    }
}
