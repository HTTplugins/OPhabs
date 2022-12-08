package commands;

import fruitSystem.devilFruit;
import fruitSystem.fruitIdentification;
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
        String order = args[0];
        String fruitCommandName = args[1];
        Player targetPlayer = Bukkit.getServer().getPlayerExact(args[2]);

        if(args.length == 3)
            if (order.equalsIgnoreCase("giveFruit"))
                if(fruitCommandName.equalsIgnoreCase(fruitIdentification.fruitCommandNameYami)
                        || fruitCommandName.equalsIgnoreCase(fruitIdentification.fruitCommandNameMera)
                        ||fruitCommandName.equalsIgnoreCase(fruitIdentification.fruitCommandNameGura)
                        || fruitCommandName.equalsIgnoreCase(fruitIdentification.fruitCommandNameMoku))
                    if( targetPlayer != null) {
                        if(plugin.getConfig().getString("FruitAssociations."+fruitCommandName).equals("none")){
                            devilFruit devFruit = new devilFruit(fruitCommandName);
                            devFruit.playerObtainFruit(targetPlayer);
                        } else {player.sendMessage("The fruits has alredy been consumed");}
                    }else player.sendMessage("Unkown player");
                else player.sendMessage("The fruit doesn't exist");
            else player.sendMessage("Parameters to giveFruit are <fruitCommandName> <PlayerName>");
        else player.sendMessage("Unknown command");

        return false;

    }

}
