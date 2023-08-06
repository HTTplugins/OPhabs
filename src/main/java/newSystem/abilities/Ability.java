package newSystem.abilities;

public class Ability implements IAbility, IDisplayableAbility
{
    protected String name;
    protected IAbility abilityLogic;

    public Ability(String name, IAbility abilityLogic)
    {
        this.name = name;
        this.abilityLogic = abilityLogic;
    }

    @Override
    public String getDisplayableString()
    {
        return name;
    }

    @Override
    public void invoke()
    {
        abilityLogic.invoke();
    }
}
