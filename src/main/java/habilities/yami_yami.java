package habilities;

import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class yami_yami implements Listener {
    OPhabs plugin;
    public yami_yami(OPhabs plugin){this.plugin = plugin;}
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Bukkit.getConsoleSender().sendMessage("event");


        Location playerLoc = event.getPlayer().getLocation();

        new BukkitRunnable() {
            int capa = 0;
            int maxCapa = 3;
            Location blackBlock;
            public void run() {


                   for(int x = playerLoc.getBlockX()-capa; x <= playerLoc.getBlockX()+capa; x++) {
                       blackBlock = new Location(playerLoc.getWorld(), x, playerLoc.getBlockY() - 1, playerLoc.getBlockZ()+capa);
                       if (blackBlock.getBlock().getType() != Material.AIR)
                           blackBlock.getBlock().setType(Material.BLACK_CONCRETE);

                       blackBlock = new Location(playerLoc.getWorld(), x, playerLoc.getBlockY() - 1, playerLoc.getBlockZ()-capa);
                       if (blackBlock.getBlock().getType() != Material.AIR)
                           blackBlock.getBlock().setType(Material.BLACK_CONCRETE);
                   }

                   for(int z = playerLoc.getBlockZ()-capa+1; z <= playerLoc.getBlockZ()+capa-1; z++){
                       blackBlock = new Location(playerLoc.getWorld(), playerLoc.getBlockX()+capa, playerLoc.getBlockY() - 1, z);
                       if (blackBlock.getBlock().getType() != Material.AIR)
                           blackBlock.getBlock().setType(Material.BLACK_CONCRETE);

                       blackBlock = new Location(playerLoc.getWorld(), playerLoc.getBlockX()-capa, playerLoc.getBlockY() - 1, z);
                       if (blackBlock.getBlock().getType() != Material.AIR)
                           blackBlock.getBlock().setType(Material.BLACK_CONCRETE);


                   }


                    capa++;

                if(capa == maxCapa)
                    cancelTask(this.getTaskId());


            }

            public void cancelTask(int id){
                Bukkit.getScheduler().cancelTask(id);
            }



        }.runTaskTimer(plugin, 0,50);






    }




}
