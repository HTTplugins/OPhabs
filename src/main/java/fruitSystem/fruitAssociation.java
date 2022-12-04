package fruitSystem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class fruitAssociation implements Listener {

    final String fruitItemNameyami_yami = "yami yami no mi";
    final String fruitItemNamemera_mera = "mera mera no mi";

    public fruitAssociation(){

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack fruit = event.getItem();
        String fruititemName = null;
        Material castItem = null;


        switch (fruit.getItemMeta().getDisplayName()){

            case fruitItemNameyami_yami:

                fruititemName = fruitItemNameyami_yami;
                castItem = Material.STICK;

                break;
            case fruitItemNamemera_mera:
                fruititemName = fruitItemNamemera_mera;
                castItem = Material.CHARCOAL;

                break;

            default:
                break;
        }

        fruit_player.associateFruit(new fruit_player(fruititemName,event.getPlayer()));
        event.getPlayer().getInventory().addItem(new ItemStack(castItem));


    }
}
