package castSystem;


import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import abilitieSystem.*;
import org.bukkit.scheduler.BukkitRunnable;
import skin.skinsChanger;

import org.bukkit.event.inventory.InventoryClickEvent;


import java.util.HashMap;
import java.util.Map;


/**
 * @brief Caster class. We monitorize here all the events that are related to the users abilities.
 * @author RedRiotTank, MiixZ, Vaelico786.
 */
public class caster implements Listener {
    private Map<String, abilityUser> users = new HashMap<>();
    private OPhabs plugin;

    /**
     * @brief Caster constructor.
     * @param plugin OPhabs plugin.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    public caster(coolDown cooldown, OPhabs plugin){
        this.plugin = plugin; 
        this.users = plugin.users;
    }

    /**
     * @brief Event that is triggered when a player interacts with a block.
     * This is where we allocated most of the caster abilities.
     * @param event PlayerInteractEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            Action action = event.getAction();
            if (event.getItem() != null && castIdentification.itemIsCaster(event.getItem(), event.getPlayer()) && user.hasFruit()) {

                String casterItemName = event.getItem().getItemMeta().getDisplayName();
                Material casterMaterial = event.getMaterial();

                if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)){
                    user.abilityActive();
                }


                //else
                //user.switchAbility();

            }
        }
    }

    /**
     * @brief Event that is triggered when a player drops an item.
     * This is where we change the ability of the caster.(Q case default)
     * @param event PlayerDropItemEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
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

    /**
     * @brief Event that is triggered when a entity receives damage.
     * @param event EntityDamageEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(users.containsKey(event.getEntity().getName())) {
            abilityUser user = users.get(event.getEntity().getName());
            user.onEntityDamage(event);
            if(event.getCause() == DamageCause.FALL)
                user.onFall(event);
        }
    }

    /**
     * @brief Event that is triggered when a entity receives damage from another entity.
     * @param event EntityDamageByEntityEven event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player &&  users.containsKey(event.getDamager().getName())) {
            abilityUser user = users.get(event.getDamager().getName());
            user.onEntityDamageByUser(event);
        }
        else{
            if(event.getEntity() instanceof Player && users.containsKey(event.getEntity().getName())) {
                abilityUser user = users.get(event.getEntity().getName());
                user.onUserDamageByEntity(event);
            }
        }

    }

    /**
     * @brief Event that is triggered when a  player toggles sneak (Shift default).
     * @param event PlayerToggleSneakEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e){
        if(users.containsKey(e.getPlayer().getName())) {
            abilityUser user = users.get(e.getPlayer().getName());
            user.onPlayerToggleSneak(e);
        }
    }

    /**
     * @brief Event that is triggered when a player toggles sneak (Shift default).
     * @param event PlayerToggleSneakEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */   
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onPlayerItemConsume(event);
        }
    }

    /**
     * @brief Event that is triggered when an entity picks up an item.
     * @param event EntityPickupItemEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */   
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event){
        if(users.containsKey(event.getEntity().getName())) {
            abilityUser user = users.get(event.getEntity().getName());
            user.onEntityPickupItem(event);
        }
    }

    /**
     * @brief Event that is triggered when an entity throws an egg.
     * @param event PlayerEggThrowEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event){
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onPlayerEggThrow(event);
        }
    }
 
    /**
     * @brief Event that is triggered when an entity shoots a bow.
     * @param event EntityShootBowEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event){
        if(users.containsKey(event.getEntity().getName())) {
            abilityUser user = users.get(event.getEntity().getName());
            user.onEntityShootBow(event);
        }
    }

    /**
     * @brief Event that is triggered when an entity change a block.
     * @param event EntityChangeBlockEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event){
        yami_yami.onEntityChangeBlock(event);
        bane_bane.onEntityChangeBlock(event);
    }
 
    /**
     * @brief Event that is triggered when a player click on an item with the mouse.
     * @param event InventoryClickEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(users.containsKey(event.getWhoClicked().getName())) {
            abilityUser user = users.get(event.getWhoClicked().getName());
            user.onInventoryClick(event);
        }
    }
  
    /**
     * @brief Event that is triggered when an entity breaks a block.
     * @param event BlockBreakEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        // Check if something breaks a block from room method and cancel it.
        ope_ope.roomBlockBreak(event);
    }

    /**
     * @brief Event that is triggered when a player respawns.
     * @param event PlayerRespawnEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        new BukkitRunnable() {
            @Override
            public void run() {
                skinsChanger.resetSkin(event.getPlayer());
            }
        }.runTaskLater(plugin, 5);

        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onPlayerRespawn(event);
        }
    }

    /**
     * @brief Event that is triggered when a player use a tool and 
     * decrease its durability
     * @param event PlayerItemDamageEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event){
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onItemDamage(event);
        }
    }
 
    /**
     * @brief Event that is triggered when a player toggles glide mode(Elytra).
     * @param event EntityToggleGlideEven event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(users.containsKey(player.getName())) {
                abilityUser user = users.get(player.getName());
                user.onEntityToggleGlide(event);
            }
        }
    }

    /**
     * @brief Event that is triggered when a player moves.
     * @param event PlayerMoveEvent event.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            user.onPlayerMove(event);
        }
    }
 
    /**
     * @brief Event that is triggered when a player joins to the server.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        String sukeUserName;
        Player sukeUser, other = event.getPlayer();

        sukeUserName = plugin.getConfig().getString("FruitAssociations.suke_suke");
        if(!sukeUserName.equals("none")) {
            sukeUser = Bukkit.getPlayer(sukeUserName);

            if(suke_suke.getInvisible())
                other.hidePlayer(plugin, sukeUser);
            else if(sukeUser != null)
                if(sukeUser.isOnline())
                    other.showPlayer(plugin, sukeUser);
        }
        if(users.containsKey(other.getName())) {
            abilityUser user = users.get(other.getName());
            user.onPlayerJoin(event);
        }
    }


    /**
     * @brief Event that is triggered when a player tries to put the caster on a frame.
     * @author RedRiotTank, MiixZ, Vaelico786.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block clickedBlock = event.getClickedBlock();
                
                if (event.getItem() != null && castIdentification.itemIsCaster(event.getItem(), event.getPlayer()) && user.hasFruit()) {
                    if (clickedBlock != null && clickedBlock.getType() == Material.ITEM_FRAME) {
                    event.setCancelled(true); // Cancelar la colocación del objeto en el item frame
                    }
                }
            }
        }
    }
    

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if(users.containsKey(event.getPlayer().getName())) {
            abilityUser user = users.get(event.getPlayer().getName());
            if (event.getRightClicked() instanceof ArmorStand) {
                ArmorStand armorStand = (ArmorStand) event.getRightClicked();
                Player player = event.getPlayer();
                
                //Comprobar si tienes i
                if (player.getInventory().getItemInMainHand() != null && castIdentification.itemIsCaster(player.getInventory().getItemInMainHand(), event.getPlayer()) && user.hasFruit()) {
                    event.setCancelled(true); // Cancelar la interacción del jugador con el ArmorStand
                }
            }
        }
    }
    
}

