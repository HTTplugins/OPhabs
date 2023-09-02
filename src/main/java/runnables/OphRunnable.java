package runnables;

import htt.ophabs.OPhabs;
import logs.msgSystem;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public abstract class OphRunnable extends BukkitRunnable {

    private final UUID ownerUUID;
    private final int maxRunIterations;
    private int currentRunIteration = 0;
    private static final int DEFAULT_MAX_RUN_ITERATIONS = 300;

    // Constructor por defecto
    public OphRunnable() {
        this(null, DEFAULT_MAX_RUN_ITERATIONS);
    }

    // Constructor con límite de iteraciones personalizado
    public OphRunnable(int maxRunIterations) {
        this(null, maxRunIterations);
    }

    // Constructor con propietario y límite de iteraciones por defecto
    public OphRunnable(UUID ownerUUID) {
        this(ownerUUID, DEFAULT_MAX_RUN_ITERATIONS);
    }

    // Constructor completo
    public OphRunnable(UUID ownerUUID, int maxRunIterations) {
        this.ownerUUID = ownerUUID;
        this.maxRunIterations = maxRunIterations;
    }

    @Override
    public void run() {
        if (ownerUUID != null && Bukkit.getPlayer(ownerUUID) == null) {
            cancelAndLog("OphRunnable cancelled due to runnable owner being offline");
            return;
        }

        if (currentRunIteration > maxRunIterations) {
            cancelAndLog("OphRunnable cancelled due to maxRunIterations");
            return;
        }

        try {
            OphRun();
        } catch (Exception e) {
            msgSystem.OphConsoleError("OphRunnable cancelled due to Exception. Here is the stacktrace:");
            e.printStackTrace();
            this.cancel();
            return;
        }

        currentRunIteration++;
    }

    private void cancelAndLog(String message) {
        this.cancel();
        msgSystem.OphConsoleError(message);
    }

    // Resto del código sin cambios

    public abstract void OphRun();

    public void ophRunTaskTimer(long delay, long period) {
        this.runTaskTimer(OPhabs.getInstance(), delay, period);
    }

    public void ophRunTaskLater(int delay) {
        this.runTaskLater(OPhabs.getInstance(), delay);
    }

    public void ophCancel() {
        this.cancel();
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public int getMaxRunIterations() {
        return maxRunIterations;
    }

    public int getCurrentRunIteration() {
        return currentRunIteration;
    }
}
