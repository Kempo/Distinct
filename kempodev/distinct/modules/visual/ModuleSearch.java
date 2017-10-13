package kempodev.distinct.modules.visual;

import java.util.ArrayList;
import java.util.HashMap;

import kempodev.distinct.base.BaseModule;
import kempodev.distinct.hooks.HookTypes;
import kempodev.distinct.main.Distinct;
import kempodev.distinct.modules.misc.BlockSelection;
import kempodev.distinct.utilities.ColorUtil;
import kempodev.distinct.utilities.RenderUtil;
import kempodev.distinct.modules.misc.Location;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class ModuleSearch extends BaseModule{

	/**
	 * Work of art right here(imo).
	 * One of my proudest modules yet, thanks to Ramisme for the isAlreadyRendering method
	 * TODO:
	 * Only render blocks in front of you, blocks behind you get rendered and very few blocks are actually
	 * in your view as you turn on Search.
	 * ~ kempdawg
	 */
	
	private RenderUtil renderUtil = new RenderUtil();
	private ArrayList<Block> cache = new ArrayList<Block>();
	private BlockSelection r = new BlockSelection();
	private boolean checkBlocks = false;
	public ModuleSearch() {
		super("Search", -1, -1, "!s add (name) | remove (name) | clear");
		this.enabled = true;
		this.visible = false;
		this.setBindable(false);
		addHookType(HookTypes.RENDER);
		addField("searchCache",1000);
		addField("searchRange",125);
		addField("viewAirBlocks",false);
		//getClient().getLocationManager().getSearchedBlocks().put(56, 0x1BDDE0);
	}
	private boolean exceedsLimit(int max) {
		return cache.size() >= max;
	}
	private String returnColor(int blockID) {
			switch(blockID) {
			case 56: return "0x2EE8DF";
			case 14: return "0xE3E01E";
			case 15: return "0xAF5D271";
			case 16: return "0xA4ABB0";
			case 46: return "0xFFC400";
			case 52: return "0x4FDE47";
			default: 
				return "0xB5BDB3";
			}
			
	}
	@Override
	public void onCommand(String i) {
		String args[] = i.split(" ");
		if(args[0].equalsIgnoreCase(this.setCommand("s"))) {
			if(args[1].equalsIgnoreCase("add")) {
				
				Integer l = getClient().getID(args[2]);
				int color = Integer.decode(returnColor(l));
				if(l != -1) {
				getClient().getLocationManager().getSearchedBlocks().put(l, color);
				getClient().getChat().sendClientMessage("Block " + l + " has been added with color of " + color);
				
				//Distinct.getInstance().getPlayer().sendQueue.func_147297_a(new S23PacketBlockChange((int)getClient().getPlayer().posX, (int)getClient().getPlayer().posY, (int)getClient().getPlayer().posZ, getClient().getMinecraft().theWorld.getWorld()));
				}else{
					getClient().getChat().sendClientMessage("Specified block name is not recognized.");
				}
			}
			if(args[1].equalsIgnoreCase("remove")) {
				Integer r = getClient().getID(args[2]);
				if(getClient().getLocationManager().getSearchedBlocks().containsKey(r)) {
					getClient().getLocationManager().getSearchedBlocks().remove(r);
					getClient().getChat().sendClientMessage("Block " + r + " has been removed.");
				}
			}
			if(args[1].equalsIgnoreCase("clear")) {
				getClient().getLocationManager().getLocations().clear();
				getClient().getLocationManager().getSearchedBlocks().clear();
				cache.clear();
			}
			if(args[1].equalsIgnoreCase("cache")) {
				setField("blockCache",Integer.parseInt(args[2]));
				reloadItems();
				getClient().getChat().sendClientMessage("Rendering cache limit has been set to " + getValueByField("searchCache") + " blocks.");
			}
			if(args[1].equalsIgnoreCase("range")) {
				setField("blockRange",Double.parseDouble(args[2]));
				reloadItems();
				getClient().getChat().sendClientMessage("Rendering block range has been set to " + getValueByField("searchRange") + " blocks.");
			}
			if(args[1].equalsIgnoreCase("visible")) {
				setField("viewAirBlocks",!Boolean.parseBoolean(getValueByField("viewAirBlocks").toString()));
				reloadItems();
				getClient().getChat().sendClientMessage("Rendering only blocks with air in radius " + (Boolean.parseBoolean(getValueByField("viewAirBlocks").toString()) ? "enabled" : "disabled"));
			}
		}
	}
	public void reloadItems() {
		HashMap<Integer,Integer> copy = new HashMap<Integer,Integer>();
		for(int o : getClient().getLocationManager().getSearchedBlocks().keySet()) {
			int blockID = o;
			int hex = getClient().getLocationManager().getSearchedBlocks().get(blockID);
			copy.put(blockID, hex);
		}
		getClient().getLocationManager().getLocations().clear();
		getClient().getLocationManager().getSearchedBlocks().clear();
		getClient().getMinecraft().renderGlobal.loadRenderers();
		for(int o : copy.keySet()) {
			int blockID = o;
			int hex = copy.get(blockID);
			getClient().getLocationManager().getSearchedBlocks().put(blockID, hex);
		}
	}
	@Override
	public boolean onPacketRecieve(Packet packet) {
		if(packet instanceof S23PacketBlockChange) {
			S23PacketBlockChange s23 = (S23PacketBlockChange) packet;
			for(Location b : getClient().getLocationManager().getLocations()) {
				if(b.getPosX() == s23.field_148887_a && b.getPosY() == s23.field_148885_b && b.getPosZ() == s23.field_148886_c) {
					Block block = getClient().getWorld().getBlock(s23.field_148887_a, s23.field_148885_b, s23.field_148886_c);
					if(block == null || Block.getIdFromBlock(block) == 0) {
						getClient().getLocationManager().getLocations().remove(b);
					}
				}
			}
			
		}
		return false;
	}
	@Override
	public void onDisable() {
		getClient().getLocationManager().getLocations().clear();
		cache.clear();
		removeAllHookTypes();
	}
	@Override
	public void onRender() {
		if(!getClient().getLocationManager().getLocations().isEmpty()) {
			for(Location b : getClient().getLocationManager().getLocations()) {
				if(b != null) {
					Block block = getClient().getWorld().getBlock(b.getPosX(), b.getPosY(), b.getPosZ());
					if(Block.getIdFromBlock(block) == b.getID()) {
						ColorUtil colorUtil = new ColorUtil();
						colorUtil.setHex(b.getColor());
						GL11.glColor3d(colorUtil.getRed(), colorUtil.getGreen(), colorUtil.getBlue());
						renderUtil.glOpen();
						double renderX = RenderManager.renderPosX - b.getPosX();
						double renderY = RenderManager.renderPosY - b.getPosY();
						double renderZ = RenderManager.renderPosZ - b.getPosZ();
						renderBox(b,-renderX,-renderY, -renderZ);
						renderUtil.glClose();
					}
				}
			}
		}
	}
	public void renderBox(Location c, double x,double y,double z) {
		Block var8 = getClient().getWorld().getBlock(c.getPosX(), c.getPosY(), c.getPosZ());
		if(Block.getIdFromBlock(var8) != 0) {
			if(cache.size() <= getClient().getLocationManager().getLocations().size()) {
			cache.add(var8);
			}
			RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x + var8.field_149759_B /**min x **/, 
				    y + var8.field_149760_C, 
				    z + var8.field_149754_D, 
				    x + var8.field_149755_E, 
				    y + var8.field_149756_F, 
				    z + var8.field_149757_G));
			//RenderGlobal.func_147590_a(new AxisAlignedBB(x + Block.blocksList[block].minX, y + Block.blocksList[block].minY, z + Block.blocksList[block].minZ, x + Block.blocksList[block].maxX, y + Block.blocksList[block].maxY, z + Block.blocksList[block].maxZ), -1);
		}
	}
	private boolean isAlreadyRendering(final Location block) {
	    for (final Location blockLoc : getClient().getLocationManager().getLocations()) {
	        if(blockLoc.equals(block)) {
	            return true;
	        }
	    }
	    return false;
	}
	@Override
	public void onRenderChunk(int i3,int x,int y,int z) {
		for(Object b : Distinct.getInstance().getLocationManager().getSearchedBlocks().keySet()) {
			Integer blockID = (Integer)b;
			Integer color = (Integer)Distinct.getInstance().getLocationManager().getSearchedBlocks().get(blockID);
			if(i3 == blockID) {
				Location c = new Location(i3,x,y,z,color);
				if(!isAlreadyRendering(c) && inRange(c.getPosX(),c.getPosY(),c.getPosZ()) && !exceedsLimit(Integer.parseInt(getValueByField("searchCache").toString()))) {
					Distinct.getInstance().getLocationManager().addLocation(c);
				}
			}
		}
	}
	
	private boolean inRange(double var1, double var3, double var5)
    {
        return Distinct.getInstance().getPlayer().getDistance(var1, var3, var5) <= Double.parseDouble(getValueByField("searchRange").toString());
    }
}
