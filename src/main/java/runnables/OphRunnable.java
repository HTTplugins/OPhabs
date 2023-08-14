package runnables;

import htt.ophabs.OPhabs;
import logs.msgSystem;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class OphRunnable extends BukkitRunnable {

    private final int maxRunIterations;
    private int currentRunIteration = 0;
    public OphRunnable() {
        this.maxRunIterations = 300;
    }

    public OphRunnable(int maxRunIterations) {
        this.maxRunIterations = maxRunIterations;
    }

    @Override
    public final void run(){

        if(currentRunIteration > maxRunIterations){
            this.cancel();

            msgSystem.OphConsoleError("OphRunnable cancelled due to maxRunIterations");

            return;
        }
        try{
            OphRun();
        } catch (Exception e){
            msgSystem.OphConsoleError("OphRunnable cancelled due to Exception. Here is the stacktrace:");
            e.printStackTrace();
            this.cancel();
            return;
        }

        currentRunIteration++;

    }

    public int getMaxRunIterations() {
        return maxRunIterations;
    }

    public int getCurrentRunIteration() {
        return currentRunIteration;
    }

    public abstract void OphRun();

    public void ophRunTaskTimer(long delay, long period){
        this.runTaskTimer(OPhabs.getInstance(), delay, period);
    }

    public void ophRunTaskLater(int delay){
        this.runTaskLater(OPhabs.getInstance(),delay);
    }

    public void ophCancel(){
        this.cancel();
    }



}

