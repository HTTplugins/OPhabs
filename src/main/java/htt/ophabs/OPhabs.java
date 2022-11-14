package htt.ophabs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class OPhabs extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage("OPhabs started correctly");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("HTTrolplay closed correctly");
    }
}
