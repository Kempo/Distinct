package kempodev.distinct.modules;

import kempodev.distinct.base.BaseMacro;
import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

import org.lwjgl.input.Keyboard;

public class ModuleMacro extends BaseModule{

	public ModuleMacro() {
		super("Macro System", -1, -1, "!m:add:(name):(command):(key)");
		this.setBindable(false);
		this.setToggeable(false);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCommand(String i) {
		String[] var2 = i.split(":");
		String var3[] = i.split(" ");
        if (var2[0].equalsIgnoreCase(this.setCommand("m")))
        {
            if (var2[1].equalsIgnoreCase("add"))
            {
                new BaseMacro(var2[2], var2[3], Keyboard.getKeyIndex(var2[4]));
                Distinct.getInstance().getChat().sendClientMessage("Macro added. Key set to " + Keyboard.getKeyIndex(var2[4]));
            }

            if (var3[1].equalsIgnoreCase("clear"))
            {
                Distinct.getInstance().getMacroManager().getMacros().clear();
                Distinct.getInstance().getChat().sendClientMessage("All macros cleared.");
            }
            
        }
	}
}
