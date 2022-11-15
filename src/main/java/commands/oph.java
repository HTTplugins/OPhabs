package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import htt.ophabs.*;
import org.bukkit.entity.Player;

public class oph implements CommandExecutor {
    private  final OPhabs plugin;
    public oph(OPhabs plugin){this.plugin = plugin;}
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;

        if(args.length > 0) {
            if (args[0].equalsIgnoreCase("test"))
                player.sendMessage("oph working");
            else
                printHelp(player);

        } else {
            printHelp(player);
        }

        return false;
    }

    public void printHelp(Player player){
        player.sendMessage("Ayuda");
    }
}
