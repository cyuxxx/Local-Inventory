# 📦 LocalInventory

A simple utility for players to store their entire inventory in the server's data folder rather than using physical chests.

## 🛠 Commands

* **`/sli`** (Save)
    Saves your entire inventory, armor, and offhand items to local storage and clears your character. Requires confirmation if a save already exists.
    
* **`/lli`** (Load)
    Restores your saved items and armor, then deletes the save file to prevent duplicates.
    
* **`/ali <player>`** (Admin)
    Opens a view-only menu for OPs to see what a specific player has stored.

## 🔑 Permissions

* `localinv.admin`: Required to use the `/ali` command (Defaults to OP).

## 🚀 Compatibility

* **Server Jars:** Paper, Purpur, Spigot.
* **Versions:** Minecraft 1.20.x and 1.21.x.
