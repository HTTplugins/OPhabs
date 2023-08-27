package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;
import htt.ophabs.OPhabs;
import libs.*;
import logs.msgSystem;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import runnables.OphAnimTextureRunnable;
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

    private BukkitTask smokeBodyParticlesTask;

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

        //Smoke Body
        basicSet.addAbility(new Ability("Smoke Body", this::smokeBody));

        this.abilitySets.add(basicSet);

    }

    // ----- Logia behavior ----- //

    @Override
    protected void logiaModeON() {
        super.logiaModeON(OPHAnimationLib.swingParticleAroundPlayer(user,element));
    }

    // ----- Abilities ----- //


    public void smokePunch() {
        int damage = 14;
        Player player = this.user.getPlayer();
        new OphAnimTextureRunnable(Material.QUARTZ, 1004, player.getLocation(), player.getLocation().getDirection(),0.5 ){
            @Override
            public void OphRun() {
                this.getArmorStand().getWorld().getNearbyEntities(this.getArmorStand().getLocation(), 2,2,2).forEach(ent -> {
                    if(ent instanceof LivingEntity && ent != player && this.getArmorStand() != ent && !(ent instanceof ArmorStand) && !(ent instanceof Vex)) {
                        ((LivingEntity) ent).damage(damage,player);
                    }
                });
            }

            @Override
            public void particleAnimation() {
                for(double i = -2; i<0; i+=0.2)
                    OPHLib.circleEyeVector
                            (0.4,1,i,null,Particle.CLOUD,player, this.getArmorStand().getLocation().add(0,1,0));

            }
        }.ophRunTaskTimer(0,1);

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

        OPHLib.affectAreaLivingEntitiesRunnable(user.getPlayer(),80,5, smokeWorldAmplitude, smokeWorldAmplitude/2, smokeWorldAmplitude,
            (LivingEntity livEnt) -> {
                smokeWorldEntities.add(livEnt);
                livEnt.setRemainingAir(livEnt.getRemainingAir() - 10);
            });
    }

    public void smokeBody() {
        Player player = user.getPlayer();

        player.getWorld().playSound(player.getLocation(), "vanish", 1, 1);

        if (!logiaBodyON) {
            logiaBodyON = true;
            ignoreLogiaOff = true;
            user.getPlayer().setAllowFlight(true);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 4, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9999999, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 3, false, false));

            smokeBodyParticlesTask = smokeBodyParticles();
        } else {
            ignoreLogiaOff = false;
            logiaBodyON = false;

            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

            smokeBodyParticlesTask.cancel();
        }
    }

    public BukkitTask smokeBodyParticles() {
        Player player = user.getPlayer();
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!(logiaBodyON || user.getPlayer().isDead()))
                    cancelTask();
                player.getWorld().spawnParticle(element, player.getLocation(), 10, 0.5, 0.5, 0.5, 0);
            }

            public void cancelTask() {
                Bukkit.getScheduler().cancelTask(this.getTaskId());
            }
        }.runTaskTimer(OPhabs.getInstance(), 5, 3);
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
