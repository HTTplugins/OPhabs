package oldSystem.htt.ophabs;


import htt.layeredstructures.LayeredStructuresAPI;
import oldSystem.abilitieSystem.*;
import oldSystem.castSystem.caster;
import oldSystem.castSystem.coolDown;
import oldSystem.commands.oph;
import oldSystem.commands.weaponShop;
import oldSystem.fruitSystem.fruitAssociation;
import oldSystem.fruitSystem.loseFruit;
import oldSystem.hakiSystem.hakiAssociation;
import oldSystem.rokushikiSystem.rokushikiAssociation;
import oldSystem.scoreboardSystem.abilitiesScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @brief Main class of OPhabs plugin.
 * @author RedRiotTank, Vaelico786.
 */
public final class OPhabs extends JavaPlugin {
    public static Map<String, abilityUser> users = new HashMap<>();
    public static ArrayList<df> abilitiesList = new ArrayList<>();
    private ScheduledExecutorService scheduler; //Planificador guardar archivo
    /**
     * @brief Set up of the plugin (start configuration). Literally the main.
     * @author RedRiotTank, Vaelico786.
     */
    @Override
    public void onEnable()
    {
        //
        // VERSIÓN PREVIA
        //

        //--------------
        //abilitieSystem

        abilitiesList.add(new yami_yami(this));
        abilitiesList.add(new mera_mera(this));
        abilitiesList.add(new gura_gura(this));
        abilitiesList.add(new moku_moku(this));
        abilitiesList.add(new neko_neko_reoparudo(this));
        abilitiesList.add(new magu_magu(this));
        abilitiesList.add(new goro_goro(this));
        abilitiesList.add(new ishi_ishi(this));
        abilitiesList.add(new goru_goru(this));
        abilitiesList.add(new inu_inu_okuchi(this));
        abilitiesList.add(new ryu_ryu_allosaurs(this));
        abilitiesList.add(new ope_ope(this));
        abilitiesList.add(new zushi_zushi(this));
        abilitiesList.add(new suke_suke(this));
        abilitiesList.add(new bane_bane(this));
        abilitiesList.add(new hie_hie(this));
        abilitiesList.add(new inu_inu_urufu(this));


        //---------------
        //Files
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        saveConfig();
        fileSystem.loadFileSystem();

        //--------------
        //FruitSystem
        fruitAssociation fAssociation = new fruitAssociation(this);
        loseFruit lFruit = new loseFruit(this, fAssociation.dfPlayers);
        getServer().getPluginManager().registerEvents(fAssociation, this);
        getServer().getPluginManager().registerEvents(lFruit, this);

        //
        //HakiSystem
        hakiAssociation haki = new hakiAssociation(this);
        getServer().getPluginManager().registerEvents(haki, this);

        //
        //RokushikiSystem
        rokushikiAssociation rokushiki = new rokushikiAssociation(this);
        getServer().getPluginManager().registerEvents(rokushiki, this);

        //--------------
        //CasterSystem
        coolDown cooldown = new coolDown(this);

        getServer().getPluginManager().registerEvents(new caster(cooldown,this), this);
        //getServer().getPluginManager().registerEvents(new noDropCaster(), this);

        //--------------
        //ScoreBoards

        abilitiesScoreboard scoreboard = new abilitiesScoreboard(this);
        scoreboard.ini();
        fAssociation.setScoreboard(scoreboard);
        lFruit.setScoreboard(scoreboard);

        registerCommands(abilitiesList, haki, rokushiki);

        scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            fileSystem.saveConfig(this);
        };
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);

        //--------------
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +  "OPhabs started correctly.");


        }

    /**
     * @brief settings configuration on shutdown.
     * @author RedRiotTank, Vaelico786.
     */
    @Override
    public void onDisable()
    {
        scheduler.shutdown();

        //
        fileSystem.saveConfig(this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "OPhabs closed correctly.");
    }

    /**
     * @brief Registration of the commands.
     * @author RedRiotTank, Vaelico786.
     */
    public void registerCommands(ArrayList<df> abilitiesList, hakiAssociation haki, rokushikiAssociation rokushiki){
        Objects.requireNonNull(this.getCommand("oph")).setExecutor(new oph(this, haki, rokushiki));
        Objects.requireNonNull(this.getCommand("oph")).setTabCompleter(new oph(this, haki, rokushiki));
        //Objects.requireNonNull(this.getCommand("weaponShop")).setExecutor(new weaponShop(this));
    }
}
