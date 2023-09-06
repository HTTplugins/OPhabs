package cast;

import htt.ophabs.OPhabs;
import textures.TextureMapper;
import users.OPUser;
import abilities.AbilitySet;
import display.IFruitCasterDisplay;
import fruits.DevilFruit;
import registry.fruits.IFruitRegistry;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

// Permite interactuar
public class FruitCaster extends Caster
{
    protected DevilFruit fruit;
    protected int selectedAbility = 0;
    protected int selectedAbilitySet = 0;

    protected IFruitCasterDisplay fruitCasterDisplay;

    public static ItemStack getAsItem(int fruitID)
    {
        ItemStack casterItem = new ItemStack(CASTER_MATERIAL);
        DevilFruit fruit = OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(fruitID);

        if (fruit != null)
        {
            ItemMeta casterMeta = casterItem.getItemMeta();
            casterMeta.setDisplayName(fruit.getDisplayName() + " Caster");
            casterMeta.setCustomModelData(fruitID);
            casterItem.setItemMeta(casterMeta);
        }

        return casterItem;
    }

    public FruitCaster(DevilFruit fruit, IFruitCasterDisplay fruitCasterDisplay)
    {
        super(fruit.getName());
        this.fruit = fruit;
        this.fruitCasterDisplay = fruitCasterDisplay;

        fruitCasterDisplay.addFruitCaster(this);

        // Añadir al usuario los remapeos
        OPUser user = fruit.getUser();

        for (int remappedID : OPhabs.textureMapper.getRemappedIDs(this.fruit.getID()))
            user.getActiveCasters().put(remappedID, this);
    }

    public DevilFruit getFruit()
    {
        return this.fruit;
    }

    public int getSelectedAbilityNumber()
    {
        return this.selectedAbility;
    }

    public AbilitySet getSelectedAbilitySet()
    {
        return fruit.getAbilitySets().get(this.selectedAbilitySet);
    }

    @Override
    public void dispose()
    {
        // Eliminar los remapeos del usuario
        OPUser user = fruit.getUser();

        for (int remappedID : OPhabs.textureMapper.getRemappedIDs(this.fruit.getID()))
            user.getActiveCasters().remove(remappedID);

        fruitCasterDisplay.removeFruitCaster(this);
    }

    @Override
    public boolean isThisItem(ItemStack item)
    {
        if (item.getType() != CASTER_MATERIAL)
            return false;

        // Comprobar el material y el id
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null || !itemMeta.hasCustomModelData())
            return false;

        int fruitID = fruit.getID();
        int itemID = itemMeta.getCustomModelData();

        return itemID == fruitID || OPhabs.textureMapper.isThisRemapping(fruitID, itemID);
    }

    @Override
    public boolean isOwnedBy(OPUser user)
    {
        return user.equals(fruit.getUser());
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Action eventAction = event.getAction();

        if (eventAction == Action.RIGHT_CLICK_AIR || eventAction == Action.RIGHT_CLICK_BLOCK)
        {
            if(isCombatMode()){
                // Utilizar la habilidad seleccionada
                if(!fruit.getUser().isFastCasting()){
                    this.fruit.invokeAbility(selectedAbilitySet, selectedAbility);
                }
            }
        }    
    }

    //Cambia la habilidad seleccionada o lanza la habilidad segun si fastCasting esta activo
    @Override
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event)
    {
        if (isCombatMode())
        {
            event.setCancelled(true);
            selectedAbility = event.getNewSlot();
            if(fruit.getUser().isFastCasting()){
                // Utilizar la habilidad seleccionada
                this.fruit.invokeAbility(selectedAbilitySet, selectedAbility);
            }
        }
    }

    @Override
    public void onPlayerCasterHeldEvent(PlayerItemHeldEvent event){
        fruit.onPlayerCasterHeldEvent(event);
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
        if(!isCombatMode()){
            Player player = event.getPlayer();

            ItemStack itemInHand = player.getItemInHand();
            ItemStack itemInSlot9 = player.getInventory().getItem(8); // Posición 9 (se cuentan desde 0)

            player.getInventory().setItemInMainHand(itemInSlot9); // Coloca el objeto en la mano
            player.getInventory().setItem(8, itemInHand); // Coloca el objeto en la posición 9
            player.getInventory().setHeldItemSlot(8);
        }

        setCombatMode(!isCombatMode());
    }

    @Override
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
    {
        if(isCombatMode()){
            
            selectedAbilitySet+=1;
            if(this.fruit.getAbilitySets().size() <= selectedAbilitySet){
                selectedAbilitySet = 0;
            }
            selectedAbility = selectedAbilitySet%fruit.getAbilitySets().size();
            event.setCancelled(true);
        }
    }
}
