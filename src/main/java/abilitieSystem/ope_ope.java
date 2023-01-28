package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class ope_ope extends paramecia {

    public ope_ope(OPhabs plugin){
        super(plugin,castIdentification.castMaterialOpe, castIdentification.castItemNameOpe, fruitIdentification.fruitCommandNameOpe);

        abilitiesNames.add("ab1");
        abilitiesCD.add(0);
        abilitiesNames.add("ab2");
        abilitiesCD.add(0);
        abilitiesNames.add("ab3");
        abilitiesCD.add(0);
        abilitiesNames.add("ab4");
        abilitiesCD.add(0);

    }

    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            System.out.println("AAAAA");
            abilitiesCD.set(0, 0); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){

            abilitiesCD.set(1, 20); // Pon el cooldown en segundos
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){

            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    public void ability4(){
        if(abilitiesCD.get(3) == 0){

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }





}
