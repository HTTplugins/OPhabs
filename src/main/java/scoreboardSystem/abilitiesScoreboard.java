package scoreboardSystem;

import castSystem.castIdentification;
import htt.ophabs.OPhabs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import abilitieSystem.*;

import java.util.List;

public class abilitiesScoreboard {

    OPhabs plugin;

    public abilitiesScoreboard(OPhabs plugin){
        this.plugin = plugin;
    }


    public void ini(){

        new BukkitRunnable(){

            @Override
            public void run() {

                String
                        yamiValue = plugin.getConfig().getString("FruitAssociations.yami_yami"),
                        meraValue = plugin.getConfig().getString("FruitAssociations.mera_mera"),
                        guraValue = plugin.getConfig().getString("FruitAssociations.gura_gura"),
                        mokuValue = plugin.getConfig().getString("FruitAssociations.moku_moku"),
                        nekoReoparudoValue = plugin.getConfig().getString("FruitAssociations.neko_neko_reoparudo");

                ScoreboardManager manager = Bukkit.getScoreboardManager();

                Scoreboard scoreboardYami = manager.getNewScoreboard();
                Scoreboard scoreboardMera = manager.getNewScoreboard();
                Scoreboard scoreboardGura = manager.getNewScoreboard();
                Scoreboard scoreboardMoku = manager.getNewScoreboard();
                Scoreboard scoreboardNeko = manager.getNewScoreboard();

                if(!yamiValue.equals("none"))
                    updateScoreboard(scoreboardYami ,Bukkit.getPlayerExact(yamiValue),abilitiesIdentification.namesYami);

                if(!meraValue.equals("none"))
                    updateScoreboard(scoreboardMera ,Bukkit.getPlayerExact(meraValue),abilitiesIdentification.namesMera);

                if(!guraValue.equals("none"))
                    updateScoreboard(scoreboardGura ,Bukkit.getPlayerExact(guraValue),abilitiesIdentification.namesGura);

                if(!mokuValue.equals("none"))
                    updateScoreboard( scoreboardMoku ,Bukkit.getPlayerExact(mokuValue),abilitiesIdentification.namesMoku);

                if(!nekoReoparudoValue.equals("none"))
                    updateScoreboard(scoreboardNeko ,Bukkit.getPlayerExact(nekoReoparudoValue),abilitiesIdentification.namesNekoReoparudo);



            }
        }.runTaskTimer(plugin,0,10);





    }

    public void updateScoreboard(Scoreboard scoreboard , Player player, List<String> names){

        Objective objctive = scoreboard.registerNewObjective("abilitiesScoreboard","dummy");

        ItemStack caster = player.getInventory().getItemInMainHand();

        if(castIdentification.itemIsCaster(caster)){

            objctive.setDisplaySlot(DisplaySlot.SIDEBAR);


            objctive.setDisplayName(names.get(0));


            Score score1 = objctive.getScore(names.get(1));
            score1.setScore(0);

            Score score2 = objctive.getScore(names.get(2));
            score2.setScore(1);


            player.setScoreboard(scoreboard);

        } else
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

    }


}
