package com.eyren24.screenshare.commands;

import com.eyren24.screenshare.Screenshare;
import com.eyren24.screenshare.tools.Color;
import com.eyren24.screenshare.tools.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
            if (!FileManager.get().getString("no-perm").isEmpty()) {
                player.sendMessage(Color.chatColor(FileManager.get().getString("no-perm")));
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

    public void createInventory() {
        inventory = Bukkit.createInventory(null, FileManager.get().getInt("menu.size"), Color.chatColor(FileManager.get().getString("menu.title")));
        initializeItems();
    }

    private void initializeItems() {
        for (int i = 0; i < FileManager.get().getConfigurationSection("items").getKeys(false).size(); i++) {
            inventory.setItem(FileManager.get().getInt("items." + i + ".position"), createGuiItem(FileManager.get().getString("items." + i + ".item"), FileManager.get().getString("items." + i + ".name"), FileManager.get().getStringList("items." + i + ".lore")));
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
