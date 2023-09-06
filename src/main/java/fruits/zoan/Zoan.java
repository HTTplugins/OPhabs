package fruits.zoan;

import fruits.DevilFruit;
import runnables.OphRunnable;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import abilities.Ability;
import cosmetics.cosmeticsArmor;
import skin.skinsChanger;

public abstract class Zoan extends DevilFruit {
    protected String skinUrl="", skinName="";
    protected boolean transformed = false;
    protected Ability transform;

    public Zoan(int id, String name, String displayName, String commandName, String skinName, String skinUrl) {
        super(id, name, displayName, commandName);
        this.skinName = skinName;
        this.skinUrl = skinUrl;
        skinsChanger.setSkin(skinName, skinUrl);
        transform = new Ability("Transform", () -> {transformation();});
    }

    @Override
    protected void onRemoveFruit() {
    }

    public void transformation() {
        user.getPlayer().getWorld().playSound(user.getPlayer().getLocation(), "zoantrasform", 1, 1);
        Player player = user.getPlayer();
        if(!transformed) {
            skinsChanger.changeSkin(player, skinName);
            transformed = true;
            // setDamage(damageBonus*2);
            // setArmor(armorBonus*2);
        }
        else {
            transformed = false;
            skinsChanger.resetSkin(player);
            // setDamage(damageBonus/2);
            // setArmor(armorBonus/2);
        }
        // user.reloadStats();
    }
}
