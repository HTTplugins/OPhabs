package oldSystem.fruitSystem;

import oldSystem.htt.ophabs.*;
import oldSystem.castSystem.castIdentification;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.block.Biome;
//AttributeModifier
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import oldSystem.abilitieSystem.*;
import oldSystem.scoreboardSystem.abilitiesScoreboard;

/**
 * @brief Fruit association system [Player - Fruit].
 * @author RedRiotTank, Vaelico786.
 */
public class fruitAssociation implements Listener {
    private final OPhabs plugin;
    public  Map<String, abilityUser> dfPlayers = new HashMap<>(), users = new HashMap<>();
    public  Map<String, df> abilitiesM = new HashMap<>();
    public abilitiesScoreboard scoreboard = null;
    public ArrayList<df> abilitiesList = new ArrayList<>();

    /**
     * @brief Fruit assocaition constructor, links Players (who already have a fruit linked) and abilities on start.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public fruitAssociation(OPhabs plugin) {
        this.plugin = plugin;
        this.users = OPhabs.users;
        this.abilitiesList = OPhabs.abilitiesList;


        for (df ability : abilitiesList) {
            abilitiesM.put(ability.getFruit().getFruitName(), ability);
        }

        ArrayList<String> values = new ArrayList<>();
        String aux;
        for(int i = 0; i < abilitiesList.size(); i++) {
            aux = fileSystem.getFruitLinkedUser(abilitiesList.get(i).getName());
            if(aux == null) {
                fileSystem.updateFruitLinkedUser(abilitiesList.get(i).getName(),"none");
                aux = "none";
            }
            values.add(aux);
        }
        
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            if(!value.equals("none")) {
                addDevilFruitPlayer(value, abilitiesList.get(i));
            }
        }
    }

    /**
     * @brief Link a new player with a Devil fruit power.
     * @param name Name of the player.
     * @param fruit Devil fruit linked (creation).
     * @param ability ability of the Devil fruit to be linked with user.
     * @author Vaelico786.
     */
    public void addDevilFruitPlayer(String name, df ability) {
        abilityUser user = new abilityUser(name, plugin);
        user.setFruit(ability);
        dfPlayers.put(name, user);
        if(users.containsKey(name)){
            users.get(name).setFruit(ability);
        } else {
            users.put(name, user);
        }
    }

    /**
     * @brief Set scoreboard (Casting table) to a player.
     * @param scoreboard Scoreboard that's going to be set on the user.
     * @author Vaelico786.
     */
    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }

    /**
     * @brief Linkation of a Devil fruit power (ability) with a player when player eats the Devil Fruit.
     * @param event Scoreboard that's going to be set on the user.
     * @author RedRiotTank, Vaelico786.
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        ItemStack fruit = event.getItem();
        if(isFruit(fruit)){
            ArrayList<String> values = new ArrayList<>();
            for(df ability : abilitiesM.values()) {
                values.add(ability.getName());
            }
            String casterItemName = null;
            Boolean consumedFruit = false;
            df a = null; //Devil Fruit ability

            //iterate in all Names
            for(int i = 0; i < abilitiesList.size(); i++) {
                if(fruit.getItemMeta().getDisplayName().equals(abilitiesList.get(i).getFruit().getFruitName())) {
                    consumedFruit = consumedFruit(values.get(i),event);
                    values.set(i,event.getPlayer().getName());
                    casterItemName = abilitiesList.get(i).getCasterName();
                    a = abilitiesList.get(i);
                }
            }

            ItemStack caster = new ItemStack(castIdentification.castMaterial);
            ItemMeta casterItemMeta = caster.getItemMeta();
            casterItemMeta.setDisplayName(casterItemName);
        
            //give caster to player with attribute
            casterItemMeta.setCustomModelData(a.getFruit().getCustomModelDataId());

            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", a.getFruit().getCasterDamage(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            casterItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", a.getFruit().getCasterSpeed(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            casterItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);

            caster.setItemMeta(casterItemMeta);


            if(dfPlayers.containsKey(event.getPlayer().getName())){
                event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
                event.getPlayer().setHealth(0);
            } else{

                event.getPlayer().getInventory().addItem(caster);
                System.out.println("Player " + event.getPlayer().getName() + " has eaten a " + fruit.getItemMeta().getDisplayName() + " Size: " + abilitiesM.size());
                df ability = abilitiesM.get(fruit.getItemMeta().getDisplayName());

                addDevilFruitPlayer(event.getPlayer().getName(), ability);


                fileSystem.updateFruitLinkedUser(a.getName(),event.getPlayer().getName());
                plugin.saveConfig();
                scoreboard.addScoreboard(event.getPlayer().getName());
            }
        }
    }

    /**
     * @brief Checks if a devil fruit has alredy been consumed when a player eats it.
     * @param event eating something event.
     * @return true if the Devil Fruit has already been consumed, false if not.
     * @todo checkout this functionality.
     * @author RedRiotTank.
     */
    public Boolean consumedFruit(String value, PlayerItemConsumeEvent event){

        if(!value.equals("none")){
            event.getPlayer().sendMessage("This is an error, this fruit has alredy been consumed.");
            event.getPlayer().getInventory().removeItem(event.getItem());
            return true;

        } else
            return false;
    }

    /**
     * @brief Checks if a devil fruit player is in seawater.
     * @param player player to check.
     * @return true if the player is in seawater, false if not.
     * @author Vaelico786.
     */
    public boolean isInSeaWater(Player player) {
        boolean in = false;
        Biome biome = player.getLocation().getBlock().getBiome();
        if(biome.name().contains("OCEAN") || biome.name().contains("BEACH"))
              if(player.getLocation().getBlock().getType() == Material.WATER)
                  in = true;

        return in;
    }
    /**
     * @brief Checks if an item is a DevilFruit.
     * @param player player to check.
     * @return true if the player is in seawater, false if not.
     * @author Vaelico786.
     */
    public boolean isFruit(ItemStack fruit) {
        boolean Fruit = false;
            
        if(fruit.getType() == Material.APPLE && fruit.getItemMeta().getDisplayName().contains("no Mi") && fruit.getItemMeta().getCustomModelData() != 0)
            Fruit = true;
        return Fruit;
    }
    /**
     * @brief Nerfs a Devil fruit user if he is in seawater.
     * @param event Player in water event.
     * @author Vaelico786.
     */
    @EventHandler
    public void playerOnWater(PlayerMoveEvent event) {
        if (dfPlayers.containsKey(event.getPlayer().getName())) {
            if(isInSeaWater(event.getPlayer())) {
                Player player = event.getPlayer();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 100));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 1));
            }
        }
    }
}


