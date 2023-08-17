package fruits;

import users.OPUser;
import abilities.AbilitySet;
import events.IEventProcessor;

import java.util.ArrayList;

public abstract class DevilFruit implements IEventProcessor
{
    private final int id;
    protected String name;
    protected String displayName;
    protected String commandName;
    protected ArrayList<AbilitySet> abilitySets = new ArrayList<>();
    protected OPUser user;

    // Determina si los poderes están activos (si está en el agua / es golpeado por k no)
    protected boolean isFruitActive = true;

    public static int getFruitID()
    {
        return 1000;
    }

    public DevilFruit(int id, String name, String displayName, String commandName)
    {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.commandName = commandName;
    }

    // Invocado tras establecer un usuario nuevo
    protected abstract void onAddFruit();

    // Invocado antes de eliminar al usuario actual
    protected abstract void onRemoveFruit();

    // Permite cambiar el usuario de la fruta. Llama automáticamente a los eventos de registro de fruta.
    // Además, se puede utilizar para robar frutas.
    public void setUser(OPUser user)
    {
        if (user == null)
            this.onRemoveFruit();
        else if (this.user != null && !this.user.equals(user))
            this.onRemoveFruit();

        if (user != null)
            this.onAddFruit();

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
