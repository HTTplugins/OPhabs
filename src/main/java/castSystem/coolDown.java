package castSystem;

import htt.ophabs.OPhabs;
import org.bukkit.scheduler.BukkitRunnable;

public class coolDown {

    OPhabs plugin;

    private int Abilitie1, Abilitie2, Abilitie3, Abilitie4, Abilitie5, Abilitie6;

    public coolDown(OPhabs plugin){
        this.plugin = plugin;
        Abilitie1 = 0;
        Abilitie2 = 0;
        Abilitie3 = 0;
        Abilitie4 = 0;
        Abilitie5 = 0;
        Abilitie6 = 0;
    }

    public void runCoolDownSystem(){

        new BukkitRunnable(){

            @Override
            public void run() {

                if(Abilitie1 > 0)
                    Abilitie1--;
                if(Abilitie2 > 0)
                    Abilitie2--;
                if(Abilitie3 > 0)
                    Abilitie3--;
                if(Abilitie4 > 0)
                    Abilitie4--;
                if(Abilitie5 > 0)
                    Abilitie5--;
                if(Abilitie6 > 0)
                    Abilitie6--;
            }


        }.runTaskTimer(plugin,0,20);

    }

    public void setAbilitie1CD(int CD) {
        Abilitie1 = CD;
    }

    public void setAbilitie2CD(int CD) {
        Abilitie2 = CD;
    }

    public void setAbilitie3CD(int CD) {
        Abilitie3 = CD;
    }

    public void setAbilitie4CD(int CD) {
        Abilitie4 = CD;
    }

    public void setAbilitie5CD(int CD) {
        Abilitie5 = CD;
    }

    public void setAbilitie6CD(int CD) {
        Abilitie6 = CD;
    }


    public int getAbilitie1CD() {
        return Abilitie1;
    }

    public int getAbilitie2CD() {
        return Abilitie2;
    }

    public int getAbilitie3CD() {
        return Abilitie3;
    }

    public int getAbilitie4CD() {
        return Abilitie4;
    }

    public int getAbilitie5CD() {
        return Abilitie5;
    }

    public int getAbilitie6CD() {
        return Abilitie6;
    }
}
