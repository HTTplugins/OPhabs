package fruits.logia;

import cast.FruitCaster;
import fruits.DevilFruit;
import htt.ophabs.OPhabs;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import users.OPUser;

import java.util.ArrayList;

public abstract class Logia extends DevilFruit {
    protected Particle element;
    private static final ArrayList<Logia> LOGIAFRUITS = new ArrayList<>();
    private static BukkitTask logiaBodyTask = null;

    private BukkitTask logiaModeTask = null;

    public Logia(int id, String name, String displayName, String commandName, Particle element) {
        super(id, name, displayName, commandName);
        this.element = element;

        if(logiaBodyTask == null) {
            logiaBodyTask = new BukkitRunnable(){
                @Override
                public void run() {
                    for(Logia fruit : LOGIAFRUITS){
                        OPUser user = fruit.getUser();

                        if(user != null && user.getPlayer() != null){
                            FruitCaster caster = (FruitCaster) user.getActiveCasters().get(fruit.getID());
                            Player player = user.getPlayer();

                            if(caster != null && caster.isOwnedBy(user) && (caster.isThisItem(player.getInventory().getItemInMainHand())
                                    || caster.isThisItem(player.getInventory().getItemInOffHand()))) {
                                if(fruit.logiaModeTask == null)
                                    fruit.logiaModeON();

                            } else if(fruit.logiaModeTask != null)
                                fruit.logiaModeOFF();

                        }
                    }
                }
            }.runTaskTimer(OPhabs.getInstance(),20,3);
        }

        LOGIAFRUITS.add(this);
    }

    protected abstract void logiaModeON();
    protected void logiaModeOFF(){
        user.getPlayer().setAllowFlight(false);
        logiaModeTask.cancel();
        logiaModeTask = null;
    }

    protected void logiaModeON(BukkitTask animation) {
            user.getPlayer().setAllowFlight(true);
            logiaModeTask = animation;
    }


}
