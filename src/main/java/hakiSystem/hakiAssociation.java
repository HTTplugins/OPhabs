package hakiSystem;

import htt.ophabs.OPhabs;
import org.bukkit.event.Listener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import abilitieSystem.*;
import scoreboardSystem.abilitiesScoreboard;


/**
 * @brief Haki association system [Player - Haki].
 * @author RedRiotTank, Vaelico786.
 */
public class hakiAssociation implements Listener {
    private final OPhabs plugin;
    public Map<String, abilityUser> users = new HashMap<>();
    public abilitiesScoreboard scoreboard = null;
    public ArrayList<String> Names = new ArrayList<>();
    public ArrayList<Integer> Levels = new ArrayList<>();
    public ArrayList<Integer> Exp = new ArrayList<>();


    /**
     * @brief Haki association constructor, links Players (who already have a Haki linked) and abilities on start.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public hakiAssociation(OPhabs plugin) {
        this.plugin = plugin;
        this.users = plugin.users;

        plugin.getConfig().getConfigurationSection("hakiPlayers").getKeys(false).forEach(key -> {
            addHakiPlayer(key, plugin.getConfig().getInt("hakiPlayers." + key + ".Level"),
                          plugin.getConfig().getInt("hakiPlayers." + key + ".Exp"));
        });
    }

    /**
     * @brief Links Players (who already have a Haki linked) and haki with its stats.
     * @param name Player name.
     * @param level Player level haki.
     * @param exp Player exp haki.
     * @author Vaelico786.
     */
    public void addHakiPlayer(String name, int level, int exp) {
        Names.add(name);
        Levels.add(level);
        Exp.add(exp);
        if(users.containsKey(name)){
            abilityUser user = users.get(name);
            if(!user.hasHaki()) {
                user.setHaki(new haki(plugin, user, level, exp));
                plugin.getConfig().set("hakiPlayers." + name + ".Level", level);
            }
        } else {
            abilityUser user = new abilityUser(name);
            user.setHaki(new haki(plugin, user, level, exp));
            users.put(name, user);
            plugin.getConfig().set("hakiPlayers." + name + ".Level", level);
        }
    }

}

