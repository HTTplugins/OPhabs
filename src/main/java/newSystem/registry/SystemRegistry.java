package newSystem.registry;

import htt.ophabs.OPhabs;

import java.io.File;

public class SystemRegistry
{
    private static final String SYSTEM_REGISTRY_DATA_FOLDER = "Data";
    private static int ITEM_ID = 0;

    public static int NEXT_ID()
    {
        return ITEM_ID++;
    }

    public static final String REGISTRY_DATA_PATH = "plugins/OPhab/Data";

    public SystemRegistry()
    {
        File folder = new File(OPhabs.getInstance().getDataFolder() + SYSTEM_REGISTRY_DATA_FOLDER);

        if (!folder.exists())
            folder.mkdir();
    }
}
