package com.yiorno.nbtfixer;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EditNBT {

    public void replaceGoldenCratesKey(ItemStack stack){
        if (stack != null && stack.getItemMeta() != null) {

            //Get NBT
            net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

            String itemName = stack.getItemMeta().getDisplayName();

            if (CBStack.v() != null) {
                String nbtStr = CBStack.v().toString();

                //置き換え
                String before = "\"goldencrates:gcrates_key\"";
                String after = "\"excellentcrates:crate_key.id\"";

                String newNbtStr = nbtStr.replace(before, after);

                //Convert to NBT
                //net.minecraft.world.item.ItemStack nmsItem = new net.minecraft.world.item.ItemStack(Item.getById(1));
                net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTBase nbtbase = null;

                try {
                    nbtbase = MojangsonParser.a(newNbtStr);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }

                nmsItem.c((NBTTagCompound) nbtbase);
                ItemStack newStack = CraftItemStack.asBukkitCopy(nmsItem);
                ItemMeta newMeta = newStack.getItemMeta();


                //Write
                stack.setItemMeta(newMeta);
                ConvertColor cc = new ConvertColor();
                cc.convert(stack);
            }
        }

    }

    public void removeMythicType(ItemStack stack) {
        if (stack != null && stack.getItemMeta() != null) {

            //Get NBT
            net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

            if (CBStack.v() != null) {

                String nbtStr = CBStack.v().toString();

                StringBuilder sb = new StringBuilder();
                sb.append(nbtStr);

                int startPos;
                int endPos;

                //Remove MYTHIC_TYPE
                if(nbtStr.contains("MYTHIC_TYPE")){

                    startPos = nbtStr.indexOf("MYTHIC_TYPE");
                    endPos = nbtStr.indexOf(",", startPos);

                    sb.delete(startPos, endPos + 1);

                }

                //Remove MYTHIC_ITEM_VERSION, since mm 5.0.4
                if(nbtStr.contains("MYTHIC_ITEM_VERSION")){

                    startPos = nbtStr.indexOf("MYTHIC_ITEM_VERSION");
                    endPos = nbtStr.indexOf(",", startPos);

                    sb.delete(startPos, endPos + 1);

                }

                String newNbtStr = sb.toString();

                //Convert to NBT
                //net.minecraft.world.item.ItemStack nmsItem = new net.minecraft.world.item.ItemStack(Item.getById(1));
                net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTBase nbtbase = null;

                try {
                    nbtbase = MojangsonParser.a(newNbtStr);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }

                nmsItem.c((NBTTagCompound) nbtbase);
                ItemStack newStack = CraftItemStack.asBukkitCopy(nmsItem);
                ItemMeta newMeta = newStack.getItemMeta();


                //Write
                stack.setItemMeta(newMeta);
                ConvertColor cc = new ConvertColor();
                cc.convert(stack);

            }
        }
    }

    public void replaceLegacyCoin(ItemStack stack) {
        if (stack != null && stack.getItemMeta() != null) {

            //Get NBT
            net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

            String itemName = stack.getItemMeta().getDisplayName();

            if (CBStack.v() != null) {
                String nbtStr = CBStack.v().toString();

                int startPos;
                //int endPos;

                //金額の取得
                startPos = itemName.indexOf("MOFU");

                String value = itemName.substring(0, startPos - 3);
                String rawValue = ChatColor.stripColor(value);

                //置き換え
                String before = "{Enchantments:[{id:\"minecraft:unbreaking\",lvl:1s}],";
                String after = "{CustomModelData:1,Enchantments:[{id:\"minecraft:unbreaking\",lvl:1s}],HideFlags:1,PublicBukkitValues:{\"coins:coins-type\":2,\"coins:coins-worth\":" + rawValue + ".0d},";

                String newNbtStr = nbtStr.replace(before, after);

                //Convert to NBT
                //net.minecraft.world.item.ItemStack nmsItem = new net.minecraft.world.item.ItemStack(Item.getById(1));
                net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                NBTBase nbtbase = null;

                try {
                    nbtbase = MojangsonParser.a(newNbtStr);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }

                nmsItem.c((NBTTagCompound) nbtbase);
                ItemStack newStack = CraftItemStack.asBukkitCopy(nmsItem);
                ItemMeta newMeta = newStack.getItemMeta();


                //Write
                stack.setItemMeta(newMeta);
                ConvertColor cc = new ConvertColor();
                cc.convert(stack);
            }
        }

        //上の処理に飛ばす
        //removeMythicType(stack);
    }

    public void selectThenDo(ItemStack stack){

        if(isLegacyCoin(stack)) {
            replaceLegacyCoin(stack);
        }

        if(isGoldenCratesKey(stack)) {
            replaceGoldenCratesKey(stack);
        }

        if(isMythicItem(stack)) {
            removeMythicType(stack);
        }

    }

    public boolean isMythicItem(ItemStack stack){

        //シュルカーは処理しない
        String matStr = stack.getType().toString();
        if (matStr.contains("SHULKER")) {
            return false;
        }

        //Get NBT
        net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

        if (CBStack.v() != null) {
            String nbtStr = CBStack.v().toString();

            //MYTHICMOBSじゃないアイテムは処理しない
            if (!(nbtStr.contains("MYTHIC_TYPE")) && !(nbtStr.contains("MYTHIC_ITEM_VERSION"))) {
                ConvertColor cc = new ConvertColor();
                cc.convert(stack);
                return false;
            }

            //シュルカーは処理しない(多分)
            if (nbtStr.contains("BlockEntityTag") || nbtStr.length() > 1600) {
                ConvertColor cc = new ConvertColor();
                cc.convert(stack);
                return false;
            }

            return true;
        }

        return false;
    }

    public boolean isGoldenCratesKey(ItemStack stack){

        //Get NBT
        net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

        if (CBStack.v() != null) {
            String nbtStr = CBStack.v().toString();

            return nbtStr.contains("goldencrates:gcrates_key");
        }

        return false;
    }

    public boolean isCoin(ItemStack stack){

        String itemName = stack.getItemMeta().getDisplayName();

        //一応アイテム名の確認
        if(!(itemName.contains("MOFU"))){
            return false;
        }

        //Get NBT
        net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

        if (CBStack.v() != null) {
            String nbtStr = CBStack.v().toString();

            if (stack.getType() == Material.PLAYER_HEAD) {

                //昔のお金の頭か確認
                if (nbtStr.contains("coins-worth")) {

                    return true;
                }
            }
        }
        return false;

    }


    public boolean isLegacyCoin(ItemStack stack){

        String itemName = stack.getItemMeta().getDisplayName();

        //一応アイテム名の確認
        if(!(itemName.contains("MOFU"))){
            return false;
        }

        //Get NBT
        net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

        if (CBStack.v() != null) {
            String nbtStr = CBStack.v().toString();

            if (stack.getType() == Material.PLAYER_HEAD) {

                //昔のお金の頭か確認
                if ((nbtStr.contains("SkullOwner:{Id:[I;1,65537,65536,2]")) && !(nbtStr.contains("coins-worth"))) {

//{CustomModelData:1,Enchantments:[{id:"minecraft:unbreaking",lvl:1s}],HideFlags:1,PublicBukkitValues:{"coins:coins-type":2,"coins:coins-worth":2400.0d},SkullOwner:{Id:[I;1,65537,65536,2],Name:"randomCoin",Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkyNmMxZjJjM2MxNGQwODZjNDBjZmMyMzVmZTkzODY5NGY0YTUxMDY3YWRhNDcyNmI0ODZlYTFjODdiMDNlMiJ9fX0"}]}},display:{Name:'{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"color":"yellow","text":"2400 "},{"italic":false,"color":"aqua","text":"MOFU"}],"text":""}'}}

//{Enchantments:[{id:"minecraft:unbreaking",lvl:1s}],SkullOwner:{Id:[I;1,65537,65536,2],Name:"randomCoin",Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkyNmMxZjJjM2MxNGQwODZjNDBjZmMyMzVmZTkzODY5NGY0YTUxMDY3YWRhNDcyNmI0ODZlYTFjODdiMDNlMiJ9fX0"}]}},display:{Name:'{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"color":"yellow","text":"2400 "},{"italic":false,"color":"aqua","text":"MOFU"}],"text":""}'}}

                    int startPos;
                    //int endPos;

                    //金額の取得
                    startPos = itemName.indexOf("MOFU");

                    if (startPos < 3) {
                        return false;
                    }

                    return true;
                }
            }
        }
        return false;

    }

    public void removeMythicTypeTest(Player player) {
        //Get NBT
        ItemStack stack = player.getInventory().getItemInMainHand();
        net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);
        String nbtStr = CBStack.v().toString();
        player.sendMessage(nbtStr);

        //Remove
        int startPos = nbtStr.indexOf("MYTHIC_TYPE");
        int endPos = nbtStr.indexOf(",", startPos);

        StringBuilder sb = new StringBuilder();

        sb.append(nbtStr);

        sb.delete(startPos, endPos + 1);

        String newNbtStr = sb.toString();
        player.sendMessage(newNbtStr);

        //Convert to NBT
        //net.minecraft.world.item.ItemStack nmsItem = new net.minecraft.world.item.ItemStack(Item.getById(1));
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
        NBTBase nbtbase = null;

        try {
            nbtbase = MojangsonParser.a(newNbtStr);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        nmsItem.c((NBTTagCompound) nbtbase);
        ItemStack newStack = CraftItemStack.asBukkitCopy(nmsItem);
        //ItemMeta newMeta = newStack.getItemMeta();

        //Write
        //stack.setItemMeta(newMeta);

        net.minecraft.world.item.ItemStack testCBStack = CraftItemStack.asNMSCopy(newStack);
        String testnbtStr = testCBStack.v().toString();
        player.sendMessage(testnbtStr);

    }

    public void replaceLegacyCoinTest(Player p) {
        ItemStack stack = p.getInventory().getItemInMainHand();
        String itemName = stack.getItemMeta().getDisplayName();

        //一応アイテム名の確認
        if(!(itemName.contains("MOFU"))){
            p.sendMessage("それはお金じゃないだろう");
            return;
        }

        //Get NBT
        net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);

        if (CBStack.v() != null) {
            String nbtStr = CBStack.v().toString();

            if (stack.getType() == Material.PLAYER_HEAD) {

                //昔のお金の頭か確認
                if ((nbtStr.contains("SkullOwner:{Id:[I;1,65537,65536,2]")) && !(nbtStr.contains("coins-worth"))) {

//{CustomModelData:1,Enchantments:[{id:"minecraft:unbreaking",lvl:1s}],HideFlags:1,PublicBukkitValues:{"coins:coins-type":2,"coins:coins-worth":2400.0d},SkullOwner:{Id:[I;1,65537,65536,2],Name:"randomCoin",Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkyNmMxZjJjM2MxNGQwODZjNDBjZmMyMzVmZTkzODY5NGY0YTUxMDY3YWRhNDcyNmI0ODZlYTFjODdiMDNlMiJ9fX0"}]}},display:{Name:'{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"color":"yellow","text":"2400 "},{"italic":false,"color":"aqua","text":"MOFU"}],"text":""}'}}

//{Enchantments:[{id:"minecraft:unbreaking",lvl:1s}],SkullOwner:{Id:[I;1,65537,65536,2],Name:"randomCoin",Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODkyNmMxZjJjM2MxNGQwODZjNDBjZmMyMzVmZTkzODY5NGY0YTUxMDY3YWRhNDcyNmI0ODZlYTFjODdiMDNlMiJ9fX0"}]}},display:{Name:'{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"color":"yellow","text":"2400 "},{"italic":false,"color":"aqua","text":"MOFU"}],"text":""}'}}

                    p.sendMessage("それは昔のお金だな？");
                    int startPos;
                    //int endPos;

                    //金額の取得
                    startPos = itemName.indexOf("MOFU");
                    String value = itemName.substring(0, startPos - 3);
                    String rawValue = ChatColor.stripColor(value);
                    p.sendMessage("金額" + rawValue);

                    //置き換え
                    String before = "{Enchantments:[{id:\"minecraft:unbreaking\",lvl:1s}],";
                    String after = "{CustomModelData:1,Enchantments:[{id:\"minecraft:unbreaking\",lvl:1s}],HideFlags:1,PublicBukkitValues:{\"coins:coins-type\":2,\"coins:coins-worth\":" + rawValue + ".0d},";

                    String newNbtStr = nbtStr.replace(before, after);
                    p.sendMessage(newNbtStr);

                    //Convert to NBT
                    //net.minecraft.world.item.ItemStack nmsItem = new net.minecraft.world.item.ItemStack(Item.getById(1));
                    net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                    NBTBase nbtbase = null;

                    try {
                        nbtbase = MojangsonParser.a(newNbtStr);
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }

                    nmsItem.c((NBTTagCompound) nbtbase);
                    ItemStack newStack = CraftItemStack.asBukkitCopy(nmsItem);
                    ItemMeta newMeta = newStack.getItemMeta();


                    //Write
                    stack.setItemMeta(newMeta);
                    p.sendMessage("うおおお");

                }

            }

        }

    }

    public void showNbt(Player player) {

        //Get NBT
        ItemStack stack = player.getInventory().getItemInMainHand();
        net.minecraft.world.item.ItemStack CBStack = CraftItemStack.asNMSCopy(stack);
        String nbtStr = CBStack.v().toString();
        player.sendMessage(nbtStr);

    }
}
