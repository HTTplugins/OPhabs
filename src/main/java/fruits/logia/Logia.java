package fruits.logia;

import fruits.DevilFruit;
import org.bukkit.Particle;

public abstract class Logia extends DevilFruit {
    protected Particle element;
    public Logia(int id, String name, String displayName, String commandName, Particle element) {
        super(id, name, displayName, commandName);
        this.element = element;
    }
}
