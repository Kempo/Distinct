package kempodev.distinct.modules.player;

import kempodev.distinct.base.BaseModule;

public class ModuleFastPlace extends BaseModule{
	private int speed = 0,
			prevRightClick,
			prevLeftClick;
	public ModuleFastPlace() {
		super("FastPlace", -1, 0xFFFFFF, "!fp (speed)");
		addHookType("ONUPDATE");
		addField("fastPlaceSpeed",0);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnable() {
		addHookType("ONUPDATE");
		prevRightClick = getClient().getMinecraft().rightClickDelayTimer;
		prevLeftClick = getClient().getMinecraft().leftClickCounter;
	}
	@Override
	public void onUpdate() {
		getClient().getMinecraft().rightClickDelayTimer = speed;
		getClient().getMinecraft().leftClickCounter = 0;
		
	}
	@Override
	public void onDisable() {
		getClient().getMinecraft().rightClickDelayTimer = prevRightClick;
		getClient().getMinecraft().leftClickCounter = prevLeftClick;
		removeAllHookTypes();
	}
	@Override
	public void onCommand(String s) {
		String args[] = s.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("fp"))) {
			int b = Integer.parseInt(args[1]);
			setField("fastPlaceSpeed",b);
			sendClientMessage("Fast Place speed set to " + getValueByField("fastPlaceSpeed") + " (Default configuration speed is 0).");
		}
	}
}
