package abilitieSystem;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class abilitiesIdentification {

    public static int aNumberYami = 2, aNumberMera = 0, aNumberGura = 4, aNumberMoku = 3, aNumberNekoReoparudo = 2; //number of abilities.

    public static
            List<String>  namesYami = new ArrayList<>(),
            namesMera = new ArrayList<>(),
            namesGura=new ArrayList<>(),
            namesMoku=new ArrayList<>(),
            namesNekoReoparudo=new ArrayList<>();

    public static void initialiceNames(){
        namesYami.add(ChatColor.BLACK + "" + ChatColor.BOLD + "Yami Yami");
        namesYami.add( "Black Void");
        namesYami.add( "Abilitie name 2");

        namesMera.add(ChatColor.RED + "" + ChatColor.BOLD +"Mera Mera");
        namesMera.add("Abilitie name 1");
        namesMera.add("Abilitie name 2");

        namesGura.add(ChatColor.WHITE + "" + ChatColor.BOLD +"Gura Gura");
        namesGura.add("Abilitie name 1");
        namesGura.add("Abilitie name 2");

        namesMoku.add(ChatColor.WHITE + "" + ChatColor.BOLD +"Moku Moku");
        namesMoku.add( "Abilitie name 1");
        namesMoku.add("Abilitie name 2");

        namesNekoReoparudo.add(ChatColor.YELLOW + "" + ChatColor.BOLD +"Neko Neko");
        namesNekoReoparudo.add("Abilitie name 1");
        namesNekoReoparudo.add("Abilitie name 2");

    }

}
