package abilitieSystem;

import castSystem.castIdentification;
import fruitSystem.fruitIdentification;
import htt.ophabs.OPhabs;


public class suke_suke extends paramecia {

    public suke_suke(OPhabs plugin){
        super(plugin, castIdentification.castMaterialSuke,castIdentification.castItemNameSuke, fruitIdentification.fruitCommandNameSuke);
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
            System.out.println("AAA");
            abilitiesCD.set(0, 0); // Pon el cooldown en segundos
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){

            abilitiesCD.set(1, 0); // Pon el cooldown en segundos
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){

            abilitiesCD.set(2, 0); // Pon el cooldown en segundos
        }
    }

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }
}
