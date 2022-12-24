package abilitieSystem;


import htt.ophabs.OPhabs;
import fruitSystem.devilFruitUser;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.ArrayList;
import java.util.Map;

public class abilities {
    protected OPhabs plugin;
    protected devilFruitUser user;
    protected Integer actual;
    public ArrayList<String> abilitiesNames = new ArrayList<>();

    public abilities(OPhabs plugin, devilFruitUser user){
        this.plugin = plugin;
        this.user = user;
    }
    public abilities(OPhabs plugin){
        this.plugin = plugin;
        this.user = null;
        actual=0;
    }

    public ArrayList<String> getAbilitiesNames(){
        return abilitiesNames;
    }
    
    public void setUser(devilFruitUser user){
        this.user = user;
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
