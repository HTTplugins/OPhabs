package skin;

import org.bukkit.Bukkit;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import cosmetics.cosmeticsArmor;
import runnables.OphRunnable;


public final class skinsChanger {
    public static void setSkin(String name, String skinUrl) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sr createcustom " + name + " " + skinUrl);
    }

    public static void changeSkin(Player player, String skinName) {
        String name = player.getName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + name + " " + skinName);

        new OphRunnable() {
            @Override
            public void OphRun(){
                cosmeticsArmor.reloadCosmeticArmor(player);
            }
        }.ophRunTaskLater(5);
    }

    public static void changeSkinSoldiers(Zombie zombie, String skinName) {
        String name = zombie.getName();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + name + " " + skinName);

        // new OphRunnable() {
        //     @Override
        //     public void OphRun(){
        //         cosmeticsArmor.reloadCosmeticArmor(Bukkit.getPlayer(name));
        //     }
        // }.ophRunTaskLater(5);
    }

    public static void resetSkin(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skin set " + player.getName() + " " + player.getName().toLowerCase());

        new OphRunnable() {
            @Override
            public void OphRun(){
                cosmeticsArmor.reloadCosmeticArmor(player);
            }
        }.ophRunTaskLater(5);
    }
}
