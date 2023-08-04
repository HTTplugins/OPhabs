package newSystem;

import java.util.ArrayList;

public abstract class DevilFruit
{
    protected ArrayList<AbilitySet> abilitySets = new ArrayList<>();
    protected AbilitySet selectedSet = null;
    // Aquí también va referencia al usuario para acceder a los otros componentes

    public DevilFruit(OPUser user)
    {
    }

    public void selectAbilitySet(int setID)
    {
        if (setID >= abilitySets.size() || setID < 0)
        {
            // Mostrar mensaje de error
            return;
        }

        AbilitySet abilitySetRequested = abilitySets.get(setID);

        if (abilitySetRequested.select())
            selectedSet = abilitySetRequested;
    }

    public AbilitySet getSelectedAbilitySet()
    {
        return selectedSet;
    }

    public AbilitySet getAbilitySet(int setID)
    {
        if (setID >= abilitySets.size() || setID < 0)
            return null;

        return this.abilitySets.get(setID);
    }
}
