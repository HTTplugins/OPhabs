package castSystem;

import fruitSystem.devilFruitUser;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import abilitieSystem.*;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;


import java.util.HashMap;
import java.util.Map;


public class caster implements Listener {
    private Map<String, devilFruitUser> dfPlayers = new HashMap<>();

    public caster(coolDown cooldown, Map<String, devilFruitUser> dfPlayers){
        this.dfPlayers = dfPlayers;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(dfPlayers.containsKey(event.getPlayer().getName())) {
            devilFruitUser user = dfPlayers.get(event.getPlayer().getName());
                if (event.getItem() != null && castIdentification.itemIsCaster(event.getItem(), event.getPlayer())) {

                    String casterItemName = event.getItem().getItemMeta().getDisplayName();
                    Material casterMaterial = event.getMaterial();
                    Action action = event.getAction();

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

            if(dfPlayers.containsKey(event.getPlayer().getName())) {
                devilFruitUser user = dfPlayers.get(event.getPlayer().getName());
                user.switchAbility();
            }
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(dfPlayers.containsKey(event.getEntity().getName())) {
            devilFruitUser user = dfPlayers.get(event.getEntity().getName());
            user.onEntityDamage(event);
            user.onFall(event);
        }
    }
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){
        if(dfPlayers.containsKey(e.getPlayer().getName())) {
            devilFruitUser user = dfPlayers.get(e.getPlayer().getName());
            user.onPlayerToggleSneak(e);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        if(dfPlayers.containsKey(event.getPlayer().getName())) {
            devilFruitUser user = dfPlayers.get(event.getPlayer().getName());
            user.onPlayerItemConsume(event);
        }
    }
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event){
        if(dfPlayers.containsKey(event.getEntity().getName())) {
            devilFruitUser user = dfPlayers.get(event.getEntity().getName());
            user.onEntityPickupItem(event);
        }
    }
    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event){
        if(dfPlayers.containsKey(event.getPlayer().getName())) {
            devilFruitUser user = dfPlayers.get(event.getPlayer().getName());
            user.onPlayerEggThrow(event);
        }
    }
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event){
        if(dfPlayers.containsKey(event.getEntity().getName())) {
            devilFruitUser user = dfPlayers.get(event.getEntity().getName());
            user.onEntityShootBow(event);
        }
    }
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event){
        yami_yami.onEntityChangeBlock(event);

    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(dfPlayers.containsKey(event.getWhoClicked().getName())) {
            devilFruitUser user = dfPlayers.get(event.getWhoClicked().getName());
            user.onInventoryClick(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        ope_ope.onBlockBreak(event);

    }
}
