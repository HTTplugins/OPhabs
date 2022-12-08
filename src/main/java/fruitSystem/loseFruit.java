package fruitSystem;

import htt.ophabs.OPhabs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class loseFruit implements Listener {
    private final OPhabs plugin;

    public loseFruit(OPhabs plugin){
        this.plugin = plugin;
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        String
                yamiValue = plugin.getConfig().getString("FruitAssociations.yami_yami"),
                meraValue = plugin.getConfig().getString("FruitAssociations.mera_mera"),
                guraValue = plugin.getConfig().getString("FruitAssociations.gura_gura"),
                mokuValue = plugin.getConfig().getString("FruitAssociations.moku_moku");

        if(fruitAssociation.dfPlayers.contains(event.getEntity())){
            fruitAssociation.dfPlayers.remove(event.getEntity());

            if(yamiValue.equals(player.getName())) yamiValue = "none";
            if(meraValue.equals(player.getName())) meraValue = "none";
            if(guraValue.equals(player.getName())) guraValue = "none";
            if(mokuValue.equals(player.getName())) mokuValue = "none";

            plugin.getConfig().set("FruitAssociations.yami_yami",yamiValue);
            plugin.getConfig().set("FruitAssociations.mera_mera",meraValue);
            plugin.getConfig().set("FruitAssociations.gura_gura",guraValue);
            plugin.getConfig().set("FruitAssociations.moku_moku",mokuValue);
            plugin.saveConfig();
        }
    }
}
