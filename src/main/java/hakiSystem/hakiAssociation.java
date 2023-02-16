package hakiSystem;

import htt.ophabs.OPhabs;
import org.bukkit.event.Listener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import abilitieSystem.*;
import scoreboardSystem.abilitiesScoreboard;

public class hakiAssociation implements Listener {
    private final OPhabs plugin;
    public static Map<String, abilityUser> users = new HashMap<>();
    public abilitiesScoreboard scoreboard = null;
    public ArrayList<String> Names = new ArrayList<>();
    public ArrayList<Integer> Levels = new ArrayList<>();
    public ArrayList<Integer> Exp = new ArrayList<>();

    public hakiAssociation(OPhabs plugin, Map<String, abilityUser> users) {
        this.plugin = plugin;
        this.users = users;

        plugin.getConfig().getConfigurationSection("hakiPlayers").getKeys(false).forEach(key -> {
            addHakiPlayer(key, plugin.getConfig().getInt("hakiPlayers." + key + ".Level"),
                          plugin.getConfig().getInt("hakiPlayers." + key + ".Exp"));
        });
    }

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

    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }
}

