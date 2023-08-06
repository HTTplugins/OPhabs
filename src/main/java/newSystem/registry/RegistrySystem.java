package newSystem.registry;

import htt.ophabs.OPhabs;

import java.io.File;
import java.util.ArrayList;

// TODO: Proporcionará una interfaz a modo de service locator a través del tipo de la interfaz solicitada
// TODO: EJEMPLO: registry.getRegistry<IFruitRegistry>();
public class RegistrySystem implements IRegistry
{
    private static final String SYSTEM_REGISTRY_DATA_FOLDER = "Data";
    private static int ITEM_ID = 0;

    public static int NEXT_ID()
    {
        return ITEM_ID++;
    }

    public static final String REGISTRY_DATA_PATH = "plugins/OPhab/Data";

    private final ArrayList<IRegistry> registries;

    public final FruitRegistry fruitRegistry;

    public RegistrySystem()
    {
        this.registries = new ArrayList<>();

        //
        // Creación registros
        //

        this.fruitRegistry = new FruitRegistry();

        //
        // Almacenar los registros
        //

        registries.add(this.fruitRegistry);
    }

    @Override
    public void initRegistry()
    {
        File folder = new File(OPhabs.getInstance().getDataFolder().getPath() + "/" + SYSTEM_REGISTRY_DATA_FOLDER);

        if (!folder.exists())
            folder.mkdir();

        for (IRegistry registry : this.registries)
            registry.initRegistry();
    }

    @Override
    public void loadRegistry()
    {
        for (IRegistry registry : this.registries)
            registry.loadRegistry();
    }

    @Override
    public void saveRegistry()
    {
        for (IRegistry registry : this.registries)
            registry.saveRegistry();
    }
}
