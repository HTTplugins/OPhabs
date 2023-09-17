package fruits.logia;

import abilities.Ability;
import abilities.AbilitySet;
import libs.OPHAnimationLib;
import libs.OPHLib;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Gasu_Gasu extends Logia {

	public static int getFruitID()
	{
		return 1010;
	}

	public Gasu_Gasu(int id) {
		super(id, "Gasu_Gasu", "Gasu Gasu no Mi", "Gasu_Gasu", Particle.REDSTONE);

		// ----- BasicSet ----- //
		AbilitySet basicSet = new AbilitySet("Base Set");

		// Smoke Punch
		basicSet.addAbility(new Ability("Smoke Punch", this::a));

		// Summon Smokers
		basicSet.addAbility(new Ability("Summon smokers", this::a));


		//Smoke World
		basicSet.addAbility(new Ability("Smoke World", this::a));

		//Smoke Body
		basicSet.addAbility(new Ability("Smoke Body", this::poisonWorld));

		this.abilitySets.add(basicSet);
	}

	public void a() {

	}

	public void poisonWorld() {
		Player player = user.getPlayer();
		OPHAnimationLib.fog(Particle.CLOUD,player.getLocation(),15,10);
		OPHLib.affectAreaLivingEntitiesRunnable(player, 10, 2, 15, 15, 15, (LivingEntity ent)->{
			ent.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 4, false, true));
		});
	}
	@Override
	protected void onAddFruit() {

	}

	@Override
	protected void onRemoveFruit() {

	}


	@Override
	protected void logiaModeON() {

	}

	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		event.getDamager().sendMessage("michael");
	}
}
