package display;

import htt.ophabs.OPhabs;
import users.OPUser;
import abilities.Ability;
import abilities.AbilitySet;
import fruits.DevilFruit;
import cast.FruitCaster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
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

    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private final HashMap<String, FruitCasterData> fruitCasters;
    private final Scoreboard emptyScoreboard;
    private BukkitTask updateScoreboardTask;

    public ScoreboardSystem()
    {
        this.emptyScoreboard = scoreboardManager.getNewScoreboard();

        this.fruitCasters = new HashMap<>();

        // Iniciar las actualizaciones
        this.updateScoreboardTask = this.updateScoreboards();
    }

    private BukkitTask updateScoreboards()
    {
        return new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    updateFruitScoreboard();
                }
            }.runTaskTimer(OPhabs.getInstance(), 0, 10);
    }

    private void updateFruitScoreboard()
    {
        for (FruitCasterData casterData : fruitCasters.values())
        {
            try
            {
                Scoreboard scoreboard = casterData.scoreboard;
                FruitCaster caster = casterData.caster;
                DevilFruit fruit = caster.getFruit();
                OPUser user = fruit.getUser();

                Player player = Bukkit.getPlayer(user.getUUID());

                // Offline probablemente
                if (player == null)
                    continue;

                // Limpiar la scoreboard
                Objective objective = scoreboard.getObjective("abilitiesScoreboard");

                if (objective != null)
                {
                    objective.unregister();
                }

                // Si tiene este caster en alguna mano
                if(caster.isOwnedBy(user) && (caster.isThisItem(player.getInventory().getItemInMainHand())
                    || caster.isThisItem(player.getInventory().getItemInOffHand())))
                {
                    String fruitName = fruit.getName();
                    fruitName = fruitName.substring(0,1).toUpperCase() + fruitName.substring(1);

                    AbilitySet abilitySet = caster.getSelectedAbilitySet();
                    ArrayList<String> abilityDisplayList = new ArrayList<>(abilitySet.abilities.size());

                    for (Ability ability : abilitySet.abilities)
                        abilityDisplayList.add(ability.getDisplayableString());

                    if(!user.isFastCasting()){
                        int selectedAbilityIndex = caster.getSelectedAbilityNumber();

                        if (selectedAbilityIndex < abilityDisplayList.size())
                        {
                            String activeAbility = ChatColor.RED + "" + ChatColor.BOLD + "" + abilityDisplayList.get(selectedAbilityIndex);
                            abilityDisplayList.set(selectedAbilityIndex, activeAbility);
                        }
                    }

                    objective = scoreboard.registerNewObjective("abilitiesScoreboard","dummy");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    objective.setDisplayName(ChatColor.BOLD + "" + fruitName);

                    for(int i = 0; i < abilityDisplayList.size(); i++)
                    {
                        Score score = objective.getScore(abilityDisplayList.get(i));
                        score.setScore(i);
                    }
                }

                player.setScoreboard(scoreboard);
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

    @Override
    public void addFruitCaster(FruitCaster caster)
    {
        fruitCasters.put(caster.getName(), new FruitCasterData(caster, scoreboardManager.getNewScoreboard()));
    }

    @Override
    public void removeFruitCaster(FruitCaster caster)
    {
        FruitCasterData fruitCasterData = fruitCasters.remove(caster.getName());
        fruitCasterData.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
    }
}
