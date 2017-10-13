package kempodev.distinct.input;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.ChatComponentTranslation;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

public class DistinctChat {
	public String prefix = "!";
	public boolean registerChat(String s) {
		String args[] = s.split(" ");
		
		if(s.startsWith(prefix)) {
			Iterator var2 = Distinct.getInstance().getModuleManager().getModules().iterator();

            while (var2.hasNext())
            {
                BaseModule var3 = (BaseModule)var2.next();

                try
                {
                    var3.onCommand(s);
                }
                catch (Exception var5)
                {
                    this.sendClientMessage(" " + var3.getFormat());
                    var5.printStackTrace();
                }
            }
            try{
            if(args[0].equalsIgnoreCase(prefix + "prefix")) {
            	prefix = args[1];
            	Distinct.getInstance().getChat().sendClientMessage("Prefix set to '" + prefix + "'");
            }
            if(args[0].equalsIgnoreCase(prefix + "t")) {
            	for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
            		if(m.getName().equalsIgnoreCase(args[1]) && m.getToggleable()) {
            			m.onToggle();
            			System.out.println(m.getName() + " has been " + (m.enabled ? "enabled" : "disabled"));
            			if(!m.getName().equalsIgnoreCase("vanilla")) {
            			Distinct.getInstance().getChat().sendClientMessage(m.getName() + " has been " + (m.enabled ? "enabled" : "disabled"));
            			}
            		}
            	}
            }
            if(args[0].equalsIgnoreCase(prefix + "setv")) {
            	for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
            		if(m.getName().equalsIgnoreCase(args[1])) {
            			m.visible = Boolean.parseBoolean(args[2]);
            			sendClientMessage(m.getName() + " visibility is set to " + m.visible);
            		}
            	}
            }
            if(args[0].equalsIgnoreCase(prefix + "listf")) {
            	for(Object o : Block.blockRegistry.getKeys()) {
        			String lol = o.toString();
        			lol = lol.replaceAll("minecraft:", "");
        			lol = lol.replaceAll("_", "");
        			lol = lol.replaceAll("ore", "");
        			if(lol.contains(args[1])) {
        				sendClientMessage(lol);
        			}
        			//System.out.println(s);
        		}
            }
            }catch(Exception e) {
            	sendClientMessage("Parameters were incorrect or not filled in!");
            }
			return false;
		}
		System.out.println(s);
		return true;
	}
	public void sendClientMessage(String s) {
		Distinct.getInstance().getPlayer().addChatMessage(new ChatComponentTranslation("[\247aDST\247f]" + s, new Object[0]));
	}
	private Distinct getClient() {
		return Distinct.getInstance();
	}
}
