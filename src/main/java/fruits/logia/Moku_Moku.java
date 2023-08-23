package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;
import libs.*;
import logs.msgSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import runnables.OphControlledEntRunnable;
import runnables.OphRunnable;

import java.util.HashSet;
import java.util.Set;


public class Moku_Moku extends Logia{
    private boolean smokeWorldON = false;
    final double smokeWorldDamage = 1.0;
    private final Set<LivingEntity> smokeWorldEntities = new HashSet<>();

    private boolean logiaBodyON = false;

    private final Set<LivingEntity> smokers = new HashSet<>();

    public static int getFruitID()
    {
        return 1004;
    }

    public Moku_Moku(int id) {
        super(id, "Moku_Moku", "Moku Moku no Mi", "Moku_Moku", Particle.CLOUD);

        // ----- BasicSet ----- //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Smoke Punch
        basicSet.addAbility(new Ability("Smoke Punch", this::smokePunch));

        // Summon Smokers
        basicSet.addAbility(new Ability("Summon smokers", this::summonSmoker));


        //Smoke World
        basicSet.addAbility(new Ability("Smoke World", this::smokeWorld));

        this.abilitySets.add(basicSet);

    }

    // ----- Logia behavior ----- //

    @Override
    protected void logiaModeON() {
        super.logiaModeON(OPHAnimationLib.swingParticleAroundPlayer(user,element));
    }

    // ----- Abilities ----- //

    public void smokePunch() {
        Location loc = user.getPlayer().getLocation();
        ItemStack fist = new ItemStack(Material.QUARTZ);
        ItemMeta itemMeta = fist.getItemMeta();

        itemMeta.setCustomModelData(1004);
        fist.setItemMeta(itemMeta);

        user.getPlayer().getWorld().playSound(loc, "cloudpunch", 1, 1);
        ArmorStand armorStand = OPHLib.generateCustomFloatingItem(user.getPlayer().getLocation().add(0,-0.5,0), fist, false);

        new OphRunnable(){

            // Configurar las propiedades del armor stand

            final Entity entity = armorStand;
            final Location origin=entity.getLocation();

            final Vector dir = user.getPlayer().getEyeLocation().getDirection();
            Vector aux;
            final double distancePerTick = 1.0 / 2.0;
            double distanceFromPlayer = 0;

            // Calcula la distancia total que el ArmorStand debe recorrer
            @Override
            public void OphRun() {
                distanceFromPlayer+=distancePerTick;
                Vector movement = dir.clone().multiply(distanceFromPlayer);
                entity.teleport(origin.clone().add(movement));

                entity.getWorld().getNearbyEntities(entity.getLocation(), 2,2,2).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != user.getPlayer() && entity != ent && !(ent instanceof ArmorStand) && !(ent instanceof Vex && !smokers.contains(ent))) {
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

                if (aux.length() < 0.5 && getCurrentRunIteration()>10 || getCurrentRunIteration() > 20) {
                    entity.remove();
                    cancelTask();
                }
            }
            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.ophRunTaskTimer(2, 1);
    }

    public void summonSmoker(){
        Player player = this.user.getPlayer();
        final int numMaxSmokers = 4;

        if(smokers.size() < numMaxSmokers){
            Monster smoker = OPHLib.createMonster( EntityType.VEX, player.getLocation(), 0.5,  true, null);
            OPHLib.setMonsterTarget(smoker, player, 10, smokers);
            smokers.add(smoker);

            new OphControlledEntRunnable(smoker,400,5){
                @Override
                public void OphRun() {
                    if(smoker.getTarget() == null || smoker.getTarget().isDead() || smoker.getTarget() == player)
                        OPHLib.setMonsterTarget(smoker, player, 10, smokers);
                }

                @Override
                public void particleAnimation() {
                    smoker.getWorld().spawnParticle(element, smoker.getLocation(), 10, 0.5, 0.5, 0.5, 0);
                }

                @Override
                public void onDeath() {
                    smokers.remove(smoker);
                }
            }.ophRunTaskTimer(0,3);
        } else
            msgSystem.OphPlayerError(player, "You have reached maximum smokers number (4).");

    }

    public void smokeWorld(){
        final double smokeWorldAmplitude=7;

        smokeWorldON = true;
        Player player = user.getPlayer();

        new OphRunnable(){
            @Override
            public void OphRun() {
                smokeWorldON = false;
                smokeWorldEntities.clear();
            }
        }.ophRunTaskLater(400);

        OPHSoundLib.OphPlaySound(OPHSounds.FOG, player.getLocation(), 1, 1);

        OPHAnimationLib.fog(element, player.getLocation(), smokeWorldAmplitude,80);

        OPHLib.affectAreaLivingEntitiesRunnable(user.getPlayer(),80,5, smokeWorldAmplitude, smokeWorldAmplitude/2, smokeWorldAmplitude,(LivingEntity livEnt) -> {
            smokeWorldEntities.add(livEnt);
            livEnt.setRemainingAir(livEnt.getRemainingAir() - 10);
        });
    }

    public boolean smokeBody(Player player) {
        player.getWorld().playSound(player.getLocation(), "vanish", 1, 1);
        if (!logiaBodyON) {
            logiaBodyON = true;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));
            //runParticles(player);
        } else {
            logiaBodyON = false;
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
        return logiaBodyON;
    }

    // ----- Events ----- //
    @Override
    public void onEntityAirChange(EntityAirChangeEvent event){
        if(smokeWorldON) {
            if (smokeWorldEntities.contains((LivingEntity) event.getEntity())) {
                if (event.getEntity() instanceof LivingEntity) {
                    LivingEntity livEnt = (LivingEntity) event.getEntity();
                    int newAir = event.getAmount();
                    int currentAir = livEnt.getRemainingAir();

                    if (newAir > currentAir)
                        event.setCancelled(true);


                    if (currentAir > newAir && newAir <= 10) {
                        event.setAmount(10);
                        livEnt.damage(smokeWorldDamage);
                    }
                }

            }
        }
    }

    @Override
    protected void onAddFruit() {

    }

    @Override
    protected void onRemoveFruit() {

    }


}
