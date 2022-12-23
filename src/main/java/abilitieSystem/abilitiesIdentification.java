package abilitieSystem;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class abilitiesIdentification {

    public static int aNumberYami = 4, aNumberMera = 4, aNumberGura = 4, aNumberMoku = 4, aNumberNekoReoparudo = 4, aNumberMagu = 4; //number of abilities.

    public static Particle.DustOptions yamiDO = new Particle.DustOptions(Color.BLACK,1.0F);
    public static Particle yamiParticle = Particle.REDSTONE;
    public static
            List<String>  namesYami = new ArrayList<>(),
            namesMera = new ArrayList<>(),
            namesGura=new ArrayList<>(),
            namesMoku=new ArrayList<>(),
            namesNekoReoparudo=new ArrayList<>(),
            namesMagu = new ArrayList<>();

    public static void initialiceNames(){
        namesYami.add(ChatColor.BLACK + "" + ChatColor.BOLD + "Yami Yami");
        namesYami.add( "Black Void");
        namesYami.add( "Living Void");
        namesYami.add( "Liberate Absorptions");
        namesYami.add( "Abilitie4");

        namesMera.add(ChatColor.RED + "" + ChatColor.BOLD +"Mera Mera");
        namesMera.add("Fire Pool");
        namesMera.add("Fireball Storm");
        namesMera.add("Abilitie3");
        namesMera.add("Abilitie4");

        namesGura.add(ChatColor.WHITE + "" + ChatColor.BOLD +"Gura Gura");
        namesGura.add("Earthquake");
        namesGura.add("CreateWave");
        namesGura.add("HandVibration");
        namesGura.add("ExpansionWave");

        namesMoku.add(ChatColor.WHITE + "" + ChatColor.BOLD +"Moku Moku");
        namesMoku.add( "LogiaBody");
        namesMoku.add( "SmokeLegs");
        namesMoku.add( "SummonSmoker");
        namesMoku.add( "Abilitie4");


        namesNekoReoparudo.add(ChatColor.YELLOW + "" + ChatColor.BOLD +"Neko Neko");
        namesNekoReoparudo.add("Transformation");
        namesNekoReoparudo.add("FrontAttack");
        namesNekoReoparudo.add("Abilitie3");
        namesNekoReoparudo.add("Abilitie4");

        namesMagu.add(ChatColor.BLUE + "" + ChatColor.BOLD +"MAGU MAGU");
        namesMagu.add("Abilitie1");
        namesMagu.add("Abilitie2");
        namesMagu.add("Abilitie3");
        namesMagu.add("Abilitie4");
    }

}
