package newSystem.display;

import htt.ophabs.OPhabs;
import newSystem.OPUser;
import newSystem.abilities.Ability;
import newSystem.abilities.AbilitySet;
import newSystem.fruits.DevilFruit;
import newSystem.cast.FruitCaster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreboardSystem implements IFruitCasterDisplay
{
    private class FruitCasterData
    {
        public FruitCaster caster;
        public Scoreboard scoreboard;

        public FruitCasterData(FruitCaster caster, Scoreboard scoreboard)
        {
            this.caster = caster;
            this.scoreboard = scoreboard;
        }
    }

    private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private HashMap<String, FruitCasterData> fruitCasters;

    public ScoreboardSystem()
    {
        this.fruitCasters = new HashMap<>();

        // Iniciar las actualizaciones
        this.updateFruitScoreboard();
    }

    private void updateFruitScoreboard()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (FruitCasterData casterData : fruitCasters.values())
                {
                    try
                    {
                        Scoreboard scoreboard = casterData.scoreboard;
                        FruitCaster caster = casterData.caster;
                        DevilFruit fruit = caster.getFruit();
                        OPUser user = fruit.getUser();

                        System.out.println("Proceso el usuario " + user.getPlayerName());

                        Player player = Bukkit.getPlayer(user.getUUID());

                        // Offline probablemente
                        if (player == null)
                            continue;

                        AbilitySet abilitySet = caster.getSelectedAbilitySet();
                        ArrayList<String> abilityDisplayList = new ArrayList<>(abilitySet.abilities.size());

                        for (Ability ability : abilitySet.abilities)
                            abilityDisplayList.add(ability.getDisplayableString());

                        int selectedAbilityIndex = caster.getSelectedAbilityNumber();

                        if (selectedAbilityIndex < abilityDisplayList.size())
                        {
                            String activeAbility = ChatColor.RED + "" + ChatColor.BOLD + "" + abilityDisplayList.get(selectedAbilityIndex);
                            abilityDisplayList.set(selectedAbilityIndex, activeAbility);
                        }


                        Scoreboard newScoreboard = scoreboardManager.getNewScoreboard();

                        // TODO: Si tiene este caster en alguna mano
                        if(true || true)
                        {
                            Scoreboard tmpScoreboard = scoreboardManager.getNewScoreboard();

                            Objective objective = tmpScoreboard.registerNewObjective("abilitiesScoreboard","dummy");
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                            String fruitName = fruit.getName();
                            fruitName = fruitName.substring(0,1).toUpperCase() + fruitName.substring(1);

                            objective.setDisplayName(ChatColor.BOLD + "" + fruitName);

                            for(int i = 0; i < abilityDisplayList.size(); i++)
                            {
                                Score score = objective.getScore(abilityDisplayList.get(i));
                                score.setScore(i);
                            }

                            newScoreboard = tmpScoreboard;
                        }

                        player.setScoreboard(newScoreboard);
                    }
                    catch (NullPointerException ignored)
                    {
                        System.out.println("Null PExcept");
                        // Se resetea la scoreboard
                        casterData.scoreboard = scoreboardManager.getNewScoreboard();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskTimer(OPhabs.getInstance(), 0, 10);
    }

    @Override
    public void addFruitCaster(FruitCaster caster)
    {
        fruitCasters.put(caster.getName(), new FruitCasterData(caster, scoreboardManager.getNewScoreboard()));
    }

    @Override
    public void removeFruitCaster(FruitCaster caster)
    {
        fruitCasters.remove(caster.getName());
    }
}
