package fruitSystem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class devilFruit{
    private ItemStack devilFruit;
    private boolean inUse;
    private String commandFruitName;

    public devilFruit(String fruitCommandName){
        Material devilFruitForm = null;
        String fruitItemName = null;
        ItemMeta metaDataDevilFruit = (new ItemStack(Material.APPLE)).getItemMeta().clone();
        commandFruitName = fruitCommandName;
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
            case fruitIdentification.fruitCommandNameNekoReoparudo:
                devilFruitForm = Material.CARROT;
                fruitItemName  = fruitIdentification.fruitItemNameNekoReoparudo;
                break;

            case fruitIdentification.fruitCommandNameMagu :
                devilFruitForm = Material.APPLE;
                fruitItemName = fruitIdentification.fruitItemNameMagu;
                break;

            case fruitIdentification.fruitCommandNameGoro:
                devilFruitForm = Material.CARROT;
                fruitItemName = fruitIdentification.fruitItemNameGoro;
                break;

            case fruitIdentification.fruitCommandNameIshi:
                devilFruitForm = Material.BEETROOT;
                fruitItemName = fruitIdentification.fruitItemNameIshi;
                break;

            case fruitIdentification.fruitCommandNameGoru:
                devilFruitForm = Material.GOLDEN_APPLE;
                fruitItemName = fruitIdentification.fruitItemNameGoru;
                break;
            case fruitIdentification.fruitCommandNameInuOkuchi:
                devilFruitForm = Material.CARROT;
                fruitItemName = fruitIdentification.fruitItemNameInuOkuchi;
                break;
            case fruitIdentification.fruitCommandNameRyuAllosaurs:
                devilFruitForm = Material.CARROT;
                fruitItemName = fruitIdentification.fruitItemNameRyuAllosaurs;
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

    public String getFruitName(){
        return devilFruit.getItemMeta().getDisplayName();
    }

    public void playerObtainFruit(Player player){
        player.getInventory().addItem(devilFruit);
    }


}
