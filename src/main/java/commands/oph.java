package commands;

import fruitSystem.devilFruit;
import fruitSystem.fruitIdentification;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import htt.ophabs.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class oph implements CommandExecutor, TabCompleter {
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
                        || fruitCommandName.equalsIgnoreCase(fruitIdentification.fruitCommandNameMoku)
                        || fruitCommandName.equalsIgnoreCase(fruitIdentification.fruitCommandNameNekoReoparudo))
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
    public void printHelp(Player player){
        player.sendMessage("Unknown command");
    }
    public void printUnkownPlayer(Player player){
        player.sendMessage("Unkown player");
    }

    public Player getPlayerifexists(String givenPlayerName) {
        Player existingPlayer = null;
        for (Player player : Bukkit.getServer().getOnlinePlayers() ) {
            if (player.getName().equals(givenPlayerName)) {
                existingPlayer = player;
                break;
            }
            else
                existingPlayer = null;
        }
        return existingPlayer;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            list.add("giveFruit");
        }
        if(args.length == 2){
            list.add(fruitIdentification.fruitCommandNameYami);
            list.add(fruitIdentification.fruitCommandNameMera);
            list.add(fruitIdentification.fruitCommandNameGura);
            list.add(fruitIdentification.fruitCommandNameMoku);
            list.add(fruitIdentification.fruitCommandNameNekoReoparudo);
        }
        if(args.length == 3){
            for (Player player : Bukkit.getServer().getOnlinePlayers() ) {
                list.add(player.getName());
            }
        }
        return list;
    }
}
