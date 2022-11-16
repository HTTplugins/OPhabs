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




        new BukkitRunnable() {
            int capa = 0;
            final int maxCapa = 20;

            final int profundidad = 5;


            int randomQuoteMax = 100;

            final Location playerLoc = event.getPlayer().getLocation();
            public void run() {

                createVoidFloor(playerLoc,profundidad,capa,randomQuoteMax);

                capa++;
                randomQuoteMax -= 1;

                if(capa == maxCapa)
                    cancelTask(this.getTaskId());

            }

            public void cancelTask(int id){
                Bukkit.getScheduler().cancelTask(id);
            }



        }.runTaskTimer(plugin, 0,5);

    }

    public void putVoidblock(Location playerLoc, int x, int y, int z, int randomQuoteMax){
        Location blackBlock;
        int probability = (int) (Math.random() * 100);


        if(probability < randomQuoteMax) {
            blackBlock = new Location(playerLoc.getWorld(), x, y, z);
            if (blackBlock.getBlock().getType() == Material.GRASS)
                blackBlock.getBlock().setType(Material.AIR);
            else if (blackBlock.getBlock().getType() != Material.AIR) {
                blackBlock.getBlock().setType(Material.BLACK_CONCRETE);

            }
        }

    }

    public void createVoidFloor(Location playerLoc, int profundidad, int capa, int randomQuoteMax){


        for(int prof=-profundidad; prof<profundidad; prof++) {

            //Upper and down line
            for (int x = playerLoc.getBlockX() - capa; x <= playerLoc.getBlockX() + capa; x++){
                putVoidblock(playerLoc,x,playerLoc.getBlockY()-1-prof, playerLoc.getBlockZ() + capa,randomQuoteMax);
                putVoidblock(playerLoc,x,playerLoc.getBlockY()-1-prof, playerLoc.getBlockZ() - capa,randomQuoteMax);
            }

            //laterals line
            for (int z = playerLoc.getBlockZ() - capa + 1; z <= playerLoc.getBlockZ() + capa - 1; z++) {
                putVoidblock(playerLoc, playerLoc.getBlockX() + capa, playerLoc.getBlockY() - 1 - prof, z,randomQuoteMax);
                putVoidblock(playerLoc, playerLoc.getBlockX() - capa, playerLoc.getBlockY() - 1 - prof, z,randomQuoteMax);
            }


        }
    }

}
