package castSystem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import abilitieSystem.*;


public class caster implements Listener {
    private coolDown cooldown;
    private moku_moku mokuClass;
    private yami_yami yamiClass;
    private mera_mera meraClass;
    private gura_gura guraClass;
    private neko_neko_reoparudo nekoReoparudoClass;
    private magu_magu maguClass;

    public static int yamiIndex, meraIndex, guraIndex, mokuIndex, nekoReoparudoIndex, maguIndex;

    public caster(coolDown cooldown, moku_moku mokuClass, yami_yami yamiClass, mera_mera meraClass, gura_gura guraClass, neko_neko_reoparudo nekoReoparudoClass,
                  magu_magu maguClass){
        this.cooldown = cooldown;
        this.mokuClass = mokuClass;
        this.yamiClass = yamiClass;
        this.meraClass = meraClass;
        this.guraClass = guraClass;
        this.nekoReoparudoClass = nekoReoparudoClass;
        this.maguClass = maguClass;

        yamiIndex = meraIndex = guraIndex = mokuIndex = nekoReoparudoIndex = maguIndex= 0;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getHand().equals(EquipmentSlot.HAND))
            if (castIdentification.itemIsCaster(event.getItem())){

                String casterItemName = event.getItem().getItemMeta().getDisplayName();
                Material casterMaterial = event.getMaterial();
                Action action = event.getAction();

                if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)){

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
                                    cooldown.setAbilitie2YamiCD(0); //a 0 pq estoy probando cosas
                                    yamiClass.ab2(event.getPlayer());
                                    System.out.println("HABILIDAD2");
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
                            case 0: meraClass.FirePool(event.getPlayer()); break;

                            case 1: meraClass.FireballStorm(event.getPlayer()); break;
                            case 2:
                                System.out.println("HABILIDAD3");
                                break;
                            case 3:
                                System.out.println("HABILIDAD4");
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
                                    nekoReoparudoClass.transformation(event.getPlayer());
                                }
                                break;
                            case 1:
                                if(cooldown.getFrontAttackNekoReoparudoCD() == 0){
                                    cooldown.setFrontAttack2NekoReoparudoCD(20);
                                    nekoReoparudoClass.frontAttack(event.getPlayer());
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
}
    }
}
