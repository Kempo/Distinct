package kempodev.distinct.base;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import kempodev.distinct.main.Distinct;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;

public class BaseModule{
	private String name;
	private int key;
	private int color;
	public boolean enabled = false;
	public boolean visible = true;
	private String format;
	private boolean bindable = true;
	protected ArrayList<String> hookingTypes = new ArrayList<String>();
	public ConcurrentHashMap<String, Object> fields = new ConcurrentHashMap<String, Object>();
	private boolean toggleable = true;
	public boolean disableOnClose = false;
	public BaseModule(String n, int k,int c,String s) {
		name = n; key = k; color = c; format = s;
	}
	protected Distinct getClient() {
		return Distinct.getInstance();
	}
	public boolean setToggeable(boolean e) {
		return toggleable = e;
	}
	public boolean setBindable(boolean e) {
		return bindable = e;
	}
	public boolean getToggleable() {
		return toggleable;
	}
	public boolean getBindable() {
		return bindable;
	}
	public int getKey() {
		return key;
	}
	public int setKey(int k) {
		return key = k;
	}
	public String getName() {
		return name;
	}
	public int getColor() {
		return color;
	}
	public void onToggle() {
		enabled = !enabled;
		if(enabled) {
			onEnable();
		}else{
			if(!enabled) {
			onDisable();
			}
		}
	}
	public String getFormat() {
		return format;
	}
	protected void onEnable() {	
		addAllHookTypes();
	}
	protected void onDisable() {
		removeAllHookTypes();
	}
	protected void addAllHookTypes() {
		for(int c = 0; c < hookingTypes.size(); c++) {
			String s = hookingTypes.get(c);
			addHookType(s);
		}
	}
	protected void removeAllHookTypes() {
		for(int c = 0; c < hookingTypes.size(); c++) {
			String s = hookingTypes.get(c);
			removeHookType(s);
		}
	}
	protected String setCommand(String s) {
		return Distinct.getInstance().getChat().prefix + s;
	}
	protected void sendClientMessage(String s) {
		Distinct.getInstance().getChat().sendClientMessage(s);
	}
	protected void addField(String field, Object defaultv) {
		if(!fields.contains(field)) {
		fields.put(field, defaultv);
		}
	}
	public Object getValueByField(String field) {
		return fields.get(field);
	}
	public void setField(String field, Object value) {
		fields.put(field, value);
	}
	public ArrayList<String> getTypes() {
		return hookingTypes;
	}
	public void addHookType(String s) {
		if(!hookingTypes.contains(s)) {
			hookingTypes.add(s);
		}
	}
	public void removeHookType(String s) {
		if(hookingTypes.contains(hookingTypes.indexOf(s))) {
			hookingTypes.remove(s);
		}
	}
	protected boolean isDummy(Entity e) {
		if(e instanceof EntityPlayer) {
			EntityPlayer dummy = (EntityPlayer)e;
			if(dummy.getTotalArmorValue() == 0 && dummy.getCommandSenderName().contains("Severe_")) {
				if(dummy.isInvisible()) {
					return true;
				}
			}
		}
		return false;
	}
	/**HOOKS **/
	public void onUpdate() { }
	public void onRenderUI() { }
	public void onRender() { }
	public void onPreUpdate() { }
	public void onPostUpdate() { }
	public void onCommand(String s) { }
	public boolean onHandleEntityVelocity(S12PacketEntityVelocity par1Packet28EntityVelocity) {return true; }
	public void onCameraRender(float var6,float var7,byte var8,Minecraft mc) { }
	public boolean pushOutOfBlocks() {return true; }
	public boolean onShouldSideBeRendered(int var1) { return false; }
	public String onSetPlacement(String var1){ return var1; }
	public float onSetNametagSize(Entity par1EntityLivingBase) { return 1.6F; }
	public float onSetCollisionBorderBoxes(Entity var14) {return var14.getCollisionBorderSize(); }
	public int onSetSneakingColor() {return 553648127; }
	public float onStrVsCurrentBlockMultiplier() {return 1.0F; }
	public void onRenderChunk(int i3,int x, int y,int z) { }
	public String onChatRender(String s) { return s; }
	public void onPreUpdateMotion() { };
	public boolean onAllowPacketSend(Packet e) { return true; }
	public boolean onPacketRecieve(Packet packet) {return false; }
	public boolean onRenderEntity(Entity e) {return true; }
	public boolean onRenderMisc() {return true;}
	public void onPreRenderEntity(Entity var25) { }
	public void onPostRenderEntity(Entity var25) { }
	
}
