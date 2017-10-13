package kempodev.distinct.hooks;

import java.util.Iterator;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class DistinctHook {
	/**
	 * HOOKING TYPES:
	 * 'RENDERUI'
	 * 'ONUPDATE'
	 * 'PREUPDATE'
	 * 'POSTUPDATE'
	 * 'RENDER'
	 */
	public void onPreRenderEntityHook(Entity var25) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled) {
				m.onPreRenderEntity(var25);
			}
		}
	}
	public void onPostRenderEntityHook(Entity var25) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled) {
				m.onPostRenderEntity(var25);
			}
		}
	}
	public boolean onRenderMiscHook() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && !m.onRenderMisc()) {
				return m.onRenderMisc();
			}
		}

		return true;
	}
	public boolean onPacketRecieve(Packet packet) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && m.onPacketRecieve(packet)) {
				return m.onPacketRecieve(packet);
			}
		}
		return false;
	}
	public boolean onRenderEntityHook(Entity e) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && !m.onRenderEntity(e)) {
				return m.onRenderEntity(e);
			}
		}
		return true;
	}
	public boolean onAllowPacketSend(Packet e) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && !m.onAllowPacketSend(e)) {
				return m.onAllowPacketSend(e);
			}
		}
		return true;
	}
	private boolean containsType(BaseModule m,String s) {
		if(m.getTypes().contains(s)) {
			return true;
		}
		return false;
	}
	public void onPreUpdateMotion() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled) {
				m.onPreUpdateMotion();
			}
		}
	}
	public String onChatHook(String s) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && !m.onChatRender(s).equalsIgnoreCase(s)) {
				return m.onChatRender(s);
			}
		}
		return s;
	}
	public void onRenderIngameHook() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && containsType(m,HookTypes.RENDERUI)) {
				m.onRenderUI();
			}
		}
	}
	public void onUpdateHook() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && containsType(m,HookTypes.ONUPDATE)) {
				m.onUpdate();
			}
		}
	}
	public void onPreUpdateHook() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && containsType(m,HookTypes.PREUPDATE)) {
				m.onPreUpdate();
			}
		}
	}
	public void onPostUpdateHook() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && containsType(m,HookTypes.POSTUPDATE)) {
				m.onPostUpdate();
			}
		}
	}
	public void onRenderHook() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && containsType(m,HookTypes.RENDER)) {
				m.onRender();
			}
		}
	}
	public boolean onHandleEntityVelocity(S12PacketEntityVelocity par1Packet28EntityVelocity) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && !m.onHandleEntityVelocity(par1Packet28EntityVelocity)) {
				return m.onHandleEntityVelocity(par1Packet28EntityVelocity);
			}
		}
		return true;
	}
	
	public void onCameraRenderHook(float var6,float var7,byte var8,Minecraft mc) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			m.onCameraRender(var6, var7, var8, mc);
		}
	}
	public boolean onPushOutOfBlocks() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && !m.pushOutOfBlocks()) {
				return m.pushOutOfBlocks();
			}
		}
		return true;
	}
	public boolean onShouldSideBeRenderedHook(int var1) {
		Iterator var2 = Distinct.getInstance().getModuleManager().getModules().iterator();
        BaseModule var3;

        do
        {
            if (!var2.hasNext())
            {
                return false;
            }

            var3 = (BaseModule)var2.next();
        }
        while (!var3.enabled || !var3.onShouldSideBeRendered(var1));

        return var3.onShouldSideBeRendered(var1);
	}
	public String onSetPlacementHook(String var1)
    {
        Iterator var2 = Distinct.getInstance().getModuleManager().getModules().iterator();
        BaseModule var3;

        do
        {
            if (!var2.hasNext())
            {
                return var1;
            }

            var3 = (BaseModule)var2.next();
        }
        while (!var3.enabled || var3.onSetPlacement(var1).equalsIgnoreCase(var1));

        return var3.onSetPlacement(var1);
    }
	public float onSetNametagSizeHook(Entity par1EntityLivingBase)
    {
        Iterator var2 = Distinct.getInstance().getModuleManager().getModules().iterator();
        BaseModule var3;

        do
        {
            if (!var2.hasNext())
            {
                return 1.6F;
            }

            var3 = (BaseModule)var2.next();
        }
        while (!var3.enabled || var3.onSetNametagSize(par1EntityLivingBase) == 1.6F);

        return var3.onSetNametagSize(par1EntityLivingBase);
    }
	public float onSetCollisionBorderBoxesHook(Entity var14) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && (m.onSetCollisionBorderBoxes(var14) != var14.getCollisionBorderSize())) {
				return m.onSetCollisionBorderBoxes(var14);
			}
		}
		return var14.getCollisionBorderSize();
	}
	public int onSetSneakingColorHook() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && m.onSetSneakingColor() != 553648127) {
				return m.onSetSneakingColor();
			}
		}
		return 553648127;
	}
	public float onStrVsCurrentBlockMultiplierHook() {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled && m.onStrVsCurrentBlockMultiplier() != 1.0F) {
				return m.onStrVsCurrentBlockMultiplier();
			}
		}
		return 1.0F;
	}
	public void onRenderChunkHook(int i3,int x,int y,int z) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled) {
				m.onRenderChunk(i3, x, y, z);
			}
		}
	}
	public void onChatRenderHook(String s) {
		for(BaseModule m : Distinct.getInstance().getModuleManager().getModules()) {
			if(m.enabled) {
				m.onChatRender(s);
			}
		}
	}
}
