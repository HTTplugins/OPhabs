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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// TODO: Se utilizará IFruitRegistry para los métodos de linkFruit, etc.
public class FruitRegistry implements IRegistry
{
    private static final String FRUIT_REGISTRY_DATA_PATH = "plugins/OPhabs/Data/FruitRegistry.json";
    private final HashMap<Integer, DevilFruit> fruitMap = new HashMap<>();
    private final HashMap<String, Integer> fruitNameIDMap = new HashMap<>();

    private int NEXT_FRUIT_ID = 1000; // ID INICIAL
    private int NEXT_ID()
    {
        return NEXT_FRUIT_ID++;
    }

    public FruitRegistry()
    {
        //
        // Crear todas las frutas
        //


        // NOTE: NO CAMBIAR EL ORDEN -> ItemStack físico del caster utiliza este ID
        DevilFruit[] fruits = {
            new FrutaX(NEXT_ID()),
        };

        for (DevilFruit fruit : fruits)
        {
            this.fruitMap.put(fruit.getID(), fruit);
            this.fruitNameIDMap.put(fruit.getName(), fruit.getID());
        }
    }

    public Map<Integer, DevilFruit> getFruitMap()
    {
        return Collections.unmodifiableMap(this.fruitMap);
    }

    public DevilFruit getFruit(int fruitID)
    {
        return this.fruitMap.get(fruitID);
    }

    public void linkFruitUser(String fruitName, OPUser user)
    {
        Integer fruitID = fruitNameIDMap.get(fruitName);

        if (fruitID == null)
            return;

        this.linkFruitUser(fruitID, user);
    }

    public void linkFruitUser(int fruitID, OPUser user)
    {
        DevilFruit fruit = this.fruitMap.get(fruitID);

        if (fruit != null)
        {
            if (fruit.getUser() != null)
            {
                OPUser currentUser = fruit.getUser();

                if (currentUser.getUUID() != user.getUUID())
                {
                    System.err.println("ERROR: El usuario " + currentUser.getPlayerName() + " es el poseedor de " + fruit.getName());
                    return;
                }
            }

            fruit.setUser(user);
            user.setDevilFruit(fruit);
            user.getActiveCasters().put(fruitID, new FruitCaster(fruit, OPhabs.scoreboardSystem));
        }
    }

    // PRE: Usuario con la fruta especificada y nada nulo
    public void unlinkFruitUser(int fruitID, OPUser user)
    {
        DevilFruit fruit = this.fruitMap.get(fruitID);

        // Unlink de la fruta y el usuario
        user.getActiveCasters().remove(fruitID).dispose();
        user.setDevilFruit(null);
        fruit.setUser(null);

        System.out.println("El usuario " + user.getPlayerName() + " ha perdido su fruta " + fruit.getName());
    }

    public void unlinkFruitUser(OPUser user)
    {
        DevilFruit fruit = user.getDevilFruit();

        if (fruit == null)
            return;

        this.unlinkFruitUser(fruit.getID(), user);
    }

    public void unlinkFruitUser(int fruitID)
    {
        DevilFruit fruit = this.fruitMap.get(fruitID);

        if (fruit == null || fruit.getUser() == null)
            return;

        this.unlinkFruitUser(fruitID, fruit.getUser());
    }

    public void unlinkFruitUser(DevilFruit fruit)
    {
        this.unlinkFruitUser(fruit.getID());
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

        for (DevilFruit fruit : this.fruitMap.values())
        {
            OPUser user = fruit.getUser();
            JsonObject userDataJSON = new JsonObject();

            if (user != null)
            {
                userDataJSON.addProperty("uuid", user.getUUID().toString());
                userDataJSON.addProperty("name", user.getPlayerName());
            }

            fruitDataJSON.add(fruit.getName(), userDataJSON);
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
