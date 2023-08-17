package htt.ophabs;


import htt.layeredstructures.LayeredStructuresAPI;
import logs.msgSystem;
import users.UserList;
import commands.OPHCommandManager;
import display.ScoreboardSystem;
import events.EventSystem;
import cooldown.CooldownSystem;
import events.FruitEvents;
import registry.RegistrySystem;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @brief Main class of OPhabs plugin.
 * @author RedRiotTank, Vaelico786.
 */
public final class OPhabs extends JavaPlugin {
    private static OPhabs instance = null;
    public static OPhabs getInstance()
    {
        return instance;
    }

    public static final UserList newUsers = new UserList();
    public static final RegistrySystem registrySystem = new RegistrySystem();
    public static CooldownSystem cooldownSystem;
    public static ScoreboardSystem scoreboardSystem;

    private ScheduledExecutorService scheduler; //Planificador guardar archivo

    /**
     * @brief Set up of the plugin (start configuration). Literally the main.
     * @author RedRiotTank, Vaelico786.
     */
    @Override
    public void onEnable()
    {
        OPhabs.instance = this;


        //
        // Inicializar dependencias
        //

        LayeredStructuresAPI.initialize(Bukkit.getPluginManager().getPlugin("LayeredStructures"));
        LayeredStructuresAPI.precharge("iceDrake"); //pendiente refactor jaime para limpiar del enable.
        LayeredStructuresAPI.precharge("room");

        //
        // Inicializar los registros
        //

        registrySystem.initRegistry();


        //
        // Inicializar sistemas
        //

        scoreboardSystem = new ScoreboardSystem();
        cooldownSystem = new CooldownSystem();


        //
        // Cargar los registros
        //

        registrySystem.loadRegistry();

        //
        // Activar eventos
        //

        EventSystem eventSystem = new EventSystem();
        getServer().getPluginManager().registerEvents(eventSystem, this);

        FruitEvents fruitEvents = new FruitEvents();
        getServer().getPluginManager().registerEvents(fruitEvents, this);

        //
        // Registrar comandos
        //

        OPHCommandManager ophCommandManager = new OPHCommandManager();
        Objects.requireNonNull(this.getCommand("oph")).setExecutor(ophCommandManager);
        Objects.requireNonNull(this.getCommand("oph")).setTabCompleter(ophCommandManager);

        msgSystem.OphConsoleMsg("Plugin started correctly.");
    }

    /**
     * @brief settings configuration on shutdown.
     * @author RedRiotTank, Vaelico786.
     */
    @Override
    public void onDisable()
    {
        //scheduler.shutdown();

        //
        registrySystem.saveRegistry();

        //
        msgSystem.OphConsoleMsg("Plugin disabled correctly.");
    }
}
