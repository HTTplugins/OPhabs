package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.entity.PlayerDeathEvent;
import skin.changer.skinsChanger;
import fruitSystem.devilFruitUser;


public class zoan extends abilities{
    public boolean transformed = false;
    skinsChanger skinC = new skinsChanger();
    String skinUrl="", skinAwakenUrl="";

    public zoan(OPhabs plugin){
        super(plugin);
        abilitiesNames.add("Transform");
    }

    public zoan(OPhabs plugin, devilFruitUser user){
        super(plugin, user);
        abilitiesNames.add("Transform");
    }


    public void transformation(){
        Player player = user.getPlayer();
        if(!transformed){
            skinC.changeSkin(player, skinUrl);
            transformed = true;
        }
        else{
            transformed = !transformed;
            skinC.resetSkin(player);
        }
    }

    public void ability1(){
        transformation();
    }
    public void playerOnDeath(PlayerDeathEvent event){
        if(event.getEntity().getName().equals(user.getPlayer().getName())){
            if(transformed){
                transformed = false;
                skinC.resetSkin(user.getPlayer());
                super.user = null;
            }
        }
    }
}

