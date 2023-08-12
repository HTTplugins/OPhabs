package users;

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

    // TODO: Pensar si hacerla O(1) con otro hashmap.
    // WARNING: O(n)
    @Deprecated
    public OPUser getUserByName(String playerName)
    {
        for (OPUser user : users.values())
            if (user.getPlayerName().equals(playerName))
                return user;

        return null;
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
