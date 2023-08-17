package libs;

import org.bukkit.Location;
import org.bukkit.World;
import runnables.OphRunnable;

public class OPHSoundLib {

    public static void OphPlaySound(OPHSounds sound, Location loc, int volume , int pitch){
        World world = loc.getWorld();
        assert world != null;       //This line is just for development purposes.
        world.playSound(loc, sound.toString().toLowerCase(), volume, pitch);
    }

    public static void OphPlayDelayedSound(OPHSounds sound, Location loc, int volume, int pitch, int delay){
        new OphRunnable() {
            @Override
            public void OphRun() {
                OphPlaySound(sound, loc, volume, pitch);
            }
        }.ophRunTaskLater(delay);
    }


}