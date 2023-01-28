package weapons;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.Inventory;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.UUID;

public class weaponsItems {
//Custom sword
    public static ItemStack getReinforcedStoneSword() {
        ItemStack item = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Reinforced Stone Sword");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getReinforcedStoneAxe(){
        ItemStack item = new ItemStack(Material.STONE_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Reinforced Stone Axe");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 11, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        
        item.setItemMeta(meta);
        return item;
    }

 
//Custom sword
    public static ItemStack getReinforcedGoldSword() {
        ItemStack item = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Reinforced Gold Sword");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        item.setItemMeta(meta);
        return item;
    }


    public static ItemStack getReinforcedGoldAxe() {
        ItemStack item = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Reinforced Gold Axe");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 11, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        item.setItemMeta(meta);
        return item;
    }


    //Custom pickaxe
    public static ItemStack getReinforcedGoldPickaxe() {
        ItemStack item = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Reinforced Gold Pickaxe");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getKatana(){
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Katana");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.setUnbreakable(true);
        meta.setCustomModelData(1);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack getSmokerSword(){
        ItemStack item = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Smoker Sword");
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 9, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.setLore(Arrays.asList("Material:Kairōseki"));
        meta.setCustomModelData(786);
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

    public static void weaponShop(Player player){
        Inventory inv = Bukkit.createInventory(null, 45, ("Weapon Shop"));
        ItemStack katana = getKatana();
        ItemStack smokerSword = getSmokerSword();
        ItemStack reinforcedGoldSword = getReinforcedGoldSword();
        ItemStack reinforcedGoldAxe = getReinforcedGoldAxe();
        ItemStack reinforcedGoldPickaxe = getReinforcedGoldPickaxe();
        ItemStack reinforcedStoneSword = getReinforcedStoneSword();
        ItemStack reinforcedStoneAxe = getReinforcedStoneAxe();;

        inv.setItem(0, katana);
        inv.setItem(1, smokerSword);
        inv.setItem(2, reinforcedGoldSword);
        inv.setItem(3, reinforcedGoldAxe);
        inv.setItem(4, reinforcedGoldPickaxe);
        inv.setItem(5, reinforcedStoneSword);
        inv.setItem(6, reinforcedStoneAxe);

        player.openInventory(inv);
    }

}

