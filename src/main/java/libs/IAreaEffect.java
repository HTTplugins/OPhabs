package libs;

import org.bukkit.Location;

@FunctionalInterface
public interface IAreaEffect {
    void run(double areaX, double areaY, double areaZ, Location location);
}
