package newSystem.commands;

import htt.ophabs.OPhabs;
import newSystem.OPUser;
import newSystem.consumables.ConsumableDevilFruit;
import newSystem.fruits.DevilFruit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OPHCommandManager implements CommandExecutor, TabCompleter
{
    private HashMap<String, DevilFruit> fruitCommandMap = new HashMap<>();

    public OPHCommandManager()
    {
        //
        // Frutas
        //

        for (DevilFruit fruit : OPhabs.registrySystem.fruitRegistry.getFruitMap().values())
        {
            fruitCommandMap.put(fruit.getCommandName(), fruit);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = (Player) sender;
        String order = args[0];

        if (args.length == 3)
        {
            String fruitCommandName = args[1];
            Player targetPlayer = Bukkit.getServer().getPlayerExact(args[2]);

            if (order.equalsIgnoreCase("giveFruit"))
            {
                if (fruitCommandMap.containsKey(fruitCommandName))
                {
                    DevilFruit fruit = fruitCommandMap.get(fruitCommandName);

                    if (targetPlayer != null)
                    {
                        OPUser fruitOwner = fruit.getUser();

                        if (fruitOwner == null)
                        {
                            player.getInventory().addItem(ConsumableDevilFruit.get(fruit.getID()));
                            return true;
                        }
                        else
                        {
                            player.sendMessage("The fruit has already been consumed by " + fruitOwner.getPlayerName());
                            return false;
                        }
                    }
                    else
                    {
                        player.sendMessage("Unknown player");
                        return false;
                    }
                }
                else
                {
                    player.sendMessage("The fruit doesn't exist");
                    return false;
                }
            }
        }
        else
        {
            player.sendMessage("Unknown command");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> tabOptions = new ArrayList<>();

        if (args.length == 1)
        {
            tabOptions.add("giveFruit");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("giveFruit"))
        {
            tabOptions.addAll(fruitCommandMap.keySet());
        }

        if (args.length == 3)
        {
            for (Player player : Bukkit.getServer().getOnlinePlayers())
            {
                tabOptions.add(player.getName());
            }
        }

        return tabOptions;
    }
}
