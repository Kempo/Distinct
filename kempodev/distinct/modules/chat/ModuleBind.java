package kempodev.distinct.modules.chat;

import java.util.Iterator;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

import org.lwjgl.input.Keyboard;

public class ModuleBind extends BaseModule {
	public ModuleBind() {
		super("Bind System", -1, -1, "!bind (hack) (key)");
		// TODO Auto-generated constructor stub
		this.setToggeable(false);
		this.setBindable(false);
	}

	public void onCommand(String var1)
    {
        String[] var2 = var1.split(" ");

        if (var2[0].equalsIgnoreCase(this.setCommand("bind")))
        {
            Iterator var3 = Distinct.getInstance().getModuleManager().getModules().iterator();
            while (var3.hasNext())
            {
                BaseModule var4 = (BaseModule)var3.next();
                if (var2[1].equalsIgnoreCase(var4.getName()))
                {
                    int var5 = Keyboard.getKeyIndex(var2[2].toUpperCase());
                    var4.setKey(var5);
                    Distinct.getInstance().getChat().sendClientMessage(var4.getName() + " has been bound to " + Keyboard.getKeyName(var4.getKey()));
                }
            }
        }
    }
}
