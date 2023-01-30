package abilitieSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import fruitSystem.fruitIdentification;

public class zushi_zushi extends paramecia{

    public zushi_zushi(OPhabs plugin){
        super(plugin, castIdentification.castMaterialZushi,castIdentification.castItemNameZushi,fruitIdentification.fruitCommandNameZushi);
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

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {

            abilitiesCD.set(3, 0); // Pon el cooldown en segundos
        }
    }
}
