package com.yiorno.nbtfixer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class NBTFixer extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onSpawn(ItemSpawnEvent e){
        ItemStack stack = e.getEntity().getItemStack();
        String itemName = stack.getI18NDisplayName();
        if(itemName.contains("&")){
        }
    }

    @EventHandler
    public void onHoldTest(PlayerItemHeldEvent e){
        e.getNewSlot();
        ItemStack itemStack = e.getPlayer().getInventory().getItem(e.getNewSlot());
        String itemName = itemStack.getI18NDisplayName();
        if(itemName.contains("&")){
            e.getPlayer().sendMessage("" + itemName);
        }
    }
}
