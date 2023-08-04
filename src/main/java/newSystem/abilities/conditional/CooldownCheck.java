package newSystem.abilities.conditional;

import htt.ophabs.OPhabs;

public class CooldownCheck implements IConditionalCheck
{
    protected int currentCooldown;
    protected final int cooldown;

    public CooldownCheck(int cooldown)
    {
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    @Override
    public boolean check()
    {
        return this.currentCooldown <= OPhabs.tmpGlobalCooldown;
    }

    @Override
    public void onCheckSuccess()
    {
        this.currentCooldown = OPhabs.tmpGlobalCooldown + this.cooldown;
    }
}
