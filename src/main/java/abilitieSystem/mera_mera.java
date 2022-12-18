package abilitieSystem;

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

public class mera_mera implements Listener {
    private OPhabs plugin;
    final float ExplosionRadius = 4;
    final int BerserkFireRadius = 5, Hability1Radius = 4;
    final Material NETHERRACK = Material.NETHERRACK, FIRE = Material.FIRE, DIRT = Material.DIRT, GREEN_DIRT = Material.GRASS_BLOCK,
                   OBSIDIANA = Material.OBSIDIAN, BEDROCK = Material.BEDROCK, AIR = Material.AIR;
    final Particle PARTICULA_FUEGO = Particle.FLAME;
    final Sound RESPAWN_SOUND = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
    final int N_PARTICULAS = 20, RADIO_PARTICULAS = 0, RESPAWN_HEALTH = 10, RESPAWN_FOOD = 10, BERSERK_DURATION = 3600,
              BERSERK_AMPLIFIER = 2;
    boolean BERSERK = true;

    private int FIRE_POOL_DURATION = 15, FIREBALL_STORM_COOLDOWN = 20;

    public mera_mera(OPhabs plugin){
        this.plugin = plugin;
    }

    public boolean isDirt(Block bloque) {
        boolean esTierra = false;

        if ((bloque.getType() == DIRT) || (bloque.getType() == GREEN_DIRT))
            esTierra = true;

        return esTierra;
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

    private void BerserkEffects(Player jugador){
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, BERSERK_DURATION, BERSERK_AMPLIFIER));
        jugador.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, BERSERK_DURATION, BERSERK_AMPLIFIER));
    }

    private void createBerserkEffect(Location loc) {
        for(int i = -BerserkFireRadius; i <= BerserkFireRadius; i++)
            for(int j = -BerserkFireRadius; j <= BerserkFireRadius; j++){
                Location loc_aux = loc.clone();
                loc_aux.setX(loc.getBlockX() + i);   loc_aux.setZ(loc.getBlockZ() + j);

                if(!isIndestructible(loc_aux.getBlock()))
                    loc_aux.getBlock().setType(FIRE);
            }
    }

    public void createHability1Effect(Location loc){
        Location loc_aux = loc.clone();

        for(int i = -Hability1Radius; i <= Hability1Radius; i++) {
            loc_aux.setX(loc.getX() + i);
            for(int j = -Hability1Radius; j <= Hability1Radius; j++) {
                loc_aux.setZ(loc.getZ() + j);
                if(loc_aux.getBlock().getType() == AIR)
                    loc_aux.getBlock().setType(FIRE);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity flecha = event.getEntity();
        Location loc_flecha = flecha.getLocation();

        event.getEntity().getWorld().spawnEntity(loc_flecha, EntityType.FIREBALL);
        flecha.remove();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        event.getPlayer().getWorld().spawnParticle(PARTICULA_FUEGO, event.getPlayer().getLocation(), N_PARTICULAS,
                RADIO_PARTICULAS, RADIO_PARTICULAS, RADIO_PARTICULAS);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        String nombre_user = plugin.getConfig().getString("FruitAssociations.mera_mera");
        Player p = Bukkit.getPlayerExact(nombre_user);
        Player jugador = event.getEntity();

        if(p != null && p == jugador) {
            World mundo = jugador.getWorld();
            Location loc = jugador.getLocation();

            if(BERSERK) {
                event.setDeathMessage("\n\n" + "MERA MERA USER: BY THE POWER OF THE UNDERWORLD... DESTROY THEM!! FROM NOW ON, don't u dare to " +
                        " feed yourself so much...!! \n");
                event.setKeepInventory(true);
                event.setKeepLevel(true);

                mundo.createExplosion(loc, ExplosionRadius * ExplosionRadius);
                createBerserkEffect(loc);
                mundo.playSound(jugador, RESPAWN_SOUND, SoundCategory.HOSTILE, ExplosionRadius * ExplosionRadius * ExplosionRadius,
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

    @EventHandler(ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        String nombre_user = plugin.getConfig().getString("FruitAssociations.mera_mera");
        Player p = Bukkit.getPlayerExact(nombre_user);
        Item objeto = event.getItem();

        if(event.getEntity() instanceof Player)
            if(p != null && (Player) event.getEntity() == p)
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

                createHability1Effect(player.getLocation());

                i++;
            }

            public void cancelTask() { Bukkit.getScheduler().cancelTask(this.getTaskId()); }
        }.runTaskTimer(plugin, 0, FIRE_POOL_DURATION);
    }

    public void FireballStorm(Player player) {
        World mundo = player.getWorld();

        mundo.playSound(player, Sound.MUSIC_DRAGON, 100, 10);
        mundo.spawnEntity(player.getLocation().clone().add(0, 5, 0), EntityType.FIREBALL);
        mundo.spawnEntity(player.getLocation().clone().add(0, 5, 3), EntityType.FIREBALL);
        mundo.spawnEntity(player.getLocation().clone().add(0, 5, -3), EntityType.FIREBALL);
        mundo.spawnEntity(player.getLocation().clone().add(3, 5, 0), EntityType.FIREBALL);
        mundo.spawnEntity(player.getLocation().clone().add(-3, 5, 0), EntityType.FIREBALL);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {                   // HABILIDAD 2
        Location loc = event.getEgg().getLocation();
        World mundo = event.getEgg().getWorld();

        mundo.createExplosion(loc, ExplosionRadius);

        if(event.isHatching())
            event.setHatchingType(EntityType.BLAZE);

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));
    }
}