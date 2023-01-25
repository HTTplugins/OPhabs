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
import scoreboardSystem.abilitiesScoreboard;

public class fruitAssociation implements Listener {
    private final OPhabs plugin;
    public static Map<String, devilFruitUser> dfPlayers = new HashMap<>();
    public static Map<String, abilities> abilities = new HashMap<>();
    public abilitiesScoreboard scoreboard = null;

    public fruitAssociation(OPhabs plugin, yami_yami yamiClass, mera_mera meraClass, gura_gura guraClass, moku_moku mokuClass, neko_neko_reoparudo nekoReoparudoClass, magu_magu maguClass, goro_goro goroClass, ishi_ishi ishiClass, goru_goru goruClass){
        this.plugin = plugin;
        abilities.put(fruitIdentification.fruitItemNameGura,guraClass);
        abilities.put(fruitIdentification.fruitItemNameMera,meraClass);
        abilities.put(fruitIdentification.fruitItemNameYami,yamiClass);
        abilities.put(fruitIdentification.fruitItemNameMoku,mokuClass);
        abilities.put(fruitIdentification.fruitItemNameNekoReoparudo,nekoReoparudoClass);
        abilities.put(fruitIdentification.fruitItemNameMagu,maguClass);
        abilities.put(fruitIdentification.fruitItemNameGoro,goroClass);
        abilities.put(fruitIdentification.fruitItemNameIshi,ishiClass);
        abilities.put(fruitIdentification.fruitItemNameGoru,goruClass);

    String
            yamiValue = plugin.getConfig().getString("FruitAssociations.yami_yami"),
            meraValue = plugin.getConfig().getString("FruitAssociations.mera_mera"),
            guraValue = plugin.getConfig().getString("FruitAssociations.gura_gura"),
            mokuValue = plugin.getConfig().getString("FruitAssociations.moku_moku"),
            nekoReoparudoValue = plugin.getConfig().getString("FruitAssociations.neko_neko_reoparudo"),
            maguValue = plugin.getConfig().getString("FruitAssociations.magu_magu"),
            goroValue = plugin.getConfig().getString("FruitAssociations.goro_goro"),
            ishiValue = plugin.getConfig().getString("FruitAssociations.ishi_ishi"),
            goruValue = plugin.getConfig().getString("FruitAssociations.goru_goru");



        if(!yamiValue.equals("none")){
           dfPlayers.put(yamiValue, new devilFruitUser(yamiValue, new devilFruit(fruitIdentification.fruitCommandNameYami), yamiClass));
        }
        if(!meraValue.equals("none")){
            dfPlayers.put(meraValue, new devilFruitUser(meraValue, new devilFruit(fruitIdentification.fruitCommandNameMera), meraClass));
        }
        if(!guraValue.equals("none")){
            dfPlayers.put(guraValue, new devilFruitUser(guraValue, new devilFruit(fruitIdentification.fruitCommandNameGura), guraClass));
        }
        if(!mokuValue.equals("none")){
            dfPlayers.put(mokuValue, new devilFruitUser(mokuValue, new devilFruit(fruitIdentification.fruitCommandNameMoku), mokuClass));
        }
        if(!nekoReoparudoValue.equals("none")){
            dfPlayers.put(nekoReoparudoValue, new devilFruitUser(nekoReoparudoValue, new devilFruit(fruitIdentification.fruitCommandNameNekoReoparudo), nekoReoparudoClass));
        }
        if(!maguValue.equals("none")){
            dfPlayers.put(maguValue, new devilFruitUser(maguValue, new devilFruit(fruitIdentification.fruitCommandNameMagu), maguClass));
        }

        if(!goroValue.equals("none")){
            dfPlayers.put(goroValue,new devilFruitUser(goroValue, new devilFruit(fruitIdentification.fruitCommandNameGoro), goroClass));
        }

        if(!ishiValue.equals("none")){
            dfPlayers.put(ishiValue,new devilFruitUser(ishiValue, new devilFruit(fruitIdentification.fruitCommandNameIshi), ishiClass));
        }

        if(!goruValue.equals("none")){
            dfPlayers.put(goruValue,new devilFruitUser(goruValue, new devilFruit(fruitIdentification.fruitCommandNameGoru), goruClass));
        }
    }

    public void setScoreboard(abilitiesScoreboard scoreboard){
        this.scoreboard = scoreboard;
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
                maguValue = plugin.getConfig().getString("FruitAssociations.magu_magu"),
                goroValue = plugin.getConfig().getString("FruitAssociations.goro_goro"),
                ishiValue = plugin.getConfig().getString("FruitAssociations.ishi_ishi"),
                goruValue = plugin.getConfig().getString("FruitAssociations.goru_goru");

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

            case fruitIdentification.fruitItemNameMagu:
                consumedFruit = consumedFruit(maguValue, event);
                maguValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameMagu;
                castMaterial = castIdentification.castMaterialMagu;
                break;
            case fruitIdentification.fruitItemNameGoro:
                consumedFruit = consumedFruit(goroValue,event);
                goroValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameGoro;
                castMaterial = castIdentification.castMaterialGoro;
                break;
            case fruitIdentification.fruitItemNameIshi:
                consumedFruit = consumedFruit(ishiValue,event);
                ishiValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameIshi;
                castMaterial = castIdentification.castMaterialIshi;
                break;
            case fruitIdentification.fruitItemNameGoru:
                consumedFruit = consumedFruit(goruValue,event);
                goruValue = event.getPlayer().getName();
                casterItemName = castIdentification.castItemNameGoru;
                castMaterial = castIdentification.castMaterialGoru;
                break;

            default:
                break;
        }

        if(!consumedFruit){
            plugin.getConfig().set("FruitAssociations.yami_yami",yamiValue);
            plugin.getConfig().set("FruitAssociations.mera_mera",meraValue);
            plugin.getConfig().set("FruitAssociations.gura_gura",guraValue);
            plugin.getConfig().set("FruitAssociations.moku_moku",mokuValue);
            plugin.getConfig().set("FruitAssociations.neko_neko_reoparudo",nekoReoparudoValue);
            plugin.getConfig().set("FruitAssociations.magu_magu",maguValue);
            plugin.getConfig().set("FruitAssociations.goro_goro",goroValue);
            plugin.getConfig().set("FruitAssociations.ishi_ishi",ishiValue);
            plugin.getConfig().set("FruitAssociations.goru_goru",goruValue);
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
            fruitIdentification fruitID = new fruitIdentification();
            devilFruitUser dfUser = new devilFruitUser(event.getPlayer().getName(), new devilFruit(fruitID.getCommandName(fruit.getItemMeta().getDisplayName())), ability);
            dfPlayers.put(event.getPlayer().getName(), dfUser);
            scoreboard.addScoreboard(event.getPlayer());
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

