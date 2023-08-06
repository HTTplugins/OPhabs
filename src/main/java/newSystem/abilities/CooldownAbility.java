package newSystem.abilities;

import htt.ophabs.OPhabs;
import newSystem.abilities.Ability;
import newSystem.abilities.IAbility;

public class CooldownAbility extends Ability
{
    protected int currentCooldown;
    protected final int cooldown;

    public CooldownAbility(String name, int cooldown, IAbility abilityLogic)
    {
        super(name, abilityLogic);
        this.cooldown = cooldown;

        this.currentCooldown = 0;
    }

    @Override
    public void invoke()
    {
        int globalCooldownTimer = OPhabs.cooldownSystem.getGlobalCooldownTimer();

        if (this.currentCooldown > globalCooldownTimer)
            return;

        this.currentCooldown = globalCooldownTimer + this.cooldown;

        abilityLogic.invoke();
    }
}
