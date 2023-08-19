package oldSystem.abilitieSystem;


import oldSystem.htt.ophabs.*;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.Action;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collection; 

/**
* @brief Class that contains haki skills --> Ability subtype.
* @author Vaelico786.
*/
public class rokushiki extends abilities {
    private abilityUser user;
    private Map<String, stat> stats = new HashMap<>();
    private boolean activeShigan, activeGeppo, activeRankyaku, activeSoru;
    private int geppoJumps, soruDashes;

    /**
     * @brief rokushiki constructor.
     * @param user User of the ability.
     * @param stats.get("Geppo").getLevel() Level of geppo.
     * @param stats.get("Tekkai").getLevel() Level of tekkai.
     * @param stats.get("Shigan").getLevel() Level of shigan.
     * @param stats.get("Rankyaku").getLevel() Level of rankyaku.
     * @param stats.get("Soru").getLevel() Level of soru.
     * @param stats.get("Kamie").getLevel() Level of kamie.
     * @param expGeppo Experience of geppo.
     * @param expTekkai Experience of tekkai.
     * @param expShigan Experience of shigan.
     * @param expRankyaku Experience of rankyaku.
     * @param expSoru Experience of soru.
     * @param expKamie Experience of kamie.
     * @author Vaelico786.
     */

    /**
     * @brief rokushiki constructor.
     * @param user User of the ability.
     * @author Vaelico786.
     */
    public rokushiki(OPhabs plugin) {
        super(plugin, 0, 0);
        this.user = null;

        soruDashes = 0;
        geppoJumps = 0;
        activeShigan = true;
        activeGeppo = true;
        activeRankyaku = true;
        activeSoru = true;
    }
    

    /**
     * @brief user setter.
     * @author Vaelico786.
     */
    public void setUser(abilityUser user){
        this.user = user;
    }

    /**
     * @brief Level getter.
     * @param ability Ability name.
     * @return Level of the ability skill.
     * @author Vaelico786.
     */
    public int getLevel(String ability){
        if(stats.get(ability)!=null)
            return stats.get(ability).getLevel();
        else{
            return -1;
        }
    }

    /**
     * @brief Experience getter .
     * @param ability Ability name.
     * @return Experience of the ability skill.
     * @author Vaelico786.
     */
    public double getExp(String ability){
        if(stats.get(ability)!=null)
            return stats.get(ability).getExp();
        else{
            return -1;
        }
    }
/**
     * @brief Level setter.
     * @param ability Ability name.
     * @param level level value.
     * @return Level of the ability skill.
     * @author Vaelico786.
     */
    public void setLevel(String ability, int level){
        if(stats.get(ability)!=null)
            stats.get(ability).setLevel(level);
        else
            System.out.println("El usuario no posee la habilidad " + ability);
    }

    /**
     * @brief Experience setter .
     * @param ability Ability name.
     * @param exp experience value.
     * @return Experience of the ability skill.
     * @author Vaelico786.
     */
    public void setExp(String ability, double exp){
        if(stats.get(ability)!=null)
            stats.get(ability).setExp(exp);
        else
            System.out.println("El usuario no posee la habilidad " + ability);
    }

    /**
     * @brief Learn ability method.
     * @param ability Ability name.
     * @author Vaelico786.
     */
    public void learnAbility(String ability){
        if(user.getPlayer() != null && user.getPlayer().isOnline()){
            stats.put(ability, new stat());
            user.getPlayer().sendMessage("§b§ You have learned " + ability + "!§b§");
            reloadPlayer();
        }else{
            System.out.println("User doesn't exist");
        }
    }

    /**
     * @brief level up method.
     * @param ability Ability name.
     * @param exp Experience to add.
     * @return true if the level up is done, false if not.
     * @author Vaelico786.
     */
    public boolean levelUpAbility(String ability, double exp){
        stat current = stats.get(ability);
        if(current != null && current.getLevel()>0){
            current.setExp(stats.get(ability).getExp()+exp);

            if(current.getExp() >= 100*current.getLevel()/2){
                current.setExp(current.getExp() - 100*current.getLevel()/2);
                current.setLevel(current.getLevel()+1);
                if(user.getPlayer().isOnline()){
                    user.getPlayer().sendMessage("§a§ " + ability + "§r§a level up!");
                    user.getPlayer().sendMessage("§a§ " + ability + "§r§a level: "+current.getLevel());
                }

                // plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+"." + ability + ".Level", level.get(ability));
                // plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+"." + ability + ".Exp", this.exp.get(ability));

                return true;
            }
        }
        return false;
    }

    /**
     * @brief Loads player's stats from configs.
     * @author Vaelico786.
     */
    public void loadPlayer(Map<String, stat> stats){
        this.stats = stats;
        reloadPlayer();
    }

    /**
     * @brief Saves player's stats to configs.
     * @author Vaelico786.
     */
    public Map<String, stat> getRokushikiStats(){
        return stats; 
    }

    /**
     * @brief Reloads player's stats from configs.
     * @author Vaelico786.
     */
    public void reloadPlayer(){
        new BukkitRunnable() {
            @Override
            public void run() {
                activeShigan = true;
                if(getLevel("Geppo")>0 && user.getPlayer() != null && user.getPlayer().isOnline()){
                    user.getPlayer().setAllowFlight(true);
                }
                
                soruDashes = 0;
                geppoJumps = 0;
                activeShigan = true;
                activeGeppo = true;
                activeRankyaku = true;
                activeSoru = true;

            }
        }.runTaskLater(plugin, 5);

    }

    /**
     * @brief Skill action shigan, crit hit with cooldown 10 seconds.
     * @param damage Initial damage.
     * @param entity Entity to hit.
     * @return Damage to deal.
     * @author Vaelico786.
     */
    private double shigan(double damage, Entity entity){
        if(activeShigan){
            damage = damage * getLevel("Shigan")/3.0 * 1.25;
            activeShigan = false;
            user.getPlayer().sendMessage("§a§" + " Shigan " + "§r§a");

            entity.getWorld().spawnParticle(Particle.CRIT, entity.getLocation(), 10);
            //runnable reactive on 10 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeShigan = true;
                }
            }.runTaskLater(plugin, 200);
            levelUpAbility("Shigan", damage);

            return damage;
        }
        return damage;
    }
    
    /**
     * @brief Skill action tobu shigan, level 5 combinated with haki.
     * @author Vaelico786.
     */
    public void tobuShigan(){
        if(activeShigan){
            user.getPlayer().sendMessage("§a§lTobu Shigan§r§a");
            new BukkitRunnable() {
                    Vector direction = user.getPlayer().getEyeLocation().getDirection().normalize().multiply(2);
                    Location current = user.getPlayer().getEyeLocation().add(direction);
                @Override
                public void run() {
                    Collection<Entity> entities = current.getWorld().getNearbyEntities(current, 1, 1, 1);
                    if(current.distance(user.getPlayer().getLocation()) <= 12 &&  current.getBlock().getType() == Material.AIR && (entities.contains(user.getPlayer()) || entities.isEmpty())){
                        current.getWorld().spawnParticle(Particle.CLOUD, current, 1, 0.2, 0.2, 0.2, 0);
                        current = current.add(direction);
                    }else{
                        for(Entity e: current.getWorld().getNearbyEntities(current, 1, 1, 1)){
                            if(e instanceof LivingEntity && e != user.getPlayer()){
                                ((LivingEntity)e).damage(getLevel("Shigan")+user.getHakiLevel(), (Entity) user.getPlayer());
                            }
                        }
                        
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);

            activeShigan = false;
            //runnable reactive on 10 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeShigan = true;
                }
            }.runTaskLater(plugin, 200);
        }
    }


    /**
     * @brief Skill action soru, quick dash with limit.
     * @author Vaelico786.
     */
    private void soru(){
        if(soruDashes < getLevel("Soru")){
            Location loc = user.getPlayer().getLocation();
            loc.setY(loc.getY()-1);
            if(loc.getBlock().getType() != Material.AIR){
                user.getPlayer().setVelocity(user.getPlayer().getLocation().getDirection().multiply(2));
                soruDashes++;
                levelUpAbility("Soru", 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        soruDashes--;
                    }
                }.runTaskLater(plugin, 180);
            }
        }
    }

    /**
     * @brief Skill action geppo, double jump with limit on stats.get("Geppo").getLevel() jumps.
     * @author Vaelico786.
     */
    private void geppo(){
        if(geppoJumps < getLevel("Geppo")){
            Location loc = user.getPlayer().getLocation();
            loc.setY(loc.getY()-1);
            user.getPlayer().setVelocity(user.getPlayer().getLocation().getDirection().multiply(0.5).setY(1));
            user.getPlayer().getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0, 0, 0, 0.1);
            geppoJumps++;
            levelUpAbility("Geppo", 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    geppoJumps--;
                }
            }.runTaskLater(plugin, 120);
        }
    }

    /**
     * @brief Method that summons particles with a horizontal slash form to the rankyaku skill.
     * @param loc Location to spawn the particles.
     * @param direction Direction of the slash.
     * @param perpendicular Perpendicular direction of the slash.
     * @author Vaelico786.
     */
    public void rankyakuParticles(Location loc, Vector direction, Vector perpendicular){
        loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(perpendicular), 10, perpendicular.getX(), perpendicular.getY(), perpendicular.getZ(), 0);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(perpendicular.clone().multiply(-1)), 10, perpendicular.getX(), perpendicular.getY(), perpendicular.getZ(), 0);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(direction.clone().multiply(0.2)), 10, perpendicular.getX(), perpendicular.getY(), perpendicular.getZ(), 0);
    }

    /**
     * @brief Skill action rankyaku
     * @author Vaelico786.
     */
    private void rankyaku(){
        if(activeRankyaku){
            user.getPlayer().sendMessage("§a§" + getLevel("Rankyaku") + "§r§a");

            new BukkitRunnable() {
                Vector direction = user.getPlayer().getEyeLocation().getDirection().normalize().multiply(2);
                Location current = user.getPlayer().getEyeLocation().add(direction);

                boolean horizontal = user.getPlayer().getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR, first = true, sentinel = true;
                Vector perpendicular;

                @Override
                public void run() {
                    if(first){
                        if(horizontal)
                            perpendicular = new Vector(-direction.getZ(), direction.getY(), direction.getX());
                        else
                            perpendicular = new Vector(direction.getX(), -direction.getZ(), direction.getY());
                        first = false;
                    }

                    Collection<Entity> entities = current.getWorld().getNearbyEntities(current, 1, 1, 1);
                    entities.addAll(current.getWorld().getNearbyEntities(current.clone().add(perpendicular), 1, 1, 1));
                    entities.addAll(current.getWorld().getNearbyEntities(current.clone().add(perpendicular.clone().multiply(-1)), 1, 1, 1));
                    if( current.distance(user.getPlayer().getLocation()) <= 12  && (entities.contains(user.getPlayer()) || entities.isEmpty()) && sentinel){
                        if( current.getBlock().getType() != Material.AIR || current.clone().add(perpendicular).getBlock().getType() != Material.AIR || current.clone().add(perpendicular.clone().multiply(-1)).getBlock().getType() != Material.AIR ){
                            if(current.getBlock().getType() != Material.AIR){
                                if(current.getBlock().getType().getHardness() > Material.ACACIA_LOG.getHardness())
                                    sentinel = false;
                                else
                                    current.getBlock().breakNaturally();
                            }

                            if(current.clone().add(perpendicular).getBlock().getType() != Material.AIR){
                                if(current.clone().add(perpendicular).getBlock().getType().getHardness() > Material.ACACIA_LOG.getHardness())
                                    sentinel = false;
                                else
                                    current.clone().add(perpendicular).getBlock().breakNaturally();
                            }

                            if(current.clone().add(perpendicular.clone().multiply(-1)).getBlock().getType() != Material.AIR){
                                if(current.clone().add(perpendicular.clone().multiply(-1)).getBlock().getType().getHardness() > Material.ACACIA_LOG.getHardness())
                                    sentinel = false;
                                else
                                    current.clone().add(perpendicular.clone().multiply(-1)).getBlock().breakNaturally();
                            }
                        }
                        rankyakuParticles(current, direction, perpendicular);
                        current.add(direction);
                    }else{
                        sentinel = false;
                    }
                    if(!sentinel){
                        for(Entity e: entities){
                            if(e instanceof LivingEntity && e != user.getPlayer()){
                                ((LivingEntity)e).damage(5+getLevel("Rankyaku")/2, (Entity) user.getPlayer());
                            }
                        }
                        
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);

            activeRankyaku = false;
            //runnable reactive on 7.5 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeRankyaku = true;
                }
            }.runTaskLater(plugin, 150);
        }
    }


    /**
    * @brief Event listener that activates when the user deals damage.
    * @param event The event that was triggered
    * @author Vaelico786.
    */
    public void onEntityDamageByUser(EntityDamageByEntityEvent event){
        if(getLevel("Shigan")>=1){
            if(((Player)event.getDamager()).getInventory().getItemInMainHand().getType() == Material.AIR){
                event.setDamage(shigan(event.getDamage(), event.getEntity()));
            }
        }
    }

    /**
     * @brief Event listener that activates when the user is sprinting.
     * @param event The event that was triggered
     * @author Vaelico786.
     */
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event){
        if(event.isSprinting()){
            if(getLevel("Soru")>=1){
                soru();
            }
        }
    }

    /**
     * @brief Event listener that activates when the user fly.
     * @param event The event that was triggered
     * @author Vaelico786.
     */
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event){
        if((!user.hasFruit() || (user.hasFruit() && !user.getDFAbilities().getCanFly()))  && user.getPlayer().getGameMode() == GameMode.SURVIVAL){ 
            if(event.isFlying()){
                if(getLevel("Geppo")>=1){
                    geppo();
                }
                event.setCancelled(true);
            }
        }
    }
   
    /**
    * @brief Event listener that activates when a player takes damage
    * @param event The event that was triggered
    * @author Vaelico786.
    */
    public void onEntityDamage(EntityDamageEvent event){
        if(event instanceof EntityDamageByEntityEvent){
            if(getLevel("Tekkai")>=1){
                if(event.getDamage()<=getLevel("Tekkai")){
                    event.setCancelled(true);
                }
                levelUpAbility("Tekkai", 0.5);
            }
            else{
                if(getLevel("Kamie")>=1){
                    //If the entity its not a player then a probability based on player level will be used
                    Random rand = new Random();
                    if(rand.nextInt(200)/5 <= getLevel("Kamie")){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * @brief Event listener that activates when the user right click.
     * @param event The event that was triggered
     * @author Vaelico786.
     */
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_AIR){
            if(!event.getPlayer().isSneaking()){
                if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR){
                    if(user.hasHaki()){
                        if(getLevel("Shigan")>=3 && user.getHakiLevel()>=5){
                            tobuShigan();
                        }
                    }
                }
            }else{
                if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR){
                    if(getLevel("Rankyaku")>=1 && user.getHakiLevel()>=1){
                        rankyaku();
                    }
                }
            }
            
        }
    }

    /**
     * @brief Event listener that activates when a player respawns.
     * @param event The event that was triggered
     * @author Vaelico786.
     */
    public void onPlayerRespawn(PlayerRespawnEvent event){
        new BukkitRunnable() {
            @Override
            public void run() {
                reloadPlayer();
            }
        }.runTaskLater(plugin, 5);
    }
    

}
