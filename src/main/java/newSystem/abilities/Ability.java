package newSystem.abilities;

public class Ability implements IAbility
{
    protected String name;
    protected IAbility abilityLogic;

    public Ability(String name, IAbility abilityLogic)
    {
        this.name = name;
        this.abilityLogic = abilityLogic;
    }

    @Override
    public void invoke()
    {
        abilityLogic.invoke();
    }
}
