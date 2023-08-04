package newSystem.cast;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Caster
{
    public static final Material casterMaterial = Material.QUARTZ;

    private String name;

    public Caster(String name)
    {
        this.name = name;
    }

    public abstract void onPlayerInteract(PlayerInteractEvent event);
    public abstract void onPlayerDropItem(PlayerDropItemEvent event);
    public abstract void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event);
}
