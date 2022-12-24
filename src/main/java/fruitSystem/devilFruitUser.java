package fruitSystem;

import org.bukkit.entity.Player;
import abilitieSystem.abilities;

public class devilFruitUser{
    private Player player;
    private devilFruit Fruit;
    private Integer actual;
    protected abilities ability;

    private boolean awakened;


    public devilFruitUser(Player player, devilFruit Fruit, abilities ability){
        this.player = player;
        this.Fruit=Fruit;
        this.ability = ability;
        this.ability.setUser(this);
        actual = 0;
        awakened=false;
    }

    public Player getPlayer(){
        return player;
    }

    public void playerAwakens(){
        awakened=true;
    }
		
    public boolean isAwaken(){
        return awakened;
    }
    public devilFruit getFruit(){
        return Fruit;
    }
    public void switchAbility(){
        actual++;
        actual = actual % ability.getAbilitiesNames().size();
    }
    public void abilityActive(){
        switch (actual){
            case 0:
                ability.ability1();
                break;
            case 1:
                ability.ability2();
                break;
            case 2:
                ability.ability3();
                break;
            case 3:
                ability.ability4();
                break;
            case 4:
                ability.ability5();
                break;
            case 5:
                ability.ability6();
                break;
        }

    }
}
