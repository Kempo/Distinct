package kempodev.distinct.modules.player;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

import org.lwjgl.input.Keyboard;

public class ModuleSprint extends BaseModule{
	public ModuleSprint() {
		super("Sprint", Keyboard.KEY_C, 0x63E01B, null);
		addHookType("ONUPDATE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpdate() {
		boolean canSprint = Distinct.getInstance().getPlayer().movementInput.moveForward > 0 && !Distinct.getInstance().getPlayer().isSneaking();
		if(canSprint && !Distinct.getInstance().getPlayer().isCollidedHorizontally && getClient().getPlayer().getFoodStats().getFoodLevel() > 3 && !getClient().getPlayer().isUsingItem()) {
			Distinct.getInstance().getPlayer().setSprinting(true);
		}
	}                
}
         