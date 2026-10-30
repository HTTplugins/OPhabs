package rokushiki;

import events.IEventProcessor;
import htt.ophabs.OPhabs;
import haki.Haki;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import users.OPUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Rokushiki implements IEventProcessor {
    private OPUser user;
    private Map<String, RokushikiStat> stats = new HashMap<>();
    private boolean activeShigan = true;
    private boolean activeGeppo = true;
    private boolean activeRankyaku = true;
    private boolean activeSoru = true;
    private int geppoJumps = 0;
    private int soruDashes = 0;

    public Rokushiki(OPUser user) {
        this.user = user;
        initStats();
        reloadPlayer();
    }

    private void initStats() {
        String[] abilities = {"Geppo", "Tekkai", "Shigan", "Rankyaku", "Soru", "Kamie"};
        for (String ability : abilities) {
            stats.put(ability, new RokushikiStat());
        }
    }

    public int getLevel(String ability) {
        RokushikiStat stat = stats.get(ability);
        return stat != null ? stat.getLevel() : -1;
    }

    public double getExp(String ability) {
        RokushikiStat stat = stats.get(ability);
        return stat != null ? stat.getExp() : -1;
    }

    public void setLevel(String ability, int level) {
        RokushikiStat stat = stats.get(ability);
        if (stat != null) {
            stat.setLevel(level);
        } else {
            System.out.println("El usuario no posee la habilidad " + ability);
        }
    }

    public void setExp(String ability, double exp) {
        RokushikiStat stat = stats.get(ability);
        if (stat != null) {
            stat.setExp(exp);
        } else {
            System.out.println("El usuario no posee la habilidad " + ability);
        }
    }

    public void learnAbility(String ability) {
        Player player = user.getPlayer();
        if (player != null && player.isOnline()) {
            stats.put(ability, new RokushikiStat());
            player.sendMessage("§b§ You have learned " + ability + "!§b§");
            reloadPlayer();
        } else {
            System.out.println("User doesn't exist");
        }
    }

    public boolean levelUpAbility(String ability, double exp) {
        RokushikiStat current = stats.get(ability);
        if (current != null && current.getLevel() > 0) {
            current.setExp(current.getExp() + exp);

            if (current.getExp() >= 100 * current.getLevel() / 2.0) {
                current.setExp(current.getExp() - 100 * current.getLevel() / 2.0);
                current.setLevel(current.getLevel() + 1);
                Player player = user.getPlayer();
                if (player != null && player.isOnline()) {
                    player.sendMessage("§a§ " + ability + "§r§a level up!");
                    player.sendMessage("§a§ " + ability + "§r§a level: " + current.getLevel());
                }
                return true;
            }
        }
        return false;
    }

    public void loadPlayer(Map<String, RokushikiStat> stats) {
        this.stats = stats;
        reloadPlayer();
    }

    public Map<String, RokushikiStat> getRokushikiStats() {
        return stats;
    }

    public void reloadPlayer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                activeShigan = true;
                Player player = user.getPlayer();
                if (player != null && player.isOnline() && getLevel("Geppo") > 0) {
                    player.setAllowFlight(true);
                }
                soruDashes = 0;
                geppoJumps = 0;
                activeShigan = true;
                activeGeppo = true;
                activeRankyaku = true;
                activeSoru = true;
            }
        }.runTaskLater(OPhabs.getInstance(), 5);
    }

    private double shigan(double damage, Entity entity) {
        if (activeShigan) {
            damage = damage * getLevel("Shigan") / 3.0 * 1.25;
            activeShigan = false;
            Player player = user.getPlayer();
            if (player != null) {
                player.sendMessage("§a§" + " Shigan " + "§r§a");
            }
            entity.getWorld().spawnParticle(Particle.CRIT, entity.getLocation(), 10);
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeShigan = true;
                }
            }.runTaskLater(OPhabs.getInstance(), 200);
            levelUpAbility("Shigan", damage);
            return damage;
        }
        return damage;
    }

    public void tobuShigan() {
        if (activeShigan) {
            Player player = user.getPlayer();
            if (player == null) return;
            player.sendMessage("§a§lTobu Shigan§r§a");
            new BukkitRunnable() {
                Vector direction = player.getEyeLocation().getDirection().normalize().multiply(2);
                Location current = player.getEyeLocation().add(direction);
                @Override
                public void run() {
                    if (current.distance(player.getLocation()) <= 12 && current.getBlock().getType() == Material.AIR) {
                        current.getWorld().spawnParticle(Particle.CLOUD, current, 1, 0.2, 0.2, 0.2, 0);
                        current = current.add(direction);
                    } else {
                        for (Entity e : current.getWorld().getNearbyEntities(current, 1, 1, 1)) {
                            if (e instanceof LivingEntity && e != player) {
                                Haki haki = user.haki;
                                int hakiLevel = haki != null ? haki.getLevel() : 0;
                                ((LivingEntity) e).damage(getLevel("Shigan") + hakiLevel, (Entity) player);
                            }
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(OPhabs.getInstance(), 0, 1);

            activeShigan = false;
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeShigan = true;
                }
            }.runTaskLater(OPhabs.getInstance(), 200);
        }
    }

    private void soru() {
        Player player = user.getPlayer();
        if (player == null) return;
        if (soruDashes < getLevel("Soru")) {
            Location loc = player.getLocation().clone();
            loc.setY(loc.getY() - 1);
            if (loc.getBlock().getType() != Material.AIR) {
                player.setVelocity(player.getLocation().getDirection().multiply(2));
                soruDashes++;
                levelUpAbility("Soru", 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        soruDashes--;
                    }
                }.runTaskLater(OPhabs.getInstance(), 180);
            }
        }
    }

    private void geppo() {
        Player player = user.getPlayer();
        if (player == null) return;
        if (geppoJumps < getLevel("Geppo")) {
            Location loc = player.getLocation();
            loc.setY(loc.getY() - 1);
            player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(1));
            player.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0, 0, 0, 0.1);
            geppoJumps++;
            levelUpAbility("Geppo", 1);
            new BukkitRunnable() {
                @Override
                public void run() {
                    geppoJumps--;
                }
            }.runTaskLater(OPhabs.getInstance(), 120);
        }
    }

    public void rankyakuParticles(Location loc, Vector direction, Vector perpendicular) {
        loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(perpendicular), 10, perpendicular.getX(), perpendicular.getY(), perpendicular.getZ(), 0);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(perpendicular.clone().multiply(-1)), 10, perpendicular.getX(), perpendicular.getY(), perpendicular.getZ(), 0);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(direction.clone().multiply(0.2)), 10, perpendicular.getX(), perpendicular.getY(), perpendicular.getZ(), 0);
    }

    private void rankyaku() {
        Player player = user.getPlayer();
        if (player == null) return;
        if (activeRankyaku) {
            player.sendMessage("§a§" + getLevel("Rankyaku") + "§r§a");

            new BukkitRunnable() {
                Vector direction = player.getEyeLocation().getDirection().normalize().multiply(2);
                Location current = player.getEyeLocation().add(direction);
                boolean horizontal = player.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR;
                boolean first = true, sentinel = true;
                Vector perpendicular = new Vector();

                @Override
                public void run() {
                    if (first) {
                        if (horizontal) {
                            perpendicular = new Vector(-direction.getZ(), direction.getY(), direction.getX());
                        } else {
                            perpendicular = new Vector(direction.getX(), -direction.getZ(), direction.getY());
                        }
                        first = false;
                    }

                    var entities = current.getWorld().getNearbyEntities(current, 1, 1, 1);
                    entities.addAll(current.getWorld().getNearbyEntities(current.clone().add(perpendicular), 1, 1, 1));
                    entities.addAll(current.getWorld().getNearbyEntities(current.clone().add(perpendicular.clone().multiply(-1)), 1, 1, 1));
                    
                    if (current.distance(player.getLocation()) <= 12 && (entities.contains(player) || entities.isEmpty()) && sentinel) {
                        if (current.getBlock().getType() != Material.AIR || current.clone().add(perpendicular).getBlock().getType() != Material.AIR || current.clone().add(perpendicular.clone().multiply(-1)).getBlock().getType() != Material.AIR) {
                            if (current.getBlock().getType() != Material.AIR) {
                                if (current.getBlock().getType().getHardness() > Material.OAK_LOG.getHardness()) {
                                    sentinel = false;
                                } else {
                                    current.getBlock().breakNaturally();
                                }
                            }
                            if (current.clone().add(perpendicular).getBlock().getType() != Material.AIR) {
                                if (current.clone().add(perpendicular).getBlock().getType().getHardness() > Material.OAK_LOG.getHardness()) {
                                    sentinel = false;
                                } else {
                                    current.clone().add(perpendicular).getBlock().breakNaturally();
                                }
                            }
                            if (current.clone().add(perpendicular.clone().multiply(-1)).getBlock().getType() != Material.AIR) {
                                if (current.clone().add(perpendicular.clone().multiply(-1)).getBlock().getType().getHardness() > Material.OAK_LOG.getHardness()) {
                                    sentinel = false;
                                } else {
                                    current.clone().add(perpendicular.clone().multiply(-1)).getBlock().breakNaturally();
                                }
                            }
                        }
                        rankyakuParticles(current, direction, perpendicular);
                        current.add(direction);
                    } else {
                        sentinel = false;
                    }
                    if (!sentinel) {
                        for (Entity e : entities) {
                            if (e instanceof LivingEntity && e != player) {
                                ((LivingEntity) e).damage(5 + getLevel("Rankyaku") / 2, (Entity) player);
                            }
                        }
                        this.cancel();
                    }
                }
            }.runTaskTimer(OPhabs.getInstance(), 0, 1);

            activeRankyaku = false;
            new BukkitRunnable() {
                @Override
                public void run() {
                    activeRankyaku = true;
                }
            }.runTaskLater(OPhabs.getInstance(), 150);
        }
    }

    @Override
    public void onEntityDamageByUser(EntityDamageByEntityEvent event) {
        Player damager = (Player) event.getDamager();
        if (getLevel("Shigan") >= 1 && damager.getInventory().getItemInMainHand().getType() == Material.AIR) {
            event.setDamage(shigan(event.getDamage(), event.getEntity()));
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            if (getLevel("Tekkai") >= 1) {
                if (event.getDamage() <= getLevel("Tekkai")) {
                    event.setCancelled(true);
                }
                levelUpAbility("Tekkai", 0.5);
            } else if (getLevel("Kamie") >= 1) {
                Random rand = new Random();
                if (rand.nextInt(200) / 5 <= getLevel("Kamie")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = user.getPlayer();
        if (player == null) return;
        if (event.getAction() == Action.LEFT_CLICK_AIR) {
            if (!player.isSneaking()) {
                if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    if (user.haki != null && getLevel("Shigan") >= 3 && user.haki.getLevel() >= 5) {
                        tobuShigan();
                    }
                }
            } else {
                if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                    if (getLevel("Rankyaku") >= 1 && user.haki != null && user.haki.getLevel() >= 1) {
                        rankyaku();
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                reloadPlayer();
            }
        }.runTaskLater(OPhabs.getInstance(), 5);
    }

    public static class RokushikiStat {
        private int level = 0;
        private double exp = 0;

        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public double getExp() { return exp; }
        public void setExp(double exp) { this.exp = exp; }
    }
}
