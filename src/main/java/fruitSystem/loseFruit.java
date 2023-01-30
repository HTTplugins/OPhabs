package fruitSystem;

import htt.ophabs.OPhabs;
import abilitieSystem.abilityUser;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import scoreboardSystem.abilitiesScoreboard;

import java.util.Map;

public class loseFruit implements Listener {
    private final OPhabs plugin;
    public Map<String, abilityUser> dfPlayers, users;
    public abilitiesScoreboard scoreboard = null;

    public loseFruit(OPhabs plugin, Map<String, abilityUser> dfPlayers, Map<String, abilityUser> users) {
        this.plugin = plugin;
        this.dfPlayers = dfPlayers;
        this.users = users;
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (dfPlayers.containsKey(player.getName())) {
            abilityUser dfUser = users.get(player.getName());
            dfUser.onPlayerDeath(event);
            plugin.getConfig().set(("FruitAssociations."+dfUser.getDFAbilities().getName()),"none");
            plugin.saveConfig();
            dfPlayers.remove(player.getName());
            users.remove(player.getName());
            scoreboard.removeScoreboard(player);
        }
    }

    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }
}
