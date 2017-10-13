package kempodev.distinct.modules.visual;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.HookTypes;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.utilities.ColorUtil;
import kempodev.distinct.utilities.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ModuleChestFinder extends BaseModule{

	public ModuleChestFinder() {
		super("ChestFinder", Keyboard.KEY_M, 0xE0C21B, null);
		addHookType(HookTypes.RENDER);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onRender() {
		RenderUtil r = new RenderUtil();
		r.glOpen();
		for(Object e : Distinct.getInstance().getWorld().field_147482_g) {
			if(e instanceof TileEntity) {
				if(e instanceof TileEntityChest) {
					TileEntityChest b = (TileEntityChest)e;
					if(b != null) {
						final double renderX = b.field_145851_c - RenderManager.renderPosX;
						final double renderY = b.field_145848_d - RenderManager.renderPosY;
						final double renderZ = b.field_145849_e - RenderManager.renderPosZ;
						this.renderChest(b,renderX,renderY,renderZ);
					}
				}
			}
		}
		r.glClose();
	}
	
	
	private void renderChest(TileEntity b,double x,double y,double z) {
		GL11.glLineWidth(1.5F);
		ColorUtil l = new ColorUtil();
		l.setHex(0xE0E01B);
		GL11.glColor4d(l.getRed(),l.getGreen(),l.getBlue(),1D);
		Block var8 = Distinct.getInstance().getWorld().getBlock(b.field_145851_c, b.field_145848_d, b.field_145849_e);
		RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x + var8.field_149759_B /**min x **/, 
			    y + var8.field_149760_C /**min y **/, 
			    z + var8.field_149754_D /**min z **/, 
			    x + var8.field_149755_E /**max x **/, 
			    y + var8.field_149756_F /** max y **/, 
			    z + var8.field_149757_G) /** max z **/);
	}
}
