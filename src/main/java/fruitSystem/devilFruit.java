package fruitSystem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class devilFruit {

    final String fruitNameyami_yami = "yami_yami",
            fruitNamemera_mera = "mera_mera",
            fruitNamegura_gura = "gura_gura";

    ItemStack devilFruit;
    Material devilFruitForm;
    String displayDevilFruitName;
    ItemMeta metaDataDevilFruit;

    public devilFruit(String fruitName){
        metaDataDevilFruit = (new ItemStack(Material.APPLE)).getItemMeta().clone();

        switch (fruitName){
            case fruitNameyami_yami:
                devilFruitForm = Material.APPLE;
                displayDevilFruitName = "Yami Yami no mi";
                break;
            case fruitNamemera_mera:
                devilFruitForm = Material.CARROT;
                displayDevilFruitName = "Mera Mera no mi";
                break;
            case fruitNamegura_gura:
                devilFruitForm = Material.CHORUS_FRUIT;
                displayDevilFruitName = "Gura Gura no mi";
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
