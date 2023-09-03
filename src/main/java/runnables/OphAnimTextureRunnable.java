package runnables;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import static libs.OPHLib.generateCustomFloatingItem;

public abstract class OphAnimTextureRunnable extends OphRunnable{
    ItemStack item;
    ArmorStand armorStand;
    Vector aux;
    double distanceXTicks;
    double distanceFromOrigin = 0;
    Vector direction = null;
    Location origin;

    public OphAnimTextureRunnable(Material itemMaterial, int customModel, Location startPoint, Vector direction, double distanceXTicks){
        super();
        this.item = new ItemStack(itemMaterial);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(customModel);
        this.item.setItemMeta(itemMeta);
        this.distanceXTicks = distanceXTicks;
        this.armorStand = generateCustomFloatingItem(startPoint, item, false);

        this.direction = direction;
        origin = startPoint;

    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public void run(){


        distanceFromOrigin +=distanceXTicks;
        Vector movement = direction.clone().multiply(distanceFromOrigin);
        armorStand.teleport(origin.clone().add(movement));

        aux = new Vector((armorStand.getLocation().getX()-origin.getX()), (armorStand.getLocation().getY()-origin.getY()), (armorStand.getLocation().getZ()-origin.getZ()));

        particleAnimation();

        if (aux.length() < 0.5 && getCurrentRunIteration()>10 || getCurrentRunIteration() > 20) {
            armorStand.remove();
            ophCancel();
            return;
        }

        super.run();
    }

    public abstract void particleAnimation();

}
