package abilitieSystem;
import castSystem.castIdentification;
import htt.layeredstructures.LayeredStructuresAPI;
import htt.ophabs.OPhabs;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class hie_hie extends paramecia {    //fruit_fruit is the fruit whose abilities you are going to program (command name).
    Random rand = new Random();
    boolean usingDrake = false;


    static boolean iceAge = false;

    public static List<Trident> tridents = new ArrayList<>();
    static List<Entity> cagedEnts = new ArrayList<>();

    public hie_hie(OPhabs plugin){
        super(plugin, 3, 3, 16, "hie_hie", "Hie Hie no Mi", "Hie Hie caster", 8, 1.7);
        abilitiesNames.add("Ice Age");
        abilitiesCD.add(0);
        abilitiesNames.add("Partisan");
        abilitiesCD.add(0);
        abilitiesNames.add("MorphSword");
        abilitiesCD.add(0);
        abilitiesNames.add("Ice Dragon");
        abilitiesCD.add(0);
    }

    public static void createIceBox(Entity entity) {
        Block entBlock = entity.getLocation().getBlock();

        entBlock.getRelative(1,0,0).setType(Material.BLUE_ICE);
        entBlock.getRelative(-1,0,0).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,0,1).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,0,-1).setType(Material.BLUE_ICE);

        entBlock.getRelative(1,1,0).setType(Material.BLUE_ICE);
        entBlock.getRelative(-1,1,0).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,1,1).setType(Material.BLUE_ICE);
        entBlock.getRelative(0,1,-1).setType(Material.BLUE_ICE);

        entBlock.getRelative(0,2,0).setType(Material.BLUE_ICE);
    }

    // ---------------------------------------------- AB 1 ---------------------------------------------------------------------
    public void ability1(){
        if(abilitiesCD.get(0) == 0){
            iceAge(user.getPlayer());
            abilitiesCD.set(0, 0); // Second parameter is cooldown in seconds.
        }
    }

    public void iceAge(Player player){

        iceAge = true;

        player.getWorld().playSound(player.getLocation(),"icecrack",1,1);

        new BukkitRunnable() {
            final World world = player.getWorld();
            double extension = 1;
            double convRate = 0.8;
            double xPlayer = player.getLocation().getX();
            double zPlayer = player.getLocation().getZ();
            double yPlayer = player.getLocation().getY();


            @Override
            public void run() {

                for(double i=xPlayer-extension; i < xPlayer+extension; i++){
                    for (double j=zPlayer-extension; j < zPlayer+extension; j++){
                        double convertProb = rand.nextDouble();
                        if(convertProb < convRate){

                            Location blockLoc = new Location(world,i,OPHLib.searchGround(i,j,yPlayer,world),j);
                            blockLoc.getBlock().setType(Material.BLUE_ICE);

                        }

                    }
                }

                extension++;
                convRate-=0.02;

                if(extension==50){
                    iceAge = false;
                    this.cancel();
                }

            }


        }.runTaskTimer(plugin,0,2);

        new BukkitRunnable(){
            @Override
            public void run() {

                for(Entity ent : user.getPlayer().getNearbyEntities(50,30,50)){
                    if(ent instanceof LivingEntity && !cagedEnts.contains(ent)){

                        if(ent.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.BLUE_ICE)){
                            ((LivingEntity) ent).damage(5);
                            cagedEnts.add(ent);
                            createIceBox(ent);
                        }
                    }
                }

                if(!iceAge){
                    cagedEnts.clear();
                    this.cancel();

                }

            }



        }.runTaskTimer(plugin,0,1);

    }



    // ---------------------------------------------- AB 2 ---------------------------------------------------------------------

    public void ability2(){
        if(abilitiesCD.get(1) == 0){
            partisan(user.getPlayer());

            abilitiesCD.set(1, 3); // Second parameter is cooldown in seconds.
        }
    }

    public void partisan(Player player) {
        player.getWorld().playSound(player.getLocation(),"icepartisan",1,1);


        Location headPos = player.getEyeLocation();
        Vector dir = headPos.getDirection();


        Location center = headPos.clone().add(dir).add(0, 2, 0);
        Location left = headPos.clone().add(dir.clone().rotateAroundY(Math.PI / 4)).add(0, 2, 0);
        Location right = headPos.clone().add(dir.clone().rotateAroundY(-Math.PI / 4)).add(0, 2, 0);

        Trident tridentCenter = (Trident) player.getWorld().spawnEntity(center, EntityType.TRIDENT);
        Trident tridentLeft = (Trident) player.getWorld().spawnEntity(left, EntityType.TRIDENT);
        Trident tridentRight = (Trident) player.getWorld().spawnEntity(right, EntityType.TRIDENT);

        tridentCenter.setShooter(player);
        tridentLeft.setShooter(player);
        tridentRight.setShooter(player);

        tridents.add(tridentCenter);
        tridents.add(tridentRight);
        tridents.add(tridentLeft);

        dir.multiply(1.5);
        tridentCenter.setVelocity(dir);
        tridentLeft.setVelocity(dir);
        tridentRight.setVelocity(dir);

        //particles animation:
        new BukkitRunnable(){
            int ticks = 0;
            @Override
            public void run() {
                ticks++;
                tridentCenter.getWorld().spawnParticle(Particle.BLOCK_DUST, tridentCenter.getLocation(), 10,  Material.BLUE_ICE.createBlockData() );
                tridentLeft.getWorld().spawnParticle(Particle.BLOCK_DUST, tridentLeft.getLocation(), 10,  Material.BLUE_ICE.createBlockData());
                tridentRight.getWorld().spawnParticle(Particle.BLOCK_DUST, tridentRight.getLocation(), 10,  Material.BLUE_ICE.createBlockData());
                if(tridents.isEmpty()) this.cancel();

                if(ticks > 200) this.cancel();
            }
        }.runTaskTimer(plugin,0,1);

    }


    // ---------------------------------------------- AB 3 ---------------------------------------------------------------------

    public void ability3(){
        if(abilitiesCD.get(2) == 0){
            //Here you call the ability
            morphCaster(user.getPlayer());
            abilitiesCD.set(2, 20); // Second parameter is cooldown in seconds.

        }
    }

    public void morphCaster(Player player){
        ItemStack caster;
        player.getWorld().playSound(player.getLocation(),"icecrack",1,1);
        //check which hand has the caster
        if (castIdentification.itemIsCaster(user.getPlayer().getInventory().getItemInMainHand(), user))
            caster = user.getPlayer().getInventory().getItemInMainHand();
        else
            caster = user.getPlayer().getInventory().getItemInOffHand();

        ItemMeta meta = caster.getItemMeta(), old = caster.getItemMeta().clone();


        old.setCustomModelData(fruit.getCustomModelDataId());
        meta.setCustomModelData(fruit.getCustomModelDataId()+1000);
        System.out.println(fruit.getCustomModelDataId());
        System.out.println(fruit.getCustomModelDataId() + 1000);

        /*
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
         */

        caster.setItemMeta(meta);

        // turn to custom model data(1) after 20 seconds
        new BukkitRunnable(){
            @Override
            public void run() {
                for(ItemStack item : user.getPlayer().getInventory().getContents()){

                    if(item !=null && castIdentification.itemIsCaster(item, user)){
                        item.setItemMeta(old);
                        break;
                    }
                }
            }
        }.runTaskLater(plugin, 20*20);
    }

    // ---------------------------------------------- AB 4 ---------------------------------------------------------------------

    public void ability4() {
        if (abilitiesCD.get(3) == 0) {


            drake(user.getPlayer());

            abilitiesCD.set(3, 0); // Second parameter is cooldown in seconds.
        }
    }

    public void drake(Player player){
        usingDrake = true;

        BlockFace facing = player.getFacing();


        player.getWorld().playSound(player.getLocation(),"icecrack",1,1);
        LayeredStructuresAPI.generateLayeredStructure("iceDrake", player.getLocation(),facing.toString(),0,2);

        new BukkitRunnable() {
            @Override
            public void run() {
                usingDrake = false;
            }
        }.runTaskLater(plugin,180); //180 = 40 + 140 (breath duration + blocks construction)



        OPHLib.breath(user,new Vector(0,14,0), new Vector(0,-0.9,0),40,18,140,2,0.7,30,2,2,"icebreathdrake" );

    }

    public void onPlayerMove(PlayerMoveEvent event) {
        super.onPlayerMove(event);

        World world = event.getPlayer().getWorld();
        double x = event.getFrom().getX();
        double y = event.getFrom().getY();
        double z = event.getFrom().getZ();
        float pitch = event.getTo().getPitch();
        float yaw = event.getTo().getYaw();


        if(usingDrake){
            event.setTo(new Location(world,x,y,z,yaw,pitch));

        }

    }

    public void onEntityDamage(EntityDamageEvent event){
        super.onEntityDamage(event);

        if(active && event.getCause().equals(EntityDamageEvent.DamageCause.FREEZE)){
            event.setCancelled(true);
        }

    }

    public void onProjectileHit(ProjectileHitEvent event) {

        Projectile tridente = event.getEntity();
        if (tridente instanceof Trident) {
            if(hie_hie.tridents.contains(tridente)){
                // Eliminar el tridente del mundo después de 3 segundos
                hie_hie.tridents.remove(tridente);
                Entity hitEnt = event.getHitEntity();

                if(hitEnt != null){
                    if(hitEnt instanceof LivingEntity){
                        ((LivingEntity) hitEnt).damage(5);
                        hie_hie.createIceBox(hitEnt);
                    }
                } else{
                    event.getHitBlock().setType(Material.BLUE_ICE);
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> tridente.remove(), 60L);
            }
        }
    }

}
