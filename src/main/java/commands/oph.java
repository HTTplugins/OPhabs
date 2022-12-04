package commands;

import fruitSystem.devilFruit;
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
            fruitNamegura_gura = "gura_gura";
    private  final OPhabs plugin;
    public oph(OPhabs plugin){this.plugin = plugin;}
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (args.length < 2) return false;

        String playerName = args[0];
        Player target = sender.getServer().getPlayerExact(playerName);
        if (target == null) { //check whether the player is online
            sender.sendMessage("Player " + playerName + " is not online.");
            return true;
        }
        Material itemType = Material.matchMaterial(args[1]);
        if (itemType == null) { //check whether the material exists
            sender.sendMessage("Unknown material: " + args[1] + ".");
            return true;
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
