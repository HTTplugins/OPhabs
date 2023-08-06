package newSystem.registry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import htt.ophabs.OPhabs;
import newSystem.OPUser;
import newSystem.cast.FruitCaster;
import newSystem.fruits.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

// TODO: Se utilizará IFruitRegistry para los métodos de linkFruit, etc.
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
            fruitDataMap.put(fruit.getName(), new FruitData(fruit, RegistrySystem.NEXT_ID()));
    }

    public void linkFruitUser(String fruitName, OPUser user)
    {
        FruitData fruitData = fruitDataMap.get(fruitName);

        if (fruitData != null)
        {
            if (fruitData.fruit.getUser() != null)
            {
                OPUser currentUser = fruitData.fruit.getUser();

                if (currentUser.getUUID() != user.getUUID())
                {
                    System.err.println("ERROR: El usuario " + currentUser.getPlayerName() + " es el poseedor de " + fruitName);
                    return;
                }
            }

            fruitData.fruit.setUser(user);
            user.setDevilFruit(fruitData.fruit);
            user.getActiveCasters().put(fruitName, new FruitCaster(fruitData.fruit, OPhabs.scoreboardSystem));
        }
    }

    // PRE: Usuario con la fruta especificada y nada nulo
    public void unlinkFruitUser(String fruitName, OPUser user)
    {
        FruitData fruitData = fruitDataMap.get(fruitName);

        // Unlink de la fruta y el usuario
        user.getActiveCasters().remove(fruitName).dispose();
        user.setDevilFruit(null);
        fruitData.fruit.setUser(null);

        System.out.println("El usuario " + user.getPlayerName() + " ha perdido su fruta " + fruitName);
    }

    public void unlinkFruitUser(OPUser user)
    {
        DevilFruit fruit = user.getDevilFruit();

        if (fruit == null)
            return;

        this.unlinkFruitUser(fruit.getName(), user);
    }

    public void unlinkFruitUser(String fruitName)
    {
        FruitData fruitData = fruitDataMap.get(fruitName);

        if (fruitData == null || fruitData.fruit.getUser() == null)
            return;

        this.unlinkFruitUser(fruitName, fruitData.fruit.getUser());
    }

    public void unlinkFruitUser(DevilFruit fruit)
    {
        this.unlinkFruitUser(fruit.getName());
    }


    @Override
    public void loadRegistry()
    {
        File fruitDataFile = new File(FRUIT_REGISTRY_DATA_PATH);

        if (!fruitDataFile.exists())
            return;

        //
        // Cargar los UUID de los usuarios
        //
        try
        {
            // Cargar el JSON
            String content = new String(Files.readAllBytes(Paths.get(FRUIT_REGISTRY_DATA_PATH)));
            JsonObject fruitDataJSON = JsonParser.parseString(content).getAsJsonObject();

            // Crear las asociaciones
            for (String fruitName : fruitDataJSON.keySet()) {
                // Obtenemos el objeto JSON que representa al usuario de la fruta
                JsonObject userJSON = fruitDataJSON.getAsJsonObject(fruitName);

                if (userJSON.entrySet().isEmpty())
                    continue;

                try
                {
                    UUID uuid = UUID.fromString(userJSON.get("uuid").getAsString());
                    String playerName = userJSON.get("name").getAsString();

                    this.linkFruitUser(fruitName, OPhabs.newUsers.getOrSetUser(uuid, playerName));
                }
                catch (Exception e)
                {
                    System.err.println("Error cargando usuario de la fruta " + fruitName);
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void saveRegistry()
    {
        JsonObject fruitDataJSON = new JsonObject();

        for (FruitData fruitData : this.fruitDataMap.values())
        {
            OPUser user = fruitData.fruit.getUser();
            JsonObject userDataJSON = new JsonObject();

            if (user != null)
            {
                userDataJSON.addProperty("uuid", user.getUUID().toString());
                userDataJSON.addProperty("name", user.getPlayerName());
            }

            fruitDataJSON.add(fruitData.fruit.getName(), userDataJSON);
        }

        try (FileWriter fileWriter = new FileWriter(FRUIT_REGISTRY_DATA_PATH))
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            fileWriter.write(gson.toJson(fruitDataJSON));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
