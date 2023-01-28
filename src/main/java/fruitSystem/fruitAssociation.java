package fruitSystem;

import htt.ophabs.OPhabs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import castSystem.castIdentification;
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
import java.util.List;
import java.util.Map;
import abilitieSystem.*;
import scoreboardSystem.abilitiesScoreboard;

public class fruitAssociation implements Listener {
    private final OPhabs plugin;
    public static Map<String, devilFruitUser> dfPlayers = new HashMap<>();
    public static Map<String, abilities> abilitiesM = new HashMap<>();
    public abilitiesScoreboard scoreboard = null;
    public ArrayList<String> Names = new ArrayList<>();
    public ArrayList<abilities> abilityList = new ArrayList<>();
    public fruitAssociation(OPhabs plugin, ArrayList<abilities> abilitiesList) {
        this.plugin = plugin;

        for (abilities ability : abilitiesList) {
            abilitiesM.put(fruitIdentification.getItemName(ability.getName()), ability);
        }

        ArrayList<String> values = new ArrayList<>();
        for(int i = 0; i < abilitiesList.size(); i++) {
            values.add(plugin.getConfig().getString("FruitAssociations." + abilitiesList.get(i).getName()));
            Names.add(fruitIdentification.getItemName(abilitiesList.get(i).getName()));
        }
        
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            if(!values.equals("none")) {
               dfPlayers.put(value, new devilFruitUser(value, new devilFruit(value), abilitiesList.get(i)));
            }
        }
        abilityList = abilitiesList;
    }

    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){
        ItemStack fruit = event.getItem();
        if(fruitIdentification.isFruit(fruit.getItemMeta().getDisplayName())){
            ArrayList<String> values = new ArrayList<>();
            for(abilities ability : abilitiesM.values()) {
                values.add(ability.getName());
            }
            String casterItemName = null;
            Material castMaterial = Material.STICK;
            Boolean consumedFruit = false;
            abilities a = null;
            //iterate in all Names
            for(int i = 0; i < Names.size(); i++) {
                if(fruit.getItemMeta().getDisplayName().equals(Names.get(i))) {
                    consumedFruit = consumedFruit(values.get(i),event);
                    values.set(i,event.getPlayer().getName());
                    casterItemName = abilityList.get(i).getItemName();
                    castMaterial = abilityList.get(i).getMaterial();
                    a = abilityList.get(i);
                }
            }

            ItemStack caster = new ItemStack(castMaterial);
            ItemMeta casterItemMeta = caster.getItemMeta();
            casterItemMeta.setDisplayName(casterItemName);
    //give caster to player with attribute
            casterItemMeta.setCustomModelData(1);
            AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 6, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            casterItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            casterItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);

            caster.setItemMeta(casterItemMeta);
            event.getPlayer().getInventory().addItem(caster);
            if(dfPlayers.containsKey(event.getPlayer().getName())){
                event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
                event.getPlayer().setHealth(0);
            } else{
                abilities ability = abilitiesM.get(fruit.getItemMeta().getDisplayName());
                fruitIdentification fruitID = new fruitIdentification();
                devilFruitUser dfUser = new devilFruitUser(event.getPlayer().getName(), new devilFruit(fruitID.getCommandName(fruit.getItemMeta().getDisplayName())), ability);
                dfPlayers.put(event.getPlayer().getName(), dfUser);
                plugin.getConfig().set("FruitAssociations." + a.getName(), event.getPlayer().getName());
                plugin.saveConfig();
                scoreboard.addScoreboard(event.getPlayer().getName());
            }
        }
    }

    public Boolean consumedFruit(String value, PlayerItemConsumeEvent event){

        if(!value.equals("none")){
            event.getPlayer().sendMessage("This is an error, this fruit has alredy been consumed.");
            event.getPlayer().getInventory().removeItem(event.getItem());
            return true;

        } else
            return false;
    }

    public boolean isInSeaWater(Player player) {
        boolean in = false;
        Biome biome = player.getLocation().getBlock().getBiome();
        if(biome.name().contains("OCEAN") || biome.name().contains("BEACH"))
              if(player.getLocation().getBlock().getType() == Material.WATER)
                  in = true;


        return in;
    }
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

