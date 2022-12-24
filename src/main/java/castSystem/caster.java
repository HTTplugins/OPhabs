package castSystem;

import fruitSystem.devilFruitUser;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import abilitieSystem.*;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;


import java.util.HashMap;
import java.util.Map;


public class caster implements Listener {
    private coolDown cooldown;
    private Map<String, devilFruitUser> dfPlayers = new HashMap<>();

    public static int yamiIndex, meraIndex, guraIndex, mokuIndex, nekoReoparudoIndex, maguIndex;

    public caster(coolDown cooldown, Map<String, devilFruitUser> dfPlayers){
        this.cooldown = cooldown;
        this.dfPlayers = dfPlayers;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(dfPlayers.containsKey(event.getPlayer().getName())) {
            devilFruitUser user = dfPlayers.get(event.getPlayer().getName());
            if (event.getHand().equals(EquipmentSlot.HAND))
                if (castIdentification.itemIsCaster(event.getItem())) {

                    String casterItemName = event.getItem().getItemMeta().getDisplayName();
                    Material casterMaterial = event.getMaterial();
                    Action action = event.getAction();

                    if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
                        // if(cooldown.getBlackVoidYamiCD() == 0) {
                        //     cooldown.setBlackVoidYamiCD(20);
                            user.abilityActive();
                        // }
                    }
                    else
                        user.switchAbility();


                        /*
                    if (casterItemName.equals(castIdentification.castItemNameYami) && casterMaterial.equals(castIdentification.castMaterialYami))
                        switch (yamiIndex) {
                            case 0:

                                if(cooldown.getBlackVoidYamiCD() == 0) {
                                    cooldown.setBlackVoidYamiCD(20);
                                    yamiClass.blackVoid(event.getPlayer());

                                }
                                break;
                            case 1:
                                if(cooldown.getAbilitie2YamiCD() == 0){
                                    cooldown.setAbilitie2YamiCD(20);
                                    yamiClass.livingVoid(event.getPlayer());

                                }

                                break;
                            case 2:
                                if(cooldown.getAbilitie3YamiCD() == 0){
                                    cooldown.setAbilitie3YamiCD(15);
                                    System.out.println("HABILIDAD3");
                                }

                                break;
                            case 3:
                                if(cooldown.getAbilitie4YamiCD() == 0){
                                    cooldown.setAbilitie4YamiCD(11);
                                    System.out.println("HABILIDAD4");
                                }

                                break;
                            default:
                                System.out.println("defaultswitch");
                                break;
                        }

                    if (casterItemName.equals(castIdentification.castItemNameMera) && casterMaterial.equals(castIdentification.castMaterialMera))
                        switch (meraIndex) {
                            case 0:
                                if (cooldown.getFirePoolCD() == 0) {
                                    meraClass.FirePool(event.getPlayer());
                                    cooldown.setFirePoolCD(20);
                                }
                                break;

                            case 1:
                                if (cooldown.getFireballStormCD() == 0) {
                                    meraClass.FireballStorm(event.getPlayer());
                                    cooldown.setFireballStormCD(5);
                                }
                                break;
                            case 2:
                                if(cooldown.getAbilitie3MeraCD() == 0){
                                    cooldown.setAbilitie3MeraCD(20);
                                    System.out.println("HABILIDAD3");
                                }
                                break;
                            case 3:
                                if(cooldown.getAbilitie4MeraCD() == 0){
                                    cooldown.setAbilitie4MeraCD(20);
                                    System.out.println("HABILIDAD4");
                                }

                                break;
                            default:
                                System.out.println("defaultswitch");
                                break;
                        }

                    if (casterItemName.equals(castIdentification.castItemNameMagu) && casterMaterial.equals(castIdentification.castMaterialMagu))
                        switch (maguIndex) {
                            default:
                                System.out.println("Magu Magu");
                                break;
                        }

                    if(casterItemName.equals(castIdentification.castItemNameGura) && casterMaterial.equals(castIdentification.castMaterialGura)){
                        switch (guraIndex){
                            case 0:
                                if(cooldown.getEarthquakeGuraCD() == 0){
                                    cooldown.setEarthquakeGuraCD(20);
                                    guraClass.earthquake(event.getPlayer());
                                }

                                break;
                            case 1:
                                if(cooldown.getCreateWaveGuraCD() == 0){
                                    cooldown.setCreateWaveGuraCD(20);
                                    guraClass.createWave(event.getPlayer());
                                }

                                break;
                            case 2:
                                if(cooldown.getHandVibrationGuraCD() == 0){
                                    cooldown.setHandVibrationGuraCD(20);
                                    guraClass.handVibration(event.getPlayer());
                                }

                                break;
                            case 3:
                                if(cooldown.getExpansionWaveGuraCD() == 0){
                                    cooldown.setExpansionWaveGuraCD(20);
                                    guraClass.expansionWaveBlocks(event.getPlayer());
                                }
                                break;

                               default:
                                   System.out.println("GURA GURA");
                                   break;
                            }

                        }

                    if(casterItemName.equals(castIdentification.castItemNameMoku) && casterMaterial.equals(castIdentification.castMaterialMoku)){
                        switch (mokuIndex){
                            case 0:
                                if(cooldown.getLogiaBodyMokuCD() == 0){
                                    cooldown.setLogiaBodyMokuCD(20);
                                    mokuClass.runParticles(event.getPlayer(), mokuClass.logiaBody(event.getPlayer()));
                                }
                                break;
                            case 1:
                                if(cooldown.getSmokeLegsMokuCD() == 0){
                                    cooldown.setSmokeLegsMokuCD(20);
                                    mokuClass.runParticles(event.getPlayer(), mokuClass.smokeLegs(event.getPlayer()));
                                }

                                break;
                            case 2:
                                if(cooldown.getSummonSmokerMokuCD() == 0){
                                    cooldown.setSummonSmokerMokuCD(20);
                                    mokuClass.summonSmoker(event.getPlayer());
                                }
                                break;
                            case 3:
                                if(cooldown.getAbilitie4MokuCD() == 0){
                                    cooldown.setAbilitie4MokuCD(20);
                                    System.out.println("HABILIDAD4");
                                }
                                break;

                            default:
                                System.out.println("MOKU MOKU");
                                break;
                        }
                    }
                    if(casterItemName.equals(castIdentification.castItemNameNekoReoparudo) && casterMaterial.equals(castIdentification.castMaterialNekoReoparudo)){
                        switch (nekoReoparudoIndex){
                            case 0:
                                if(cooldown.getTransformationNekoReoparudoCD() == 0){
                                    cooldown.setTransformationNekoReoparudoCD(20);
                                    nekoReoparudoClass.transformation();
                                }
                                break;
                            case 1:
                                if(cooldown.getFrontAttackNekoReoparudoCD() == 0){
                                    cooldown.setFrontAttack2NekoReoparudoCD(20);
                                    nekoReoparudoClass.frontAttack();
                                }

                                break;
                            case 2:
                                if(cooldown.getAbilitie3NekoReoparudoCD() == 0){
                                    cooldown.setAbilitie3NekoReoparudoCD(20);
                                    System.out.println("HABILIDAD3");
                                }
                                break;
                            case 3:
                                if(cooldown.getAbilitie4NekoReoparudoCD() == 0){
                                    cooldown.setAbilitie4NekoReoparudoCD(20);
                                    System.out.println("HABILIDAD4");
                                }
                                break;
                            default:
                                System.out.println("NEKO NEKO REOPARUDO");
                                break;
                        }
                    }
                    } else {

                    if(casterItemName.equals(castIdentification.castItemNameYami) && casterMaterial.equals(castIdentification.castMaterialYami)){
                        yamiIndex++;
                        yamiIndex = yamiIndex % abilitiesIdentification.aNumberYami;
                    }

                    if(casterItemName.equals(castIdentification.castItemNameMera) && casterMaterial.equals(castIdentification.castMaterialMera)){
                        meraIndex++;
                        meraIndex = meraIndex % abilitiesIdentification.aNumberMera;
                    }

                    if(casterItemName.equals(castIdentification.castItemNameGura) && casterMaterial.equals(castIdentification.castMaterialGura)){
                        guraIndex++;
                        guraIndex = guraIndex % abilitiesIdentification.aNumberGura;

                    }

                    if(casterItemName.equals(castIdentification.castItemNameMoku) && casterMaterial.equals(castIdentification.castMaterialMoku)){
                        mokuIndex++;
                        mokuIndex = mokuIndex % abilitiesIdentification.aNumberMoku;
                    }
                    if(casterItemName.equals(castIdentification.castItemNameNekoReoparudo) && casterMaterial.equals(castIdentification.castMaterialNekoReoparudo)){
                        nekoReoparudoIndex++;
                        nekoReoparudoIndex = nekoReoparudoIndex % abilitiesIdentification.aNumberNekoReoparudo;
                    }


                    }
                    */
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
    public void onPlayerDeath(PlayerDeathEvent event){
        if(dfPlayers.containsKey(event.getEntity().getName())) {
            devilFruitUser user = dfPlayers.get(event.getEntity().getName());
            user.onPlayerDeath(event);
        }
    }
/*
    @EventHandler
    public void playerOnWater(PlayerMoveEvent event){
        if(dfPlayers.containsKey(event.getPlayer().getName())) {
            devilFruitUser user = dfPlayers.get(event.getPlayer().getName());
            user.playerOnWater(event);
        }
    }
*/
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
        if(dfPlayers.containsKey(event.getEntity().getName())) {
            devilFruitUser user = dfPlayers.get(event.getEntity().getName());
            user.onEntityChangeBlock(event);
        }
    }

}
