package kempodev.distinct.modules.combat;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.main.Distinct;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

import org.lwjgl.input.Keyboard;

public class ModuleVelocity extends BaseModule{

	public ModuleVelocity() {
		super("Velocity", Keyboard.KEY_Z, 0x57C95A, null);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onHandleEntityVelocity(S12PacketEntityVelocity par1Packet28EntityVelocity) {
		return  par1Packet28EntityVelocity.func_149412_c() == getClient().getPlayer().getEntityId() ? false : true;
	}
	
}
