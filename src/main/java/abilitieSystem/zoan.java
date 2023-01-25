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

    public zoan(OPhabs plugin, Material castMaterial, String castName, String commandName) {
        super(plugin, castMaterial, castName, commandName);
        abilitiesNames.add("Transform");
        abilitiesCD.add(0);
    }

    public zoan(OPhabs plugin, devilFruitUser user, Material castMaterial, String castName, String commandName) {
        super(plugin, user, castMaterial, castName, commandName);
        abilitiesNames.add("Transform");
        abilitiesCD.add(0);
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

