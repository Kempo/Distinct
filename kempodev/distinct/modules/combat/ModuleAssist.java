package kempodev.distinct.modules.combat;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StringUtils;

import org.lwjgl.input.Keyboard;

public class ModuleAssist extends BaseModule{
	public ModuleAssist() {
		super("HitAssist", Keyboard.KEY_H, 0xF7194D, "!expand (size)");
		addField("hitBoxes",15F);
		// TODO Auto-generated constructor stub
	}
	@Override
	public float onSetCollisionBorderBoxes(Entity var14) {
		return shouldExpand(var14) ? Float.parseFloat(getValueByField("hitBoxes").toString()) : var14.getCollisionBorderSize();
	}
	private boolean shouldExpand(Entity var14) {
		return !isFriend(var14) && isLiving(var14) && !isDummy(var14);
	}
	private boolean isFriend(Entity var14) {
		return Distinct.getInstance().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(var14.getCommandSenderName()));
	}
	private boolean isLiving(Entity var14) {
		return !(var14 instanceof EntityHanging) && var14 instanceof EntityLivingBase && !var14.getCommandSenderName().startsWith(StringUtils.stripControlCodes("[S]"));
	}
	@Override
	public void onCommand(String i) {
		String args[] = i.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("expand"))) {
			Float r = Float.parseFloat(args[1]);
			setField("hitBoxes",r);
			Distinct.getInstance().getChat().sendClientMessage("Expanded all opposition hitboxes to " + Float.parseFloat(getValueByField("hitBoxes").toString()));
		}
	}
}
