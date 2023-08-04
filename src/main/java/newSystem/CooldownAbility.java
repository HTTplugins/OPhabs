package newSystem;

import htt.ophabs.OPhabs;

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
        // TODO: Repensar esto
        if (this.currentCooldown > OPhabs.tmpGlobalCooldown)
            return;

        this.currentCooldown = OPhabs.tmpGlobalCooldown + this.cooldown;

        abilityLogic.invoke();
    }
}
