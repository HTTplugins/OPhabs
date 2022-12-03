package fruitSystem;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class fruit_player {

    String fruitName;
    Player player;

    Item caster;

    static List<fruit_player> associations = new ArrayList<>();

    fruit_player(String fruitName, Player player){
        this.fruitName = fruitName;
        this.player = player;
    }

    public static void associateFruit(fruit_player association){
        associations.add(association);
        System.out.println("asociado:");
        System.out.println(associations.get(0).fruitName);
        System.out.println(associations.get(0).player.getName());
    }


}
