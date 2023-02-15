package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
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
    private final int FIRE_POOL_DURATION = 3, FIREBALL_STORM_COOLDOWN = 5, FIRE_POOL_COOLDOWN = 15, FAIYABU_COOLDOWN = 5,
                      BOMUSHOTTO_COOLDOWN = 10;

    private ArrayList<LivingEntity> golpeadosFaiyabu = new ArrayList<>();

    public mera_mera(OPhabs plugin) {
        super(plugin, Particle.FLAME, castIdentification.castMaterialMera, castIdentification.castItemNameMera, fruitIdentification.fruitCommandNameMera);
        //
        //Nombres de las habilidades:
        abilitiesNames.add("Fire Pool");
        abilitiesCD.add(0);
        abilitiesNames.add("Fireball Storm");
        abilitiesCD.add(0);
        abilitiesNames.add("Faiyābū");
        abilitiesCD.add(0);
        abilitiesNames.add("Bomushotto");
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
            Bomushotto(user.getPlayer());
            abilitiesCD.set(3, BOMUSHOTTO_COOLDOWN);
        }
    }

    public boolean comprobarUser(Player jugador) {
        String nombre_user = plugin.getConfig().getString("FruitAssociations.mera_mera");
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

    private void BerserkEffects(Player jugador) {
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, BERSERK_DURATION, BERSERK_AMPLIFIER));
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

    public void FirePool(Player player) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i >= FIRE_POOL_DURATION) cancelTask();

                player.getWorld().playSound(player, Sound.ENTITY_ENDER_DRAGON_AMBIENT, 100, 10);

                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));

                createAbilitie1Effect(player.getLocation());

                i++;
            }
            public void cancelTask() { Bukkit.getScheduler().cancelTask(this.getTaskId()); }
        }.runTaskTimer(plugin, 0, 1);

        Vector jugador = new Vector(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

        generateFirePoolEntities(player.getWorld(), player.getLocation(), jugador);

        new BukkitRunnable() {
            int k = 0, fireballs = 0;
            @Override
            public void run() {
                player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10).forEach(entity -> {
                    if (entity instanceof Fireball) {
                        fireballs++;
                        ((Fireball) entity).setDirection(new Vector(player.getLocation().getX(), entity.getLocation().getY() - 10, player.getLocation().getZ()));
                        entity.setVelocity(entity.getLocation().getDirection().multiply(2));
                        ((Fireball) entity).setDirection(((Fireball) entity).getDirection().multiply(-1));
                        entity.getWorld().createExplosion(entity.getLocation(), ExplosionRadius/2);
                    }
                });

                k += 10;

                if(fireballs >= 4 || k >= 100)
                    this.cancel();
            }
        }.runTaskTimer(plugin,0,0);
    }

    public void generateFirePoolEntities(World mundo, Location loc, Vector direccion) {
        ((Fireball)mundo.spawnEntity(loc.clone().add(3, 2, 0), EntityType.FIREBALL)).setDirection(direccion);
        ((Fireball)mundo.spawnEntity(loc.clone().add(0, 2, 3), EntityType.FIREBALL)).setDirection(direccion);
        ((Fireball)mundo.spawnEntity(loc.clone().add(0, 2, 0), EntityType.FIREBALL)).setDirection(direccion);
        ((Fireball)mundo.spawnEntity(loc.clone().add(3, 2, 3), EntityType.FIREBALL)).setDirection(direccion);
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
                Vector dir = new Vector(entity.getLocation().getX() - loc.getX(), entity.getLocation().getY() - loc.getY(),
                        entity.getLocation().getZ() - loc.getZ()).normalize();

                Entity bola1 = mundo.spawnEntity(loc.clone().add(0, 1, 0), EntityType.FIREBALL);
                Entity bola2 = mundo.spawnEntity(loc.clone().add(0, 1, 3+ i[0]), EntityType.FIREBALL);
                Entity bola3 = mundo.spawnEntity(loc.clone().add(0, 1, -3+ i[0]), EntityType.FIREBALL);

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
                ((LivingEntity) entity).damage(10);
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

    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        Location loc = event.getEgg().getLocation();
        World mundo = event.getEgg().getWorld();

        mundo.createExplosion(loc, ExplosionRadius);

        if(event.isHatching())
            event.setHatchingType(EntityType.BLAZE);

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
    }

    public void Bomushotto(Player player) {
        Location loc = player.getEyeLocation().clone();
        Vector direccion = loc.getDirection();
        World mundo = player.getWorld();

        new BukkitRunnable() {
            int k = 0;
            boolean boom = false;
            @Override
            public void run() {
                loc.add(direccion.clone().multiply(4));

                animacionBomushotto(mundo, loc.clone());
                efectoBomushotto(mundo, loc.clone(), player, boom);

                if(k > 5 || loc.getBlock().getType() != Material.AIR) {
                    boom = true;
                    efectoBomushotto(mundo, loc, player, boom);
                    this.cancel();
                }

                k++;
            }
        }.runTaskTimer(plugin, 0, 20);

        animacionBomushotto(mundo, loc);
    }

    public void efectoBomushotto(World mundo, Location loc, Player player, Boolean boom) {
        if(boom)
            mundo.createExplosion(loc, ExplosionRadius*ExplosionRadius);

        mundo.getNearbyEntities(loc, 4, 4, 4).forEach(entity -> {
            if(!entity.getName().equals(player.getName()) && entity instanceof LivingEntity) {
                ((LivingEntity) entity).damage(5);
                entity.setFireTicks(50);
            }
        });
    }

    public void animacionBomushotto(World mundo, Location loc) {
        Location loc_aux = loc.clone(), loc_aux2 = loc_aux.clone(), loc_aux3 = loc_aux2.clone(),
                 loc_aux4 = loc_aux3.clone(), loc_aux5 = loc_aux4.clone(), loc_aux6 = loc_aux5.clone(),
                 loc_aux7 = loc_aux6.clone(), loc_aux8 = loc_aux7.clone(), loc_aux9 = loc_aux8.clone(),
                 loc_aux10 = loc_aux9.clone(), loc_aux11 = loc_aux10.clone();

        for(double i = -2*PI; i < 2*PI; i+=0.2) {
            double  x = sin(i)/5,
                    y = cos(i)/5,
                    z = 0;

            mundo.spawnParticle(Particle.FLAME, loc.clone().add(x, y, z), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux.add(z, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux2.add(z, -y, -x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux3.add(-x, -y, z), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux4.add(x, y, x/2), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux5.add(x/2, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux6.add(-x, -y, -x/2), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux7.add(-x/2, -y, -x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux8.add(x, y, -x/2), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux9.add(-x/2, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux10.add(-x, -y, x/2), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc_aux11.add(x/2, -y, -x), 0, 0, 0, 0);

            /*mundo.spawnParticle(Particle.FLAME, loc.clone().add(x, y, z), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(z, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(z, -y, -x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(-x, -y, z), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(x, y, x/2), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(x/2, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(-x, -y, -x/2), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(-x/2, -y, -x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(x, y, -x/2), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(-x/2, y, x), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(-x, -y, x/2), 0, 0, 0, 0);
            mundo.spawnParticle(Particle.FLAME, loc.clone().add(x/2, -y, -x), 0, 0, 0, 0);*/
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
}
