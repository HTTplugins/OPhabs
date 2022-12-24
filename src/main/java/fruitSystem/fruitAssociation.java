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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import abilitieSystem.*;

public class fruitAssociation implements Listener {
    private final OPhabs plugin;
    public static Map<String, devilFruitUser> dfPlayers = new HashMap<>();
    public static Map<String, abilities> abilities = new HashMap<>();

    public fruitAssociation(OPhabs plugin){
        this.plugin = plugin;
        abilities.put(fruitIdentification.fruitItemNameGura,new gura_gura(plugin));/*
        abilities.put(fruitIdentification.fruitItemNameMagu,new magu_magu(plugin));
        abilities.put(fruitIdentification.fruitItemNameMera,new mera_mera(plugin));
        abilities.put(fruitIdentification.fruitItemNameMoku,new moku_moku(plugin));
        abilities.put(fruitIdentification.fruitItemNameYami,new yami_yami(plugin));
        abilities.put(fruitIdentification.fruitItemNameNekoReoparudo,new neko_neko_reoparudo(plugin));*/
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event){

       
        ItemStack fruit = event.getItem();

        String
                yamiValue = plugin.getConfig().getString("FruitAssociations.yami_yami"),
                meraValue = plugin.getConfig().getString("FruitAssociations.mera_mera"),
                guraValue = plugin.getConfig().getString("FruitAssociations.gura_gura"),
                mokuValue = plugin.getConfig().getString("FruitAssociations.moku_moku"),
                nekoReoparudoValue = plugin.getConfig().getString("FruitAssociations.neko_neko_reoparudo"),
                maguValue = plugin.getConfig().getString("FruitAssociations.magu_magu");

        String casterItemName = null;
        Material castMaterial = null;
        Boolean consumedFruit = false;

        switch (fruit.getItemMeta().getDisplayName()){

            case fruitIdentification.fruitItemNameYami:
                consumedFruit = consumedFruit(yamiValue,event);
                yamiValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameYami;
                castMaterial = castIdentification.castMaterialYami;
                break;

            case fruitIdentification.fruitItemNameMera:
                consumedFruit = consumedFruit(meraValue,event);
                meraValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameMera;
                castMaterial = castIdentification.castMaterialMera;
                break;

            case fruitIdentification.fruitItemNameGura:
                consumedFruit= consumedFruit(guraValue,event);
                guraValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameGura;
                castMaterial = castIdentification.castMaterialGura;
                break;

            case fruitIdentification.fruitItemNameMoku:
                consumedFruit = consumedFruit(mokuValue,event);
                mokuValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameMoku;
                castMaterial = castIdentification.castMaterialMoku;
                break;

            case fruitIdentification.fruitItemNameNekoReoparudo:
                consumedFruit = consumedFruit(nekoReoparudoValue,event);
                nekoReoparudoValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameNekoReoparudo;
                castMaterial = castIdentification.castMaterialNekoReoparudo;
                break;

            case fruitIdentification.fruitCommandNameMagu:
                consumedFruit = consumedFruit(maguValue, event);
                maguValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameMagu;
                castMaterial = castIdentification.castMaterialMagu;

            default:
                break;
        }

        if(!consumedFruit){
            plugin.getConfig().set("FruitAssociations.yami_yami",yamiValue);
            plugin.getConfig().set("FruitAssociations.mera_mera",meraValue);
            plugin.getConfig().set("FruitAssociations.gura_gura",guraValue);
            plugin.getConfig().set("FruitAssociations.moku_moku",mokuValue);
            plugin.getConfig().set("FruitAssociations.neko_neko_reoparudo",nekoReoparudoValue);
            plugin.saveConfig();
        }

        ItemStack caster = new ItemStack(castMaterial);
        ItemMeta casterItemMeta = caster.getItemMeta();
        casterItemMeta.setDisplayName(casterItemName);
        caster.setItemMeta(casterItemMeta);
        event.getPlayer().getInventory().addItem(caster);
        if(dfPlayers.containsKey(event.getPlayer().getName())){
            event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
            event.getPlayer().setHealth(0);
        } else{
            abilities ability = abilities.get(fruit.getItemMeta().getDisplayName());
            devilFruitUser dfUser = new devilFruitUser(event.getPlayer(), new devilFruit(fruit.getItemMeta().getDisplayName()), ability);
            dfPlayers.put(event.getPlayer().getName(), dfUser);
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

