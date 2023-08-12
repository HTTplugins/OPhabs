package registry.fruits;

import users.OPUser;
import fruits.DevilFruit;
import registry.IRegistry;

import java.util.Map;

public interface IFruitRegistry extends IRegistry
{
    public Map<Integer, DevilFruit> getFruitMap();
    public DevilFruit getFruit(int fruitID);
    public boolean linkFruitUser(String fruitName, OPUser user);
    public boolean linkFruitUser(int fruitID, OPUser user);

    // PRE: Usuario con la fruta especificada y nada nulo
    public boolean unlinkFruitUser(int fruitID, OPUser user);
    public boolean unlinkFruitUser(OPUser user);
    public boolean unlinkFruitUser(int fruitID);
    public boolean unlinkFruitUser(DevilFruit fruit);
}
