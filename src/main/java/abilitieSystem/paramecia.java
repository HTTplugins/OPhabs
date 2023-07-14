package abilitieSystem;

import htt.ophabs.OPhabs;

/**
 * @brief Paramecia --> Devil fruit Subtype.
 * @author Vaelico786.
 */
public class paramecia extends df {

    /**
     * @brief Paramecia constructor.
     * @param plugin OPhabs plugin.
     * @param castMaterial Material type of the caster.
     * @param castName Name of the caster.
     * @param commandName Name of that represents the fruit.
     * @author Vaelico786.
     */
    public paramecia(OPhabs plugin, double damageBonus, double armorBonus, int id, String fruitCommandName, String fruitItemName, String casterName, double casterDamage, double casterSpeed){
        super(plugin, damageBonus, armorBonus, id, fruitCommandName, fruitItemName, casterName, casterDamage, casterSpeed);
    }

}

