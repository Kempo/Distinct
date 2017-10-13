package kempodev.distinct.managers;

import java.util.concurrent.ConcurrentHashMap;

public class FriendManager {
	private ConcurrentHashMap friends = new ConcurrentHashMap();

    public ConcurrentHashMap getFriends()
    {
        return this.friends;
    }

    public void addFriend(String var1, String var2)
    {
        this.friends.put(var1, var2);
    }

    public void removeFriend(String var1)
    {
        this.friends.remove(var1);
    }
}
