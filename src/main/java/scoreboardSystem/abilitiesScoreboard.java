package scoreboardSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import abilitieSystem.*;
import castSystem.*;
import fruitSystem.devilFruitUser;

import java.util.ArrayList;

public class abilitiesScoreboard {

    OPhabs plugin;
    coolDown cd;
    devilFruitUser user;

    public abilitiesScoreboard(OPhabs plugin, coolDown cd, devilFruitUser user){
        this.plugin = plugin;
        this.cd = cd;
        this.user = user;
    }

    public void ini(){

        new BukkitRunnable(){

            @Override
            public void run() {

                ScoreboardManager manager = Bukkit.getScoreboardManager();

                Scoreboard scoreboard = manager.getNewScoreboard();

                updateScoreboard(scoreboard, Bukkit.getPlayerExact(user.getPlayer().getName()), user.getAbilitiesNames(), user.actual, user.getAbilitiesCD());
            }
        }.runTaskTimer(plugin,0,10);

    }

    public boolean updateScoreboard(Scoreboard scoreboard , Player player, ArrayList<String> names, int index, ArrayList<Integer> cd){

        Objective objctive = scoreboard.registerNewObjective("abilitiesScoreboard","dummy");

        ItemStack caster = player.getInventory().getItemInMainHand();
        ArrayList<String> abNames = new ArrayList<>(names);
        String active = ChatColor.RED + "" + ChatColor.BOLD + "" + abNames.get(index);

        abNames.set(index, active);

        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        if(castIdentification.itemIsCaster(caster)){

            objctive.setDisplaySlot(DisplaySlot.SIDEBAR);

            objctive.setDisplayName(user.getFruit().getFruitName());

            for(int i = 0; i < abNames.size(); i++){
                Score score = objctive.getScore(abNames.get(i));
                score.setScore(cd.get(i));
            }

            player.setScoreboard(scoreboard);

        } else
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());


        return true;
    }

}
