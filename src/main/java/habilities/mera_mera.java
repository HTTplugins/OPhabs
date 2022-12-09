package habilities;

import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class mera_mera implements Listener {
    private OPhabs plugin;
    final float ExplosionRadius = 4;
    final int BerserkFireRadius = 5, Hability1Radius = 10;
    final Material NETHERRACK = Material.NETHERRACK;
    final Material FIRE = Material.FIRE;
    final Material DIRT = Material.DIRT;
    final Material GREEN_DIRT = Material.GRASS_BLOCK;
    final Material OBSIDIANA = Material.OBSIDIAN;
    final Material BEDROCK = Material.BEDROCK;
    final Material AIR = Material.AIR;
    final Particle PARTICULA_FUEGO = Particle.FLAME;
    final Sound RESPAWN_SOUND = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
    final int N_PARTICULAS = 20, RADIO_PARTICULAS = 0, RESPAWN_HEALTH = 10, RESPAWN_FOOD = 10, BERSERK_DURATION = 3600,
    BERSERK_AMPLIFIER = 2;
    boolean BERSERK = true;

    private int Hability1ColdDown = 0;

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

    public void createNetherrackEffect(Block bloque) {
        Location delante1 = bloque.getLocation(), delante2 = bloque.getLocation(),
                delante3 = bloque.getLocation(), delante4 = bloque.getLocation(),
                delante5 = bloque.getLocation(), delante6 = bloque.getLocation(),
                delante7 = bloque.getLocation(), delante8 = bloque.getLocation();

        delante1.setX(delante1.getBlockX() + 1);
        delante2.setX(delante2.getBlockX() - 1);
        delante3.setZ(delante3.getBlockZ() + 1);
        delante4.setZ(delante4.getBlockZ() - 1);

        delante5.setX(delante5.getBlockX() + 1);
        delante5.setZ(delante5.getBlockZ() + 1);
        delante6.setX(delante6.getBlockX() + 1);
        delante6.setZ(delante6.getBlockZ() - 1);
        delante7.setX(delante7.getBlockX() - 1);
        delante7.setZ(delante7.getBlockZ() + 1);
        delante8.setX(delante8.getBlockX() - 1);
        delante8.setZ(delante8.getBlockZ() - 1);

        if (isDirt(delante1.getBlock()))
            delante1.getBlock().setType(NETHERRACK);

        if (isDirt(delante2.getBlock()))
            delante2.getBlock().setType(NETHERRACK);

        if (isDirt(delante3.getBlock()))
            delante3.getBlock().setType(NETHERRACK);

        if (isDirt(delante4.getBlock()))
            delante4.getBlock().setType(NETHERRACK);

        if (isDirt(delante5.getBlock()))
            delante5.getBlock().setType(NETHERRACK);

        if (isDirt(delante6.getBlock()))
            delante6.getBlock().setType(NETHERRACK);

        if (isDirt(delante7.getBlock()))
            delante7.getBlock().setType(NETHERRACK);

        if (isDirt(delante8.getBlock()))
            delante8.getBlock().setType(NETHERRACK);
    }

    public void createHability1Effect(Location loc, Action accion, Material arma){
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
        Entity entidad;
        Block bloque;
        Location loc;

        if ((entidad = event.getHitEntity()) != null) {
            loc = entidad.getLocation();
            Location delante = loc;

            delante.setX(delante.getBlockX() + 1);

            if (entidad.getWorld().isClearWeather() && !isIndestructible(delante.getBlock()))
                delante.getBlock().setType(FIRE);
        } else if ((bloque = event.getHitBlock()) != null) {
            if (bloque.getType() != NETHERRACK) {
                createNetherrackEffect(bloque);
                if (bloque.getWorld().isClearWeather() && !isIndestructible(bloque))
                    bloque.setType(FIRE);
            } else
                bloque.getWorld().createExplosion(bloque.getLocation(), ExplosionRadius);
        }

        event.getEntity().remove();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        event.getPlayer().getWorld().spawnParticle(PARTICULA_FUEGO, event.getPlayer().getLocation(), N_PARTICULAS,
                RADIO_PARTICULAS, RADIO_PARTICULAS, RADIO_PARTICULAS);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player jugador = event.getEntity();
        World mundo = jugador.getWorld();
        Location loc = jugador.getLocation();

        if (BERSERK) {
            event.setDeathMessage("\n\n" + "BY THE POWER OF THE UNDERWORLD... DESTROY THEM!! FROM NOW ON, YOU SHOULDN'T" +
                    " LOVE FOOD SO MUCH... HAHAHA!! \n");
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

    @EventHandler(ignoreCancelled = true)
    public void onEntityShootBow(EntityShootBowEvent event) {
        event.getEntity().getWorld().spawnParticle(PARTICULA_FUEGO, event.getEntity().getLocation(), N_PARTICULAS,
                RADIO_PARTICULAS, RADIO_PARTICULAS, RADIO_PARTICULAS);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {           // Arreglar.
        Item objeto = event.getItem();

        CookFood(objeto.getItemStack());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event){
        Action accion = event.getAction();
        Material arma = event.getMaterial();
        World mundo = event.getPlayer().getWorld();

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, BERSERK_DURATION, BERSERK_AMPLIFIER));

        if(arma == Material.BLAZE_ROD && accion == Action.RIGHT_CLICK_BLOCK && Hability1ColdDown == 0){
            createHability1Effect(event.getPlayer().getLocation(), accion, arma);
            Hability1ColdDown += 10;
        }
        else if(arma == Material.BLAZE_ROD && accion == Action.RIGHT_CLICK_BLOCK)
            Hability1ColdDown--;

        if(arma == Material.BLAZE_ROD && accion == Action.RIGHT_CLICK_AIR){
            mundo.spawnEntity(event.getPlayer().getLocation(), EntityType.FIREBALL);
            System.out.println("Deberia spawnear");
        }
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