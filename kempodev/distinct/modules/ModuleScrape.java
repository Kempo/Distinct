package kempodev.distinct.modules;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;

import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.util.StringUtils;
import kempodev.distinct.base.BaseModule;

public class ModuleScrape extends BaseModule {
	/**
	 * credits for w1dd for the original idea/code
	 */
	public ModuleScrape() {
		super("UserScraper", -1, -1, "!scrape");
		this.setBindable(false);
		this.setToggeable(false);
		this.enabled = true;
		this.visible = false;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCommand(String s) {
		String args[] = s.split(" ");
		
		if(args[0].equalsIgnoreCase(setCommand("scrape"))) {
			String users = "";
			for(Object o : getClient().getPlayer().sendQueue.playerInfoList) {
				GuiPlayerInfo p = (GuiPlayerInfo)o;
				users = users + StringUtils.stripControlCodes(p.name) + "\r\n";
			}
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(users), (ClipboardOwner)null);
			getClient().getChat().sendClientMessage(getClient().getPlayer().sendQueue.playerInfoList.size() + " usernames scraped to clipboard.");
		}
	}
}
