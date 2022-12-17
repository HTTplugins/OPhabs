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
        namesYami.add(ChatColor.BLACK + "Yami Yami");
        namesYami.add(ChatColor.BLACK + "Black Void");
        namesYami.add(ChatColor.BLACK + "Abilitie name 2");

        namesMera.add(ChatColor.RED + "Mera Mera");
        namesMera.add(ChatColor.RED + "Abilitie name 1");
        namesMera.add(ChatColor.RED + "Abilitie name 2");

        namesGura.add(ChatColor.WHITE + "Gura Gura");
        namesGura.add(ChatColor.WHITE + "Abilitie name 1");
        namesGura.add(ChatColor.WHITE + "Abilitie name 2");

        namesMoku.add(ChatColor.WHITE + "Moku Moku");
        namesMoku.add(ChatColor.WHITE + "Abilitie name 1");
        namesMoku.add(ChatColor.WHITE + "Abilitie name 2");

        namesNekoReoparudo.add(ChatColor.YELLOW + "Neko Neko");
        namesNekoReoparudo.add(ChatColor.YELLOW + "Abilitie name 1");
        namesNekoReoparudo.add(ChatColor.YELLOW + "Abilitie name 2");

    }

}
