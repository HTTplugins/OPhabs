package newSystem;

import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserList
{
    private HashMap<UUID, OPUser> users;

    public UserList()
    {
        this.users = new HashMap<>();
    }

    public OPUser getOrSetUser(UUID uuid, String playerName)
    {
        if (!users.containsKey(uuid))
            users.put(uuid, new OPUser(uuid, playerName));

        return users.get(uuid);
    }

    public boolean userExists(UUID uuid)
    {
        return users.containsKey(uuid);
    }

    public Map<UUID, OPUser> getReadonlyContainer()
    {
        return Collections.unmodifiableMap(this.users);
    }
}
