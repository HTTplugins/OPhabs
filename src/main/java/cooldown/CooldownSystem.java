package cooldown;

import htt.ophabs.OPhabs;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CooldownSystem
{
    private int globalCooldownTimer = 0;
    private BukkitTask cooldownTimerTask;

    public CooldownSystem()
    {
        this.cooldownTimerTask = this.runCooldownTimer();
    }

    public int getGlobalCooldownTimer()
    {
        return this.globalCooldownTimer;
    }

    private BukkitTask runCooldownTimer()
    {
        return new BukkitRunnable()
        {
            @Override
            public void run()
            {
                globalCooldownTimer++;
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 20);
    }
}
