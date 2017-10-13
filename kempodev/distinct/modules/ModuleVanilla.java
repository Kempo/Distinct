package kempodev.distinct.modules;

import java.util.ArrayList;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.modules.player.ModuleFreecam;

public class ModuleVanilla extends BaseModule{
	private ArrayList<BaseModule> toggledModules = new ArrayList<BaseModule>();
	public ModuleVanilla() {
		super("Vanilla", -1, -1, null);
		this.visible = false;
		addHookType("ONUPDATE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnable() {
		addAllHookTypes();
	}
	@Override
	public void onDisable() {
		for(BaseModule m : toggledModules) {
			if(!(m instanceof ModuleFreecam)) {
			m.enabled = true;
			}
		}
		removeAllHookTypes();
	}
	@Override
	public void onUpdate() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && m != this) {
				addToggledModule(m);
				m.enabled = false;
			}
		}
		if(getClient().getWorld().getEntityByID(-1) != null) {
			getClient().getMinecraft().renderViewEntity = getClient().getPlayer();
			getClient().getWorld().removeEntityFromWorld(-1);
		}
	}
	private void addToggledModule(BaseModule e) {
		if(!toggledModules.contains(e)) {
			toggledModules.add(e);
		}
	}
}
