package commands;

import abilitieSystem.*;
import fruitSystem.devilFruit;
import hakiSystem.hakiAssociation;
import htt.ophabs.*;
import htt.ophabs.fileSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import rokushikiSystem.rokushikiAssociation;

/**
 * @brief OPhabs main command.
 * @author RedRiotTank, Vaelico786.
 */
public class oph implements CommandExecutor, TabCompleter {
    private final OPhabs plugin;
    public Map<String, df> fruitCommands = new HashMap<>();
    public ArrayList<String> rokushikiNames = new ArrayList<>();
    public hakiAssociation haki;
    public rokushikiAssociation rokushiki;

    /**
     * @brief Main Command constructor. Initialization of devil fruits and haki.
     * @param plugin OPhabs plugin.
     * @param haki Haki Association.
     * @author Vaelico786.
     */
    public oph(OPhabs plugin, hakiAssociation haki, rokushikiAssociation rokushiki) {
        this.plugin = plugin;
        for (df ability : plugin.abilitiesList) {
            fruitCommands.put(ability.getName(), ability);
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        String order = args[0];

        if (args.length == 4) {
            // change level of a rokushiki ability
            if (order.equalsIgnoreCase("setLvlRokushiki")) {
                if (args[3] != null) {
                    if (OPhabs.users.containsKey(args[3]) && 
                        OPhabs.users.get(args[3]).hasRokushiki()) {
                        if (rokushikiNames.contains(args[1])) {
                            OPhabs.users.get(args[3]).getRokushikiAbilities().setLevel(args[1], Integer.parseInt(args[2]));
                            fileSystem.updateRokushikiUser(args[3], args[1], Integer.parseInt(args[2]), OPhabs.users.get(args[3]).getRokushikiAbilities().getExp(args[1]));
                        }
                    }
                }
            }
        } else {
            if (args.length == 3) {
                String fruitCommandName = args[1];
                Player targetPlayer = Bukkit.getServer().getPlayerExact(args[2]);

                // Rokushiki learn command
                if (order.equalsIgnoreCase("learnRokushiki")) {
                    if (targetPlayer != null) {
                        // if the user knows rokushiki
                        if (OPhabs.users.containsKey(targetPlayer.getName())) {
                            abilityUser user = OPhabs.users.get(targetPlayer.getName());
                            if (user.hasRokushiki()) {
                                rokushiki.addRokushikiPlayerAbility(args[2], fruitCommandName);
                            } else {
                                rokushiki.addRokushikiPlayer(targetPlayer.getName(), fruitCommandName);
                            }
                        } else {
                            rokushiki.addRokushikiPlayer(targetPlayer.getName(), fruitCommandName);
                        }
                    }
                }

                if (order.equalsIgnoreCase("giveFruit")) {
                    if (fruitCommands.containsKey(fruitCommandName)) {
                        df ability = fruitCommands.get(fruitCommandName);
                        if (targetPlayer != null) {
                            if (ability.getUser() == null) {
                                devilFruit devFruit = ability.getFruit();
                                devFruit.playerObtainFruit(targetPlayer);
                            } else {
                                player.sendMessage("The fruits has alredy been consumed");
                            }
                        } else
                            player.sendMessage("Unkown player");
                    } else
                        player.sendMessage("The fruit doesn't exist");
                }

                if (order.equalsIgnoreCase("setHakiLevel")) {
                    if (targetPlayer != null) {
                        if (haki.users.containsKey(args[2])) {
                            abilityUser user = haki.users.get(args[2]);
                            if (user.hasHaki()) {
                                user.getHakiAbilities().setLevel(Integer.parseInt(args[1]));
                            }
                        }
                    } else
                        player.sendMessage("Unkown player");
                }

            } else {
                if (args.length == 2) {
                    if (order.equalsIgnoreCase("giveHaki")) {
                        if (Bukkit.getServer().getPlayerExact(args[1]) != null) {
                            haki.addHakiPlayer(args[1], 1, 0);
                        }
                    } else if (order.equalsIgnoreCase("reloadHaki")) {
                        if (Bukkit.getServer().getPlayerExact(args[1]) != null) {
                            if (haki.users.containsKey(args[1])) {
                                abilityUser user = haki.users.get(args[1]);
                                if (user.hasHaki()) {
                                    user.getPlayer().sendMessage(
                                        "Reloading haki " + user.getPlayer().getName());
                                    user.getHakiAbilities().reloadPlayer();
                                }
                            }
                        }
                    }
                } else
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
    public List<String> onTabComplete(
        CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("giveFruit");
            list.add("giveHaki");
            list.add("reloadHaki");
            list.add("setHakiLevel");
            list.add("learnRokushiki");
            list.add("setLvlRokushiki");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("giveFruit")) {
            for (String fruitCommandName : fruitCommands.keySet()) {
                list.add(fruitCommandName);
            }
        } else if (args.length == 2
            && (args[0].equalsIgnoreCase("giveHaki") || args[0].equalsIgnoreCase("reloadHaki"))) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                list.add(player.getName());
            }
        } else if (args.length == 2
            && (args[0].equalsIgnoreCase("setLvlRokushiki")
                || args[0].equalsIgnoreCase("learnRokushiki"))) {
            for (String rokushikiName : rokushikiNames) {
                list.add(rokushikiName);
            }
        }
        if (args.length == 3 && !args[0].equalsIgnoreCase("setLvlRokushiki")) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                list.add(player.getName());
            }
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("setLvlRokushiki")) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                list.add(player.getName());
            }
        }
        return list;
    }
}
