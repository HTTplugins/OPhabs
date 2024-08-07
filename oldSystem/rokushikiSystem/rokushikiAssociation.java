package oldSystem.rokushikiSystem;

import oldSystem.htt.ophabs.*;

import org.bukkit.event.Listener;
//AttributeModifier

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import oldSystem.abilitieSystem.*;
import oldSystem.scoreboardSystem.abilitiesScoreboard;

/**
 * @brief This class is used to manage the rokushiki association.
 * @author Vaelico786
 */
public class rokushikiAssociation implements Listener {
    private final OPhabs plugin;
    public Map<String, abilityUser> users = new HashMap<>();
    public abilitiesScoreboard scoreboard = null;
    public ArrayList<abilities> abilityList = new ArrayList<>();

    /**
     * @brief Constructor of the class.
     * @param plugin The plugin.
     * @param users All abilityUsers.
     * @author Vaelico786
     */
    public rokushikiAssociation(OPhabs plugin) {
        this.plugin = plugin;
        this.users = OPhabs.users;

        Set<String> userKeys = fileSystem.getRokushikiUsersKeys();
        for(String username : userKeys){
            addRokushikiPlayer(username,fileSystem.getRokushikiAbilities(username));
        }
    }

    /**
     * @brief This method is used to load the stats of a rokushikiPlayer.
     * @param name The name of the player.
     * @author Vaelico786
     */
    public void addRokushikiPlayer(String name, String ability) {
        abilityUser user;
        if(users.containsKey(name)){
            user = users.get(name);
            if(!user.hasRokushiki()) {
                user.setRokushiki(new rokushiki(plugin));
            }
        } else {
            user = new abilityUser(name, plugin);
            user.setRokushiki(new rokushiki(plugin));
            users.put(name, user);
        }

        user.getRokushikiAbilities().learnAbility(ability);
        fileSystem.addRokushikiUser(name, ability, 1, 0);
    }

    /**
     * @brief This method is used to load the stats of a rokushikiPlayer.
     * @param name The name of the player.
     * @author Vaelico786
     */
    public void addRokushikiPlayer(String name, Map<String, stat> stats) {
        abilityUser user;
        if(users.containsKey(name)){
            user = users.get(name);
            if(!user.hasRokushiki()) {
                user.setRokushiki(new rokushiki(plugin));
            }
        } else {
            user = new abilityUser(name, plugin);
            user.setRokushiki(new rokushiki(plugin));
            users.put(name, user);
        }

        user.getRokushikiAbilities().loadPlayer(stats);

        fileSystem.addRokushikiUser(name, stats);
    }


    /**
     * @brief This method is used to load the stats of a rokushikiPlayer.
     * @param name The name of the player.
     * @author Vaelico786
     */
    public void addRokushikiPlayerAbility(String name, String ability) {
        abilityUser user;
        if(users.containsKey(name)){
            user = users.get(name);
            if(!user.hasRokushiki()) {
                user.setRokushiki(new rokushiki(plugin));
            }
        }
        else {
            System.out.println("Doesn't exist this rokushikiplayer.");
            return;
        }


        user.getRokushikiAbilities().learnAbility(ability);
        fileSystem.addRokushikiAbilityToUser(name, ability, 1, 0);
    }

    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }

    // @EventHandler(ignoreCancelled = true)
    // public void onPlayerItemConsume(PlayerItemConsumeEvent event){
    // }

}

