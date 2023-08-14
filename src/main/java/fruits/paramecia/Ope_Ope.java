package fruits.paramecia;

import oldSystem.abilitieSystem.OPHLib;
import htt.ophabs.OPhabs;
import abilities.Ability;
import abilities.AbilitySet;
import abilities.CooldownAbility;
import oldSystem.abilitieSystem.zushi_zushi;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import runnables.OphRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class Ope_Ope extends Paramecia
{

    private boolean exploded = false;   //To control meteor explosion.
    private static List<Entity> heavyEntity = null;
    private static List<Player> togglePlayer = new ArrayList<>();
    private final Random random = new Random();

    private boolean resort;
    private ItemStack glove;

    public Ope_Ope(int id)
    {
        super(id, "Ope_Ope", "Ope Ope no Mi", "Ope_Ope");

        //
        // BasicSet
        //
        AbilitySet basicSet = new AbilitySet("Base Set");

        // Heavy Field
        //basicSet.addAbility(new Ability("Heavy Field", this::resort_ability));


        basicSet.addAbility(new CooldownAbility("test", 5, () -> {
            Player player = user.getPlayer();
            this.test(player);
        }));

        basicSet.addAbility(new CooldownAbility("test 2", 5, () -> {
            Player player = user.getPlayer();
            this.test2(player);
        }));

        //
        // Guardar sets
        //
        this.abilitySets.add(basicSet);
    }


    public void test(Player player){

        new OphRunnable(60){

            @Override
            public void OphRun() {
                player.sendMessage(Integer.toString(this.getCurrentRunIteration()));
            }
        }.ophRunTaskTimer( 0, 1);

    }

    public void test2(Player player){

        new OphRunnable(){

            @Override
            public void OphRun() {
                player.sendMessage("delayed");
            }
        }.ophRunTaskLater(60);

    }





    @Override
    protected void onAddFruit()
    {

    }

    @Override
    protected void onRemoveFruit()
    {
        this.resort = false;
    }



    @Override
    public void onFall(EntityDamageEvent event)
    {
        if (this.isFruitActive)
        {
            if (this.resort) {
                event.setCancelled(true);

                Player player = user.getPlayer();

                if (!player.isSneaking()) {
                    player.playSound(player.getLocation(), "resort", 1, 1);
                    Vector velocity = player.getVelocity();

                    double v = (-3.7 + sqrt(9 - 8*(-(player.getFallDistance())))) / 4;
                    velocity.setY(v);

                    if (player.isSprinting()) {
                        velocity.setX(player.getEyeLocation().getDirection().getX() * 4);
                        velocity.setZ(player.getEyeLocation().getDirection().getZ() * 4);
                    }

                    player.setVelocity(velocity);
                }
                user.getPlayer().setFallDistance(0);
            }
        }
    }
}
