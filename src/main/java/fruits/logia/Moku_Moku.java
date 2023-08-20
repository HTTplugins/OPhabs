package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;
import libs.OPHAnimationLib;
import libs.OPHLib;
import libs.OPHSoundLib;
import libs.OPHSounds;
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
import runnables.OphRunnable;

import java.util.HashSet;
import java.util.Set;

public class Moku_Moku extends Logia{
    private ItemStack fist;

    private boolean smokeWorldON = false;
    private final Set<LivingEntity> smokeWorldEntities = new HashSet<>();
    final double smokeWorldDamage = 1.0;


    public static int getFruitID()
    {
        return 1004;
    }

    public Moku_Moku(int id) {
        super(id, "Moku_Moku", "Moku Moku no Mi", "Moku_Moku", Particle.CLOUD);
        fist = new ItemStack(Material.QUARTZ);
        ItemMeta itemMeta = fist.getItemMeta();
        itemMeta.setCustomModelData(1004); //
        fist.setItemMeta(itemMeta);

        //
        // BasicSet
        //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Smoke Punch
        basicSet.addAbility(new Ability("Smoke Punch", () -> {
            Location loc = user.getPlayer().getLocation();
            this.smokePunch(loc);
        }));


        //Smoke World
        basicSet.addAbility(new Ability("Smoke World", this::smokeWorld));

        //
        // Guardar sets
        //
        this.abilitySets.add(basicSet);

    }

    public void smokePunch(Location loc) {
        user.getPlayer().getWorld().playSound(loc, "cloudpunch", 1, 1);
        ArmorStand armorStand = OPHLib.generateCustomFloatingItem(user.getPlayer().getLocation().add(0,-0.5,0), fist, false);


        new OphRunnable(){

            // Configurar las propiedades del armor stand

            Entity entity = armorStand;
            Location origin=entity.getLocation();


            int ticks = 0;
            boolean first = true;
            Vector dir = user.getPlayer().getEyeLocation().getDirection(), aux;
            double distancePerTick = 1.0 / 2.0, distanceFromPlayer = 0;

            // Calcula la distancia total que el ArmorStand debe recorrer
            @Override
            public void OphRun() {
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
        }.ophRunTaskTimer(2, 1);
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


    @Override
    public void onEntityAirChange(EntityAirChangeEvent event){
        if(smokeWorldON) {
            if (smokeWorldEntities.contains(event.getEntity())) {
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
