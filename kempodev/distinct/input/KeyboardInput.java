package kempodev.distinct.input;

import kempodev.distinct.base.BaseMacro;
import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

public class KeyboardInput {
	public void onPress(int k) {
		for(BaseModule m : getClient().getModuleManager().getModules()) {
			if(m.getKey() == k) {
				m.onToggle();
			}
		}
		for(BaseMacro m : getClient().getMacroManager().getMacros()) {
			if(m.getMacroKey() == k) {
				m.sendCommand();
			}
		}
	}
	private Distinct getClient() {
		return Distinct.getInstance();
	}
}
