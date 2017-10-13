package kempodev.distinct.modules.misc;

import net.minecraft.block.Block;
import kempodev.distinct.main.Distinct;

public class BlockSelection {
	/**
	public boolean isVisible(int x,int y,int z,int b) {
		Block block = Distinct.getInstance().getWorld().func_147439_a(x, y, z);
		if(blockID == b) {
			return airAroundBlock(x,y,z);
		}
		return false;
	}
	private boolean airAroundBlock(int x,int y,int z) {
		Block top = Distinct.getInstance().getWorld().func_147439_a(x, y + 1, z);
		Block side2 = Distinct.getInstance().getWorld().func_147439_a(x + 1, y, z);
		Block side3 = Distinct.getInstance().getWorld().func_147439_a(x, y, z + 1);
		Block bottom = Distinct.getInstance().getWorld().func_147439_a(x, y - 1, z);
		Block side4 = Distinct.getInstance().getWorld().func_147439_a(x - 1, y, z);
		Block side5 = Distinct.getInstance().getWorld().func_147439_a(x, y, z - 1);
		if(top == 0 || side2 == 0 || side3 == 0 || bottom == 0 || side4 == 0 || side5 == 0) {
			return true;
		}
		return false;
	}
	**/
}
