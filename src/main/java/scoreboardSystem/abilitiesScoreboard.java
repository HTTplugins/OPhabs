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
            goroValue = plugin.getConfig().getString("FruitAssociations.goro_goro");
            if(!yamiValue.equals("none")){
                addScoreboard(Bukkit.getPlayerExact(yamiValue));
            }
            if(!meraValue.equals("none")){
                addScoreboard(Bukkit.getPlayerExact(meraValue));
            }
            if(!guraValue.equals("none")){
                addScoreboard(Bukkit.getPlayerExact(guraValue));
            }
            if(!mokuValue.equals("none")){
                addScoreboard(Bukkit.getPlayerExact(mokuValue));
            }
            if(!nekoReoparudoValue.equals("none")){
                addScoreboard(Bukkit.getPlayerExact(nekoReoparudoValue));
            }
            if(!maguValue.equals("none")){
                addScoreboard(Bukkit.getPlayerExact(maguValue));
            }

            if(!goroValue.equals("none")){
                addScoreboard(Bukkit.getPlayerExact(goroValue));
            }
    }

    public void ini() {
        new BukkitRunnable(){
            @Override
            public void run() {
                updateScoreboards();
            }
        }.runTaskTimer(plugin,0,10);
    }

    public void addScoreboard(Player player){
        if(player != null){
            Scoreboard scoreboard = manager.getNewScoreboard();
            scoreboards.put(player.getName(), scoreboard);
        }
    }

    public void removeScoreboard(Player player){
        scoreboards.remove(player.getName());
    }

    public boolean updateScoreboards(){
        for (Map.Entry<String, Scoreboard> userScoreboard : scoreboards.entrySet()) {
            String playerName = userScoreboard.getKey();
            Scoreboard scoreboard = manager.getNewScoreboard();
            devilFruitUser user = dfPlayers.get(playerName);
            Player player = Bukkit.getPlayerExact(playerName);
            Objective objective = scoreboard.registerNewObjective("abilitiesScoreboard","dummy");

            if(player != null){
                ItemStack caster = player.getInventory().getItemInMainHand();
                ArrayList<String> abNames = new ArrayList<>(user.getAbilitiesNames());
                String active = ChatColor.RED + "" + ChatColor.BOLD + "" + abNames.get(user.actual);

                abNames.set(user.actual, active);

                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                if(castIdentification.itemIsCaster(caster, user.getPlayer())){
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                    objective.setDisplayName(user.getFruit().getFruitName());

                    for(int i = 0; i < abNames.size(); i++){
                        Score score = objective.getScore(abNames.get(i));
                        score.setScore(user.getAbilitiesCD().get(i));
                    }

                    player.setScoreboard(scoreboard);
                } else
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                userScoreboard.setValue(scoreboard);
            }
        }

        return true;
    }
}
