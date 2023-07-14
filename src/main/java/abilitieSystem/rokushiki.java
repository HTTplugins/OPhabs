package abilitieSystem;


import htt.ophabs.OPhabs;
import abilitieSystem.auxBiblio;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.Action;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collection;



/**
* @brief Class that contains haki skills --> Ability subtype.
* @author Vaelico786.
*/
public class rokushiki extends abilities {
    private abilityUser user;
    private int lGeppo, lTekkai, lShigan, lRankyaku, lSoru, lKamie;
    private boolean activeShigan, activeGeppo, activeRankyaku, activeSoru;
    private int geppoJumps, soruDashes;
    private double expGeppo, expTekkai, expShigan, expRankyaku, expSoru, expKamie;

    /**
     * @brief rokushiki constructor.
     * @param user User of the ability.
     * @param lGeppo Level of geppo.
     * @param lTekkai Level of tekkai.
     * @param lShigan Level of shigan.
     * @param lRankyaku Level of rankyaku.
     * @param lSoru Level of soru.
     * @param lKamie Level of kamie.
     * @param expGeppo Experience of geppo.
     * @param expTekkai Experience of tekkai.
     * @param expShigan Experience of shigan.
     * @param expRankyaku Experience of rankyaku.
     * @param expSoru Experience of soru.
     * @param expKamie Experience of kamie.
     * @author Vaelico786.
     */
    public rokushiki(OPhabs plugin, abilityUser user, int lGeppo, int lTekkai, int lShigan, int lRankyaku, int lSoru, int lKamie, double expGeppo, double expTekkai, double expShigan, double expRankyaku, double expSoru, double expKamie) {
        super(plugin);
        this.user = user;
        this.lGeppo = lGeppo;
        this.lTekkai = lTekkai;
        this.lShigan = lShigan;

        this.lRankyaku = lRankyaku;
        this.lSoru = lSoru;
        this.lKamie = lKamie;
        this.expGeppo = expGeppo;
        this.expTekkai = expTekkai;
        this.expShigan = expShigan;
        this.expRankyaku = expRankyaku;
        this.expSoru = expSoru;
        this.expKamie = expKamie;

        soruDashes = 0;
        geppoJumps = 0;
        activeShigan = true;
        activeGeppo = true;
        activeRankyaku = true;
        activeSoru = true;
    }


    /**
     * @brief rokushiki constructor.
     * @param user User of the ability.
     * @author Vaelico786.
     */
    public rokushiki(OPhabs plugin, abilityUser user) {
        super(plugin);
        this.user = user;
        this.lGeppo = 0;
        this.lTekkai = 0;
        this.lShigan = 0;
        this.lRankyaku = 0;
        this.lSoru = 0;
        this.lKamie = 0;
        this.expGeppo = 0;
        this.expTekkai = 0;
        this.expShigan = 0;
        this.expRankyaku = 0;
        this.expSoru = 0;
        this.expKamie = 0;

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
     * @brief Level getter for geppo abilitie.
     * @return Level of the Geppo skill.
     * @author Vaelico786.
     */
    public int getLevelGeppo(){
        return lGeppo;
    }

    /**
     * @brief Level getter for tekkai abilitie.
     * @return Level of the Tekkai skill.
     * @author Vaelico786.
     */
    public int getLevelTekkai(){
        return lTekkai;
    }

    /**
     * @brief Level getter for shigan abilitie.
     * @return Level of the Shigan skill.
     * @author Vaelico786.
     */
    public int getLevelShigan(){
        return lShigan;
    }

    /**
     * @brief Level getter for rankyaku abilitie.
     * @return Level of the Rankyaku skill.
     * @author Vaelico786.
     */
    public int getLevelRankyaku(){
        return lRankyaku;
    }

    /**
     * @brief Level getter for soru abilitie.
     * @return Level of the Soru skill.
     * @author Vaelico786.
     */
    public int getLevelSoru(){
        return lSoru;
    }

    /**
     * @brief Level getter for kamie abilitie.
     * @return Level of the Kamie skill.
     * @author Vaelico786.
     */
    public int getLevelKamie(){
        return lKamie;
    }

    /**
     * @brief Experience getter for geppo abilitie.
     * @return Experience of the Geppo skill.
     * @author Vaelico786.
     */
    public double getExpGeppo(){
        return expGeppo;
    }

    /**
     * @brief Experience getter for tekkai abilitie.
     * @return Experience of the Tekkai skill.
     * @author Vaelico786.
     */
    public double getExpTekkai(){
        return expTekkai;
    }

    /**
     * @brief Experience getter for shigan abilitie.
     * @return Experience of the Shigan skill.
     * @author Vaelico786.
     */
    public double getExpShigan(){
        return expShigan;
    }

    /**
     * @brief Experience getter for rankyaku abilitie.
     * @return Experience of the Rankyaku skill.
     * @author Vaelico786.
     */
    public double getExpRankyaku(){
        return expRankyaku;
    }

    /**
     * @brief Experience getter for soru abilitie.
     * @return Experience of the Soru skill.
     * @author Vaelico786.
     */
    public double getExpSoru(){
        return expSoru;
    }

    /**
     * @brief Experience getter for kamie abilitie.
     * @return Experience of the Kamie skill.
     * @author Vaelico786.
     */
    public double getExpKamie(){
        return expKamie;
    }

    /**
     * @brief Geppo learn method.
     * @author Vaelico786.
     */
    public void learnGeppo(){
        this.lGeppo = 1;
        user.getPlayer().sendMessage("§b§You have learned Geppo!§b§");
        user.getPlayer().setAllowFlight(true);
    }

    /**
     * @brief Tekkai learn method.
     * @author Vaelico786.
     */
    public void learnTekkai(){
        this.lTekkai = 1;
        user.getPlayer().sendMessage("§b§You have learned Tekkai!§b§");
    }

    /**
     * @brief Shigan learn method.
     * @author Vaelico786.
     */
    public void learnShigan(){
        this.lShigan = 1;
        user.getPlayer().sendMessage("§b§You have learned Shigan!§b§");
    }

    /**
     * @brief Rankyaku learn method.
     * @author Vaelico786.
     */
    public void learnRankyaku(){
        this.lRankyaku = 1;
        user.getPlayer().sendMessage("§b§You have learned Rankyaku!§b§");
    }

    /**
     * @brief Soru learn method.
     * @author Vaelico786.
     */
    public void learnSoru(){
        this.lSoru = 1;
        user.getPlayer().sendMessage("§b§You have learned Soru!§b§");
    }

    /**
     * @brief Kamie learn method.
     * @author Vaelico786.
     */
    public void learnKamie(){
        this.lKamie = 1;
        user.getPlayer().sendMessage("§b§You have learned Kamie!§b§");
    }

    /**
     * @brief Geppo level up method.
     * @param exp Experience to add.
     * @return true if the level up is done, false if not.
     * @author Vaelico786.
     */
    public boolean levelUpGeppo(double exp){
        if(this.lGeppo>0){
            this.expGeppo += exp;
            if(this.expGeppo >= 100*this.lGeppo/2){
                this.expGeppo -= 100*this.lGeppo/2;
                this.lGeppo++;
                user.getPlayer().sendMessage("§a§Geppo§r§a level up!");
                user.getPlayer().sendMessage("§a§Geppo§r§a level: "+lGeppo);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Geppo.Level", lGeppo);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Geppo.Exp", expGeppo);

                return true;
            }
        }
        return false;
    }

    /**
     * @brief Tekkai level up method.
     * @param exp Experience to add.
     * @return true if the level up is done, false if not.
     * @author Vaelico786.
     */
    public boolean levelUpTekkai(double exp){
        if(this.lTekkai>0){
            this.expTekkai += exp;
            if(this.expTekkai >= 100*this.lTekkai/2){
                this.expTekkai -= 100*this.lTekkai/2;
                this.lTekkai++;
                user.getPlayer().sendMessage("§a§Tekkai§r§a level up!");
                user.getPlayer().sendMessage("§a§Tekkai§r§a level: "+lTekkai);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Tekkai.Level", lTekkai);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Tekkai.Exp", expTekkai);

                return true;
            }
        }
        return false;
    }

    /**
     * @brief Shigan level up method.
     * @param exp Experience to add.
     * @return true if the level up is done, false if not.
     * @author Vaelico786.
     */
    public boolean levelUpShigan(double exp){
        if(this.lShigan>0){
            this.expShigan += exp;
            if(this.expShigan >= 100*this.lShigan/2){
                this.expShigan -= 100*this.lShigan/2;
                this.lShigan++;
                user.getPlayer().sendMessage("§a§Shigan§r§a level up!");
                user.getPlayer().sendMessage("§a§Shigan§r§a level: "+lShigan);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Shigan.Level", lShigan);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Shigan.Exp", expShigan);

                return true;
            }
        }
        return false;
    }

    /**
     * @brief Rankyaku level up method.
     * @param exp Experience to add.
     * @return true if the level up is done, false if not.
     * @author Vaelico786.
     */
    public boolean levelUpRankyaku(double exp){
        if(this.lRankyaku>0){
            this.expRankyaku += exp;
            if(this.expRankyaku >= 100*this.lRankyaku/2){
                this.expRankyaku -= 100*this.lRankyaku/2;
                this.lRankyaku++;
                user.getPlayer().sendMessage("§a§Rankyaku§r§a level up!");
                user.getPlayer().sendMessage("§a§Rankyaku§r§a level: "+lRankyaku);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Rankyaku.Level", lRankyaku);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Rankyaku.Exp", expRankyaku);

                return true;
            }
        }
        return false;
    }

    /**
     * @brief Soru level up method.
     * @param exp Experience to add.
     * @return true if the level up is done, false if not.
     * @author Vaelico786.
     */
    public boolean levelUpSoru(double exp){
        if(this.lSoru>0){
            this.expSoru += exp;
            if(this.expSoru >= 100*this.lSoru/2){
                this.expSoru -= 100*this.lSoru/2;
                this.lSoru++;
                user.getPlayer().sendMessage("§a§Soru§r§a level up!");
                user.getPlayer().sendMessage("§a§Soru§r§a level: "+lSoru);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Soru.Level", lSoru);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Soru.Exp", expSoru);

                return true;
            }
        }
        return false;
    }

    /**
     * @brief Kamie level up method.
     * @param exp Experience to add.
     * @return true if the level up is done, false if not.
     * @author Vaelico786.
     */
    public boolean levelUpKamie(double exp){
        if(this.lKamie>0){
            this.expKamie += exp;
            if(this.expKamie >= 100*this.lKamie/2){
                this.expKamie -= 100*this.lKamie/2;
                this.lKamie++;
                user.getPlayer().sendMessage("§a§Kami-e§r§a level up!");
                user.getPlayer().sendMessage("§a§Kami-e§r§a level: "+lKamie);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Kamie.Level", lKamie);
                plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Kamie.Exp", expKamie);

                return true;
            }
        }
        return false;
    }

    /**
     * @brief Loads player's stats from configs.
     * @author Vaelico786.
     */
    public void loadPlayer(){
        this.lGeppo = plugin.getConfig().getInt("rokushikiPlayers."+user.getPlayerName()+".Geppo.Level");
        this.expGeppo = plugin.getConfig().getDouble("rokushikiPlayers."+user.getPlayerName()+".Geppo.Exp");

        this.lTekkai = plugin.getConfig().getInt("rokushikiPlayers."+user.getPlayerName()+".Tekkai.Level");
        this.expTekkai = plugin.getConfig().getDouble("rokushikiPlayers."+user.getPlayerName()+".Tekkai.Exp");
        
        this.lShigan = plugin.getConfig().getInt("rokushikiPlayers."+user.getPlayerName()+".Shigan.Level");
        this.expShigan = plugin.getConfig().getDouble("rokushikiPlayers."+user.getPlayerName()+".Shigan.Exp");

        
        this.lRankyaku = plugin.getConfig().getInt("rokushikiPlayers."+user.getPlayerName()+".Rankyaku.Level");
        this.expRankyaku = plugin.getConfig().getDouble("rokushikiPlayers."+user.getPlayerName()+".Rankyaku.Exp");
        
        this.lSoru = plugin.getConfig().getInt("rokushikiPlayers."+user.getPlayerName()+".Soru.Level");
        this.expSoru = plugin.getConfig().getDouble("rokushikiPlayers."+user.getPlayerName()+".Soru.Exp");
        
        this.lKamie = plugin.getConfig().getInt("rokushikiPlayers."+user.getPlayerName()+".Kamie.Level");
        this.expKamie = plugin.getConfig().getDouble("rokushikiPlayers."+user.getPlayerName()+".Kamie.Exp");

         if(user.getPlayer()!=null){
            user.getPlayer().sendMessage("§a§lGeppo§r§a level: "+lGeppo);
            user.getPlayer().sendMessage("§a§lTekkai§r§a level: "+lTekkai);
            user.getPlayer().sendMessage("§a§lShigan§r§a level: "+lShigan);
            user.getPlayer().sendMessage("§a§lSoru§r§a level: "+lSoru);
            user.getPlayer().sendMessage("§a§lRankyaku§r§a level: "+lRankyaku);
            user.getPlayer().sendMessage("§a§lKami-e§r§a level: "+lKamie);
            if(lGeppo>0){
                user.getPlayer().setAllowFlight(true);
            }
        }
    }

    /**
     * @brief Saves player's stats to configs.
     * @author Vaelico786.
     */
    public void savePlayer(){
        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Geppo.Level", lGeppo);
        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Geppo.Exp", expGeppo);

        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Tekkai.Level", lTekkai);
        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Tekkai.Exp", expTekkai);

        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Shigan.Level", lShigan);
        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Shigan.Exp", expShigan);

        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Rankyaku.Level", lRankyaku);
        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Rankyaku.Exp", expRankyaku);

        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Soru.Level", lSoru);
        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Soru.Exp", expSoru);

        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Kamie.Level", lKamie);
        plugin.getConfig().set("rokushikiPlayers."+user.getPlayerName()+".Kamie.Exp", expKamie);

        plugin.saveConfig();
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
            damage = damage * lShigan/3.0 * 1.25;
            activeShigan = false;
            user.getPlayer().sendMessage("§a§lShigan§r§a");

            entity.getWorld().spawnParticle(Particle.CRIT, entity.getLocation(), 10);
            //runnable reactive on 10 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeShigan = true;
                }
            }.runTaskLater(plugin, 200);
            levelUpShigan(damage);

            return damage;
        }
        return damage;
    }
    
    /**
     * @brief Skill action rankyaku, level 5 combinated with haki.
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
                                ((LivingEntity)e).damage(lShigan+user.getHakiLevel());
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
        if(soruDashes < lSoru){
            Location loc = user.getPlayer().getLocation();
            loc.setY(loc.getY()-1);
            if(loc.getBlock().getType() != Material.AIR){
                user.getPlayer().setVelocity(user.getPlayer().getLocation().getDirection().multiply(2));
                soruDashes++;
                levelUpSoru(1);
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
     * @brief Skill action geppo, double jump with limit on lGeppo jumps.
     * @author Vaelico786.
     */
    private void geppo(){
        if(geppoJumps < lGeppo){
            Location loc = user.getPlayer().getLocation();
            loc.setY(loc.getY()-1);
            user.getPlayer().setVelocity(user.getPlayer().getLocation().getDirection().multiply(0.5).setY(1));
            user.getPlayer().getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0, 0, 0, 0.1);
            geppoJumps++;
            levelUpGeppo(1);
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
            user.getPlayer().sendMessage("§a§lRankyaku§r§a");

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
                                ((LivingEntity)e).damage(5+lRankyaku/2);
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
    public void onUserDamageAnotherEntity(EntityDamageByEntityEvent event){
        if(lShigan>=1){
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
            if(lSoru>=1){
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
        if(!user.hasFruit() && user.getPlayer().getGameMode() == GameMode.SURVIVAL){
            if(event.isFlying()){
                if(lGeppo>=1){
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
            if(lTekkai>=1){
                if(event.getDamage()<=lTekkai){
                    event.setCancelled(true);
                }
                levelUpTekkai(0.5);
            }
            else{
                if(lKamie>=1){
                    //If the entity its not a player then a probability based on player level will be used
                    Random rand = new Random();
                    if(rand.nextInt(200)/5 <= lKamie){
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
                        if(lShigan>=3 && user.getHakiLevel()>=5){
                            tobuShigan();
                        }
                    }
                }
            }else{
                if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR){
                    if(lRankyaku>=1 && user.getHakiLevel()>=1){
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
                if(lGeppo>=1){
                    user.getPlayer().setAllowFlight(true);
                }
            }
        }.runTaskLater(plugin, 5);
    }

}
