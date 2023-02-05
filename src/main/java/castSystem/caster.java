package castSystem;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import abilitieSystem.*;
import skin.skinsChanger;

import org.bukkit.event.inventory.InventoryClickEvent;


import java.util.HashMap;
import java.util.Map;


public class caster implements Listener {
    private Map<String, abilityUser> users = new HashMap<>();

    public caster(coolDown cooldown, Map<String, abilityUser> users){
        this.users = users;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            Action action = event.getAction();
            if (event.getItem() != null && castIdentification.itemIsCaster(event.getItem(), event.getPlayer()) && user.hasFruit()) {

                String casterItemName = event.getItem().getItemMeta().getDisplayName();
                Material casterMaterial = event.getMaterial();

                if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK))
                    user.abilityActive();


                //else
                //user.switchAbility();

            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if(castIdentification.itemIsCaster(event.getItemDrop().getItemStack(), event.getPlayer())){
            event.setCancelled(true);

            if(users.containsKey(event.getPlayer().getName())) {
                abilityUser user = users.get(event.getPlayer().getName());
                user.switchAbility();
            }
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(users.containsKey(event.getEntity().getName())) {
            abilityUser user = users.get(event.getEntity().getName());
            user.onEntityDamage(event);
            user.onFall(event);
        }
    }
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player &&  users.containsKey(event.getDamager().getName())) {
            abilityUser user = users.get(event.getDamager().getName());
            user.onUserDamageAnotherEntity(event);
        }
    }
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){
        if(users.containsKey(e.getPlayer().getName())) {
            abilityUser user = users.get(e.getPlayer().getName());
            user.onPlayerToggleSneak(e);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onPlayerItemConsume(event);
        }
    }
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event){
        if(users.containsKey(event.getEntity().getName())) {
            abilityUser user = users.get(event.getEntity().getName());
            user.onEntityPickupItem(event);
        }
    }
    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event){
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onPlayerEggThrow(event);
        }
    }
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event){
        if(users.containsKey(event.getEntity().getName())) {
            abilityUser user = users.get(event.getEntity().getName());
            user.onEntityShootBow(event);
        }
    }
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event){
        yami_yami.onEntityChangeBlock(event);

    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(users.containsKey(event.getWhoClicked().getName())) {
            abilityUser user = users.get(event.getWhoClicked().getName());
            user.onInventoryClick(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        ope_ope.onBlockBreak(event);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        skinsChanger.resetSkin(event.getPlayer());
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onPlayerRespawn(event);
        }
    }

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event){
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onItemDamage(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        zushi_zushi.onEntityToggleGlide(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        zushi_zushi.onPlayerMove(event);

    }
}
