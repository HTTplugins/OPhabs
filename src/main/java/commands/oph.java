package commands;

import abilitieSystem.*;
import fruitSystem.devilFruit;
import fruitSystem.fruitIdentification;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import htt.ophabs.*;
import hakiSystem.hakiAssociation;
import rokushikiSystem.rokushikiAssociation;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.List;

/**
 * @brief OPhabs main command.
 * @author RedRiotTank, Vaelico786.
 */
public class oph implements CommandExecutor, TabCompleter {
    private  final OPhabs plugin;
    public ArrayList<String> fruitCommands = new ArrayList<String>(), rokushikiNames =new ArrayList<String>();
    public hakiAssociation haki;
    public rokushikiAssociation rokushiki;

    /**
     * @brief Main Command constructor. Initialization of devil fruits and haki.
     * @param plugin OPhabs plugin.
     * @param abilitiesList Devil fruit abilities list.
     * @param haki Haki Association.
     * @author Vaelico786.
     */
    public oph(OPhabs plugin, ArrayList<df> abilitiesList, hakiAssociation haki, rokushikiAssociation rokushiki) {

        this.plugin = plugin;
        for (df ability : abilitiesList){
            fruitCommands.add(ability.getName());
        }
        rokushikiNames.add("Geppo");
        rokushikiNames.add("Tekkai");
        rokushikiNames.add("Shigan");
        rokushikiNames.add("Rankyaku");
        rokushikiNames.add("Soru");
        rokushikiNames.add("Kamie");

        this.haki = haki;
        this.rokushiki = rokushiki;
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

        
        if(args.length == 4){
            //change level of a rokusiki ability
            if (order.equalsIgnoreCase("setLvlRokushiki")){
                if(args[3] != null){
                    if(rokushiki.users.containsKey(args[3])){
                        if(rokushikiNames.contains(args[1])){
                            plugin.getConfig().set("rokushikiPlayers."+args[3]+"."+args[1]+".Level", Integer.parseInt(args[2]));
                            rokushiki.users.get(args[3]).getRokushikiAbilities().loadPlayer();
                        }
                    }
                }
            }
        }
        else{
            if(args.length == 3){
                String fruitCommandName = args[1];
                Player targetPlayer = Bukkit.getServer().getPlayerExact(args[2]);
                
                //Give fruit to a player command.
                if (order.equalsIgnoreCase("giveFruit")){
                    if(fruitCommands.contains(fruitCommandName))
                        if( targetPlayer != null) {
                            if(plugin.getConfig().getString("FruitAssociations."+fruitCommandName).equals("none")){
                                devilFruit devFruit = new devilFruit(fruitCommandName);
                                devFruit.playerObtainFruit(targetPlayer);
                            } else {player.sendMessage("The fruits has alredy been consumed");}
                        }else player.sendMessage("Unkown player");
                    else player.sendMessage("The fruit doesn't exist");
                }else{
                    //Rokushiki learn command
                    if(order.equalsIgnoreCase("learnRokushiki")){
                        if(targetPlayer != null){
                            //if the user knows rokushiki
                            if(rokushiki.rokushikiPlayers.containsKey(targetPlayer.getName())){
                                abilityUser user = rokushiki.users.get(targetPlayer.getName());
                                if(user.hasRokushiki()){
                                    if(plugin.getConfig().getInt("rokushikiAssociations."+user.getPlayerName()+"."+fruitCommandName+".Level") == 0){
                                        plugin.getConfig().set("rokushikiAssociations."+user.getPlayerName()+"."+fruitCommandName+".Level", 1);
                                        user.getRokushikiAbilities().loadPlayer();
                                    }
                                }
                            }
                            else
                                rokushiki.addRokushikiPlayer(targetPlayer.getName(), fruitCommandName);
                        }
                    }
                }
                //Haki set level command
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
            list.add("learnRokushiki");
            list.add("setLvlRokushiki");
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
        }
        else if(args.length == 2 && (args[0].equalsIgnoreCase("setLvlRokushiki") || args[0].equalsIgnoreCase("learnRokushiki"))){
            for (String rokushikiName : rokushikiNames){
                list.add(rokushikiName);
            }
        }
        if(args.length == 3 && !args[0].equalsIgnoreCase("setLvlRokushiki")){
            for (Player player : Bukkit.getServer().getOnlinePlayers() ) {
                list.add(player.getName());
            }
        }
        if(args.length == 4 && args[0].equalsIgnoreCase("setLvlRokushiki")){
            for (Player player : Bukkit.getServer().getOnlinePlayers() ) {
                list.add(player.getName());
            }
        }
        return list;
    }
}
