package kempodev.distinct.modules.visual;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.HookTypes;
import kempodev.distinct.main.Distinct;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ModulePotionEffects extends BaseModule{

	public ModulePotionEffects() {
		super("PotionEffects", -1, -1, null);
		this.setBindable(false);
		this.visible = false;
		this.enabled = true;
		addHookType(HookTypes.RENDERUI);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onRenderUI() {
		if(getClient().getMinecraft().inGameHasFocus) {
		ScaledResolution var2 = new ScaledResolution(Distinct.getInstance().getMinecraft().gameSettings, Distinct.getInstance().getMinecraft().displayWidth, Distinct.getInstance().getMinecraft().displayHeight);
		//int x = var2.getScaledWidth();
		int y = var2.getScaledHeight() - 10;
		for (Object o : Distinct.getInstance().getPlayer().getActivePotionEffects()) {
			PotionEffect c = (PotionEffect) o;
			Potion p = Potion.potionTypes[c.getPotionID()];
			try{
			String name = I18n.format(p.getName(), new Object[0]);
			if (c.getAmplifier() == 1)
            {
                name = name + " II";
            }
            else if (c.getAmplifier() == 2)
            {
                name = name + " III";
            }
            else if (c.getAmplifier() == 3)
            {
                name = name + " IV";
            }
			String dur = Potion.getDurationString(c);
			int x = var2.getScaledWidth() - getClient().getFontRenderer().getStringWidth(name + " (" + dur + ")");
			Distinct.getInstance().getFontRenderer().drawStringWithShadow(name + " (" + dur + ")", x, y, 0xFFFFFF);
			y -= 10;
			}catch(Exception e) {
			}
		}
		}
	}
}
