package abilitieSystem;

import htt.ophabs.OPhabs;
import org.bukkit.event.Listener;
import org.bukkit.Particle;

public class magu_magu extends logia {

    public magu_magu(OPhabs plugin){
        super(plugin, Particle.FLAME);
        abilitiesNames.add("AB1");
        abilitiesCD.add(0);
        abilitiesNames.add("AB2");
        abilitiesCD.add(0);
    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            System.out.println("AB1");
            abilitiesCD.set(0, 20); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            System.out.println("AB2");;
            abilitiesCD.set(1, 20); // Pon el cooldown en segundos
        }
    }

    @Override
    public void runParticles() {

    }
}
