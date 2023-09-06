package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;

import org.bukkit.Particle;

public class Goro_Goro extends Logia{

    public static int getFruitID(){
        return 1005;
    }

    public Goro_Goro(int id){
        super(id, "Goro_Goro", "Goro Goro no Mi", "Goro_Goro", Particle.CLOUD);
        //
        // BasicSet
        //
        AbilitySet basicSet = new AbilitySet("Base Set");
        basicSet.addAbility(new Ability("Goro Goro no Pistol", this::tell));


        //
        // Guardar sets
        //
        this.abilitySets.add(basicSet);
    }

    // ----- Logia behavior ----- //

    // ----- Abilities ----- //

    public void tell(){
        System.out.println("Goro Goro");
    }



    @Override
    protected void onAddFruit() {

    }

    @Override
    protected void onRemoveFruit() {

    }

    @Override
    protected void logiaModeON() {


    }
}
