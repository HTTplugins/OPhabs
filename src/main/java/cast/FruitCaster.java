package cast;

import htt.customent.api.CustomEntAPI;
import htt.ophabs.OPhabs;
import textures.TextureMapper;
import textures.OPTexture;
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
    private boolean animationPlaying = false;

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
        // Stop any running animation
        stopAnimation();

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
                if(eventAction == Action.RIGHT_CLICK_AIR && !fruit.getUser().isFastCasting()){
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
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
        if(!isCombatMode()){
            Player player = event.getPlayer();

            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            ItemStack itemInSlot9 = player.getInventory().getItem(8); // Posición 9 (se cuentan desde 0)

            player.getInventory().setItemInMainHand(itemInSlot9); // Coloca el objeto en la mano
            player.getInventory().setItem(8, itemInHand); // Coloca el objeto en la posición 9
            player.getInventory().setHeldItemSlot(8);
        }

        setCombatMode(!isCombatMode());

        // Toggle animation on combat mode change
        if (isCombatMode()) {
            startAnimation();
        } else {
            stopAnimation();
        }
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

    /**
     * Start the item animation for this fruit caster
     */
    public void startAnimation() {
        if (animationPlaying || fruit.getUser() == null) return;
        
        Player player = fruit.getUser().getPlayer();
        if (player == null || !player.isOnline()) return;

        // Get the caster item in hand
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!isThisItem(itemInHand)) {
            // Check offhand
            itemInHand = player.getInventory().getItemInOffHand();
            if (!isThisItem(itemInHand)) return;
        }

        // Get animation frames from texture mapper
        int[] frames = getAnimationFrames();
        if (frames == null || frames.length == 0) return;

        // Start animation via CustomEntAPI
        if (CustomEntAPI.isInitialized()) {
            CustomEntAPI.animateItem(player, itemInHand, frames, 5, true); // 5 ticks per frame, looping
            animationPlaying = true;
        }
    }

    /**
     * Stop the item animation
     */
    public void stopAnimation() {
        if (!animationPlaying) return;
        
        Player player = fruit.getUser() != null ? fruit.getUser().getPlayer() : null;
        if (player != null && CustomEntAPI.isInitialized()) {
            CustomEntAPI.stopAnimation(player);
        }
        animationPlaying = false;
    }

    /**
     * Get animation frames for this fruit from texture mapper
     * Returns array of CustomModelData values for each frame
     */
    private int[] getAnimationFrames() {
        // For now, use the remapped IDs as animation frames
        // This can be extended to support custom frame sequences per fruit
        var remappedIds = OPhabs.textureMapper.getRemappedIDs(fruit.getID());
        if (remappedIds.isEmpty()) return new int[0];
        
        // Include base ID as first frame
        int[] frames = new int[remappedIds.size() + 1];
        frames[0] = fruit.getID();
        int i = 1;
        for (int id : remappedIds) {
            frames[i++] = id;
        }
        return frames;
    }
}
