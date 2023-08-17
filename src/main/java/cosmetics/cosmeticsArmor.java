package cosmetics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class cosmeticsArmor {
    public static void summonCosmeticArmor(int id, String name, Player player, Material material){
        System.out.println("summonent " +  player.getName() + " " + id + " " + name  + " " + material.toString());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summonent " +  player.getName() + " " + id + " " + name  + " " + material.toString());
    }

    public static void summonCosmeticArmor(int id, String name, Player player, Material material, int idAnimationMax,int tickPerFrame){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summonent " +  player.getName() + " " + id + " " + name  + " " + material.toString() + " " + idAnimationMax + " " + tickPerFrame);
    }

    public static void summonCosmeticArmor(int id, String name, Player player, Material material, int idAnimationMax,int tickPerFrame, boolean cyclic){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summonent " +  player.getName() + " " + id + " " + name  + " " + material.toString() + " " + idAnimationMax + " " + tickPerFrame + " " + cyclic);
    }
    public static void reloadCosmeticArmor(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reloadent " + player.getName());
    }

    public static void reloadCosmeticArmor(Player player, String name) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reloadent " + player.getName() + " " + name);
    }

    public static void killCosmeticArmor(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killent " + player.getName());
    }

    public static void killCosmeticArmor(Player player, String name) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "killent " + player.getName() + " " + name);
    }
}
