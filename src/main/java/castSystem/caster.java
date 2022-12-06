package castSystem;

import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;

public class caster implements Listener {
    OPhabs plugin;

    public caster(OPhabs plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) )
            if(e.getHand().equals(EquipmentSlot.HAND))
                if(e.getItem().getType().equals(Material.IRON_INGOT) ) {
                    //Click Logic
                    System.out.println("CLICKED");
                }


    }

}
