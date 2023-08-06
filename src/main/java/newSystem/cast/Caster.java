package newSystem.cast;

import newSystem.events.IEventProcessor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Caster implements IEventProcessor
{
    public static final Material casterMaterial = Material.QUARTZ;

    protected String name;

    public Caster(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public abstract void dispose();

    @Override
    public abstract void onPlayerInteract(PlayerInteractEvent event);

    @Override
    public abstract void onPlayerDropItem(PlayerDropItemEvent event);

    @Override
    public abstract void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event);
}
