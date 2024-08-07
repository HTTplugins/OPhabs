package oldSystem.scoreboardSystem;

import oldSystem.htt.ophabs.*;
import oldSystem.castSystem.castIdentification;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import oldSystem.abilitieSystem.abilityUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @brief Scoreboard for abilities class.
 * @author RedRiotTank, Vaelico786.
 */
public class abilitiesScoreboard {
    OPhabs plugin;
    public Map<String, abilityUser> users = new HashMap<>();
    Map <String,Scoreboard> scoreboards = new HashMap<>();
    ScoreboardManager manager = Bukkit.getScoreboardManager();

    /**
     * @brief Scoreboard constructor.
     * @param plugin OPhabs plugin.
     * @author RedRiotTank, Vaelico786.
     */
    public abilitiesScoreboard(OPhabs plugin) {
        this.plugin = plugin;
        this.users = OPhabs.users;

        ArrayList<String> values = new ArrayList<>();
        for(abilityUser user : users.values()) {
            if (user.hasFruit())
                if (!fileSystem.getFruitLinkedUser(user.getDFAbilities().getName()).equals("none"))
                    values.add(fileSystem.getFruitLinkedUser(user.getDFAbilities().getName()));
        }

        for(String value : values) {
            addScoreboard(value);
        }
    }

    /**
     * @brief Scoreboard initialization.
     * Updates the scoreboard every 10 ticks
     * @author RedRiotTank, Vaelico786.
     */
    public void ini(){
        new BukkitRunnable(){
            @Override
            public void run() {
                updateScoreboards();
            }
        }.runTaskTimer(plugin,0,10);
    }

    /**
     * @brief Adds an scoreboard to the scoreboards map.
     * @param playerName Name of the player linked to the scoreboard.
     * @author RedRiotTank, Vaelico786.
     */
    public void addScoreboard(String playerName){
        Scoreboard scoreboard = manager.getNewScoreboard();
        scoreboards.put(playerName,scoreboard);
    }

    /**
     * @brief Removes an scoreboard from the scoreboards map.
     * @param player Name of the player linked to the scoreboard.
     * @author RedRiotTank, Vaelico786.
     */
    public void removeScoreboard(Player player){
        scoreboards.remove(player.getName());
    }

    /**
     * @brief Updates the data from the scoreboard.
     * @author RedRiotTank, Vaelico786.
     */
    public boolean updateScoreboards() {
        for(Map.Entry<String, Scoreboard> userScoreboard : scoreboards.entrySet()) {
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

                ArrayList<String> abNames = new ArrayList<>(user.getAbilitiesNames());
                String active = ChatColor.RED + "" + ChatColor.BOLD + "" + abNames.get(user.actual);

                abNames.set(user.actual, active);

                if( Bukkit.getPlayerExact(playerName) != null)
                    player = Bukkit.getPlayerExact(playerName);
                else
                    player = null;

                if(player != null) {
                        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

                    if((castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), user)) ||
                            castIdentification.itemIsCaster(player.getInventory().getItemInOffHand(), user)) {

                        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                        String fruit = user.getFruit().getFruitName();
                        fruit = fruit.substring(0,1).toUpperCase() + fruit.substring(1);

                        objective.setDisplayName(ChatColor.BOLD + "" + fruit);

                        for(int i = 0; i < abNames.size(); i++) {
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

                        if(player != null) {
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
