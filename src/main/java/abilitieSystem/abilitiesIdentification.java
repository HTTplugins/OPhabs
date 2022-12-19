package abilitieSystem;

import org.bukkit.ChatColor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class abilitiesIdentification {

    public static int aNumberYami = 4, aNumberMera = 4, aNumberGura = 4, aNumberMoku = 4, aNumberNekoReoparudo = 4, aNumberMagu = 4; //number of abilities.

    public static
            List<String>  namesYami = new ArrayList<>(),
            namesMera = new ArrayList<>(),
            namesGura=new ArrayList<>(),
            namesMoku=new ArrayList<>(),
            namesNekoReoparudo=new ArrayList<>(),
            namesMagu = new ArrayList<>();

    public static void initialiceNames(){
        namesYami.add(ChatColor.BLACK + "" + ChatColor.BOLD + "Yami Yami");
        namesYami.add( "BlackVoid");
        namesYami.add( "LivingVoid");
        namesYami.add( "Abilitie3");
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
