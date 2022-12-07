package castSystem;

import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import habilities.*;

import java.sql.SQLOutput;

import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;

public class caster implements Listener {
    OPhabs plugin;

    int yamiIndex = 0, meraIndex = 0, guraIndex = 0, mokuIndex = 0;

    public caster(OPhabs plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {


            if(e.getHand().equals(EquipmentSlot.HAND)){
                if(castIdentification.itemIsCaster(e.getItem())){

                    String casterItemName = e.getItem().getItemMeta().getDisplayName();
                    Material casterMaterial = e.getMaterial();

                    if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
                        if(casterItemName.equals(castIdentification.castItemNameYami) && casterMaterial.equals(castIdentification.castMaterialYami)){
                            yami_yami yamiClass = new yami_yami(plugin);
                            switch (yamiIndex){
                                case 0:

                                     yamiClass.blackVoid(e.getPlayer());
                                    break;
                                case 1:
                                    System.out.println("HABILIDAD2");
                                    break;
                                default:
                                    System.out.println("YAMI YAMI");
                                    break;
                            }

                        }

                        if(casterItemName.equals(castIdentification.castItemNameMera) && casterMaterial.equals(castIdentification.castMaterialMera)){
                            switch (meraIndex){
                                default:
                                    System.out.println("Mera Mera");
                                    break;
                            }

                        }

                        if(casterItemName.equals(castIdentification.castItemNameGura) && casterMaterial.equals(castIdentification.castMaterialGura)){
                            switch (guraIndex){
                                default:
                                    System.out.println("GURA GURA");
                                    break;
                            }

                        }

                        if(casterItemName.equals(castIdentification.castItemNameMoku) && casterMaterial.equals(castIdentification.castMaterialMoku)){
                            switch (mokuIndex){
                                default:
                                    System.out.println("MOKU MOKU");
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
                            mokuIndex = mokuIndex % abilitiesIdentification.anumberMoku;
                        }


                    }




                }
            }



    }


}
