package htt.ophabs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;

public class fileSystem {

    private static final String FRUIT_ASSOCIATION_PATH = "plugins/OPhabs/fruitAssociation.json";
    private static final String HAKI_ASSOCIATION_PATH = "plugins/OPhabs/hakiAssociation.json";
    private static JsonObject fruitAssociations, hakiAssociations;

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
    }

    private static void createDefaultFruitFile() {
        fruitAssociations = new JsonObject();
        fruitAssociations.addProperty("yami_yami", "none");
        fruitAssociations.addProperty("mera_mera", "none");
        fruitAssociations.addProperty("gura_gura", "none");
        fruitAssociations.addProperty("moku_moku", "none");
        fruitAssociations.addProperty("neko_neko_reoparudo", "none");
        fruitAssociations.addProperty("magu_magu", "none");
        fruitAssociations.addProperty("goro_goro", "none");
        fruitAssociations.addProperty("ishi_ishi", "none");
        fruitAssociations.addProperty("goru_goru", "none");
        fruitAssociations.addProperty("inu_inu_okuchi", "none");
        fruitAssociations.addProperty("ryu_ryu_allosaurs", "none");
        fruitAssociations.addProperty("ope_ope", "none");
        fruitAssociations.addProperty("zushi_zushi", "none");
        fruitAssociations.addProperty("suke_suke", "none");
        fruitAssociations.addProperty("hie_hie", "none");
        fruitAssociations.addProperty("bane_bane", "none");

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

    public static String getFruitLinkedUser(String fruit)  {

        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(FRUIT_ASSOCIATION_PATH)));

            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(fileContent).getAsJsonObject();

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

            System.out.println("El valor de linkedUser ha sido modificado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addHakiUser(String nombre, int level, int exp) {
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

            System.out.println("Se ha agregado un nuevo usuario de HAKI al fichero.");

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

                System.out.println("Se ha actualizado el usuario " + nombre + " en el fichero de HAKI.");
            } else {
                System.out.println("El usuario " + nombre + " no está registrado en el fichero de HAKI.");
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
}
