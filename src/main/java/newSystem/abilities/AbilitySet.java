package newSystem.abilities;

import java.util.ArrayList;

public class AbilitySet
{
    public String name;
    public ArrayList<Ability> abilities = new ArrayList<>();

    public AbilitySet(String name)
    {
        this.name = name;
    }

    // Permite añadir condiciones de selección
    public boolean select()
    {
        return true;
    }

    public void addAbility(Ability ability)
    {
        this.abilities.add(ability);
    }

    public void invokeAbility(int abilityID)
    {
        if (abilityID >= abilities.size() || abilityID < 0)
            return;

        abilities.get(abilityID).invoke();
    }

    public ArrayList<Ability> getAbilities()
    {
        return this.abilities;
    }
}
