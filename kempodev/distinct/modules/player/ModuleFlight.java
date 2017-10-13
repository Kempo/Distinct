package kempodev.distinct.modules.player;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

public class ModuleFlight extends BaseModule{
	private float flySpeed = 0.05F;
	public ModuleFlight() {
		super("Flight", -1, -1, "!fs (speed)");
		addHookType("ONUPDATE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnable()
	{
		addAllHookTypes();
		Distinct.getInstance().getPlayer().capabilities.isFlying = true;
	} 
	@Override
	public void onDisable() 
	{
		removeAllHookTypes();
		Distinct.getInstance().getPlayer().capabilities.isFlying = false;
	}
	@Override
	public void onUpdate()
	{
		Distinct.getInstance().getPlayer().capabilities.setFlySpeed(this.flySpeed);

		if (Distinct.getInstance().getPlayer().movementInput.jump && Distinct.getInstance().getPlayer().capabilities.allowFlying)
		{
			Distinct.getInstance().getPlayer().capabilities.isFlying = true;
		}

		if (!Distinct.getInstance().getPlayer().capabilities.allowFlying)
		{
			Distinct.getInstance().getPlayer().capabilities.isFlying = this.enabled;
		}
	}
	@Override
	public void onCommand(String var1)
	{
		String[] var2 = var1.split(" ");

		if (var2[0].equalsIgnoreCase(this.setCommand("fs")))
		{
			this.flySpeed = Float.parseFloat(var2[1]);
			Distinct.getInstance().getChat().sendClientMessage("Fly speed has been set to " + this.flySpeed);
		}
	}
}
