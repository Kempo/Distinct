package kempodev.distinct.modules.visual;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.*;
import net.minecraft.entity.player.EntityPlayer;
import kempodev.distinct.base.BaseModule;

public class ModuleNoRender extends BaseModule {

	public ModuleNoRender() {
		super("NoRender", -1, -1, "!nr (false | true)");
		this.visible = false;
		this.setBindable(false);
		addField("allEntities",false);
		addField("miscEntities",true);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onRenderEntity(Entity e) {
			if(Boolean.parseBoolean(getValueByField("allEntities").toString())) {
				//System.out.println("renderin nothin lel");
				return false;
			}else{
				if(!Boolean.parseBoolean(getValueByField("allEntities").toString()) && !getClient().getPlayer().canEntityBeSeen(e) && e instanceof EntityLiving && !(e instanceof EntityDragon)) {
					//System.out.println("renderin only visible human entities");
					return false;
				}
			}
		return true;
	}
	@Override
	public void onCommand(String s) {
		String args[] = s.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("nr"))) {
			Boolean e = Boolean.parseBoolean(args[1]);
			setField("allEntities",e);
			getClient().getChat().sendClientMessage("Disabling rendering of all entities is now " + (Boolean.parseBoolean(getValueByField("allEntities").toString()) ? "enabled" : "disabled"));
		}
		if(args[0].equalsIgnoreCase(this.setCommand("me"))) {
			Boolean e = Boolean.parseBoolean(args[1]);
			setField("miscEntities",e);
			getClient().getChat().sendClientMessage("Rendering of miscellaneous entities is now " + (Boolean.parseBoolean(getValueByField("miscEntities").toString()) ? "enabled" : "disabled"));
		}
	}
	@Override
	public boolean onRenderMisc() {
		return Boolean.parseBoolean(getValueByField("miscEntities").toString()) ? false : true;
	}
}
