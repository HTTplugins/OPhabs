package cast;

import users.OPUser;
import events.IEventProcessor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Caster implements IEventProcessor
{
    // Material por defecto
    public static final Material CASTER_MATERIAL = Material.QUARTZ;


    protected String name;

    private boolean combatMode = false;

    public Caster(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isCombatMode(){return this.combatMode;}

    public void setCombatMode(boolean active){this.combatMode = active;}

    public abstract void dispose();

    public abstract boolean isThisItem(ItemStack item);

    public abstract boolean isOwnedBy(OPUser user);

    @Override
    public abstract void onPlayerInteract(PlayerInteractEvent event);

    @Override
    public abstract void onPlayerDropItem(PlayerDropItemEvent event);

    @Override
    public abstract void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event);

    @Override
    public abstract void onPlayerItemHeldEvent(PlayerItemHeldEvent event);

}
