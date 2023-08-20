package events;

import fruits.logia.Moku_Moku;
import fruits.paramecia.Ope_Ope;
import fruits.paramecia.Zushi_Zushi;
import htt.ophabs.OPhabs;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import users.OPUser;
import cast.Caster;
import cast.FruitCaster;
import consumables.ConsumableDevilFruit;
import registry.fruits.IFruitRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class FruitEvents implements Listener
{
    private IFruitRegistry fruitRegistry;

    public FruitEvents()
    {
        this.fruitRegistry = OPhabs.registrySystem.getRegistry(IFruitRegistry.class);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event)
    {
        ItemStack item = event.getItem();

        if (ConsumableDevilFruit.isConsumableDevilFruit(item))
        {
            Player player = event.getPlayer();
            OPUser user = OPhabs.newUsers.getOrSetUser(player.getUniqueId(), player.getName());
            int fruitID = item.getItemMeta().getCustomModelData();

            // Ya tiene una fruta
            if (user.hasDevilFruit() && user.getDevilFruit().getID() != fruitID)
            {
                player.getWorld().strikeLightningEffect(player.getLocation());
                player.setHealth(0);
                return;
            }

            if (this.fruitRegistry.linkFruitUser(fruitID, user))
            {
                // Darle el caster
                player.getInventory().addItem(FruitCaster.getAsItem(fruitID));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        OPUser user = OPhabs.newUsers.getOrSetUser(player.getUniqueId(), player.getName());

        if (user.hasDevilFruit())
        {
            Map<Integer, Caster> casters = user.getActiveCasters();

            // Eliminar el caster de los drops
            for (ItemStack drop : event.getDrops())
            {
                if (!drop.hasItemMeta() || !drop.getItemMeta().hasCustomModelData())
                    continue;

                Caster caster = casters.get(drop.getItemMeta().getCustomModelData());

                if (caster != null && caster.isThisItem(drop))
                    drop.setAmount(0);
            }

            user.getDevilFruit().onPlayerDeath(event);
            this.fruitRegistry.unlinkFruitUser(user);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(Zushi_Zushi.getFruitID()).onEntityToggleGlide(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(Ope_Ope.getFruitID()).onBlockBreak(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(Ope_Ope.getFruitID()).onEntityDeath(event);

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(Ope_Ope.getFruitID()).onPlayerJoin(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(Ope_Ope.getFruitID()).onEntityDamageByEntity(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityAirChange(EntityAirChangeEvent event) {
        OPhabs.registrySystem.getRegistry(IFruitRegistry.class).getFruit(Moku_Moku.getFruitID()).onEntityAirChange(event);
    }
}
