package htt.ophabs;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import commands.*;
import fruitSystem.*;
import abilitieSystem.*;
import castSystem.*;
import org.bukkit.ChatColor;
import scoreboardSystem.*;
import weapons.*;

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
        ArrayList<abilities> abilitiesList = new ArrayList<>();

        abilitiesList.add(new yami_yami(this));
        abilitiesList.add(new mera_mera(this));
        abilitiesList.add(new gura_gura(this));
        abilitiesList.add(new moku_moku(this));
        abilitiesList.add(new neko_neko_reoparudo(this));
        abilitiesList.add(new magu_magu(this));
        abilitiesList.add(new goro_goro(this));
        abilitiesList.add(new ishi_ishi(this));
        abilitiesList.add(new goru_goru(this));

        //--------------
        //FruitSystem
        fruitAssociation association = new fruitAssociation(this, abilitiesList);
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
        Objects.requireNonNull(this.getCommand("weaponShop")).setExecutor(new weaponShop(this));
    }
}
