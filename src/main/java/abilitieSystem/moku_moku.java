package abilitieSystem;


import castSystem.castIdentification;
import htt.ophabs.OPhabs;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Vex;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;;
import java.util.Random;

import static java.lang.Math.*;
import java.util.ArrayList;

public class moku_moku extends logia {
    final int numMaxSmokers=4, smokeWorldAmplitude=7, smokeWorldTicks=400;
    static ArrayList<LivingEntity> smokeWorldEntities = new ArrayList<>();
    static boolean smokeWorldON = false;

    public int numSmokers=0;
    private ItemStack fist;
    

    public moku_moku(OPhabs plugin) {
        super(plugin, 0, 0, Particle.CLOUD, 4, "moku_moku", "Moku Moku no Mi", "Moku Moku caster", 5, 1.9);

        abilitiesNames.add("Smoke Body");
        abilitiesCD.add(0);
        abilitiesNames.add("Summon Smoker");
        abilitiesCD.add(0);
        abilitiesNames.add("Smoke Punch");
        abilitiesCD.add(0);
        abilitiesNames.add("Smoke World");
        abilitiesCD.add(0);

        fist = new ItemStack(castIdentification.castMaterial);
        ItemMeta itemMeta = fist.getItemMeta();
        itemMeta.setCustomModelData(getFruit().getCustomModelDataId()); // Establecer el Custom Model Data
        fist.setItemMeta(itemMeta);
        runParticles();
    }

    public void ability1() {
        if(abilitiesCD.get(0)==0) {
            logiaBody(user.getPlayer());
            abilitiesCD.set(0, 0);
        }
    }
    public void ability2() {
        if(abilitiesCD.get(1)==0) {
            summonSmoker(user.getPlayer());
            abilitiesCD.set(1, 3);
        }
    }

    public void ability3() {
        if(abilitiesCD.get(2)==0) {
            smokePunch(user.getPlayer().getLocation().add(user.getPlayer().getEyeLocation().clone().getDirection()).add(new Vector(0,0.7,0)));
            abilitiesCD.set(2, 2);
        }
    }

    public void ability4() {
        if(abilitiesCD.get(3)==0) {
            smokeWorld();
            abilitiesCD.set(3, 30);
        }
    }

    public void toggleFly(Player player) {
        if (!logiaBodyON){
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
    
    public void runParticles(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(logiaBodyON || user.getPlayer().isDead()))
                    cancelTask();
                player.getWorld().spawnParticle(element, player.getLocation(), 10, 0.5, 0.5, 0.5, 0);
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 5, 3);
    }

    @Override
    public void runParticles() {
        new BukkitRunnable() {
            int ticks = 0;
            double i = 0;
            double y = 0;

            Random random = new Random();
            @Override
            public void run() {
                Player player = null;
                if(user != null)
                    if(user.getPlayer() != null) {
                        player = user.getPlayer();

                        ItemStack caster = null;
                        boolean isCaster = false;
                        if(player != null) {
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user)) {
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else{
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user)) {
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(fruit.getCasterName())) {
                            double x = sin(i)/2;
                            double z = cos(i)/2;

                            double xr = player.getLocation().getX() + x;
                            double yr = player.getLocation().getY() + y;
                            double zr = player.getLocation().getZ() + z;

                            Location partLoc = new Location(player.getWorld(),xr,yr,zr);
                            player.spawnParticle(Particle.CLOUD,partLoc, 0,0,0,0);

                            summonParticle(0.5,player);
                            
                            player.setAllowFlight(true);

                        } else {
                            if(!logiaBodyON){
                                player.setAllowFlight(false);
                                player.setFlying(false);
                            }
                        }
                    }
                i+= 0.5;
                y+=0.05;

                if(y > 2)
                    y = 0;
            }

            public void summonParticle(double y,Player player) {
                double xdecimals;
                double ydecimals;
                double zdecimals;
                double x,z;

                xdecimals = random.nextDouble();
                ydecimals = random.nextDouble();
                zdecimals = random.nextDouble();

                x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                y += random.nextInt(1 + 1 ) - 1 + ydecimals;
                z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                player.getWorld().spawnParticle(element,player.getLocation().add(x,y,z),0,0,0,0);
            }
        }.runTaskTimer(plugin,0,1);
    }
//    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        super.onPlayerDeath(event);
        Player player = event.getEntity();
        if (logiaBodyON)
            logiaBody(player);
    }

    public boolean logiaBody(Player player) {
            if (!logiaBodyON) {
                logiaBodyON = true;
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));
                runParticles(player);
            } else {
                logiaBodyON = false;
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            return logiaBodyON;
    }

    public void setTarget(Vex entity, Player player){
        Location loc = entity.getLocation();
        boolean found = false;
          for (Entity e : loc.getWorld().getNearbyEntities(loc, 10, 10, 10)){
            if(e instanceof LivingEntity && e != player && e != entity && !(e instanceof Vex)){
                entity.setTarget(((LivingEntity)e));
                found = true;
                break;
            }
          }
        if(!found){
            entity.setTarget(null);
        }

    }
    public void summonSmoker(Player player){
        if(numSmokers<=numMaxSmokers){
            Location loc = player.getLocation();
            Vex smoker = ((Vex) player.getWorld().spawnEntity(loc,EntityType.VEX));
            smoker.setCustomName("Smoker");
            smoker.setHealth(0.5);
            numSmokers++;
            smoker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 2, false, false));
            smoker.getEquipment().setItemInMainHand(null); 
            new BukkitRunnable() {
                @Override
                public void run() {
                    //if smoker is dead or player is dead or has been 20 seconds
                    if (smoker.isDead() || player.isDead() || smoker.getTicksLived() > 400 ){//|| setTarget(((Vex)smoker), player) == null ) {
                        smoker.setHealth(0);
                        numSmokers--;
                        cancelTask();
                    }
                    if(smoker.getTarget() == null || smoker.getTarget().isDead() || smoker.getTarget() == player ){
                        setTarget(smoker, player);
                    }
                    smoker.getWorld().spawnParticle(element, smoker.getLocation(), 10, 0.5, 0.5, 0.5, 0);
                }

                public void cancelTask() {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }
            }.runTaskTimer(plugin, 0, 3); 
        }
    }



    /**
     * @brief CORE ABILITY 3: "Smoke Punch". Unleash a punch in front of the player that deals extra damage.
     * @author Vaelico786.
     */
    public void smokePunch(Location loc) {

            ArmorStand armorStand = OPHLib.generateCustomFloatingItem(user.getPlayer().getLocation().add(0,-0.5,0), fist, false);


        new BukkitRunnable(){

            // Configurar las propiedades del armor stand

            Entity entity = armorStand;
            Location origin=entity.getLocation();


            int ticks = 0;
            boolean first = true;
            Vector dir = user.getPlayer().getEyeLocation().getDirection(), aux;
            double distancePerTick = 1.0 / 2.0, distanceFromPlayer = 0;
            
            // Calcula la distancia total que el ArmorStand debe recorrer
            @Override
            public void run() {
                distanceFromPlayer+=distancePerTick;
                Vector movement = dir.clone().multiply(distanceFromPlayer);
                entity.teleport(origin.clone().add(movement));

                entity.getWorld().getNearbyEntities(entity.getLocation(), 2,2,2).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != user.getPlayer() && entity != ent && !(ent instanceof ArmorStand) && !(ent instanceof Vex && ent.getCustomName().equals("Smoker"))) {
                        ((LivingEntity) ent).damage(14,(Entity) user.getPlayer());
                    }
                });

                aux = new Vector((entity.getLocation().getX()-origin.getX()), (entity.getLocation().getY()-origin.getY()), (entity.getLocation().getZ()-origin.getZ()));



                for(double i = -2; i<0; i+=0.2)
                    OPHLib.circleEyeVector
                        (0.4,1,i,null,element,user.getPlayer(), entity.getLocation().add(0,1,0));


                if (entity.isDead()) {
                    entity.getLocation().getBlock().setType(Material.AIR);
                    cancelTask();
                }

                if (aux.length() < 0.5 && ticks>10 || ticks > 20) {
                    entity.remove();
                    cancelTask();
                }
                
                ticks++;
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 2, 1);
    }


    public void smokeWorld(){
        int period = 5;
        smokeWorldON=true;
        Player player = user.getPlayer();
        new BukkitRunnable() {
            int tickCount=0;
            @Override
            public void run() {

                smokeWorldEntities.clear();
                if ( player.isDead() || tickCount > smokeWorldTicks / period){
                    cancelTask();
                }

                player.getWorld().spawnParticle(element,
                                                player.getLocation(), 
                                                400, 
                                                smokeWorldAmplitude, smokeWorldAmplitude/2, smokeWorldAmplitude, 
                                                0);

                player.getNearbyEntities(smokeWorldAmplitude, smokeWorldAmplitude/2, smokeWorldAmplitude).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != player){
                        // ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, period*2, 3, false));
                        ((LivingEntity)ent).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, period+2, 2, false));
                        smokeWorldEntities.add((LivingEntity) ent);


                        // Redice air
                        if(((LivingEntity)ent).getRemainingAir() == ((LivingEntity) ent).getMaximumAir())
                            ((LivingEntity) ent).setRemainingAir(300);
                    }

                });

                tickCount++;
            }
            public void cancelTask() {
                smokeWorldON=false;
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(plugin, 0, period); 
    }

    public static void onEntityAirChangeEvent(EntityAirChangeEvent event){
        if(OPhabs.abilitiesList.get(3).getUser() != null && 
           OPhabs.abilitiesList.get(3).getUser().getPlayer() != null){
            Player player = plugin.abilitiesList.get(3).getUser().getPlayer();
            if(smokeWorldEntities.contains(event.getEntity()) && event.getAmount()>0){
                if(smokeWorldON){
                    LivingEntity entity = (LivingEntity) event.getEntity();
                    if(entity.getRemainingAir() == 0){
                        entity.damage(1, player);
                        event.setCancelled(true);
                    }
                    else{
                        event.setAmount(entity.getRemainingAir()-2);
                    }
                }
                else
                    smokeWorldEntities.clear();
            }
        }
    }

    public static void onEntityDamageBySmoker(EntityDamageByEntityEvent event){
        Player player = OPhabs.abilitiesList.get(3).getUser().getPlayer();
        if(player != null){
            ((LivingEntity) event.getEntity()).damage(3, player);
            event.setCancelled(true);
        }
    }

}
