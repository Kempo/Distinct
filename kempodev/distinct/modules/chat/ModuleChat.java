package kempodev.distinct.modules.chat;

import kempodev.distinct.base.BaseModule;

public class ModuleChat extends BaseModule{

	public ModuleChat() {
		super("Chat", -1, -1, null);
		this.enabled = true;
		this.visible = false;
		this.setBindable(false);
	}
}
