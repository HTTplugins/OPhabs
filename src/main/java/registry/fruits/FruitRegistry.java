package registry.fruits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fruits.logia.Goro_Goro;
import fruits.logia.Mera_Mera;
import fruits.logia.Moku_Moku;
import fruits.logia.Magu_Magu;
import fruits.logia.Yami_Yami;
import fruits.paramecia.Ope_Ope;
import fruits.paramecia.Zushi_Zushi;
import fruits.paramecia.Hie_Hie;
import fruits.paramecia.Gura_Gura;
import fruits.paramecia.Suke_Suke;
import fruits.paramecia.Ishi_Ishi;
import fruits.paramecia.Goru_Goru;
import fruits.paramecia.Bane_Bane;
import fruits.zoan.Neko_Neko_Reoparudo;
import fruits.zoan.Inu_Inu_Okuchi;
import fruits.zoan.Inu_Inu_Urufu;
import fruits.zoan.Ryu_Ryu_Allosaurs;
import htt.ophabs.OPhabs;
import users.OPUser;
import cast.Caster;
import cast.FruitCaster;
import fruits.DevilFruit;
import registry.fruits.IFruitRegistry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FruitRegistry implements IFruitRegistry
{
    private static final String FRUIT_REGISTRY_DATA_PATH = "plugins/OPhabs/Data/FruitRegistry.json";
    private final HashMap<Integer, DevilFruit> fruitMap = new HashMap<>();
    private final HashMap<String, Integer> fruitNameIDMap = new HashMap<>();

    public FruitRegistry()
    {
        //
        // Crear todas las frutas
        //

        DevilFruit[] fruits = {
            new Bane_Bane(Bane_Bane.getFruitID()),
            new Zushi_Zushi(Zushi_Zushi.getFruitID()),
            new Ope_Ope(Ope_Ope.getFruitID()),
            new Moku_Moku(Moku_Moku.getFruitID()),
            new Goro_Goro(Goro_Goro.getFruitID()),
            new Mera_Mera(Mera_Mera.getFruitID()),
            new Hie_Hie(Hie_Hie.getFruitID()),
            new Gura_Gura(Gura_Gura.getFruitID()),
            new Magu_Magu(Magu_Magu.getFruitID()),
            new Yami_Yami(Yami_Yami.getFruitID()),
            new Suke_Suke(Suke_Suke.getFruitID()),
            new Ishi_Ishi(Ishi_Ishi.getFruitID()),
            new Goru_Goru(Goru_Goru.getFruitID()),
            new Inu_Inu_Urufu(Inu_Inu_Urufu.getFruitID()),
            new Inu_Inu_Okuchi(Inu_Inu_Okuchi.getFruitID()),
            new Neko_Neko_Reoparudo(Neko_Neko_Reoparudo.getFruitID()),
            new Ryu_Ryu_Allosaurs(Ryu_Ryu_Allosaurs.getFruitID())
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

    public boolean linkFruitUser(String fruitName, OPUser user)
    {
        Integer fruitID = fruitNameIDMap.get(fruitName);

        if (fruitID == null)
            return false;

        return this.linkFruitUser(fruitID, user);
    }

    public boolean linkFruitUser(int fruitID, OPUser user)
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
                    return false;
                }
            }

            if (user.getDevilFruit() != null)
                return false;

            fruit.setUser(user);
            user.setDevilFruit(fruit);
            user.getActiveCasters().put(fruitID, new FruitCaster(fruit, OPhabs.scoreboardSystem));
            return true;
        }

        return false;
    }

    public boolean unlinkFruitUser(int fruitID, OPUser user)
    {
        DevilFruit fruit = this.fruitMap.get(fruitID);

        if (fruit == null || fruit.getUser() == null)
            return false;

        // Actually perform the unlink
        fruit.setUser(null);
        user.setDevilFruit(null);
        user.getActiveCasters().remove(fruitID);
        fruit.onRemoveFruit();
        return true;
    }

    public boolean unlinkFruitUser(OPUser user)
    {
        DevilFruit fruit = user.getDevilFruit();

        if (fruit == null)
            return false;

        return this.unlinkFruitUser(fruit.getID(), user);
    }

    public boolean unlinkFruitUser(int fruitID)
    {
        DevilFruit fruit = this.fruitMap.get(fruitID);

        if (fruit == null || fruit.getUser() == null)
            return false;

        return this.unlinkFruitUser(fruitID, fruit.getUser());
    }

    public boolean unlinkFruitUser(DevilFruit fruit)
    {
        return this.unlinkFruitUser(fruit.getID());
    }

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
