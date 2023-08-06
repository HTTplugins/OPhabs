package fruitSystem;

import htt.ophabs.OPhabs;
import htt.ophabs.fileSystem;
import abilitieSystem.abilityUser;
import castSystem.noDropCaster;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import scoreboardSystem.abilitiesScoreboard;

import java.util.Map;

/**
 * @brief Losing fruit system (when players dies, they lose their fruit).
 * @author RedRiotTank, Vaelico786.
 */
public class loseFruit implements Listener {
    private final OPhabs plugin;
    public Map<String, abilityUser> dfPlayers, users;
    public abilitiesScoreboard scoreboard = null;

    /**
     * @brief LoseFruit system construction.
     * @param plugin OPhabs plugin.
     * @param dfPlayers List with all devil fruit abilities (abilitieSystem abilities).
     * @author Vaelico786.
     */
    public loseFruit(OPhabs plugin, Map<String, abilityUser> dfPlayers) {
        this.plugin = plugin;
        this.users = OPhabs.users;
        this.dfPlayers = dfPlayers;
    }

    /**
     * @brief Unlinks Player and Devil Fruit abilities when tha player die and liberates
     * access to that fruit again.
     * @param event Death of a player event.
     * @author RedRiotTank, Vaelico786.
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        noDropCaster.onPlayerDeath(event);
        if (dfPlayers.containsKey(player.getName())) {
            abilityUser dfUser = users.get(player.getName());
            dfUser.onPlayerDeath(event);
            fileSystem.updateFruitLinkedUser(dfUser.getDFAbilities().getName(),"none");
            plugin.saveConfig();
            dfPlayers.remove(player.getName());
            users.get(player.getName()).removeFruit();
            scoreboard.removeScoreboard(player);
        }
    }

    /**
     * @brief Deletes scoreboard.
     * @param scoreboard New scoreboard (empty one).
     * @author Vaelico786.
     */
    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }
}
