package fruitSystem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.graalvm.compiler.nodes.NodeView;

public class devilFruit{


    ItemStack devilFruit;
    boolean inUse;
    public static int numFruits = 4;



    public devilFruit(String fruitCommandName){
        Material devilFruitForm = null;
        String fruitItemName = null;
        ItemMeta metaDataDevilFruit = (new ItemStack(Material.APPLE)).getItemMeta().clone();

        switch (fruitCommandName){
            case fruitIdentification.fruitCommandNameYami:
                devilFruitForm = Material.APPLE;
                fruitItemName = fruitIdentification.fruitItemNameYami;
                break;

            case fruitIdentification.fruitCommandNameMera:
                devilFruitForm = Material.CARROT;
                fruitItemName  = fruitIdentification.fruitItemNameMera;
                break;
            case fruitIdentification.fruitCommandNameGura:
                devilFruitForm = Material.CARROT;
                fruitItemName  = fruitIdentification.fruitItemNameGura;
                break;
            case fruitIdentification.fruitCommandNameMoku:
                devilFruitForm = Material.CARROT;
                fruitItemName  = fruitIdentification.fruitItemNameMoku;
                break;
            default:
                devilFruitForm = Material.ACACIA_BOAT;
                fruitItemName  = "ERROR";
                break;

        }

        devilFruit = new ItemStack(devilFruitForm);
        metaDataDevilFruit.setDisplayName(fruitItemName);
        devilFruit.setItemMeta(metaDataDevilFruit);
        inUse = false;

    }

    public void playerObtainFruit(Player player){

        player.getInventory().addItem(devilFruit);

    }


}