package kempodev.distinct.modules.player;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

public class ModuleItemDropper extends BaseModule {
	private ArrayList<Integer> items = new ArrayList<Integer>();
	public ModuleItemDropper() {
		super("ItemDropper", -1, -1, "!dropitem (itemName)");
		this.visible = false;
		this.setBindable(false);
		addHookType("ONUPDATE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCommand(String s) {
		String args[] = s.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("dropitem"))) {
			int r = getClient().getID(args[1]);
			if(r > 0) {
			items.add(r);
			getClient().getChat().sendClientMessage("ItemID " + r + " added to the drop list.");
			}
		}
	}
	@Override
	public void onUpdate() {
		if(!items.isEmpty()) {
			for(Integer e : items) {
				if(e != null && e != -1 && e > 0) {
					dropAllItemsInHotbar(e);
				}
			}
		}
	}
	private void dropAllItemsInHotbar(int id) {
		for (int var2 = 44; var2 >= 9; --var2)
		{
			if(var2 >= 36 && var2 <= 44) {
				ItemStack var1 = Distinct.getInstance().getPlayer().inventoryContainer.getSlot(var2).getStack();
				if (var1 != null && Item.getIdFromItem(var1.getItem()) == id && !getClient().getPlayer().isSwingInProgress && !getClient().getPlayer().isBlocking())
				{
					int b = var2 - 36;
					int oldSlot = Distinct.getInstance().getPlayer().inventory.currentItem;
					Distinct.getInstance().getPlayer().inventory.currentItem = b;
					//System.out.println("set at " + b);
					Distinct.getInstance().getPlayer().dropOneItem(true);
					return;
				}
			}
		}
	}
}
