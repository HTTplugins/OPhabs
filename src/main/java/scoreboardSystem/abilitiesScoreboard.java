package scoreboardSystem;

import fruitSystem.fruitIdentification;
import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import abilitieSystem.abilityUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class abilitiesScoreboard {

    OPhabs plugin;
    public Map<String, abilityUser> users = new HashMap<>();
    Map <String,Scoreboard> scoreboards = new HashMap<>();
    ScoreboardManager manager = Bukkit.getScoreboardManager();

    public abilitiesScoreboard(OPhabs plugin, Map<String, abilityUser> users) {
        this.plugin = plugin;
        this.users = users;

        ArrayList<String> values = new ArrayList<>();
        for(abilityUser user : users.values()) {
            if (user.hasFruit())
                if (!plugin.getConfig().getString("FruitAssociations." + user.getDFAbilities().getName()).equals("none"))
                    values.add(plugin.getConfig().getString("FruitAssociations." + user.getDFAbilities().getName()));
        }

        
        for(String value : values) {
            addScoreboard(value);
        }

    }


    public void ini(){
        new BukkitRunnable(){
            @Override
            public void run() {
                updateScoreboards();
            }
        }.runTaskTimer(plugin,0,10);

    }

    public void addScoreboard(String playerName){
        Scoreboard scoreboard = manager.getNewScoreboard();
        scoreboards.put(playerName,scoreboard);
    }

    public void removeScoreboard(Player player){
        scoreboards.remove(player.getName());
    }

    public boolean updateScoreboards(){
        for (Map.Entry<String, Scoreboard> userScoreboard : scoreboards.entrySet()) {

            String playerName = userScoreboard.getKey();
            if(Bukkit.getPlayer(playerName) != null) {

                Scoreboard scoreboard = manager.getNewScoreboard();
                abilityUser user = users.get(playerName);

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

                if(player != null){
                    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                if( (castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), player)) || castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), player)){
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                    String fruit = fruitIdentification.getItemName(user.getDFAbilities().getName());
                    fruit = fruit.substring(0,1).toUpperCase() + fruit.substring(1);
                    
                    objective.setDisplayName(ChatColor.BOLD + "" + fruit);
                    

                    for(int i = 0; i < abNames.size(); i++){
                        String name = abNames.get(i);
                        if(user.getAbilitiesCD().get(i) > 0)
                            name += " (" + user.getAbilitiesCD().get(i) + ")";
                        Score score = objective.getScore(name);
                        score.setScore(i);
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
                }
                userScoreboard.setValue(scoreboard);
            }
        
        }

        return true;
    }
}
