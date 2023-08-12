package newSystem.consumables;

import htt.ophabs.OPhabs;
import newSystem.registry.fruits.IFruitRegistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import newSystem.fruits.DevilFruit;

//
// Gestiona el Item que se puede comer para activar los poderes de la fruteira
//
public class ConsumableDevilFruit
{
    public static final Material FRUIT_MATERIAL = Material.APPLE;

    public static ItemStack getItem(int fruitID)
    {
        ItemStack fruitItem = new ItemStack(FRUIT_MATERIAL);
        DevilFruit fruit = OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(fruitID);

        if (fruit != null)
        {
            ItemMeta fruitMeta = fruitItem.getItemMeta();
            fruitMeta.setDisplayName(fruit.getDisplayName());
            fruitMeta.setCustomModelData(fruitID);
            fruitItem.setItemMeta(fruitMeta);
        }

        return fruitItem;
    }

    // Determina si un item es una fruta consumible
    public static boolean isConsumableDevilFruit(ItemStack item)
    {
        if (item.getType() != FRUIT_MATERIAL)
            return false;

        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null)
            return false;

        DevilFruit fruit = OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(itemMeta.getCustomModelData());

        if (fruit == null || fruit.getUser() != null)
            return false;

        return fruit.getDisplayName().equals(itemMeta.getDisplayName());
    }
}
