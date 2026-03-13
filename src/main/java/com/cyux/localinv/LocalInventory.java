package com.cyux.localinv;

import org.bukkit.plugin.java.JavaPlugin;

public class LocalInventory extends JavaPlugin {
    @Override
    public void onEnable() {
        // Creates the config folder and saves the empty config.yml if it doesn't exist
        saveDefaultConfig();
        
        getCommand("sli").setExecutor(new InventoryCommands(this));
        getCommand("lli").setExecutor(new InventoryCommands(this));
        getCommand("ali").setExecutor(new InventoryCommands(this));
        
        getLogger().info("LocalInventory: Enabled successfully.");
    }
}
