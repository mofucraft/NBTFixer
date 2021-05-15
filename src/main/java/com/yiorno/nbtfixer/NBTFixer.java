package com.yiorno.nbtfixer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

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
        ConvertColor convertColor = new ConvertColor();
        convertColor.convert(stack);
    }

    @EventHandler
    public void onHold(PlayerItemHeldEvent e){
        ItemStack stack = e.getPlayer().getInventory().getItem(e.getNewSlot());

        if(stack != null) {

            ConvertColor convertColor = new ConvertColor();
            convertColor.convert(stack);

        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        ItemStack stack = e.getItemDrop().getItemStack();
        ConvertColor convertColor = new ConvertColor();
        convertColor.convert(stack);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        ItemStack stack = e.getItem().getItemStack();
        ConvertColor convertColor = new ConvertColor();
        convertColor.convert(stack);
    }

    //@EventHandler
    //public void onClick(InventoryClickEvent e){
    //    if (e.getCurrentItem() == null){
    //        return;
    //    }

    //    ItemStack stack = e.getCurrentItem();
    //    ConvertColor convertColor = new ConvertColor();
    //    convertColor.convert(stack);
    //}
}
