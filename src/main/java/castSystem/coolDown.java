package castSystem;

import htt.ophabs.OPhabs;
import abilitieSystem.abilityUser;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class coolDown {

    private OPhabs plugin;
    private Map<String, abilityUser> users = new HashMap<>();

    public coolDown(OPhabs plugin, Map<String, abilityUser> users) {
        this.plugin = plugin;
        this.users = users;

        runCoolDownSystem();
    }

    public void runCoolDownSystem(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (abilityUser user : users.values()) {
                    if(user.hasFruit()){
                        ArrayList<Integer> coolDownList = user.getAbilitiesCD();
                        for ( int i = 0; i < coolDownList.size(); i++){
                            if (coolDownList.get(i) > 0) {
                                coolDownList.set(i, (coolDownList.get(i)-1));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
