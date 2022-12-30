package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.toRadians;

public class magu_magu extends logia {

    public magu_magu(OPhabs plugin){
        super(plugin, Particle.FLAME);
        abilitiesNames.add("AB1");
        abilitiesCD.add(0);
        abilitiesNames.add("AB2");
        abilitiesCD.add(0);
        this.runParticles();

    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            System.out.println("AB1");
            abilitiesCD.set(0, 20); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            System.out.println("AB2");;
            abilitiesCD.set(1, 20); // Pon el cooldown en segundos
        }
    }

    @Override
    public void runParticles() {



        new BukkitRunnable(){

            int ticks = 0;

            Random random = new Random();
            boolean lavaAmbiance = false;
            @Override
            public void run() {

                Player player = null;
                if(user != null)
                    if(user.getPlayer() != null){
                        player = user.getPlayer();

                        ItemStack caster = null;

                        if(player != null)
                            caster = player.getInventory().getItemInMainHand();

                        if(castIdentification.itemIsCaster(caster,player) && caster.getItemMeta().getDisplayName().equals(castIdentification.castItemNameMagu)){

                            double xdecimals = random.nextDouble();
                            double zdecimals = random.nextDouble();

                            double x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                            double z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                            player.getWorld().spawnParticle(Particle.LAVA,player.getLocation().add(x,1,z),0,0,0,0);

                             xdecimals = random.nextDouble();
                             zdecimals = random.nextDouble();

                             x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                             z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                            player.getWorld().spawnParticle(Particle.LAVA,player.getLocation().add(x,1,z),0,0,0,0);



                            if (!lavaAmbiance){
                                lavaAmbiance = true;
                                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_LAVA_AMBIENT,100,1);

                                double r = random.nextDouble();

                                if(r < 0.5)
                                    player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP,100,1);
                            }

                            if(ticks == 80){
                                lavaAmbiance = false;
                                ticks = 0;
                            }

                        }else {
                            lavaAmbiance = false;

                        }
                    }

                ticks++;

            }

        }.runTaskTimer(plugin,0,2);

    }
}
