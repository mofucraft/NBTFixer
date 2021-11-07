package com.yiorno.nbtfixer;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.Item;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditNBT {

    public void removeMythicType(ItemStack stack){
        if(stack != null && stack.getItemMeta() !=null) {

            //Get NBT
            net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

            if(CBStack.getTag() != null) {
                String nbtStr = CBStack.getTag().toString();

                if (!(nbtStr.contains("MYTHIC_TYPE"))) {
                    ConvertColor cc = new ConvertColor();
                    cc.convert(stack);
                    return;
                }

                //Remove
                int startPos = nbtStr.indexOf("MYTHIC_TYPE");
                int endPos = nbtStr.indexOf(",", startPos);

                StringBuilder sb = new StringBuilder();

                sb.append(nbtStr);

                sb.delete(startPos - 1, endPos);

                String newNbtStr = sb.toString();

                //Convert to NBT
                net.minecraft.world.item.ItemStack nmsItem = new net.minecraft.world.item.ItemStack(Item.getById(1));
                NBTBase nbtbase = null;

                try {
                    nbtbase = MojangsonParser.parse(newNbtStr);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }

                nmsItem.setTag((NBTTagCompound) nbtbase);
                ItemStack newStack = CraftItemStack.asBukkitCopy(nmsItem);
                ItemMeta newMeta = newStack.getItemMeta();

                //Write
                stack.setItemMeta(newMeta);
                ConvertColor cc = new ConvertColor();
                cc.convert(stack);

            }
        }
    }


    public void removeMythicTypeTest(Player player){
        //Get NBT
        ItemStack i = player.getInventory().getItemInMainHand();
        net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(i);
        String nbtStr = CBStack.getTag().toString();

        if(!(nbtStr.contains("MYTHIC_TYPE"))){
            return;
        }

        //Remove
        int startPos = nbtStr.indexOf("MYTHIC_TYPE");
        int endPos = nbtStr.indexOf(",", startPos);

        StringBuilder sb = new StringBuilder();

        sb.append(nbtStr);

        sb.delete(startPos-1, endPos);

        System.out.println(player + "のアイテムを置き換えました");


    }

}
