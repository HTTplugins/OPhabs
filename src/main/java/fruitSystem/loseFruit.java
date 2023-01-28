package fruitSystem;

import htt.ophabs.OPhabs;
import skin.skinsChanger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import scoreboardSystem.abilitiesScoreboard;

import java.util.Map;

public class loseFruit implements Listener {
    private final OPhabs plugin;
    public Map<String, devilFruitUser> dfPlayers;
    public abilitiesScoreboard scoreboard = null;

    public loseFruit(OPhabs plugin, Map<String, devilFruitUser> dfPlayers) {
        this.plugin = plugin;
        this.dfPlayers = dfPlayers;
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (dfPlayers.containsKey(player.getName())) {
            devilFruitUser dfUser = dfPlayers.get(player.getName());
            dfUser.onPlayerDeath(event);
            plugin.getConfig().set(("FruitAssociations."+dfUser.ability.getName()),"none");
            plugin.saveConfig();
            dfPlayers.remove(player.getName()); 
            scoreboard.removeScoreboard(player);
        }
    }

    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }
}
