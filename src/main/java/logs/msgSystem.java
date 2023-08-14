package logs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class msgSystem {

    private final static String prefix = ChatColor.GREEN + "[OPhabs]: ";
    private final static String errorPrefix = ChatColor.RED + "[OPhabs]: ";

    public static void OphConsoleMsg(String msg){
        Bukkit.getConsoleSender().sendMessage( prefix + ChatColor.WHITE +  msg);
    }

    public static void OphConsoleError(String msg){
        Bukkit.getConsoleSender().sendMessage( errorPrefix + ChatColor.WHITE +  msg);
    }

    public static void OphPlayerMsg(Player player, String msg){
        player.sendMessage(prefix + ChatColor.WHITE +  msg);
    }

    public static void OphPlayerError(Player player, String msg){
        player.sendMessage(errorPrefix + ChatColor.WHITE +  msg);
    }
}
