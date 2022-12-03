package fruitSystem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class devilFruit{
    final String fruitNameyami_yami = "yami_yami",
            fruitNamemera_mera = "mera_mera";

    final String fruitItemNameyami_yami = "yami yami no mi";
    final String fruitItemNamemera_mera = "mera mera no mi";


    ItemStack devilFruit;
    Material devilFruitForm;
    String displayDevilFruitName;
    ItemMeta metaDataDevilFruit;

    public devilFruit(String fruitName){
        metaDataDevilFruit = (new ItemStack(Material.APPLE)).getItemMeta().clone();

        switch (fruitName){
            case fruitNameyami_yami:
                devilFruitForm = Material.APPLE;
                displayDevilFruitName = fruitItemNameyami_yami;

                break;
            case fruitNamemera_mera:
                devilFruitForm = Material.CARROT;
                displayDevilFruitName = fruitItemNamemera_mera;
                break;
        }

        devilFruit = new ItemStack(devilFruitForm);
        metaDataDevilFruit.setDisplayName(displayDevilFruitName);
        devilFruit.setItemMeta(metaDataDevilFruit);
    }

    public void playerObtainFruit(Player player){

        player.getInventory().addItem(devilFruit);

    }


}
