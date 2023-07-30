package htt.ophabs;

import abilitieSystem.abilityUser;
import abilitieSystem.df;
import abilitieSystem.rokushiki;
import abilitieSystem.stat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import commands.weaponShop;
import com.google.gson.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;



public class fileSystem {
    private static final String FRUIT_ASSOCIATION_PATH = "plugins/OPhabs/fruitAssociation.json";
    private static final String HAKI_ASSOCIATION_PATH = "plugins/OPhabs/hakiAssociation.json";
    private static final String ROKUSHIKI_ASSOCIATION_PATH =
        "plugins/OPhabs/rokushikiAssociation.json";
    private static JsonObject fruitAssociations, hakiAssociations, rokushikiAssociations;

    private static final String LAYERED_BUILDINGS_PATH = "plugins/OPhabs/";
    public static void loadFileSystem() {

        File jsonFruitFile = new File(FRUIT_ASSOCIATION_PATH);
        if (!jsonFruitFile.exists()) {
            System.out.println("Generando un nuevo fichero de frutas");
            createDefaultFruitFile();
        } else {
            System.out.println("Cargando un fichero de frutas");
            readFromFruitFile();
        }

        File jsonhakiFile = new File(HAKI_ASSOCIATION_PATH);
        if (!jsonhakiFile.exists()) {
            System.out.println("Generando un nuevo fichero de haki");
            createDefaultHakiFile();
        } else {
            System.out.println("Cargando un fichero de haki");
            readFromHakiFile();
        }

        File jsonRokushikiFile = new File(ROKUSHIKI_ASSOCIATION_PATH);
        if (!jsonRokushikiFile.exists()) {
            System.out.println("Generando un nuevo fichero de rokushiki");
            createDefaultRokushikiFile();
        } else {
            System.out.println("Cargando un fichero de rokushiki");
            readFromRokushikiFile();
        }
    }

    private static void createDefaultFruitFile() {
        fruitAssociations = new JsonObject();
        for (df a : OPhabs.abilitiesList) fruitAssociations.addProperty(a.getName(), "none");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(fruitAssociations);

        try (FileWriter fileWriter = new FileWriter(FRUIT_ASSOCIATION_PATH)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultHakiFile() {
        hakiAssociations = new JsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(hakiAssociations);

        try (FileWriter fileWriter = new FileWriter(HAKI_ASSOCIATION_PATH)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefaultRokushikiFile() {
        rokushikiAssociations = new JsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(rokushikiAssociations);

        try (FileWriter fileWriter = new FileWriter(ROKUSHIKI_ASSOCIATION_PATH)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFromFruitFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FRUIT_ASSOCIATION_PATH)));
            fruitAssociations = JsonParser.parseString(content).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFromHakiFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(HAKI_ASSOCIATION_PATH)));
            hakiAssociations = JsonParser.parseString(content).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readFromRokushikiFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));
            rokushikiAssociations = JsonParser.parseString(content).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFruitLinkedUser(String fruit) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(FRUIT_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();
            if (!jsonObject.has(fruit)) {
                fruitAssociations.addProperty(fruit, "none");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(fruitAssociations);

                try (FileWriter fileWriter = new FileWriter(FRUIT_ASSOCIATION_PATH)) {
                    fileWriter.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return jsonObject.get(fruit).getAsString();

        } catch (IOException e) {
            e.printStackTrace();
            return "none";
        }
    }
    public static void updateFruitLinkedUser(String fruit, String linkedUser) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(FRUIT_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            jsonObject.addProperty(fruit, linkedUser);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(FRUIT_ASSOCIATION_PATH);
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addHakiUser(String nombre, int level, double exp) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(HAKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            JsonObject characterObject = new JsonObject();
            characterObject.addProperty("Level", level);
            characterObject.addProperty("Exp", exp);

            jsonObject.add(nombre, characterObject);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(HAKI_ASSOCIATION_PATH);
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateHakiUser(String nombre, int newLevel, int newExp) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(HAKI_ASSOCIATION_PATH)));
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre)) {
                JsonObject userObject = jsonObject.getAsJsonObject(nombre);

                userObject.addProperty("Level", newLevel);
                userObject.addProperty("Exp", newExp);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                FileWriter fileWriter = new FileWriter(HAKI_ASSOCIATION_PATH);
                fileWriter.write(gson.toJson(jsonObject));
                fileWriter.close();

            } else {
                System.out.println(
                    "El usuario " + nombre + " no está registrado en el fichero de HAKI.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getHakiUserLevel(String nombre) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(HAKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre)) {
                JsonObject userObject = jsonObject.getAsJsonObject(nombre);

                if (userObject.has("Level")) {
                    return userObject.get("Level").getAsInt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static int getHakiUserExp(String nombre) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(HAKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre)) {
                JsonObject userObject = jsonObject.getAsJsonObject(nombre);

                if (userObject.has("Exp")) {
                    return userObject.get("Exp").getAsInt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static Set<String> getHakiUserKeys() {
        try {

            String fileContent = new String(Files.readAllBytes(Paths.get(HAKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            return jsonObject.keySet();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptySet();
    }

    public static void addRokushikiUser(String nombre, String ability, int level, double exp) {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre))
                return;

            JsonObject characterObject = new JsonObject();
            characterObject.addProperty("Level", level);
            characterObject.addProperty("Exp", exp);

            JsonObject abilityObject = new JsonObject();
            abilityObject.add(ability, characterObject);
            jsonObject.add(nombre, abilityObject);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(ROKUSHIKI_ASSOCIATION_PATH);
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRokushikiUser(String nombre, Map<String, stat> ability) {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre))
                return;
            for (Map.Entry<String, stat> skill : ability.entrySet()) {
                JsonObject abilityObject = new JsonObject();

                JsonObject characterObject = new JsonObject();

                characterObject.addProperty("Level", skill.getValue().getLevel());
                characterObject.addProperty("Exp", skill.getValue().getExp());
                abilityObject.add(skill.getKey(), characterObject);
                jsonObject.add(nombre, abilityObject);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(ROKUSHIKI_ASSOCIATION_PATH);
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addRokushikiAbilityToUser(
        String nombre, String ability, int level, double exp) {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre)) {
                JsonObject userObject = jsonObject.getAsJsonObject(nombre);
                JsonObject abilityObject = new JsonObject();

                if (!userObject.has(ability)) {
                    abilityObject.addProperty("Level", level);
                    abilityObject.addProperty("Exp", exp);

                    userObject.add(ability, abilityObject);
                } else {
                    System.out.println("El usuario ya tiene esa habilidad.");
                    return;
                }
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(ROKUSHIKI_ASSOCIATION_PATH);
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateRokushikiUser(
        String nombre, String ability, int newLevel, double newExp) {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre)) {
                JsonObject userObject = jsonObject.getAsJsonObject(nombre);
                if (userObject.has(ability)) {
                    JsonObject abilityObject = userObject.getAsJsonObject(ability);
                    abilityObject.addProperty("Level", newLevel);
                    abilityObject.addProperty("Exp", newExp);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    FileWriter fileWriter = new FileWriter(ROKUSHIKI_ASSOCIATION_PATH);
                    fileWriter.write(gson.toJson(jsonObject));
                    fileWriter.close();

                } else {
                    System.out.println("El usuario " + nombre + " no tiene esa habilidad.");
                    return;
                }
                // System.out.println("Se ha actualizado el usuario " + nombre + " en el fichero de
                // HAKI.");
            } else {
                System.out.println(
                    "El usuario " + nombre + " no está registrado en el fichero de Rokushiki.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateRokushikiUser(String nombre, Map<String, stat> ability) {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre)) {
                JsonObject userObject = jsonObject.getAsJsonObject(nombre);

                for (Map.Entry<String, stat> skill : ability.entrySet()) {
                    if (userObject.has(skill.getKey())) {
                        JsonObject abilityObject = userObject.getAsJsonObject(skill.getKey());
                        abilityObject.addProperty("Level", skill.getValue().getLevel());
                        abilityObject.addProperty("Exp", skill.getValue().getExp());

                        userObject.add(skill.getKey(), abilityObject);
                    } else {
                        System.out.println(
                            "El usuario " + nombre + " no tiene la habilidad " + skill.getKey());
                        return;
                    }
                }

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                FileWriter fileWriter = new FileWriter(ROKUSHIKI_ASSOCIATION_PATH);
                fileWriter.write(gson.toJson(jsonObject));
                fileWriter.close();

            } else {
                System.out.println(
                    "El usuario " + nombre + " no está registrado en el fichero de Rokushiki.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getRokushikiAbilityUserLevel(String nombre, String ability) {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre)) {
                JsonObject userObject = jsonObject.getAsJsonObject(nombre);

                if (userObject.has(ability)) {
                    JsonObject abilityObject = jsonObject.getAsJsonObject(ability);

                    if (abilityObject.has("Level")) {
                        return userObject.get("Level").getAsInt();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static double getRokushikiUserExp(String nombre, String ability) {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(nombre)) {
                JsonObject userObject = jsonObject.getAsJsonObject(nombre);

                if (userObject.has(ability)) {
                    JsonObject abilityObject = jsonObject.getAsJsonObject(ability);

                    if (abilityObject.has("Exp")) {
                        return userObject.get("Exp").getAsDouble();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static Set<String> getRokushikiUsersKeys() {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            return jsonObject.keySet();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptySet();
    }

    public static Map<String, stat> getRokushikiAbilities(String username) {
        try {
            String fileContent =
                new String(Files.readAllBytes(Paths.get(ROKUSHIKI_ASSOCIATION_PATH)));
            Map<String, stat> skills = new HashMap<>();

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

            if (jsonObject.has(username)) {
                JsonObject userObject = jsonObject.getAsJsonObject(username);

                for (String key : userObject.keySet()) {
                    JsonObject abilityObject = userObject.getAsJsonObject(key);
                    skills.put(key,
                        new stat(abilityObject.get("Level").getAsInt(),
                            abilityObject.get("Exp").getAsDouble()));
                }
            }
            return skills;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyMap();
    }

    public static void saveConfig(OPhabs plugin) {
        for (Map.Entry<String, abilityUser> entry : plugin.users.entrySet()) {
            String nombre = entry.getKey();
            abilityUser user = entry.getValue();

            if (user.hasHaki()) {
                updateHakiUser(nombre, user.getHakiLevel(), user.getHakiExp());
            }
            if (user.hasFruit()) {
                updateFruitLinkedUser(user.getDFAbilities().getName(), nombre);
            }
            if (user.hasRokushiki()) {
                updateRokushikiUser(nombre, user.getRokushikiAbilities().getRokushikiStats());
            }
        }
    }



   public static class layedBlock{
        private int x;
        private int y;
        private int z;
        private Material blockMaterial;

       public int getX() {
           return x;
       }

       public int getY() {
           return y;
       }

       public int getZ() {
           return z;
       }

       public Material getBlockMaterial() {
           return blockMaterial;
       }

       layedBlock(int x, int y, int z, String material){
            this.x = x;
            this.y = y;
            this.z = z;
            this.blockMaterial = Material.matchMaterial(material);


        }
    }


    public static ArrayList<ArrayList<layedBlock>> loadLayers(String buildingJSON) {
        ArrayList<ArrayList<layedBlock>> layersArray = new ArrayList<>();

        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(LAYERED_BUILDINGS_PATH + buildingJSON)));
            JsonParser parser = new JsonParser();
            JsonObject document = parser.parse(fileContent).getAsJsonObject();

            int numLayers = document.get("numLayers").getAsInt();

            for (int i = 0; i < numLayers; i++) {

                JsonObject layerObject = document.getAsJsonObject("Layers").getAsJsonObject(Integer.toString(i));

                int numBlocks = layerObject.get("numBlocks").getAsInt();
                JsonArray blocks = layerObject.getAsJsonArray("blocks");

                ArrayList<layedBlock> layer = new ArrayList<>();

                for (int j = 0; j < numBlocks; j++) {
                    JsonArray blockArray = blocks.get(j).getAsJsonArray();

                    int x = blockArray.get(0).getAsInt();
                    int y = blockArray.get(1).getAsInt();
                    int z = blockArray.get(2).getAsInt();
                    String blockType = blockArray.get(3).getAsString();

                    layedBlock block = new layedBlock(x,y,z,blockType);
                    layer.add(block);

                }

                layersArray.add(layer);





            }
            return layersArray;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<Block> saveLayerStructure(Location loc1, Location loc2, int layerNumber, ArrayList<Block> previsLayer) {
        World world = loc1.getWorld();
        double minX, maxX, minY, maxY, minZ, maxZ;
        ArrayList<Block> newLayer = new ArrayList<>();

        if (previsLayer != null && layerNumber != 0) {
            for (Block block : previsLayer) {
                block.setType(Material.AIR);
            }
        }

        if (loc1.getX() < loc2.getX()) {
            minX = loc1.getX();
            maxX = loc2.getX();
        } else {
            minX = loc2.getX();
            maxX = loc1.getX();
        }

        if (loc1.getY() < loc2.getY()) {
            minY = loc1.getY();
            maxY = loc2.getY();

        } else {
            minY = loc2.getY();
            maxY = loc1.getY();
        }

        if(loc1.getZ() < loc2.getZ()) {
            minZ = loc1.getZ();
            maxZ = loc2.getZ();
        } else {
            minZ = loc2.getZ();
            maxZ = loc1.getZ();
        }

        for(double x = minX; x <= maxX; x++){
            for(double y = minY; y <= maxY; y++){
                for(double z = minZ; z <= maxZ; z++){
                    Block block = world.getBlockAt((int)x, (int)y, (int)z);
                    if(block.getType() != Material.AIR){
                        newLayer.add(block);
                    }





                }
            }
        }





        return null;
    }
}
