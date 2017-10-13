package kempodev.distinct.modules;

import java.util.ArrayList;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.HookTypes;

public class ModulePotion extends BaseModule{
	ArrayList<Potion> debuffs = new ArrayList<Potion>();
	public ModulePotion() {
		super("DebuffRemover", -1, -1, null);
		this.visible = false;
		this.setBindable(false);
		this.enabled = true;
		addHookType(HookTypes.ONUPDATE);
		debuffs.add(Potion.blindness);
		debuffs.add(Potion.confusion);
		debuffs.add(Potion.digSlowdown);
		debuffs.add(Potion.moveSlowdown);
		debuffs.add(Potion.weakness);
	}
	@Override
	public void onUpdate() {
		removeDebuffs();
	}
	private void removeDebuffs() {
		for(Potion p : debuffs) {
			if(p != null) {
				if(getClient().getPlayer().isPotionActive(p) && !getClient().getPlayer().getActivePotionEffect(p).getIsPotionDurationMax() && getClient().getPlayer().getActivePotionEffect(p).getDuration() > 0) {
					for(int c = 0; c < 1000; c++) {
						getClient().getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer());
					}
				}
			}
		}
	}
}
