package newSystem.registry;

import newSystem.OPUser;
import newSystem.fruits.*;

import java.util.HashMap;

public class FruitRegistry implements IRegistry
{
    private static final String FRUIT_REGISTRY_DATA_PATH = "plugins/OPhabs/Data/FruitRegistry.json";
    private final HashMap<String, FruitData> fruitDataMap = new HashMap<>();

    public FruitRegistry()
    {
        //
        // Crear todas las frutas
        //

        DevilFruit[] fruits = {
                new FrutaX(),
        };

        for (DevilFruit fruit : fruits)
            fruitDataMap.put(fruit.getName(), new FruitData(fruit, SystemRegistry.NEXT_ID()));
    }

    public void linkFruitUser(String fruitName, OPUser user)
    {
        FruitData fruitData = fruitDataMap.get(fruitName);

        if (fruitData != null)
        {
            fruitData.fruit.setUser(user);
            user.setDevilFruit(fruitData.fruit);
        }
    }

    public void unlinkFruitUser(String fruitName, OPUser user)
    {
        FruitData fruitData = fruitDataMap.get(fruitName);

        if (fruitData != null)
        {
            user.setDevilFruit(null);
            fruitData.fruit.setUser(null);
        }
    }

    @Override
    public void initRegistry()
    {

    }

    @Override
    public void loadRegistry()
    {

    }

    @Override
    public void saveRegistry()
    {

    }
}
