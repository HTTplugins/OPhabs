package rokushikiSystem;

import htt.ophabs.OPhabs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.block.Biome;
//AttributeModifier
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import abilitieSystem.*;
import scoreboardSystem.abilitiesScoreboard;

/**
 * @brief This class is used to manage the rokushiki association.
 * @author Vaelico786
 */
public class rokushikiAssociation implements Listener {
    private final OPhabs plugin;
    public static Map<String, abilityUser>  rokushikiPlayers = new HashMap<>(), users = new HashMap<>();
    public abilitiesScoreboard scoreboard = null;
    public ArrayList<abilities> abilityList = new ArrayList<>();

    /**
     * @brief Constructor of the class.
     * @param plugin The plugin.
     * @param users All abilityUsers.
     * @author Vaelico786
     */
    public rokushikiAssociation(OPhabs plugin, Map<String, abilityUser> users) {
        this.plugin = plugin;
        this.users = users;

        if(plugin.getConfig().getConfigurationSection("rokushikiPlayers") != null){
            plugin.getConfig().getConfigurationSection("rokushikiPlayers").getKeys(false).forEach(key -> {
                System.out.println(key);
                loadRokushikiPlayer(key);
            });
        }
    }

    /**
     * @brief This method is used to load the stats of a rokushikiPlayer.
     * @param name The name of the player.
     * @author Vaelico786
     */
    public void addRokushikiPlayer(String name, String ability) {
        if(users.containsKey(name)){
            abilityUser user = users.get(name);
            if(!user.hasRokushiki()) {
                user.setRokushiki(new rokushiki(plugin, user));
                plugin.getConfig().set("rokushikiPlayers." + name + "." + ability + ".Level", 1);
                user.getRokushikiAbilities().loadPlayer();
            }
        } else {
            abilityUser user = new abilityUser(name);
            user.setRokushiki(new rokushiki(plugin, user));
            users.put(name, user);
            plugin.getConfig().set("rokushikiPlayers." + name + "." + ability + ".Level", 1);
            user.getRokushikiAbilities().loadPlayer();
        }
    }

    /**
     * @brief This method is used to add a new rokushikiPlayer.
     * @param name The name of the player.
     * @author Vaelico786
     */
    public void loadRokushikiPlayer(String name) {
        if(users.containsKey(name)){
            abilityUser user = users.get(name);
            if(!user.hasRokushiki()) {
                user.setRokushiki(new rokushiki(plugin, user));
                user.getRokushikiAbilities().loadPlayer();
            }
        } else {
            abilityUser user = new abilityUser(name);
            user.setRokushiki(new rokushiki(plugin, user));
            user.getRokushikiAbilities().loadPlayer();
            users.put(name, user);
        }
    }

    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }

    // @EventHandler(ignoreCancelled = true)
    // public void onPlayerItemConsume(PlayerItemConsumeEvent event){
    // }

}

