package kempodev.distinct.modules.player;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

import org.lwjgl.input.Keyboard;

public class ModuleJesus extends BaseModule{
	/**
	 * nigga test nigga
	 */
	public ModuleJesus() {
		super("Jesus", Keyboard.KEY_J, 0x925DA1, null);
		this.addHookType("ONUPDATE");
	}
	@Override
	public void onUpdate() {
		if (Distinct.getInstance().getPlayer().isInWater() && !Distinct.getInstance().getPlayer().isCollidedHorizontally) {
			Distinct.getInstance().getPlayer().motionY = 0.02;
			if(getClient().getPlayer().movementInput.moveForward > 0) {
				//System.out.println("dawwd");
				getClient().getPlayer().motionY = 0.03;
			}
		}
	}
}
