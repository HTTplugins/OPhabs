package commands;

import weapons.weaponsItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import htt.ophabs.*;
import org.bukkit.entity.Player;

/**
 * @brief OPhabs main command.
 * @author RedRiotTank, Vaelico786.
 */
public class weaponShop implements CommandExecutor {
    private  final OPhabs plugin;
    
    /**
     * @brief Main Command constructor.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    public weaponShop(OPhabs plugin){this.plugin = plugin;}

    /**
     * @brief Spawns a weapons shop inventory.
     * @param plugin OPhabs plugin.
     * @author Vaelico786.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player player = (Player) sender;

        weaponsItems.weaponShop(player);

        return false;
    }

}
