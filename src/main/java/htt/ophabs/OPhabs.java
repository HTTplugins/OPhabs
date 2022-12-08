package htt.ophabs;

import castSystem.caster;
import castSystem.dropCaster;
import castSystem.noDropCaster;
import fruitSystem.fruitAssociation;
import fruitSystem.loseFruit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import commands.oph;
import habilities.*;

import java.io.File;

public final class OPhabs extends JavaPlugin {

    public String configPath;

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerCommands();

        //---------------
        //files

        getConfig().options().copyDefaults();
        saveDefaultConfig();


        //--------------

        moku_moku mokuClass = new moku_moku(this);
        yami_yami yamiClass = new yami_yami(this);
        gura_gura guraClass = new gura_gura(this);
        mera_mera meraClass = new mera_mera(this);


        getServer().getPluginManager().registerEvents(new yami_yami(this), this);
        //getServer().getPluginManager().registerEvents(new mera_mera(this), this);
        //getServer().getPluginManager().registerEvents(new gura_gura(this),this);
        //getServer().getPluginManager().registerEvents(new moku_moku(this),this);
        getServer().getPluginManager().registerEvents(new fruitAssociation(this), this);
        getServer().getPluginManager().registerEvents(new dropCaster(), this);
        getServer().getPluginManager().registerEvents(new loseFruit(this), this);
        getServer().getPluginManager().registerEvents(new caster(this, mokuClass, yamiClass, meraClass, guraClass), this);
        getServer().getPluginManager().registerEvents(new noDropCaster(), this);




        //registerConfig();
        Bukkit.getConsoleSender().sendMessage("OPhabs started correctly");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage("HTTrolplay closed correctly");
    }

    public void registerCommands(){
        this.getCommand("oph").setExecutor(new oph(this));
    }

    public void registerConfig(){
        File config = new File(this.getDataFolder(), "config.yml");
        configPath = config.getPath();

        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
}
