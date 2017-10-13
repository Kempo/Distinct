package kempodev.distinct.modules.combat;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

import org.lwjgl.input.Keyboard;

public class ModuleSoup extends BaseModule{
	private int invSlot;
	private ItemStack item;
	private int emptyBowls,filledBowls;
	private boolean shouldMove = true;
	private int MUSHROOM_SOUP = 282, EMPTY_BOWL = 281;
	public ModuleSoup() {
		super("Soup", Keyboard.KEY_U, 0xE01BB9, null);
		addHookType("ONUPDATE");
		addHookType("RENDERUI");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpdate()
	{
		if (Distinct.getInstance().getPlayer().getHealth() <= 10)
		{
			this.eatSoup();
		}else{
			if(shouldMove) {
			prioritizeItemsInHotbar();
			}
		}
	}
	/**
	@Override
	public void onRenderUI() {
		ScaledResolution r = new ScaledResolution(getClient().getMinecraft().gameSettings, getClient().getMinecraft().displayWidth, getClient().getMinecraft().displayHeight);
		countSoups();
		String s = "Soups: " + filledBowls;
		getClient().getFontRenderer().drawStringWithShadow(s, r.getScaledWidth() - getClient().getFontRenderer().getStringWidth(s), r.getScaledHeight() - 50, -1);
		filledBowls = 0;
	}
	**/
	private void countSoups() {
		for (int var2 = 44; var2 >= 9; --var2)
		{
			ItemStack var1 = Distinct.getInstance().getPlayer().inventoryContainer.getSlot(var2).getStack();

			if (var1 != null)
			{
				if(Item.getIdFromItem(var1.getItem()) == MUSHROOM_SOUP) {
					
					if(filledBowls <= 36) {
					filledBowls += 1;
					}
				}
			}
		}
	}
	private void stackSoups() {
		final int b = this.getSlotWithItem(EMPTY_BOWL);
		for (int var2 = 44; var2 >= 9; --var2)
		{
			ItemStack var1 = Distinct.getInstance().getPlayer().inventoryContainer.getSlot(var2).getStack();
			if (var1 != null)
			{
				if(Item.getIdFromItem(var1.getItem()) == EMPTY_BOWL && var2 != b) {
					Distinct.getInstance().getMinecraft().playerController.windowClick(0, var2, 0, 0, Distinct.getInstance().getPlayer());
					Distinct.getInstance().getMinecraft().playerController.windowClick(0, b, 0, 0, Distinct.getInstance().getPlayer());
				}
			}
		}
	}
	private void eatSoup()
	{
		this.invSlot = -1;
		for (int var1 = 44; var1 >= 9; --var1)
		{
			this.item = Distinct.getInstance().getMinecraft().thePlayer.inventoryContainer.getSlot(var1).getStack();

			if (this.item != null && Item.getIdFromItem(item.getItem()) == MUSHROOM_SOUP)
			{
				if (var1 >= 36 && var1 <= 44)
				{
					int var2 = this.invSlot - 36;

					if (Distinct.getInstance().getMinecraft().thePlayer.inventory.currentItem != var2)
					{
						Distinct.getInstance().getMinecraft().thePlayer.inventory.currentItem = var1 - 36;
						Distinct.getInstance().getPlayer().sendQueue.addToSendQueue(new C09PacketHeldItemChange(Distinct.getInstance().getMinecraft().thePlayer.inventory.currentItem));
					}

					Distinct.getInstance().getMinecraft().playerController.updateController();
					Distinct.getInstance().getPlayer().sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255, Distinct.getInstance().getMinecraft().thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
					Distinct.getInstance().getMinecraft().playerController.updateController();
					//Distinct.getInstance().getPlayer().dropOneItem(true);
					stackSoups();
					return;
				}

				Distinct.getInstance().getMinecraft().playerController.windowClick(0, var1, 0, 0, Distinct.getInstance().getPlayer());
				Distinct.getInstance().getMinecraft().playerController.windowClick(0, this.getFreeSlotInHotbar(), 0, 0, Distinct.getInstance().getPlayer());
				this.invSlot = var1;
				break;
			}
		}                   
	}
	private void prioritizeItemsInHotbar() {
		if(getFreeSlotInInventory() != -1 && getFreeSlotInInventory() >= 9 && getFreeSlotInInventory() <= 35) {
			int b = getFreeSlotInInventory();
			for (int slot = 36; slot <= 44; ++slot)
			{
				ItemStack var1 = Distinct.getInstance().getPlayer().inventoryContainer.getSlot(slot).getStack();

				if (var1 != null)
				{
					if(!(Item.getIdFromItem(var1.getItem()) != EMPTY_BOWL || Item.getIdFromItem(var1.getItem()) == MUSHROOM_SOUP) && !(var1.getItem() instanceof ItemSword)) {
						Distinct.getInstance().getMinecraft().playerController.windowClick(0, slot, 0, 0, Distinct.getInstance().getPlayer()); // get armor
						Distinct.getInstance().getMinecraft().playerController.windowClick(0, b, 0, 0, Distinct.getInstance().getPlayer()); // put to a free slot in the inv
						return;
					}
				}
			}
		}
	}
	private int getFreeSlotInHotbar()
	{
		for (int var2 = 36; var2 <= 44; ++var2)
		{
			ItemStack var1 = Distinct.getInstance().getPlayer().inventoryContainer.getSlot(var2).getStack();

			if (var1 == null)
			{
				return var2;
			}
		}
		return -1;
	}
	private int getSlotWithItem(int id) {
		for (int var2 = 44; var2 >= 9; --var2)
		{
			ItemStack var1 = Distinct.getInstance().getPlayer().inventoryContainer.getSlot(var2).getStack();
			if (var1 != null && Item.getIdFromItem(var1.getItem()) == id)
			{
				return var2;
			}
		}
		return -1;
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
}
