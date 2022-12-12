package commands;

import fruitSystem.devilFruit;
import fruitSystem.fruitIdentification;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import htt.ophabs.*;
import org.bukkit.entity.Player;

public class oph implements CommandExecutor {
    final String fruitNameyami_yami = "yami_yami",
            fruitNamemera_mera = "mera_mera",
            fruitNamegura_gura = "gura_gura",
            fruitNamemoku_moku = "moku_moku",
            fruitNameneko_neko_reoparudo = "neko_neko_reoparudo";

    private  final OPhabs plugin;
    public oph(OPhabs plugin){this.plugin = plugin;}
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){


        Player player = (Player) sender;
        String order = args[0];
        String fruitCommandName = args[1];
        Player targetPlayer = getPlayerifexists(args[2]);


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
                    }else printUnkownPlayer(player);
                else player.sendMessage("The fruit doesn't exist");
            else player.sendMessage("Parameters to giveFruit are <fruitCommandName> <PlayerName>");
        else printHelp(player);


        return false;

    }

    public void printHelp(Player player){
        player.sendMessage("Unknown command");
    }
    public void printUnkownPlayer(Player player){player.sendMessage("Unkown player");}

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
}
