package haki;

import events.IEventProcessor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import users.OPUser;

import java.util.Random;

public class Haki implements IEventProcessor {
    private OPUser user;
    private int level = 0;
    private int exp = 0;
    private double health = 20;
    private double armorBonus = 0;
    private double damageBonus = 0;

    public Haki(OPUser user) {
        this.user = user;
        reloadPlayer();
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public void setLevel(int level) {
        if (level > 0) {
            this.level = level;
            user.reloadStats();
            Player player = user.getPlayer();
            if (player != null && player.isOnline()) {
                player.sendMessage("§a§lHaki§r§a level: " + level);
            }
        }
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void levelUp() {
        if (exp >= (400 + level * 100)) {
            exp = exp - (400 + level * 100);
            level++;
            Player player = user.getPlayer();
            if (player != null && player.isOnline()) {
                player.sendMessage("§a§lHaki§r§a level up!");
                player.sendMessage("§a§lHaki§r§a level: " + level);
            }
            user.reloadStats();
        }
    }

    public void upHealth() {
        Player player = user.getPlayer();
        if (player != null) {
            player.setMaxHealth(20 + 2 * level);
            health = player.getMaxHealth();
        }
    }

    public void upArmor() {
        if (level >= 2) {
            armorBonus = (1.0 * level) / 2;
        }
    }

    public void upDamage() {
        if (level >= 5) {
            damageBonus = (1.0 * level) / 2;
        }
    }

    public void reloadPlayer() {
        upHealth();
        upArmor();
        upDamage();
    }

    @Override
    public void onEntityDamageByUser(EntityDamageByEntityEvent event) {
        if (level >= 5) {
            Player damager = (Player) event.getDamager();
            if (damager.getInventory().getItemInMainHand().getType() == Material.AIR) {
                event.setDamage(event.getDamage() + 5);
            }
        }
        exp += (int) event.getDamage();
        levelUp();
    }

    @Override
    public void onItemDamage(PlayerItemDamageEvent event) {
        if (level >= 5) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {}

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        Player player = user.getPlayer();
        if (player == null) return;
        
        Random rand = new Random();
        if (rand.nextInt(100) <= level) {
            event.setDamage(0);
        }
    }

    @Override
    public void onEntityShootBow(EntityShootBowEvent event) {}
}
