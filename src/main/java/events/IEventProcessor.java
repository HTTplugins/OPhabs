package events;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public interface IEventProcessor
{
    default void onPlayerDeath(PlayerDeathEvent event) {}
    default void onPlayerOnWater(PlayerMoveEvent event) {}
    default void onPlayerToggleSneak(PlayerToggleSneakEvent event) {}
    default void onFall(EntityDamageEvent event) {}
    default void onPlayerItemConsume(PlayerItemConsumeEvent event) {}
    default void onEntityPickupItem(EntityPickupItemEvent event) {}
    default void onPlayerEggThrow(PlayerEggThrowEvent event) {}
    default void onEntityShootBow(EntityShootBowEvent event) {}
    default void onInventoryClick(InventoryClickEvent event) {}
    default void onEntityDamage(EntityDamageEvent event) {}
    default void onPlayerRespawn(PlayerRespawnEvent event) {}
    default void onItemDamage(PlayerItemDamageEvent event) {}
    default void onEntityDamageByUser(EntityDamageByEntityEvent event){}
    default void onEntityDamageByEntity(EntityDamageByEntityEvent event) {}
    default void onPlayerJoin(PlayerJoinEvent event) {}
    default void onPlayerMove(PlayerMoveEvent event) {}
    default void onEntityToggleGlide(EntityToggleGlideEvent event) {}
    default void onBlockBreak(BlockBreakEvent event) {}
    default void onProjectileHit(ProjectileHitEvent event) {}

    default void onPlayerInteract(PlayerInteractEvent event) {}

    default void onPlayerDropItem(PlayerDropItemEvent event) {}

    default void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {}
    default void onEntityDeath(EntityDeathEvent event) {}

    default void onEntityAirChange(EntityAirChangeEvent event) {}
}
