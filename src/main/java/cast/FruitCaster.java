package cast;

import htt.ophabs.OPhabs;
import users.OPUser;
import abilities.AbilitySet;
import display.IFruitCasterDisplay;
import fruits.DevilFruit;
import registry.fruits.IFruitRegistry;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
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
    }

    public DevilFruit getFruit()
    {
        return this.fruit;
    }

    public AbilitySet getSelectedAbilitySet()
    {
        return fruit.getAbilitySets().get(this.selectedAbilitySet);
    }

    public int getSelectedAbilityNumber()
    {
        return this.selectedAbility;
    }

    @Override
    public void dispose()
    {
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

        return itemMeta.getCustomModelData() == fruit.getID();
    }

    @Override
    public boolean isOwnedBy(OPUser user)
    {
        return user.equals(fruit.getUser());
    }

    // TODO: Esto hace un trigger de la habilidad por la cara.
    // Hay que comprobar el tipo de evento para que sea hacer click
    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Action eventAction = event.getAction();

        if (eventAction == Action.RIGHT_CLICK_AIR || eventAction == Action.RIGHT_CLICK_BLOCK)
        {
            // Utilizar la habilidad seleccionada
            this.fruit.invokeAbility(selectedAbilitySet, selectedAbility);
        }
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        event.setCancelled(true);

        // Rotar las habilidades
        ArrayList<AbilitySet> abilitySets = this.fruit.getAbilitySets();

        if (!abilitySets.isEmpty())
        {
            this.selectedAbility++;

            if (this.selectedAbility >= abilitySets.get(selectedAbilitySet).getAbilities().size())
                this.selectedAbility = 0;
        }
    }

    @Override
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event)
    {
        event.setCancelled(true);

        // Rotar el set
        this.selectedAbilitySet++;

        if (this.selectedAbilitySet >= this.fruit.getAbilitySets().size())
            this.selectedAbilitySet = 0;
    }
}
