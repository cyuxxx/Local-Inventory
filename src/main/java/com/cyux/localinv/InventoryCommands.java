package com.cyux.localinv;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class InventoryCommands implements CommandExecutor {
    private final LocalInventory plugin;

    public InventoryCommands(LocalInventory plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        // --- ADMIN COMMAND (/ali <player>) ---
        if (label.equalsIgnoreCase("ali")) {
            if (!p.hasPermission("localinv.admin")) {
                p.sendMessage("§cNo permission.");
                return true;
            }
            if (args.length == 0) {
                p.sendMessage("§cUsage: /ali <player>");
                return true;
            }

            @SuppressWarnings("deprecation")
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            String path = target.getUniqueId().toString();

            if (!plugin.getConfig().contains(path)) {
                p.sendMessage("§c" + args[0] + " has no saved inventory.");
                return true;
            }

            openAdminGUI(p, target);
            return true;
        }

        // --- SAVE COMMAND (/sli) ---
        if (label.equalsIgnoreCase("sli")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("confirm")) {
                saveAndClear(p);
                return true;
            }
            if (plugin.getConfig().contains(p.getUniqueId().toString())) {
                p.sendMessage("§e§l(!) §cOverwrite current save?");
                TextComponent yes = new TextComponent("§a§l[YES] ");
                yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sli confirm"));
                p.spigot().sendMessage(yes);
            } else {
                saveAndClear(p);
            }
            return true;
        }

        // --- LOAD COMMAND (/lli) ---
        if (label.equalsIgnoreCase("lli")) {
            String path = p.getUniqueId().toString();
            if (!plugin.getConfig().contains(path)) {
                p.sendMessage("§cNo save found.");
                return true;
            }
            try {
                p.getInventory().setContents(loadItems(path + ".main"));
                p.getInventory().setArmorContents(loadItems(path + ".armor"));
                p.getInventory().setItemInOffHand(plugin.getConfig().getItemStack(path + ".offhand"));
                plugin.getConfig().set(path, null);
                plugin.saveConfig();
                p.sendMessage("§aInventory loaded!");
            } catch (Exception e) { p.sendMessage("§cLoad error."); }
            return true;
        }
        return true;
    }

    private void openAdminGUI(Player admin, OfflinePlayer target) {
        String path = target.getUniqueId().toString();
        Inventory gui = Bukkit.createInventory(null, 54, "§0Storage: " + target.getName());

        ItemStack[] main = loadItems(path + ".main");
        ItemStack[] armor = loadItems(path + ".armor");
        ItemStack offhand = plugin.getConfig().getItemStack(path + ".offhand");

        if (main != null) gui.setContents(main);
        // Put armor in the bottom row for visibility
        if (armor != null) {
            for (int i = 0; i < armor.length; i++) gui.setItem(45 + i, armor[i]);
        }
        gui.setItem(53, offhand);

        admin.openInventory(gui);
        admin.sendMessage("§7Opening saved items for §e" + target.getName());
    }

    private ItemStack[] loadItems(String path) {
        Object data = plugin.getConfig().get(path);
        if (data instanceof List) return ((List<?>) data).toArray(new ItemStack[0]);
        if (data instanceof ItemStack[]) return (ItemStack[]) data;
        return null;
    }

    private void saveAndClear(Player p) {
        String path = p.getUniqueId().toString();
        plugin.getConfig().set(path + ".main", p.getInventory().getContents());
        plugin.getConfig().set(path + ".armor", p.getInventory().getArmorContents());
        plugin.getConfig().set(path + ".offhand", p.getInventory().getItemInOffHand());
        plugin.saveConfig();
        p.getInventory().clear();
        p.getInventory().setArmorContents(new ItemStack[4]);
        p.getInventory().setItemInOffHand(null);
        p.sendMessage("§a§lSUCCESS! §7Your inventory was saved.");
    }
}
