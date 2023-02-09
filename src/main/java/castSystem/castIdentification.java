package castSystem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class castIdentification {
    public static Material castMaterialYami = Material.BLACK_DYE,
            castMaterialMera = Material.BLAZE_ROD,
            castMaterialGura = Material.QUARTZ,
            castMaterialMoku = Material.FEATHER,
            castMaterialNekoReoparudo = Material.YELLOW_DYE,
            castMaterialMagu = Material.MAGMA_CREAM,
            castMaterialGoro = Material.IRON_INGOT,
            castMaterialIshi = Material.FLINT,
            castMaterialGoru = Material.GOLD_INGOT,
            castMaterialInuOkuchi = Material.YELLOW_DYE,
            castMaterialRyuAllosaurs = Material.YELLOW_DYE,
            castMaterialOpe = Material.BRICK,
            castMaterialZushi = Material.ARROW,
            castMaterialSuke = Material.LEATHER;

    public static String castItemNameYami = "Yami Yami caster",
            castItemNameMera = "Mera Mera caster",
            castItemNameGura = "Gura Gura caster",
            castItemNameMoku = "Moku Moku caster",
            castItemNameNekoReoparudo = "Neko Neko Reoparudo caster",
            castItemNameMagu = "Magu Magu caster",
            castItemNameGoro = "Goro Goro caster",
            castItemNameIshi = "Ishi Ishi caster",
            castItemNameGoru = "Goru Goru caster",
            castItemNameInuOkuchi = "Inu Inu Okuchi caster",
            castItemNameRyuAllosaurs = "Ryu Ryu Allosaurs caster",
            castItemNameOpe = "Ope Ope caster",
            castItemNameZushi = "Zushi Zushi caster",
            castItemNameSuke = "Suke Suke caster";

    public static boolean itemIsCaster(ItemStack item , Player player) {

        if(player == null || !player.isOnline())
            return false;
        if (item.getType() == Material.AIR){

            return false;
        }

        String itemName = item.getItemMeta().getDisplayName();
        Material itemMaterial = item.getType();

        if((itemName.equals(castIdentification.castItemNameYami) && itemMaterial.equals(castIdentification.castMaterialYami))
                || (itemName.equals(castIdentification.castItemNameMera) && itemMaterial.equals(castIdentification.castMaterialMera))
                || (itemName.equals(castIdentification.castItemNameGura) && itemMaterial.equals(castIdentification.castMaterialGura))
                || (itemName.equals(castIdentification.castItemNameMoku) && itemMaterial.equals(castIdentification.castMaterialMoku))
                || (itemName.equals(castIdentification.castItemNameNekoReoparudo) && itemMaterial.equals(castIdentification.castMaterialNekoReoparudo))
                || (itemName.equals(castIdentification.castItemNameMagu) && itemMaterial.equals(castIdentification.castMaterialMagu))
                || (itemName.equals(castIdentification.castItemNameGoro) && itemMaterial.equals(castIdentification.castMaterialGoro))
                || (itemName.equals(castIdentification.castItemNameIshi) && itemMaterial.equals(castIdentification.castMaterialIshi))
                || (itemName.equals(castIdentification.castItemNameGoru) && itemMaterial.equals(castIdentification.castMaterialGoru))
                || (itemName.equals(castIdentification.castItemNameInuOkuchi) && itemMaterial.equals(castIdentification.castMaterialInuOkuchi))
                || (itemName.equals(castIdentification.castItemNameRyuAllosaurs) && itemMaterial.equals(castIdentification.castMaterialRyuAllosaurs))
                ||(itemName.equals(castIdentification.castItemNameOpe) && itemMaterial.equals(castIdentification.castMaterialOpe))
                || (itemName.equals(castIdentification.castItemNameZushi) && itemMaterial.equals(castIdentification.castMaterialZushi))
                || (itemName.equals(castIdentification.castItemNameSuke) && itemMaterial.equals(castIdentification.castMaterialSuke))){
            return true;
        }
        else
            return false;
    }

}
