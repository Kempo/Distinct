package kempodev.distinct.modules.player;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.modules.misc.FreecamInput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import org.lwjgl.input.Keyboard;

public class ModuleFreecam extends BaseModule{
	private EntityPlayerSP fakePlayer = null;
	private double prevX,prevY,prevZ;
	public ModuleFreecam() {
		super("Freecam", Keyboard.KEY_Y, 0xB9E01B, null);
		addHookType("ONUPDATE");
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnable() {
		addAllHookTypes();
		createEntity();
		
	}
	@Override
	public void onDisable() {
		destroyEntity();
		removeAllHookTypes();
	}
	private EntityPlayerSP getEntity() {
		return fakePlayer;
	}
	private EntityPlayerSP setEntity(EntityPlayerSP e) {
		return fakePlayer = e;
	}
	private void createEntity() {
		double x = Distinct.getInstance().getPlayer().posX;
        double y = Distinct.getInstance().getPlayer().posY;
        double z = Distinct.getInstance().getPlayer().posZ;
        prevX = x;
        prevY = y;
        prevZ = z;
        float yaw = Distinct.getInstance().getPlayer().rotationYaw;
        float pitch = Distinct.getInstance().getPlayer().rotationPitch;
		setEntity(new EntityPlayerSP(Distinct.getInstance().getMinecraft(), Distinct.getInstance().getWorld(), Distinct.getInstance().getMinecraft().getSession(), Distinct.getInstance().getPlayer().dimension));
		Distinct.getInstance().getWorld().addEntityToWorld(-1, getEntity());
		this.getEntity().setPositionAndRotation(x, y, z, yaw, pitch);
		this.getEntity().capabilities.isFlying = true;
		this.getEntity().noClip = true;
		Distinct.getInstance().getMinecraft().renderViewEntity = this.getEntity();
		this.getEntity().movementInput = new FreecamInput();
	}
	private void destroyEntity() {
		if(getEntity() != null) {
		getEntity().setPosition(prevX, prevY, prevZ);
		Distinct.getInstance().getMinecraft().renderViewEntity = Distinct.getInstance().getPlayer();
		Distinct.getInstance().getWorld().removeEntityFromWorld(-1);
		}
	}
	@Override
	public void onUpdate() {
		boolean var1 = Distinct.getInstance().getMinecraft().inGameHasFocus;
        if (var1 && getEntity() != null)
        {
            if (Distinct.getInstance().getMinecraft().gameSettings.keyBindJump.pressed)
            {
                getEntity().motionY += 0.2D;
            }

            if (Distinct.getInstance().getMinecraft().gameSettings.keyBindSneak.pressed)
            {
                getEntity().motionY -= 0.2D;
            }
        }
        if(getEntity() != null && getEntity().isSwingInProgress) {
        	getEntity().isSwingInProgress = false;
        }
	}
	@Override
	public void onCameraRender(float var6, float var7, byte var8, Minecraft mc) {
		if(enabled && getEntity() != null) {
		getEntity().setAngles(var6, var7 * (float)var8);
		}else{
			Distinct.getInstance().getMinecraft().thePlayer.setAngles(var6, var7 * (float)var8);
		}
	}
	@Override
	public boolean pushOutOfBlocks() {
		// TODO Auto-generated method stub
		return false;
	}
}
