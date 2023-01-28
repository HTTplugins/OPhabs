package commands;

import weapons.weaponsItems;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import htt.ophabs.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class weaponShop implements CommandExecutor {
    private  final OPhabs plugin;

    public weaponShop(OPhabs plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;

        weaponsItems.weaponShop(player);

        return false;
    }

}
