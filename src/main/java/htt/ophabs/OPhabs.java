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
import java.util.ArrayList;

public final class OPhabs extends JavaPlugin {

    @Override
    public void onEnable(){
        registerCommands();

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
        goro_goro goroClass = new goro_goro(this);
        ishi_ishi ishiClass = new ishi_ishi(this);

        //--------------
        //FruitSystem
        fruitAssociation association = new fruitAssociation(this, yamiClass, meraClass, guraClass, mokuClass, nekoReoparudoClass, maguClass, goroClass, ishiClass);
        loseFruit lFruit = new loseFruit(this, association.dfPlayers);
        getServer().getPluginManager().registerEvents(association, this);
        getServer().getPluginManager().registerEvents(lFruit, this);

        //--------------
        //CasterSystem
        coolDown cooldown = new coolDown(this, association.dfPlayers);

        getServer().getPluginManager().registerEvents(new caster(cooldown,association.dfPlayers), this);
        getServer().getPluginManager().registerEvents(new noDropCaster(), this);

        //--------------
        //ScoreBoards

        abilitiesScoreboard scoreboard = new abilitiesScoreboard(this, association.dfPlayers);
        scoreboard.ini();
        association.setScoreboard(scoreboard);
        lFruit.setScoreboard(scoreboard);

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
