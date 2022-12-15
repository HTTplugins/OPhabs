package castSystem;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import abilitieSystem.*;

public class caster implements Listener {
    private moku_moku mokuClass;
    private yami_yami yamiClass;
    private mera_mera meraClass;
    private gura_gura guraClass;
    private neko_neko_reoparudo nekoReoparudoClass;

    private int yamiIndex, meraIndex, guraIndex, mokuIndex, nekoReoparudoIndex;

    public caster(moku_moku mokuClass, yami_yami yamiClass, mera_mera meraClass, gura_gura guraClass, neko_neko_reoparudo nekoReoparudoClass){
        this.mokuClass = mokuClass;
        this.yamiClass = yamiClass;
        this.meraClass = meraClass;
        this.guraClass = guraClass;
        this.nekoReoparudoClass = nekoReoparudoClass;

        yamiIndex = meraIndex = guraIndex = mokuIndex = nekoReoparudoIndex= 0;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getHand().equals(EquipmentSlot.HAND))
            if (castIdentification.itemIsCaster(event.getItem())){

                String casterItemName = event.getItem().getItemMeta().getDisplayName();
                Material casterMaterial = event.getMaterial();
                Action action = event.getAction();

                if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {

                    if (casterItemName.equals(castIdentification.castItemNameYami) && casterMaterial.equals(castIdentification.castMaterialYami))
                        switch (yamiIndex) {
                            case 0:
                                yamiClass.blackVoid(event.getPlayer());
                                break;
                            case 1:
                                System.out.println("HABILIDAD2");
                                break;
                            default:
                                System.out.println("YAMI YAMI");
                                break;
                        }

                    if (casterItemName.equals(castIdentification.castItemNameMera) && casterMaterial.equals(castIdentification.castMaterialMera))
                        switch (meraIndex) {
                            default:
                                System.out.println("Mera Mera");
                                break;
                        }

                        if(casterItemName.equals(castIdentification.castItemNameGura) && casterMaterial.equals(castIdentification.castMaterialGura)){
                            switch (guraIndex){
                                case 0:
                                    guraClass.earthquake(event.getPlayer());
                                    break;
                                case 1:
                                    guraClass.createWave(event.getPlayer());
                                    break;
                                case 2:
                                    guraClass.handVibration(event.getPlayer());
                                    break;
                                case 3:
                                    guraClass.expansionWaveBlocks(event.getPlayer());
                                    break;
                                default:
                                    System.out.println("GURA GURA");
                                    event.getPlayer().sendMessage("GURA GURA");
                                    break;
                            }

                        }

                        if(casterItemName.equals(castIdentification.castItemNameMoku) && casterMaterial.equals(castIdentification.castMaterialMoku)){
                            switch (mokuIndex){
                                default:
                                    System.out.println("MOKU MOKU");
                                    break;
                                case 0:
                                    mokuClass.runParticles(event.getPlayer(), mokuClass.logiaBody(event.getPlayer()));
                                    break;
                                case 1:
                                    mokuClass.runParticles(event.getPlayer(), mokuClass.smokeLegs(event.getPlayer()));
                                    break;
                                case 2:
                                    mokuClass.summonSmoker(event.getPlayer());
                            }
                        }
                        if(casterItemName.equals(castIdentification.castItemNameNekoReoparudo) && casterMaterial.equals(castIdentification.castMaterialNekoReoparudo)){
                            switch (nekoReoparudoIndex){
                                default:
                                    System.out.println("NEKO NEKO REOPARUDO");
                                    break;
                                case 0:
                                    nekoReoparudoClass.transformation(event.getPlayer());
                                    break;
                                case 1:
                                    nekoReoparudoClass.frontAttack(event.getPlayer());
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
