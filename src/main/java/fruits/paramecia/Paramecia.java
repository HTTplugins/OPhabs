package fruits.paramecia;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import fruits.DevilFruit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class Paramecia extends DevilFruit
{
    public Paramecia(int id, String name, String displayName, String commandName)
    {
        super(id, name, displayName, commandName);

    }
}
