package kempodev.distinct.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.HookTypes;
import kempodev.distinct.main.Distinct;

public class ModuleTrigger extends BaseModule{
    private long CUR_MS = -1;
    private long LAST_MS = -1;
	public ModuleTrigger() {
		super("Trigger", -1, 0xF08B8B, "!tdelay (milliseconds)");
		addHookType(HookTypes.ONUPDATE);
		addField("threshold",75L);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onUpdate() {
		try{
		this.CUR_MS = System.nanoTime() / 1000000L;
		Long delay = Long.parseLong(getValueByField("threshold").toString());
		boolean canAttack = this.CUR_MS - this.LAST_MS >= delay || this.LAST_MS == -1L;
		if(getClient().getMinecraft().objectMouseOver.entityHit != null && !getClient().getMinecraft().objectMouseOver.entityHit.isDead && (getClient().getMinecraft().objectMouseOver.entityHit instanceof EntityPlayer) && !isFriend(getClient().getMinecraft().objectMouseOver.entityHit) && canAttack) {
			getClient().getPlayer().swingItem();
			getClient().getMinecraft().playerController.attackEntity(getClient().getPlayer(), getClient().getMinecraft().objectMouseOver.entityHit);
			 this.LAST_MS = System.nanoTime() / 1000000L;
		}
		if(getClient().getPlayer().isDead || getClient().getPlayer().getHealth() <= 0) {
			this.enabled = false;
		}
		}catch(Exception e) {
			System.out.println("Encountered an error with trigger!");
			this.enabled = false;
		}
	}
	private boolean isFriend(Entity e) {
		if(getClient().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(e.getCommandSenderName()))) {
			return true;
		}
		return false;
	}
	@Override
	public void onCommand(String i) {
		String args[] = i.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("tdelay"))) {
			setField("threshold",Long.parseLong(args[1]));
			Distinct.getInstance().getChat().sendClientMessage("Trigger attack speed has been set to " + getValueByField("threshold"));
		}
	}
}
