package kempodev.distinct.modules.chat;

import java.util.Iterator;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

public class ModuleFriend extends BaseModule{

	public ModuleFriend() {
		super("Friend System", -1, -1, "!f add (name) (alias) | remove (alias or name)");
		this.visible = false;
		this.enabled = true;
		this.setBindable(false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCommand(String var1) {
		String[] var2 = var1.split(" ");

		if (var2[0].equalsIgnoreCase(this.setCommand("f")))
		{
			if (var2[1].equalsIgnoreCase("add"))
			{
				Distinct.getInstance().getFriendManager().addFriend(var2[2], var2[3]);
				Distinct.getInstance().getChat().sendClientMessage(Distinct.getInstance().getFriendManager().getFriends().get(var2[2]).toString() + " is now added");
			}

			else if (var2[1].equalsIgnoreCase("remove"))
			{
				Iterator var3 = Distinct.getInstance().getFriendManager().getFriends().keySet().iterator();

				while (var3.hasNext())
				{
					Object var4 = var3.next();
					String var5 = var4.toString();
					String var6 = Distinct.getInstance().getFriendManager().getFriends().get(var5).toString();

					if (var2[2].equalsIgnoreCase(var6) || var2[2].equalsIgnoreCase(var5))
					{
						Distinct.getInstance().getFriendManager().removeFriend(var5);
						Distinct.getInstance().getChat().sendClientMessage("Friend is now removed");
					}
				}
			}
			else if (var2[1].equalsIgnoreCase("clear")) {
				Distinct.getInstance().getFriendManager().getFriends().clear();
				Distinct.getInstance().getChat().sendClientMessage("Cleared friends list.");
			}
		}
	}
}
