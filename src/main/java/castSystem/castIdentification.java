package castSystem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class castIdentification {


    public static Material castMaterialYami = Material.BLACK_DYE,
                            castMaterialMera = Material.MAGMA_CREAM,
                            castMaterialGura = Material.QUARTZ,
                            castMaterialMoku = Material.FEATHER,
                            castMaterialNekoReoparudo = Material.YELLOW_DYE;

    public static String castItemNameYami = "Yami Yami caster",
                         castItemNameMera = "Mera Mera caster",
                         castItemNameGura = "Gura Gura caster",
                         castItemNameMoku = "Moku Moku caster",
                         castItemNameNekoReoparudo = "Neko Neko Reoparudo caster";



    public static boolean itemIsCaster(ItemStack item) {
        if (item == null) {
            return false;
        }
        String itemName = item.getItemMeta().getDisplayName();
        Material itemMaterial = item.getType();

        if ((itemName.equals(castIdentification.castItemNameYami) && itemMaterial.equals(castIdentification.castMaterialYami))
                || (itemName.equals(castIdentification.castItemNameMera) && itemMaterial.equals(castIdentification.castMaterialMera))
                || (itemName.equals(castIdentification.castItemNameGura) && itemMaterial.equals(castIdentification.castMaterialGura))
                || (itemName.equals(castIdentification.castItemNameMoku) && itemMaterial.equals(castIdentification.castMaterialMoku))
                || (itemName.equals(castIdentification.castItemNameNekoReoparudo) && itemMaterial.equals(castIdentification.castMaterialNekoReoparudo))) 
            return true;
        else
            return false;
    }

}
