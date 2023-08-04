package newSystem.fruits;

import newSystem.OPUser;
import newSystem.abilities.Ability;
import newSystem.abilities.AbilitySet;
import newSystem.abilities.CooldownAbility;

public class FrutaX extends DevilFruit
{
    public FrutaX(OPUser user)
    {
        super(user);

        //
        // Crear el set de habilidades 1
        //
        AbilitySet set1 = new AbilitySet("Set de habilidades 1");

        set1.addAbility(new Ability("H1", () -> {
            habilidad1_set1(25);
            System.out.println("También puedes hacer habilidades compuestas fácilmente :)");
            habilidad1_set1(25);
        }));

        set1.addAbility(new Ability("H2", this::habilidad2_set1));

        //
        // Crear el set de habilidades 2
        //
        AbilitySet set2 = new AbilitySet("Set de habilidades 2");

        set2.addAbility(new CooldownAbility("H1", 25, this::habilidad1_set2));
        set2.addAbility(new Ability("H2", () -> habilidad2_set2(25, 50)));
    }

    //

    // Implementación habilidades

    public void habilidad1_set1(int damage)
    {
        System.out.println("Soy Hab1 S1");
    }

    public void habilidad2_set1()
    {
        System.out.println("Soy Hab2 S1");
    }

    public void habilidad1_set2()
    {
        System.out.println(this.abilitySets.size());
        System.out.println("Soy Hab1 S2");
    }

    public void habilidad2_set2(int damage, int user)
    {
        System.out.println("Marica");
    }
}
