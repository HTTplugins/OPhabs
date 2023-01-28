package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

import static java.lang.Math.*;


public class mera_mera extends logia {
    final float ExplosionRadius = 4;
    final int Abilitie1Radius = 4;
    final Material FIRE = Material.FIRE, OBSIDIANA = Material.OBSIDIAN, BEDROCK = Material.BEDROCK, AIR = Material.AIR;
    final Particle PARTICULA_FUEGO = Particle.FLAME;
    final int N_PARTICULAS = 20, RADIO_PARTICULAS = 0, RESPAWN_HEALTH = 10, RESPAWN_FOOD = 10, BERSERK_DURATION = 3600,
              BERSERK_AMPLIFIER = 2;
    boolean BERSERK = true;

    private int FIRE_POOL_DURATION = 5, FIREBALL_STORM_COOLDOWN = 20;

    public mera_mera(OPhabs plugin){
        super(plugin, Particle.FLAME, castIdentification.castMaterialMera, castIdentification.castItemNameMera, fruitIdentification.fruitCommandNameMera);
        //
        //Nombres de las habilidades:
        abilitiesNames.add("Fire Pool");
        abilitiesCD.add(0);
        abilitiesNames.add("Fireball Storm");
        abilitiesCD.add(0);
        this.runParticles();
    }

    //Habilidades activas:
    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            FirePool(user.getPlayer());
            abilitiesCD.set(0, 20); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            FireballStorm(user.getPlayer());
            abilitiesCD.set(1, 20); // Pon el cooldown en segundos
        }
    }

    public boolean comprobarUser(Player jugador){
        String nombre_user = plugin.getConfig().getString("FruitAssociations.mera_mera");
        boolean es_user = false;
        Player user = Bukkit.getPlayerExact(nombre_user);

        if(!nombre_user.equals("none") && user.equals(jugador))
            es_user = true;

        return es_user;
    }

    private void CookFood(ItemStack comida){
        final Material material = comida.getType();

        if(material == Material.BEEF)
            comida.setType(Material.COOKED_BEEF);

        if(material == Material.PORKCHOP)
            comida.setType(Material.COOKED_PORKCHOP);

        if(material == Material.SALMON)
            comida.setType(Material.COOKED_SALMON);

        if(material == Material.RABBIT)
            comida.setType(Material.COOKED_RABBIT);

        if(material == Material.MUTTON)
            comida.setType(Material.COOKED_MUTTON);

        if(material == Material.COD)
            comida.setType(Material.COOKED_COD);
    }

    public boolean isIndestructible(Block bloque) {
        boolean esIndestructible = false;

        if ((bloque.getType() == OBSIDIANA) || (bloque.getType() == BEDROCK))
            esIndestructible = true;

        return esIndestructible;
    }

    private void BerserkEffects(Player jugador) {
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, BERSERK_DURATION, BERSERK_AMPLIFIER));
    }

    public void createAbilitie1Effect(Location loc){
        Location loc_aux = loc.clone();

        for(int i = -Abilitie1Radius; i <= Abilitie1Radius; i++) {
            loc_aux.setX(loc.getX() + i);
            for(int j = -Abilitie1Radius; j <= Abilitie1Radius; j++) {
                loc_aux.setZ(loc.getZ() + j);
                if(loc_aux.getBlock().getType() == AIR)
                    loc_aux.getBlock().setType(FIRE);
            }
        }
    }

    /*public void onEntityShootBow(EntityShootBowEvent event) {
        Entity flecha = event.getProjectile();

        if(event.getEntity() instanceof Player) {
            if (comprobarUser((Player) event.getEntity())) {
                if (flecha instanceof Arrow) {
                    Entity fb = event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation().add(0, 1, 0), EntityType.FIREBALL);
                    fb.getVelocity().multiply(1.5);

                    flecha.remove();
                }
            }
        }
    }*/

    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if(comprobarUser(event.getPlayer()))
            event.getPlayer().getWorld().spawnParticle(PARTICULA_FUEGO, event.getPlayer().getLocation(), N_PARTICULAS,
                    RADIO_PARTICULAS, RADIO_PARTICULAS, RADIO_PARTICULAS);
    }


//  @EventHandler
    //public void onEntityDamage(EntityDamageEvent event) {
    //    if (event.getEntity() instanceof Player) {
    //        Player player = (Player) event.getEntity();
    //        if (event.getFinalDamage() >= player.getHealth()) {
    //            //Aqui seria la el método como tal
    //        }
    //    }
    //}

    public void onPlayerDeath(PlayerDeathEvent event) {
        Player jugador = event.getEntity();

        if(comprobarUser(jugador)) {
            World mundo = jugador.getWorld();
            Location loc = jugador.getLocation();

            if(BERSERK) {
                event.setDeathMessage("\n\n" + "MERA MERA USER: BY THE POWER OF THE UNDERWORLD... DESTROY THEM!! FROM NOW ON, don't u dare to " +
                        " feed yourself so much...!! \n");
                event.setKeepInventory(true);
                event.setKeepLevel(true);

                mundo.createExplosion(loc, ExplosionRadius * ExplosionRadius);
                createAbilitie1Effect(loc);
                mundo.playSound(jugador, Sound.MUSIC_NETHER_SOUL_SAND_VALLEY, SoundCategory.HOSTILE, ExplosionRadius * ExplosionRadius * ExplosionRadius,
                        100);

                jugador.setHealth(RESPAWN_HEALTH);
                jugador.setFoodLevel(RESPAWN_FOOD);
                BerserkEffects(jugador);
                new BukkitRunnable(){
                    @Override
                    public void run(){
                        if(BERSERK)
                            cancelTask();

                        mundo.spawnParticle(PARTICULA_FUEGO, jugador.getLocation(), N_PARTICULAS, RADIO_PARTICULAS,
                                            RADIO_PARTICULAS, RADIO_PARTICULAS, 0.05);
                    }
                    public void cancelTask(){
                        Bukkit.getScheduler().cancelTask(this.getTaskId());
                    }
                }.runTaskTimer(plugin, 0, 5);

                event.getDrops().clear();
                BERSERK = false;
            } else {
                event.setKeepInventory(false);
                event.setKeepLevel(false);
                BERSERK = true;
            }
        }
    }

    public void onEntityPickupItem(EntityPickupItemEvent event) {
        Item objeto = event.getItem();

        if(event.getEntity() instanceof Player)
            if(comprobarUser((Player) event.getEntity()))
                CookFood(objeto.getItemStack());

    }

    public void FirePool(Player player){
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i >= FIRE_POOL_DURATION) cancelTask();

                player.getWorld().playSound(player, Sound.MUSIC_DRAGON, 100, 10);

                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));

                createAbilitie1Effect(player.getLocation());

                i++;
            }

            public void cancelTask() { Bukkit.getScheduler().cancelTask(this.getTaskId()); }
        }.runTaskTimer(plugin, 0, FIRE_POOL_DURATION);
    }

    public void FireballStorm(Player player) {
        World mundo = player.getWorld();

        mundo.playSound(player, Sound.MUSIC_NETHER_NETHER_WASTES, 100, 10);
        mundo.spawnEntity(player.getLocation().clone().add(0, 5, 0), EntityType.FIREBALL);
        mundo.spawnEntity(player.getLocation().clone().add(0, 5, 3), EntityType.FIREBALL);
        mundo.spawnEntity(player.getLocation().clone().add(0, 5, -3), EntityType.FIREBALL);
        mundo.spawnEntity(player.getLocation().clone().add(3, 5, 0), EntityType.FIREBALL);
        mundo.spawnEntity(player.getLocation().clone().add(-3, 5, 0), EntityType.FIREBALL);
    }

    public void a3(Player player) {

    }

    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        Location loc = event.getEgg().getLocation();
        World mundo = event.getEgg().getWorld();

        mundo.createExplosion(loc, ExplosionRadius);

        if(event.isHatching())
            event.setHatchingType(EntityType.BLAZE);

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
    }

    @Override
    public void runParticles() {
        new BukkitRunnable(){
            int ticks = 0;
            double i = 0;
            double y = 0;

            Random random = new Random();
            @Override
            public void run() {

                Player player = null;
                if(user != null)
                    if(user.getPlayer() != null){
                        player = user.getPlayer();

                        ItemStack caster = null;
                        boolean isCaster = false;
                        if(player != null){
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), player)){
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else{
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), player)){
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }

                        if(isCaster && caster.getItemMeta().getDisplayName().equals(castIdentification.castItemNameMera)){

                            double x = sin(i)/2;
                            double z = cos(i)/2;

                            double xr = player.getLocation().getX() + x;
                            double yr = player.getLocation().getY() + y;
                            double zr = player.getLocation().getZ() + z;

                            Location partLoc = new Location(player.getWorld(),xr,yr,zr);
                            player.spawnParticle(element,partLoc, 0,0,0,0);

                            summonParticle(player);
                            summonParticle(player);
                            player.setAllowFlight(true);

                        }else {
                            player.setAllowFlight(false);
                            player.setFlying(false);

                        }
                    }
                i+= 0.5;
                y += 0.05;

                if(y > 2)
                    y = 0;


            }

            public void summonParticle(Player player){

                double xdecimals;
                double ydecimals;
                double zdecimals;

                double x,y,z;


                xdecimals = random.nextDouble();
                ydecimals = random.nextDouble();
                zdecimals = random.nextDouble();

                x = random.nextInt(1 + 1 ) - 1 + xdecimals;
                y = random.nextInt(2 )  + ydecimals ;
                z = random.nextInt(1 + 1 ) - 1 + zdecimals;

                player.getWorld().spawnParticle(element,player.getLocation().add(x,y,z),0,0,0,0);

            }



        }.runTaskTimer(plugin,0,1);

    }
}
