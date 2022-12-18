package htt.ophabs;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import commands.*;
import fruitSystem.*;
import abilitieSystem.*;
import castSystem.*;
import org.bukkit.ChatColor;
import scoreboardSystem.*;

import java.util.Objects;

public final class OPhabs extends JavaPlugin {

    @Override
    public void onEnable(){
        registerCommands();

        //---------------
        //Initializations:

        abilitiesIdentification.initialiceNames();

        //---------------
        //Files

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //--------------
        //FruitSystem

        getServer().getPluginManager().registerEvents(new fruitAssociation(this), this);
        getServer().getPluginManager().registerEvents(new loseFruit(this), this);

        //--------------
        //abilitieSystem

        yami_yami yamiClass = new yami_yami(this);
        mera_mera meraClass = new mera_mera(this);
        gura_gura guraClass = new gura_gura(this);
        moku_moku mokuClass = new moku_moku(this);
        neko_neko_reoparudo nekoReoparudoClass = new neko_neko_reoparudo(this);
        magu_magu maguClass = new magu_magu(this);

        getServer().getPluginManager().registerEvents(yamiClass, this);
        getServer().getPluginManager().registerEvents(nekoReoparudoClass,this);
        //getServer().getPluginManager().registerEvents(meraClass,this);

        //--------------
        //CasterSystem
        coolDown cooldown = new coolDown(this);
        cooldown.runCoolDownSystem();

        getServer().getPluginManager().registerEvents(new caster(cooldown, mokuClass, yamiClass, meraClass, guraClass, nekoReoparudoClass, maguClass), this);
        getServer().getPluginManager().registerEvents(new noDropCaster(), this);

        //--------------
        //ScoreBoards

        abilitiesScoreboard scoreboards = new abilitiesScoreboard(this,cooldown);
        scoreboards.ini();



        //--------------
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD +  "OPhabs started correctly.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "HTTrolplay closed correctly.");
    }

    public void registerCommands(){
        Objects.requireNonNull(this.getCommand("oph")).setExecutor(new oph(this));
    }
}
