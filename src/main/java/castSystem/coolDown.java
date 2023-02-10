package castSystem;

import htt.ophabs.OPhabs;
import abilitieSystem.abilityUser;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * @brief coolDown system for ability casting.
 * @author RedRiotTank, Vaelico786.
 */
public class coolDown {

    private OPhabs plugin;
    private Map<String, abilityUser> users = new HashMap<>();

    /**
     * @brief CoolDown system constructor.
     * @param plugin OPhabs plugin.
     * @param users map of <user - abilityUser>.
     * @author RedRiotTank, Vaelico786.
     */
    public coolDown(OPhabs plugin, Map<String, abilityUser> users) {
        this.plugin = plugin;
        this.users = users;

        runCoolDownSystem();
    }

    /**
     * @brief CoolDown system execution.
     * @author RedRiotTank, Vaelico786.
     */
    public void runCoolDownSystem(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (abilityUser user : users.values())
                    if(user.hasFruit()){
                        ArrayList<Integer> coolDownList = user.getAbilitiesCD();
                        for ( int i = 0; i < coolDownList.size(); i++)
                            if (coolDownList.get(i) > 0)
                                coolDownList.set(i, (coolDownList.get(i)-1));
                    }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
