package oldSystem.castSystem;

import oldSystem.abilitieSystem.abilityUser;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.String;

/**
 * @brief Cast identification class. We initialize here caster materials and caster item names.
 * @author RedRiotTank, MiixZ, Vaelico786.
 */
public class castIdentification {
    public static Material castMaterial = Material.QUARTZ;

    /**
     * @brief Checks if an item is a caster.
     * @param item Item to check.
     * @param player Player that haves the caster.
     * @return true if the item is a caster, false if not.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    public static boolean itemIsCaster(ItemStack item , abilityUser user) {

        if(user == null || !user.getPlayer().isOnline())
            return false;
        if (item.getType() != castMaterial)
            return false;
        

        String itemName = item.getItemMeta().getDisplayName();

        if (itemName.contains(user.getDFAbilities().getCasterName()))
            return true;
        else
            return false;
    }

}
