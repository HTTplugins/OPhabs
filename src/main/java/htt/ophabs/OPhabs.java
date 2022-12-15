package htt.ophabs;


import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import commands.*;
import fruitSystem.*;
import abilitieSystem.*;
import castSystem.*;
import org.bukkit.ChatColor;

public final class OPhabs extends JavaPlugin {

    @Override
    public void onEnable(){
        registerCommands();

        //---------------
        //Files

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //--------------
        //FruitSystem

        getServer().getPluginManager().registerEvents(new fruitAssociation(this), this);
        getServer().getPluginManager().registerEvents(new loseFruit(this), this);

        //--------------
        //abilitiesSystem

        yami_yami yamiClass = new yami_yami(this);
        mera_mera meraClass = new mera_mera(this);
        gura_gura guraClass = new gura_gura(this);
        moku_moku mokuClass = new moku_moku(this);
        neko_neko_reoparudo nekoReoparudoClass = new neko_neko_reoparudo(this);


        getServer().getPluginManager().registerEvents(yamiClass, this);
        getServer().getPluginManager().registerEvents(nekoReoparudoClass,this);
        getServer().getPluginManager().registerEvents(meraClass,this);

        getServer().getPluginManager().registerEvents(new caster(mokuClass, yamiClass, meraClass, guraClass, nekoReoparudoClass), this);
        getServer().getPluginManager().registerEvents(new noDropCaster(), this);

        //--------------
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD +  "OPhabs started correctly.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "HTTrolplay closed correctly.");
    }

    public void registerCommands(){
        this.getCommand("oph").setExecutor(new oph(this));
    }
}
