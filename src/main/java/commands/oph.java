package commands;

import fruitSystem.devilFruit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import htt.ophabs.*;
import org.bukkit.entity.Player;

public class oph implements CommandExecutor {
    final String fruitNameyami_yami = "yami_yami",
            fruitNamemera_mera = "mera_mera",
            fruitNamegura_gura = "gura_gura";
    private  final OPhabs plugin;
    public oph(OPhabs plugin){this.plugin = plugin;}
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;
        String fruitName = args[1];
        Player targetPlayer = getPlayerifexists(args[2]);


        if(args.length == 3) {
            if (args[0].equalsIgnoreCase("giveFruit")){


                if(fruitName.equalsIgnoreCase(fruitNameyami_yami) || fruitName.equalsIgnoreCase(fruitNamemera_mera) || fruitName.equalsIgnoreCase(fruitNamegura_gura)){
                    if( targetPlayer != null){

                        devilFruit devFruit = new devilFruit(fruitName);

                        devFruit.playerObtainFruit(targetPlayer);

                    }
                    else
                        printUnkownPlayer(player);

                }

                else {
                    player.sendMessage("The fruit doesnt exist or isn't avaiable");
                }
            }

        } else {
            printHelp(player);
        }

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
