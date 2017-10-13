package kempodev.distinct.modules.chat;

import org.lwjgl.input.Keyboard;

import kempodev.distinct.base.BaseModule;

public class ModuleHelp extends BaseModule{

	public ModuleHelp() {
		super("Help System", -1, -1, "!commands | !keybinds");
		this.setBindable(false);
		this.visible = false;
		this.setToggeable(false);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCommand(String s) {
		String args[] = s.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("commands"))) {
			for(BaseModule m : getClient().getModuleManager().getModules()) {
				if(m.getFormat() != null && m != this && !m.getFormat().isEmpty()) {
				String c = m.getFormat();
				getClient().getChat().sendClientMessage(m.getName() + ": " + c);
				}
			}
		}
		if(args[0].equalsIgnoreCase(this.setCommand("keybinds"))) {
			for(BaseModule m : getClient().getModuleManager().getModules()) {
				if(m.getKey() != -1 && m.getBindable()) {
				getClient().getChat().sendClientMessage(m.getName() + ": " + Keyboard.getKeyName(m.getKey()));
				}
			}
		}
	}
}
