package commands;

import htt.ophabs.fileSystem;
import abilitieSystem.*;
import fruitSystem.devilFruit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import htt.ophabs.*;
import hakiSystem.hakiAssociation;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.List;

/**
 * @brief OPhabs main command.
 * @author RedRiotTank, Vaelico786.
 */
public class oph implements CommandExecutor, TabCompleter {
    private  final OPhabs plugin;
    public ArrayList<String> fruitCommands = new ArrayList<String>();
    public hakiAssociation haki;

    /**
     * @brief Main Command constructor. Initialization of devil fruits and haki.
     * @param plugin OPhabs plugin.
     * @param haki Haki Association.
     * @author Vaelico786.
     */
    public oph(OPhabs plugin, hakiAssociation haki){

        this.plugin = plugin;
        for (df ability : plugin.abilitiesList){
            fruitCommands.add(ability.getName());
        }
        this.haki = haki;
    }

    /**
     * @brief Command execution.
     * @param sender Sender of the command. (player, server,...)
     * @param command command sent.
     * @param label set label.
     * @param args arguments of the command.
     * @author RedRiotTank, Vaelico786.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;
        String order = args[0];

        if(args.length == 3){
            String fruitCommandName = args[1];
            Player targetPlayer = Bukkit.getServer().getPlayerExact(args[2]);

            if (order.equalsIgnoreCase("giveFruit")){
                if(fruitCommands.contains(fruitCommandName))
                    if( targetPlayer != null) {
                        if(fileSystem.getFruitLinkedUser(fruitCommandName).equals("none")){
                            devilFruit devFruit = new devilFruit(fruitCommandName);
                            devFruit.playerObtainFruit(targetPlayer);
                        } else {player.sendMessage("The fruits has alredy been consumed");}
                    }else player.sendMessage("Unkown player");
                else player.sendMessage("The fruit doesn't exist");
            }
            if(order.equalsIgnoreCase("setHakiLevel")){
                if(targetPlayer != null){
                    if(haki.users.containsKey(args[2])){
                        abilityUser user = haki.users.get(args[2]);
                        if(user.hasHaki()){
                            user.getHakiAbilities().setLevel(Integer.parseInt(args[1]));
                        }
                    }
                } else player.sendMessage("Unkown player");
            }

        }
        else {
            if(args.length == 2){
                if (order.equalsIgnoreCase("giveHaki")){
                    if(Bukkit.getServer().getPlayerExact(args[1]) != null){
                        haki.addHakiPlayer(args[1], 1, 0);
                    }
                }
                else if (order.equalsIgnoreCase("reloadHaki")){
                    if(Bukkit.getServer().getPlayerExact(args[1]) != null){
                        if(haki.users.containsKey(args[1])){
                            abilityUser user = haki.users.get(args[1]);
                            if(user.hasHaki()){
                                user.getPlayer().sendMessage("Reloading haki " + user.getPlayer().getName());
                                user.getHakiAbilities().reloadPlayer();
                            }
                        }
                    }
                }
            }
            else 
                player.sendMessage("Unknown command");
            
        }



        return false;

    }


    /**
     * @brief Command autocomplete
     * @param sender Sender of the command. (player, server,...)
     * @param command command sent.
     * @param alias alias of the command
     * @param args arguments of the command.
     * @author Vaelico786.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            list.add("giveFruit");
            list.add("giveHaki");
            list.add("reloadHaki");
            list.add("setHakiLevel");
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("giveFruit")){
            for (String fruitCommandName : fruitCommands){
                list.add(fruitCommandName);
            }
        }
        else if(args.length == 2 && (args[0].equalsIgnoreCase("giveHaki") || args[0].equalsIgnoreCase("reloadHaki"))){
            for (Player player : Bukkit.getServer().getOnlinePlayers() ) {
                list.add(player.getName());
            }
        }else{
            if(args.length == 3 ){
                for (Player player : Bukkit.getServer().getOnlinePlayers() ) {
                    list.add(player.getName());
                }
            }
        }
        return list;
    }
}
