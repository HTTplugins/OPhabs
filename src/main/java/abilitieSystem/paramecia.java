package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;

/**
 * @brief Paramecia --> Devil fruit Subtype.
 * @author Vaelico786.
 */
public class paramecia extends df{

    /**
     * @brief Paramecia constructor.
     * @param plugin OPhabs plugin.
     * @param castMaterial Material type of the caster.
     * @param castName Name of the caster.
     * @param commandName Name of that represents the fruit.
     * @author Vaelico786.
     */
    public paramecia(OPhabs plugin, Material castMaterial, String castName, String commandName){
        super(plugin, castMaterial, castName, commandName);
    }

    /**
     * @brief Paramecia constructor (with user).
     * @param plugin OPhabs plugin.
     * @param castMaterial Material type of the caster.
     * @param castName Name of the caster.
     * @param commandName Name of that represents the fruit.
     * @param user Zoan user.
     * @author Vaelico786.
     */
    public paramecia(OPhabs plugin, abilityUser user, Material castMaterial, String castName, String commandName){
        super(plugin, user, castMaterial, castName, commandName);
    }
}

