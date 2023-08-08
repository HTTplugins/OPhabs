package newSystem.fruits;

import newSystem.OPUser;
import newSystem.abilities.AbilitySet;
import newSystem.events.IEventProcessor;

import java.util.ArrayList;

public abstract class DevilFruit implements IEventProcessor
{
    private final int id;
    protected String name;
    protected String displayName;
    protected String commandName;
    protected ArrayList<AbilitySet> abilitySets = new ArrayList<>();
    protected OPUser user;
    protected boolean isFruitActive = true;

    public DevilFruit(int id, String name, String displayName, String commandName)
    {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.commandName = commandName;
    }

    // Permite robar frutas temporalmente
    public void setUser(OPUser user)
    {
        this.user = user;
    }

    public OPUser getUser()
    {
        return this.user;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public String getCommandName()
    {
        return this.commandName;
    }

    public int getID()
    {
        return this.id;
    }

    public void invokeAbility(int abilitySetID, int abilityID)
    {
        if (abilitySetID < 0 || abilitySetID >= this.abilitySets.size())
            return;

        this.abilitySets.get(abilitySetID).invokeAbility(abilityID);
    }

    public ArrayList<AbilitySet> getAbilitySets()
    {
        return this.abilitySets;
    }
}
