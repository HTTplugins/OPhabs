package abilitieSystem;


import htt.ophabs.OPhabs;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.ArrayList;

public class abilities {
    protected OPhabs plugin;
    protected devilFruitUser user;
    public ArrayList<String> abilitiesNames = new ArrayList<>();

    public abilities(OPhabs plugin, devilFruitUser user){
        this.plugin = plugin;
        this.user = null;
    }

					public void ability1(){}
    public void ability2(){}
					public void ability3(){}
					public void ability4(){}
					public void ability5(){}
					public void ability6(){}
    public void onEntityDamage(EntityDamageEvent event){}
    public void onPlayerDeath(PlayerDeathEvent event){}
    public void playerOnWater(PlayerMoveEvent event){}
    

}
