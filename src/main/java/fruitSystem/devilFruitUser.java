package fruitSystem;

import org.bukkit.entity.Player;


public class devilFruitUser{
    private Player user;
    private devilFruit Fruit;
    private boolean logia, awakened;

    public devilFruitUser(Player player, devilFruit Fruit, boolean logia){
        user = player;
        this.Fruit=Fruit;
        this.logia=logia;
        awakened=false;
    }

    public void playerAwakens(){
        awakened=true;
    }
					
    public static boolean isAwaken(){
        return awakened;
    }
    public static boolean isLogia(){
        return logia;
    }
    public static devilFruit getFruit(){
        return Fruit;
    }

}
