package castSystem;

import htt.ophabs.OPhabs;
import fruitSystem.devilFruitUser;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class coolDown {

    private OPhabs plugin;
    private Map<String, devilFruitUser> dfPlayers = new HashMap<>();

    public coolDown(OPhabs plugin, Map<String, devilFruitUser> dfPlayers) {
        this.plugin = plugin;
        this.dfPlayers = dfPlayers;

        runCoolDownSystem();
    }

    public void runCoolDownSystem(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (devilFruitUser dfUser : dfPlayers.values()) {
                    ArrayList<Integer> coolDownList = dfUser.getAbilitiesCD();
                    for ( int i = 0; i < coolDownList.size(); i++){
                        if (coolDownList.get(i) > 0) {
                            coolDownList.set(i, (coolDownList.get(i)-1));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
