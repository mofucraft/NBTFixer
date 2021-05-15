package com.yiorno.nbtfixer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConvertColor {

    public void convert(ItemStack stack){

        ItemMeta meta = stack.getItemMeta();

        //Get Name and Lores
        String name = meta.getDisplayName();
        Boolean loreChange = true;

        if(stack.getItemMeta().hasLore() == true){
            loreChange = true;
            List<String> loreList = meta.getLore();
            List<String> newLoreList = new ArrayList<String>();
            String newLore;

            int loreSize = loreList.size();
            int i = 0;

            if(loreChange == true) {
                while (i < loreSize) {
                    newLore = ChatColor.translateAlternateColorCodes('&', loreList.get(i));
                    newLoreList.add(newLore);
                    i++;
                }

                meta.setLore(newLoreList);
            }

        } else {
            loreChange = false;
        }

        String newName = ChatColor.translateAlternateColorCodes('&', name);
        meta.setDisplayName(newName);
        stack.setItemMeta(meta);

        return;
    }

}
