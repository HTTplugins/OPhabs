package abilities.conditional;

import abilities.Ability;
import abilities.IAbility;

// Habilidad con condicionales
public class ConditionalAbility extends Ability
{
    private IConditionalCheck conditionalCheck;

    public ConditionalAbility(String name, IAbility abilityLogic, IConditionalCheck conditionalCheck)
    {
        super(name, abilityLogic);
    }

    @Override
    public void invoke()
    {
        if (!this.conditionalCheck.check())
        {
            this.conditionalCheck.onCheckFail();
            return;
        }

        this.conditionalCheck.onCheckSuccess();

        this.abilityLogic.invoke();
    }
}
