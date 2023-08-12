package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import htt.ophabs.fileSystem;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class mera_mera extends logia {
    final float ExplosionRadius = 4;
    final int Abilitie1Radius = 4;
    final Material FIRE = Material.FIRE, OBSIDIANA = Material.OBSIDIAN, BEDROCK = Material.BEDROCK, AIR = Material.AIR;
    final Particle PARTICULA_FUEGO = Particle.FLAME;
    final int N_PARTICULAS = 20, RADIO_PARTICULAS = 0, RESPAWN_HEALTH = 10, RESPAWN_FOOD = 10;
    private final int FIRE_POOL_DURATION = 3, FIREBALL_STORM_COOLDOWN = 5, FIRE_POOL_COOLDOWN = 15, FAIYABU_COOLDOWN = 5,
                      FIREBREATH_COOLDOWN = 10;

    private ArrayList<LivingEntity> golpeadosFaiyabu = new ArrayList<>();

    public mera_mera(OPhabs plugin) {
        super(plugin, 4, 0, Particle.FLAME, 2, "mera_mera", "Mera Mera no Mi", "Mera Mera caster", 7, 1.7);
        //Nombres de las habilidades:
        abilitiesNames.add("Fire Pool");
        abilitiesCD.add(0);
        abilitiesNames.add("Fireball Storm");
        abilitiesCD.add(0);
        abilitiesNames.add("Faiyābū");
        abilitiesCD.add(0);
        abilitiesNames.add("Fire Breath");
        abilitiesCD.add(0);
        this.runParticles();
    }

    // Habilidades activas:
    public void ability1() {
        if(abilitiesCD.get(0) == 0) {
            FirePool(user.getPlayer());
            abilitiesCD.set(0, FIRE_POOL_COOLDOWN);
        }
    }

    public void ability2() {
        if(abilitiesCD.get(1) == 0) {
            FireballStorm(user.getPlayer());
            abilitiesCD.set(1, FIREBALL_STORM_COOLDOWN);
        }
    }

    public void ability3() {
        if(abilitiesCD.get(2) == 0) {
            Faiyabu(user.getPlayer());
            abilitiesCD.set(2, FAIYABU_COOLDOWN);
        }
    }

    public void ability4() {
        if(abilitiesCD.get(3) == 0) {
            OPHLib.fireBreath(user,new Vector(0,0,0), new Vector(0,0,0),
                              0, 10, 140, 5, 0.1,
                              3, 1.5, 4,"firebreath");
            abilitiesCD.set(3, FIREBREATH_COOLDOWN);
        }
    }

    public boolean comprobarUser(Player jugador) {
        String nombre_user = fileSystem.getFruitLinkedUser("mera_mera");
        boolean es_user = false;
        Player user = Bukkit.getPlayerExact(nombre_user);

        if(!nombre_user.equals("none") && user.equals(jugador))
            es_user = true;

        return es_user;
    }

    private void CookFood(ItemStack comida) {
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

    public void createAbilitie1Effect(Location loc) {
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

    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if(comprobarUser(event.getPlayer()))
            event.getPlayer().getWorld().spawnParticle(PARTICULA_FUEGO, event.getPlayer().getLocation(), N_PARTICULAS,
                    RADIO_PARTICULAS, RADIO_PARTICULAS, RADIO_PARTICULAS);
    }

    public void onEntityPickupItem(EntityPickupItemEvent event) {
        Item objeto = event.getItem();

        if(event.getEntity() instanceof Player)
            if(comprobarUser((Player) event.getEntity()))
                CookFood(objeto.getItemStack());

    }

    public void FirePool(Player player) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i >= FIRE_POOL_DURATION) cancelTask();

                player.getWorld().playSound(player, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 100, 10);

                createAbilitie1Effect(player.getLocation());

                i++;
            }
            public void cancelTask() { Bukkit.getScheduler().cancelTask(this.getTaskId()); }
        }.runTaskTimer(plugin, 0, 1);

        Vector dir = player.getEyeLocation().getDirection();


        new BukkitRunnable() {
            ArrayList<Entity> fireballs = generateFirePoolEntities(player.getWorld(), player.getLocation(), dir);
            int k = 0;
            @Override
            public void run() {

                for(int i = 0; i<fireballs.size(); i++){
                    Entity entity = fireballs.get(i);
                    if(entity.isDead()) {
                        fireballs.remove(i);
                        entity.getWorld().createExplosion(entity.getLocation(), 5, true, false, player);
                    }
                }

                k++;
                if(fireballs.isEmpty() || k >= 100)
                    this.cancel();
            }
        }.runTaskTimer(plugin,0,1);
    }

    public ArrayList<Entity> generateFirePoolEntities(World mundo, Location loc, Vector direccion) {
        ArrayList<Entity> fireballs = new ArrayList<>(); 
        Entity fireball1 = mundo.spawnEntity(loc.clone().add(1.5, 5, -1.5), EntityType.FIREBALL);
        fireball1.setCustomName("fireball1");
        ((Fireball)fireball1).setDirection(direccion);
        fireballs.add(fireball1);

        Entity fireball2 = mundo.spawnEntity(loc.clone().add(-1.5, 5, 1.5), EntityType.FIREBALL);
        ((Fireball)fireball2).setDirection(direccion);
        fireball2.setCustomName("fireball2");
        fireballs.add(fireball2);

        Entity fireball3 = mundo.spawnEntity(loc.clone().add(-1.5, 5, -1.5), EntityType.FIREBALL);
        ((Fireball)fireball3).setDirection(direccion);
        fireball3.setCustomName("fireball3");
        fireballs.add(fireball3);

        Entity fireball4 = mundo.spawnEntity(loc.clone().add(1.5, 5, 1.5), EntityType.FIREBALL);
        ((Fireball)fireball4).setDirection(direccion);
        fireball4.setCustomName("fireball4");
        fireballs.add(fireball4);

        return fireballs;
    }

    public void FireballStorm(Player player) {
        World mundo = player.getWorld();

        mundo.playSound(player, Sound.AMBIENT_NETHER_WASTES_LOOP, 100, 10);
        spawnBolas(mundo, player.getLocation());
    }

    public void spawnBolas(World mundo, Location loc) {
        final int[] i = {0};

        mundo.getNearbyEntities(loc, 10, 6, 10).forEach(entity -> {
            if (entity instanceof LivingEntity && !entity.getName().equals(user.getPlayerName())) {
                Vector dir = new Vector(entity.getLocation().getX() - loc.getX(), 
                                        entity.getLocation().getY() - loc.getY(),
                                        entity.getLocation().getZ() - loc.getZ()).normalize();

                Vector distance = dir.clone().normalize().multiply(3);
                Entity bola1 = mundo.spawnEntity(loc.clone().add(0, 1, 0).add(distance), EntityType.FIREBALL);
                Entity bola2 = mundo.spawnEntity(loc.clone().add(0, 1, 3+ i[0]).add(distance), EntityType.FIREBALL);
                Entity bola3 = mundo.spawnEntity(loc.clone().add(0, 1, -3+ i[0]).add(distance), EntityType.FIREBALL);

                bola1.setVelocity(dir);
                bola2.setVelocity(dir);
                bola3.setVelocity(dir);

                entity.setFireTicks(100);
                i[0]++;

                Particle particula = Particle.LAVA;

                for(double y = -2; y < 2; y+=0.5) {
                    mundo.spawnParticle(particula, entity.getLocation().clone().add(y, y, y), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, entity.getLocation().clone().add(-y, y, y), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, entity.getLocation().clone().add(y, y, -y), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, entity.getLocation().clone().add(-y, y, -y), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, entity.getLocation().clone().add(y, -y, y), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, entity.getLocation().clone().add(-y, -y, y), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, entity.getLocation().clone().add(y, -y, -y), 0, 0, 0, 0);
                    mundo.spawnParticle(particula, entity.getLocation().clone().add(-y, -y, -y), 0, 0, 0, 0);
                }
            }
        });
    }

    public void Faiyabu(Player player) {
        Location loc = player.getEyeLocation().clone();
        Vector direccion = loc.getDirection();
        World mundo = player.getWorld();

        mundo.playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 100, 10);

        loc.add(0, -0.5, 0);

        new BukkitRunnable() {
            int k = 0;
            public void run() {
                if(k > 30)
                    this.cancel();

                loc.add(direccion);

                animacionFaiyabu(mundo, loc.clone());
                efectoFaiyabu(mundo, loc.clone(), player);

                k++;
            }
        }.runTaskTimer(plugin, 0, 0);

        golpeadosFaiyabu.clear();
    }

    public void efectoFaiyabu(World mundo, Location loc, Player player) {
        mundo.getNearbyEntities(loc, 1, 1, 1).forEach(entity -> {
            if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity && !golpeadosFaiyabu.contains((LivingEntity) entity)) {
                ((LivingEntity) entity).damage(10, (Entity) user.getPlayer());
                entity.setFireTicks(100);
                mundo.createExplosion(entity.getLocation().add(1, 1, 1), ExplosionRadius/2);
                mundo.playSound(entity.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 100, 10);
                golpeadosFaiyabu.add((LivingEntity) entity);
            }
        });
    }

    public void animacionFaiyabu(World mundo, Location loc) {
            Location loc_aux = loc.clone(), loc_aux2 = loc_aux.clone(), loc_aux3 = loc_aux2.clone();

            for(double i = -2*PI; i < 2*PI; i+=0.2) {
                double  x = sin(i)/5,
                        y = cos(i)/5,
                        z = 0;

                mundo.spawnParticle(Particle.FLAME, loc_aux.add(z, y, x), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.FLAME, loc.add(x, y, z), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.FLAME, loc_aux2.add(z, -y, -x), 0, 0, 0, 0);
                mundo.spawnParticle(Particle.FLAME, loc_aux3.add(-x, -y, z), 0, 0, 0, 0);
            }
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
                            if(castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user)){
                                caster = player.getInventory().getItemInMainHand();
                                isCaster = true;
                            }
                            else{
                                if(castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user)){
                                    caster = player.getInventory().getItemInOffHand();
                                    isCaster = true;
                                }
                            }
                        }
                        if(isCaster && caster.getItemMeta().getDisplayName().equals(fruit.getCasterName())){
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
                i += 0.5;
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


    public void onEntityDamage(EntityDamageEvent event){
        super.onEntityDamage(event);

        if(active && event.getCause().equals(EntityDamageEvent.DamageCause.FIRE)){
            event.setCancelled(true);
        }
    }
}
