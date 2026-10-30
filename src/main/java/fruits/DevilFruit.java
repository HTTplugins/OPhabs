package fruits;

import users.OPUser;
import abilities.AbilitySet;
import events.IEventProcessor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.ArrayList;

public abstract class DevilFruit implements IEventProcessor
{
    private final int id;
    protected String name;
    protected String displayName;
    protected String commandName;
    protected ArrayList<AbilitySet> abilitySets = new ArrayList<>();
    protected OPUser user;

    // Determina si los poderes están activos (si está en el agua / es golpeado por k no)
    protected boolean isFruitActive = true;

    public static int getFruitID()
    {
        return 1000;
    }

    public DevilFruit(int id, String name, String displayName, String commandName)
    {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.commandName = commandName;
    }

    // Invocado tras establecer un usuario nuevo
    public abstract void onAddFruit();

    // Invocado antes de eliminar al usuario actual
    public abstract void onRemoveFruit();

    // Permite cambiar el usuario de la fruta. Llama automáticamente a los eventos de registro de fruta.
    // Además, se puede utilizar para robar frutas.
    public void setUser(OPUser user)
    {
        if (user == null)
            this.onRemoveFruit();
        else if (this.user != null && !this.user.equals(user))
            this.onRemoveFruit();

        if (user != null)
            this.onAddFruit();

        this.user = user;
    }

    public OPUser getUser()
    {
        return this.user;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public String getCommandName()
    {
        return this.commandName;
    }

    public int getID()
    {
        return this.id;
    }

    public boolean isFruitActive()
    {
        return isFruitActive;
    }

    public void invokeAbility(int abilitySetID, int abilityID)
    {
        if (abilitySetID < 0 || abilitySetID >= this.abilitySets.size() || !isFruitActive)
            return;

        this.abilitySets.get(abilitySetID).invokeAbility(abilityID);
    }

    public ArrayList<AbilitySet> getAbilitySets()
    {
        return this.abilitySets;
    }

    // Llamado cuando el jugador se mueve - verificar agua
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) return;

        // Verificar si el jugador está en agua (nadando o sumergido)
        boolean inWater = isInWater(player);

        if (inWater && isFruitActive) {
            // Desactivar poderes en el agua
            setFruitActive(false);
            applyWaterDebuff(player);
        } else if (!inWater && !isFruitActive) {
            // Reactivar poderes fuera del agua
            setFruitActive(true);
            removeWaterDebuff(player);
        }
    }

    // Inventory event handlers
    public void onInventoryClick(InventoryClickEvent event) {}

    public void onInventoryDrag(InventoryDragEvent event) {}

    protected boolean isInWater(Player player) {
        Location loc = player.getLocation();
        Block block = loc.getBlock();
        Block headBlock = loc.clone().add(0, player.getEyeHeight(), 0).getBlock();

        return block.getType() == Material.WATER || 
               headBlock.getType() == Material.WATER ||
               player.isSwimming();
    }

    protected void setFruitActive(boolean active) {
        this.isFruitActive = active;
    }

    protected void applyWaterDebuff(Player player) {
        // Efectos de debuff en el agua: lentitud, debilidad, no puede usar habilidades
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 2, false, false));
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 2, false, false));
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(
            org.bukkit.potion.PotionEffectType.MINING_FATIGUE, Integer.MAX_VALUE, 2, false, false));
    }

    protected void removeWaterDebuff(Player player) {
        player.removePotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS);
        player.removePotionEffect(org.bukkit.potion.PotionEffectType.WEAKNESS);
        player.removePotionEffect(org.bukkit.potion.PotionEffectType.MINING_FATIGUE);
    }

}
