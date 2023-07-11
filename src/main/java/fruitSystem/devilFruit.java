package fruitSystem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @brief Devil fruit creation class.
 * @author RedRiotTank, Vaelico786.
 */
public class devilFruit{
    private ItemStack devilFruit;
    private boolean inUse;
    private String commandFruitName;

    /**
     * @brief Devil fruit constructor (creation of the DF).
     * @param fruitCommandName Command alias of the devil fruit.
     * @author RedRiotTank, Vaelico786.
     */
    public devilFruit(String fruitCommandName){
        String fruitItemName = null;
        int modelData = 0;
        ItemMeta metaDataDevilFruit = (new ItemStack(Material.APPLE)).getItemMeta().clone();
        commandFruitName = fruitCommandName;
        switch (fruitCommandName){
            case fruitIdentification.fruitCommandNameYami:
                fruitItemName = fruitIdentification.fruitItemNameYami;
                modelData = 1;
                break;

            case fruitIdentification.fruitCommandNameMera:
                fruitItemName  = fruitIdentification.fruitItemNameMera;
                modelData = 2;
                break;

            case fruitIdentification.fruitCommandNameGura:
                fruitItemName  = fruitIdentification.fruitItemNameGura;
                modelData = 3;
                break;

            case fruitIdentification.fruitCommandNameMoku:
                fruitItemName  = fruitIdentification.fruitItemNameMoku;
                modelData = 4;
                break;
            case fruitIdentification.fruitCommandNameNekoReoparudo:
                fruitItemName  = fruitIdentification.fruitItemNameNekoReoparudo;
                modelData = 5;
                break;

            case fruitIdentification.fruitCommandNameMagu :
                fruitItemName = fruitIdentification.fruitItemNameMagu;
                modelData = 6;
                break;

            case fruitIdentification.fruitCommandNameGoro:
                fruitItemName = fruitIdentification.fruitItemNameGoro;
                modelData = 7;
                break;

            case fruitIdentification.fruitCommandNameIshi:
                fruitItemName = fruitIdentification.fruitItemNameIshi;
                modelData = 8;
                break;

            case fruitIdentification.fruitCommandNameGoru:
                fruitItemName = fruitIdentification.fruitItemNameGoru;
                modelData = 9;
                break;
            case fruitIdentification.fruitCommandNameInuOkuchi:
                fruitItemName = fruitIdentification.fruitItemNameInuOkuchi;
                modelData = 10;
                break;
            case fruitIdentification.fruitCommandNameRyuAllosaurs:
                fruitItemName = fruitIdentification.fruitItemNameRyuAllosaurs;
                modelData = 11;
                break;
            case fruitIdentification.fruitCommandNameOpe:
                fruitItemName = fruitIdentification.fruitItemNameOpe;
                modelData = 12;
                break;

            case fruitIdentification.fruitCommandNameZushi:
                fruitItemName = fruitIdentification.fruitItemNameZushi;
                modelData = 13;
                break;
            case fruitIdentification.fruitCommandNameSuke:
                fruitItemName = fruitIdentification.fruitItemNameSuke;
                modelData = 14;
                break;
            case fruitIdentification.fruitCommandNameHie:
                fruitItemName = fruitIdentification.fruitItemNameHie;
                modelData = 15;
                break;
            case fruitIdentification.fruitCommandNameBane:
                fruitItemName = fruitIdentification.fruitItemNameBane;
                modelData = 17;
                break;
            default:
                fruitItemName  = "ERROR";
                break;
        }

        devilFruit = new ItemStack(Material.APPLE);
        metaDataDevilFruit.setDisplayName(fruitItemName);
        metaDataDevilFruit.setCustomModelData(modelData);
        devilFruit.setItemMeta(metaDataDevilFruit);

        inUse = false;

    }

    /**
     * @brief gives the DevilFruit name.
     * @return gives the DevilFruit name.
     * @author Vaelico786.
     */
    public String getFruitName(){
        return devilFruit.getItemMeta().getDisplayName();
    }

     /**
     * @brief gives the DevilFruit command name.
     * @return gives the DevilFruit command name.
     * @author Vaelico786.
     */   
    public String getCommandFruitName(){
        return commandFruitName;
    }

    /**
     * @brief Gives the devil fruit to a player.
     * @param player Player to give the Devil Fruit.
     * @author RedRiotTank.
     */
    public void playerObtainFruit(Player player){
        player.getInventory().addItem(devilFruit);
    }


}
