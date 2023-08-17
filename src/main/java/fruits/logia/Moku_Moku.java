package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import libs.OPHLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import runnables.OphRunnable;

public class Moku_Moku extends Logia{
    private ItemStack fist;

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

        // Resort
        basicSet.addAbility(new Ability("Smoke Punch", () -> {
            Location loc = user.getPlayer().getLocation();
            this.smokePunch(loc);
        }));

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

    @Override
    protected void onAddFruit() {

    }

    @Override
    protected void onRemoveFruit() {

    }
}
