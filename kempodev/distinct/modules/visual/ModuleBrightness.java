package kempodev.distinct.modules.visual;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import org.lwjgl.input.Keyboard;

public class ModuleBrightness extends BaseModule{
	public ModuleBrightness() {
		super("Brightness", Keyboard.KEY_B, 0xF5F558,"!bl (float)");
		addField("brightnessLevel", 10);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnable() {
		getClient().getMinecraft().gameSettings.gammaSetting = Float.parseFloat(getValueByField("brightnessLevel").toString());
	}
	@Override
	public void onDisable() {
		
		getClient().getMinecraft().gameSettings.gammaSetting = 0.0F;
	}
	@Override
	public void onCommand(String s) {
		String args[] = s.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("bl"))) {
			setField("brightnessLevel",Float.parseFloat(args[1]));
			getClient().getChat().sendClientMessage("Brightness level set to " + Float.parseFloat(getValueByField("brightnessLevel").toString()));
		}
	}
}
