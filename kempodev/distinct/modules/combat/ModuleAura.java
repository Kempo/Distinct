package kempodev.distinct.modules.combat;

import java.util.ArrayList;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.helpers.PlayerHelper;
import kempodev.distinct.main.Distinct;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

import org.lwjgl.input.Keyboard;

public class ModuleAura extends BaseModule{
	private PlayerHelper player = new PlayerHelper();
	private EntityLivingBase target = null;
	private float prevPitch,prevYaw,prevYawHead;
    private long CUR_MS, LAST_MS;
    private ArrayList<Class> entityClasses = new ArrayList<Class>();
    
	public ModuleAura() {
		super("KillAura", Keyboard.KEY_F, 0xE01B1B, "!ks (milliseconds) | !kr (double) | !aura (mobs | players | animals)");
		addHookType("ONUPDATE");
		addHookType("PREUPDATE");
		addHookType("POSTUPDATE");
		addField("threshold",110L);
		addField("attackPlayers",true);
		addField("attackMobs",false);
		addField("attackAnimals",false);
		addField("blockRange",3.95F);
		Distinct.getInstance().lol.add("distinct?");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onDisable() {
		removeAllHookTypes();
		this.target = null;
	}
	@Override
	public void onEnable() {
		addAllHookTypes();
		this.target = null;
	}
	@Override
	public void onUpdate() {
		Long delay = Long.parseLong(getValueByField("threshold").toString());
	    boolean canAttack = this.CUR_MS - this.LAST_MS >= delay || this.LAST_MS == -1L;
	    if(canAttack && target != null && target.getHealth() > 0) {
	    	attackEntity(target);
	    }
		if(target == null || (target != null && (!getClient().getPlayer().isSwingInProgress || getClient().getPlayer().getDistanceToEntity(target) > Float.parseFloat(getValueByField("blockRange").toString())))) {
			target = getClosestEntityToPlayer(Float.parseFloat(getValueByField("blockRange").toString()));
		}
        if(Distinct.getInstance().getPlayer().getHealth() <= 0) {
        	this.enabled = false;
        }
	}
	private void faceEntity() {
        float var1 = Distinct.getInstance().getPlayer().rotationYaw;
        float var2 = Distinct.getInstance().getPlayer().rotationPitch;

        if (this.target != null)
        {
            //player.faceEntity(target);
        	getClient().getPlayer().faceEntity(target,360F,360F);
            boolean var3 = MathHelper.abs(Distinct.getInstance().getPlayer().rotationYaw - var1) <= 85.0F;
            if (var3)
            {
                Distinct.getInstance().getPlayer().rotationYaw = var1;
                Distinct.getInstance().getPlayer().rotationPitch = var2;
                return;
            }
            
            //player.faceEntity(target);
            getClient().getPlayer().faceEntity(target,45F,360F);
            Distinct.getInstance().getPlayer().rotationPitch += 8.0F;
            Distinct.getInstance().getPlayer().rotationYawHead = Distinct.getInstance().getPlayer().rotationYaw;
                
        }
	}
	@Override
	public void onPreUpdate() {
		this.CUR_MS = System.nanoTime() / 1000000L;
		this.prevPitch = Distinct.getInstance().getPlayer().rotationPitch;
        this.prevYaw = Distinct.getInstance().getPlayer().rotationYaw;
        this.prevYawHead = Distinct.getInstance().getPlayer().rotationYawHead;
        faceEntity();
	}
	@Override
	public void onPostUpdate() {
		Distinct.getInstance().getPlayer().rotationPitch = this.prevPitch;
        Distinct.getInstance().getPlayer().rotationYaw = this.prevYaw;
        Distinct.getInstance().getPlayer().rotationYawHead = this.prevYawHead;
	}
	private void attackEntity(EntityLivingBase e) {
		getSword();
		Distinct.getInstance().getPlayer().swingItem();
        Distinct.getInstance().getMinecraft().playerController.attackEntity(Distinct.getInstance().getPlayer(), e);
        this.LAST_MS = System.nanoTime() / 1000000L;
	}
	private EntityLivingBase getClosestEntityToPlayer(float range) {
		double distance = range;
		EntityLivingBase setTarget = null;
		for(Object o : Distinct.getInstance().getWorld().loadedEntityList) {
			if(o instanceof EntityLivingBase) {
				EntityLivingBase e = (EntityLivingBase)o;
				double curDistance = Distinct.getInstance().getPlayer().getDistanceToEntity(e);
				if(!e.isDead && e != Distinct.getInstance().getPlayer() && !isShop(e)) {
					if (curDistance <= distance && shouldAttack(e) && !this.isDummy(e)) {
						distance = curDistance;
						setTarget = e;
						//System.out.println(distance + e.getCommandSenderName());
					}
				}
			}
		}
		return setTarget;
	}
	private boolean shouldAttack(EntityLivingBase e) {
		return (Boolean.parseBoolean(getValueByField("attackPlayers").toString()) && e instanceof EntityPlayer && !Distinct.getInstance().getFriendManager().getFriends().containsKey(StringUtils.stripControlCodes(e.getCommandSenderName()))) || (Boolean.parseBoolean(getValueByField("attackMobs").toString()) && (e instanceof EntityMob || e instanceof EntityAmbientCreature || e instanceof EntityDragon || e instanceof EntityFlying || e instanceof EntitySlime)) || (Boolean.parseBoolean(getValueByField("attackAnimals").toString())&& e instanceof EntityAnimal);
	}

	@Override
	public void onCommand(String i) {
		String args[] = i.split(" ");
		if(args[0].equalsIgnoreCase(setCommand("ks"))) {
			if(args[1].length() > 1) {
				Long b = Long.parseLong(args[1]);
				setField("threshold",b);
				sendClientMessage("Attack speed set to " + getValueByField("threshold") + " milliseconds.");
			}
		}
		if(args[0].equalsIgnoreCase(setCommand("kr"))) {
			if(args[1].length() > 1) {
				Float b = Float.parseFloat(args[1]);
				setField("blockRange",b);
				sendClientMessage("Attack range set to " + getValueByField("blockRange") + " blocks.");
			}
		}
		if (args[0].equalsIgnoreCase(this.setCommand("aura")))
        {
            if (args[1].equalsIgnoreCase("players"))
            {
                setField("attackPlayers",!Boolean.parseBoolean(getValueByField("attackPlayers").toString()));
                sendClientMessage("Player targeting has been " + (Boolean.parseBoolean(getValueByField("attackPlayers").toString()) ? "enabled" : "disabled"));
            }
            else if (args[1].equalsIgnoreCase("mobs"))
            {
            	setField("attackMobs",!Boolean.parseBoolean(getValueByField("attackMobs").toString()));
                sendClientMessage("Mob targeting has been " + (Boolean.parseBoolean(getValueByField("attackMobs").toString()) ? "enabled" : "disabled"));
            }
            else if (args[1].equalsIgnoreCase("animals"))
            {
            	setField("attackAnimals",!Boolean.parseBoolean(getValueByField("attackAnimals").toString()));
                sendClientMessage("Animal targeting has been " + (Boolean.parseBoolean(getValueByField("attackAnimals").toString()) ? "enabled" : "disabled"));
            }
        }
	}
	private boolean isShop(EntityLivingBase e) {
		for(Object b : getClient().getWorld().field_147482_g) {
			if(b instanceof TileEntityChest) {
				TileEntityChest c = (TileEntityChest)b;
				if(withinRange(e.posX,e.posY,e.posZ,c.field_145851_c,c.field_145848_d,c.field_145849_e) && e.getTotalArmorValue() == 0) {
					return true;
				}
			}
		}
		String name = StringUtils.stripControlCodes(e.getCommandSenderName());
		if(name.contains("[S]")) {
			return true;
		}
		return false;
	}
	private void getSword() {
		for (int var2 = 44; var2 >= 9; --var2)
		{
			if(var2 >= 36 && var2 <= 44) {
				ItemStack var1 = Distinct.getInstance().getPlayer().inventoryContainer.getSlot(var2).getStack();
				if (var1 != null && var1.getItem() instanceof ItemSword && !getClient().getPlayer().isSwingInProgress && !getClient().getPlayer().isBlocking())
				{
					int b = var2 - 36;
					int oldSlot = Distinct.getInstance().getPlayer().inventory.currentItem;
					Distinct.getInstance().getPlayer().inventory.currentItem = b;
					//System.out.println("set at " + b);
					return;
				}
			}
		}
	}
	private boolean withinRange(double x,double y,double z, int x2,int y2,int z2) {
		return getDistance(x,y,z,x2,y2,z2) <= 1;
	}
	public int getDistance(double x,double y, double z,int par1, int par3, int par5)
    {
        double var7 = x - par1;
        double var9 = y - par3;
        double var11 = z - par5;
        return (int) MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
    }
	/**OUTDATED **/
	public void addClassByName(String s) {
		try {
			Class<?> c = Class.forName("net.minecraft.src." + s);
			entityClasses.add(c);
			for(Class e : entityClasses) {
				System.out.println(e.getName());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
