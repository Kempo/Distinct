package kempodev.distinct.helpers;

import kempodev.distinct.main.Distinct;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class PlayerHelper {
	public void faceEntity(final EntityLivingBase e) {
		double difX = e.posX - Distinct.getInstance().getPlayer().posX, 
		difY = (e.posY - Distinct.getInstance().getPlayer().posY)+ (e.getEyeHeight() / 1.4F), 
		difZ = e.posZ- Distinct.getInstance().getPlayer().posZ;
		double helper = Math.sqrt(difX * difX + difZ * difZ);
		float yaw = (float) (Math.atan2(difZ, difX) * 180 / Math.PI) - 90;
		float pitch = (float) -(Math.atan2(difY, helper) * 180 / Math.PI);
		Distinct.getInstance().getPlayer().rotationPitch = pitch;
		Distinct.getInstance().getPlayer().rotationYaw = yaw;
		Distinct.getInstance().getPlayer().rotationYawHead = yaw;
	}

}
