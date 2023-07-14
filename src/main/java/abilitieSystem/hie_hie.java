package abilitieSystem;
import htt.ophabs.OPhabs;

public class hie_hie extends paramecia {    //fruit_fruit is the fruit whose abilities you are going to program (command name).

    public hie_hie(OPhabs plugin){

        super(plugin, 16, "hie_hie", "Hie Hie no Mi", "Hie Hie caster", 8, 1.7);    //Here you associate with  df Casting material, name and command.

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

            abilitiesCD.set(0, 0); // Second parameter is cooldown in seconds.
        }
    }

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            //Here you call the ability
            abilitiesCD.set(1, 0); // Second parameter is cooldown in seconds.
        }
    }

    public void ability3(){
        if(abilitiesCD.get(2) == 0){
            //Here you call the ability
            abilitiesCD.set(2, 0); // Second parameter is cooldown in seconds.
        }
    }

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {
            //Here you call the ability
            abilitiesCD.set(3, 0); // Second parameter is cooldown in seconds.
        }
    }
}
