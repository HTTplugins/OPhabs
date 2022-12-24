package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import skin.changer.skinsChanger;


public class zoan extends abilities{
    public boolean transformed = false;
    skinsChanger skinC = new skinsChanger();
    String skinUrl="", skinAwakenUrl="";
    public zoan(OPhabs plugin){
        super(plugin);
    }

    public void transformation(){
        Player player = user.getPlayer();
        if(!transformed){
            skinC.changeSkin(player, skinUrl);
            transformed = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (user.getPlayer().isDead() || !transformed) {
                        skinC.resetSkin(user.getPlayer());
                        cancelTask();
                    }
                }

                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 0, 10);
        }
        else
            transformed = !transformed;    
    }
}

