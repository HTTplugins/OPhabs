package registry;

import htt.ophabs.OPhabs;
import registry.fruits.FruitRegistry;
import registry.fruits.IFruitRegistry;

import java.io.File;
import java.util.HashMap;

public class RegistrySystem implements IRegistry
{
    private static final String SYSTEM_REGISTRY_DATA_FOLDER = "Data";

    public static final String REGISTRY_DATA_PATH = "plugins/OPhab/Data";

    private final HashMap<Class<?>, IRegistry> registries;

    public RegistrySystem()
    {
        this.registries = new HashMap<>();

        //
        // Creación registros
        //

        FruitRegistry fruitRegistry = new FruitRegistry();

        //
        // Almacenar los registros
        //

        registries.put(IFruitRegistry.class, fruitRegistry);
    }

    public <T extends IRegistry> T getRegistry(Class<T> registryType)
    {
        IRegistry registry = registries.get(registryType);

        if (registry != null)
            return registryType.cast(registry);

        System.err.println("Requested registry is null " + registryType.toString());
        return null;
    }

    @Override
    public void initRegistry()
    {
        File folder = new File(OPhabs.getInstance().getDataFolder().getPath() + "/" + SYSTEM_REGISTRY_DATA_FOLDER);

        if (!folder.exists())
            folder.mkdir();

        for (IRegistry registry : this.registries.values())
            registry.initRegistry();
    }

    @Override
    public void loadRegistry()
    {
        for (IRegistry registry : this.registries.values())
            registry.loadRegistry();
    }

    @Override
    public void saveRegistry()
    {
        for (IRegistry registry : this.registries.values())
            registry.saveRegistry();
    }
}
