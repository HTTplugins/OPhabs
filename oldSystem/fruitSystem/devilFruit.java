package oldSystem.fruitSystem;

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
    private String commandFruitName, fruitItemName, casterName;
    private int id;
    private double casterDamage, casterSpeed;

    /**
     * @brief Devil fruit constructor (creation of the DF).
     * @param fruitCommandName Command alias of the devil fruit.
     * @author RedRiotTank, Vaelico786.
     */
    public devilFruit(int id, String fruitCommandName, String fruitItemName, String casterName, double casterDamage, double casterSpeed){
        this.id = id;
        ItemMeta metaDataDevilFruit = (new ItemStack(Material.APPLE)).getItemMeta().clone();

        this.commandFruitName = commandFruitName;
        this.fruitItemName = fruitItemName;

        this.casterName = casterName;
        this.casterDamage = casterDamage;
        this.casterSpeed = casterSpeed;

        
        this.devilFruit = new ItemStack(Material.APPLE);
        

        metaDataDevilFruit.setDisplayName(fruitItemName);
        metaDataDevilFruit.setCustomModelData(this.id);
        devilFruit.setItemMeta(metaDataDevilFruit);
    }

    /**
     * @brief gives the DevilFruit name.
     * @return gives the DevilFruit name.
     * @author Vaelico786.
     */
    public String getFruitName(){
        return fruitItemName;
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
     * @brief gives the Caster name.
     * @return gives the Caster name.
     * @author Vaelico786.
     */   
    public String getCasterName(){
        return casterName;
    }
    /**
     * @brief Gives the devil fruit to a player.
     * @param player Player to give the Devil Fruit.
     * @author RedRiotTank.
     */
    public void playerObtainFruit(Player player){
        player.getInventory().addItem(devilFruit);
    }

     /**
     * @brief gives the Custom Model data id.
     * @return gives the Custom Model data id.
     * @author Vaelico786.
     */   
    public int getCustomModelDataId(){
        return id;
    }

    /**
     * @brief gives the caster damage.
     * @return gives the caster damage value.
     * @author Vaelico786.
     */   
    public double getCasterDamage(){
        return casterDamage;
    }

    /**
     * @brief gives the caster speed.
     * @return gives the caster speed value.
     * @author Vaelico786.
     */   
    public double getCasterSpeed(){
        return casterSpeed;
    }
}
