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
        //abilitieSystem

        yami_yami yamiClass = new yami_yami(this);
        mera_mera meraClass = new mera_mera(this);
        gura_gura guraClass = new gura_gura(this);
        moku_moku mokuClass = new moku_moku(this);
        neko_neko_reoparudo nekoReoparudoClass = new neko_neko_reoparudo(this);
        magu_magu maguClass = new magu_magu(this);

        //--------------
        //FruitSystem
        fruitAssociation association = new fruitAssociation(this, yamiClass, meraClass, guraClass, mokuClass, nekoReoparudoClass, maguClass);
        getServer().getPluginManager().registerEvents(association, this);
        getServer().getPluginManager().registerEvents(new loseFruit(this, association.dfPlayers), this);



        //--------------
        //CasterSystem
        coolDown cooldown = new coolDown(this);
        cooldown.runCoolDownSystem();

        getServer().getPluginManager().registerEvents(new caster(cooldown,association.dfPlayers), this);
        getServer().getPluginManager().registerEvents(new noDropCaster(), this);

        //--------------
        //ScoreBoards

        abilitiesScoreboard scoreboards = new abilitiesScoreboard(this,cooldown);
        //scoreboards.ini();



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
