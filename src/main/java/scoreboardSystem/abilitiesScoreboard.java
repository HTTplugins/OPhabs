package scoreboardSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import abilitieSystem.*;
import castSystem.*;
import fruitSystem.devilFruitUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class abilitiesScoreboard {

    OPhabs plugin;
    public Map<String, devilFruitUser> dfPlayers = new HashMap<>();
    Map <String,Scoreboard> scoreboards = new HashMap<>();
    ScoreboardManager manager = Bukkit.getScoreboardManager();
// Hashmap nameplayer, pair <dfuser,scoreboard>

    public abilitiesScoreboard(OPhabs plugin, Map<String,devilFruitUser> dfPlayers) {
        this.plugin = plugin;
        this.dfPlayers = dfPlayers;

        String
            yamiValue = plugin.getConfig().getString("FruitAssociations.yami_yami"),
            meraValue = plugin.getConfig().getString("FruitAssociations.mera_mera"),
            guraValue = plugin.getConfig().getString("FruitAssociations.gura_gura"),
            mokuValue = plugin.getConfig().getString("FruitAssociations.moku_moku"),
            nekoReoparudoValue = plugin.getConfig().getString("FruitAssociations.neko_neko_reoparudo"),
            maguValue = plugin.getConfig().getString("FruitAssociations.magu_magu"),
            goroValue = plugin.getConfig().getString("FruitAssociations.goro_goro"),
            ishiValue = plugin.getConfig().getString("FruitAssociations.ishi_ishi"),
            goruValue = plugin.getConfig().getString("FruitAssociations.goru_goru");

            if(dfPlayers.get(yamiValue) != null)
                addScoreboard(dfPlayers.get(yamiValue).getPlayer());

            if(dfPlayers.get(meraValue) != null)
                addScoreboard(dfPlayers.get(meraValue).getPlayer());

            if(dfPlayers.get(guraValue) != null)
                addScoreboard(dfPlayers.get(guraValue).getPlayer());

            if(dfPlayers.get(mokuValue) != null)
                addScoreboard(dfPlayers.get(mokuValue).getPlayer());

            if(dfPlayers.get(nekoReoparudoValue) != null)
                addScoreboard(dfPlayers.get(nekoReoparudoValue).getPlayer());

            if(dfPlayers.get(maguValue) != null)
                addScoreboard(dfPlayers.get(maguValue).getPlayer());

            if(dfPlayers.get(goroValue) != null)
                addScoreboard(dfPlayers.get(goroValue).getPlayer());

            if(dfPlayers.get(ishiValue) != null)
                addScoreboard(dfPlayers.get(ishiValue).getPlayer());

            if(dfPlayers.get(goruValue) != null)
                addScoreboard(dfPlayers.get(goruValue).getPlayer());

    }


    public void ini(){
        new BukkitRunnable(){
            @Override
            public void run() {
                updateScoreboards();
            }
        }.runTaskTimer(plugin,0,10);

    }

    public void addScoreboard(Player player){
        Scoreboard scoreboard = manager.getNewScoreboard();
        scoreboards.put(player.getName(),scoreboard);
    }

    public void removeScoreboard(Player player){
        scoreboards.remove(player.getName());
    }

    public boolean updateScoreboards(){ 
        for (Map.Entry<String, Scoreboard> userScoreboard : scoreboards.entrySet()) {

            String playerName = userScoreboard.getKey();
            Scoreboard scoreboard = manager.getNewScoreboard();
            devilFruitUser user = dfPlayers.get(playerName);

            Player player = null;

            if( Bukkit.getPlayerExact(playerName) != null)
                player = Bukkit.getPlayerExact(playerName);
            else
                player = null;

            Objective objective = scoreboard.registerNewObjective("abilitiesScoreboard","dummy");

            ItemStack caster = null;

            if(player != null)
                caster = player.getInventory().getItemInMainHand();

            ArrayList<String> abNames = new ArrayList<>(user.getAbilitiesNames());
            String active = ChatColor.RED + "" + ChatColor.BOLD + "" + abNames.get(user.actual);

            abNames.set(user.actual, active);

            if( Bukkit.getPlayerExact(playerName) != null)
                player = Bukkit.getPlayerExact(playerName);
            else
                player = null;

            if(player != null)
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            if( (castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), player)) || castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), player)){
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                objective.setDisplayName(user.getFruit().getFruitName());
                

                for(int i = 0; i < abNames.size(); i++){
                    Score score = objective.getScore(abNames.get(i));
                    score.setScore(user.getAbilitiesCD().get(i));
                }

                if( Bukkit.getPlayerExact(playerName) != null)
                    player = Bukkit.getPlayerExact(playerName);
                else
                    player = null;

                if(player != null){
                    player.setScoreboard(scoreboard);
                }

            } else {
                if(player != null)
                    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

            }
            userScoreboard.setValue(scoreboard);
        }

    return true;
    }
}
