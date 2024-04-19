package com.yiorno.nbtfixer;

import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        EditNBT editNBT = new EditNBT();
        editNBT.selectThenDo(stack);

    }

    @EventHandler
    public void onHold(PlayerItemHeldEvent e){

        ItemStack stack = e.getPlayer().getInventory().getItem(e.getNewSlot());

        if(stack != null) {

            EditNBT editNBT = new EditNBT();
            editNBT.selectThenDo(stack);

        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){

        ItemStack stack = e.getItemDrop().getItemStack();
        EditNBT editNBT = new EditNBT();
        editNBT.selectThenDo(stack);

    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){

        ItemStack stack = e.getItem().getItemStack();
        EditNBT editNBT = new EditNBT();
        editNBT.selectThenDo(stack);

    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null) {

            ItemStack stack = e.getCurrentItem();
            EditNBT editNBT = new EditNBT();
            editNBT.selectThenDo(stack);

        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if(cmd.getName().equalsIgnoreCase("nbt")){

            Player player = (Player)sender;
            EditNBT editNBT = new EditNBT();
            editNBT.showNbt(player);

        }

        if(cmd.getName().equalsIgnoreCase("mythicnbttest")){

            Player player = (Player)sender;
            EditNBT editNBT = new EditNBT();
            editNBT.removeMythicTypeTest(player);

        }

        if(cmd.getName().equalsIgnoreCase("replacecointest")){

            Player player = (Player)sender;
            EditNBT editNBT = new EditNBT();
            editNBT.replaceLegacyCoinTest(player);

        }

        return false;
    }

    //チェストに接してコイン置けないようにする for Coins 1.13.1
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(PlayerInteractEvent e){

        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            return;
        }

        ItemStack stack = e.getItem();
        EditNBT editNBT = new EditNBT();

        if((stack != null) && (e.getClickedBlock() == null)) {

            if((e.getClickedBlock().getType().equals(Material.CHEST)) || (e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))) {
                if (editNBT.isCoin(stack)) {

                    e.getPlayer().sendMessage("このアイテムは置けません");
                    e.setCancelled(true);

                }

            }
        }
    }
}
