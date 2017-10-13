package kempodev.distinct.modules.player;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class ModuleAutoFish extends BaseModule{
	private EntityFishHook fishHook = null;
	public ModuleAutoFish() {
		super("AutoFish", -1, -1, null);
		addHookType("ONUPDATE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpdate() {
		fishHook = getFishHook();
		if(Distinct.getInstance().getPlayer().getCurrentEquippedItem() != null) {
			if(fishHook != null && isBobbing()) {
				//System.out.println(fishHook.motionY);
				Distinct.getInstance().getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, -1, getClient().getPlayer().inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
				Distinct.getInstance().getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, -1, getClient().getPlayer().inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
				return;
			}
		}
	}
	private EntityFishHook getFishHook() {
		for(Object o : getClient().getWorld().loadedEntityList) {
			if(o instanceof EntityFishHook) {
				EntityFishHook r = (EntityFishHook)o;
				if(r.field_146042_b == Distinct.getInstance().getPlayer()) {
					return r;
				}
			}
		}
		return null;
	}
	private int getFreeSlotInInventory() {
		for (int var2 = 44; var2 >= 9; --var2)
		{
			ItemStack var1 = Distinct.getInstance().getPlayer().inventoryContainer.getSlot(var2).getStack();

			if (var1 == null)
			{
				return var2;
			}
		}
		return -1;
	}
	private boolean isBobbing() {
		return fishHook != null && fishHook.motionX == 0 && fishHook.motionZ == 0 && (fishHook.motionY != 0);
	}
}
