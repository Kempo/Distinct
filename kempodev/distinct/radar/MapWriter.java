package kempodev.distinct.radar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

import java.util.*;
import java.util.concurrent.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.lwjgl.input.Keyboard;

/*

data transfers
---------------
chunk image (16x16 int[]) -> texture (512x512 GL texture)	| every chunk update
region file png -> texture (512x512 GL texture)				| on region load (slow, disk access)
texture (512x512 GL texture) -> region file png				| on region unload (slow, disk access)
chunk (Chunk object) -> anvil save file						| on chunk unload, separate thread handled by minecraft

background thread
------------------
performs all data transfers except Chunk->Anvil, which is handled by ThreadedFileIOBase in minecraft.
regions created in main thread when necessary, but filled from the background thread.

*/

public class MapWriter {
	// Hooks in other minecraft files call methods in MapWriter using:
	//  MapWriter.instance.methodName()
	public static MapWriter instance;
	
	private Minecraft mc = null;
	MapWriterColours colours = new MapWriterColours();
	public boolean multiplayer = false;
	private int tickCounter = 0;
	private int chunkCount = 0;
	private String worldName;
	private File worldDir = null;
	private File imageDir = null;
	
	private final static int[] keyBindArray = {
		Keyboard.KEY_M, 				// key for map mode toggle
		Keyboard.KEY_COMMA,				// marker set/remove
		Keyboard.KEY_PERIOD,			// marker colour
		Keyboard.KEY_UP,				// map pan up
		Keyboard.KEY_DOWN,				// map pan down
		Keyboard.KEY_LEFT,				// map pan left
		Keyboard.KEY_RIGHT};			// map pan right
	
	// whether a key was down the previous tick
	// array must be the same size as keyBindArray above.
	private boolean[] keyDownLastArray = {false, false, false, false, false, false, false};
	
	// constants
	public final static int CHUNK_SIZE = 16;
	public final static int REGION_SIZE = 32 * 16;
	public final static int REGION_MASK = ~(REGION_SIZE - 1);
	public final static int UNLOAD_DIST = REGION_SIZE * 3;
	public final static int PAN_INCREMENT = REGION_SIZE / 2;
	public final static String saveDir = "worlds";
	
	// material colours
	public static final int BC_AIR     = 0x00000000;
	public static final int BC_STONE   = 0xff707070;
	public static final int BC_GRAVEL  = 0xff716053;
	public static final int BC_GRASS   = 0xff7fb238;
	public static final int BC_SAND    = 0xfff7e9a3;
	public static final int BC_SSTONE  = 0xfff5e38c;
	public static final int BC_CLOTH   = 0xffa7a7a7;
	public static final int BC_LAVA    = 0xffff0000;
	public static final int BC_ICE     = 0xffa0a0ff;
	public static final int BC_IRON    = 0xffa7a7a7;
	public static final int BC_FOLIAGE = 0xff007c00;
	public static final int BC_SNOW    = 0xffffffff;
	public static final int BC_CLAY    = 0xffa4a8b8;
	//public static final int BC_DIRT    = 0xffb76a2f;
	//public static final int BC_DIRT    = 0xff715036;
	public static final int BC_DIRT    = 0xff945d32;
	public static final int BC_WATER   = 0x404040ff;
	public static final int BC_DWATER  = 0x402727ff;
	public static final int BC_WOOD    = 0xff685332;
	public static final int BC_OBSID   = 0xff1a1627;
	public static final int BC_BRICK   = 0xff874a3a;
	
	// block colours for the first 256 block ID's
	public static final int[] blockColourArray = {
		// 0 - 3f
		BC_AIR, BC_STONE, BC_GRASS, BC_DIRT, BC_STONE, BC_WOOD, BC_AIR, BC_STONE,
		BC_WATER, BC_WATER, BC_LAVA, BC_LAVA, BC_SAND, BC_GRAVEL, BC_STONE, BC_IRON,
		BC_STONE, BC_WOOD, BC_FOLIAGE, BC_AIR, BC_AIR, BC_STONE, BC_WATER, BC_AIR,
		BC_SSTONE, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_FOLIAGE,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_IRON, BC_STONE, BC_STONE, BC_BRICK, BC_AIR, BC_WOOD,
		BC_STONE, BC_OBSID, BC_AIR, BC_AIR, BC_AIR, BC_WOOD, BC_WOOD, BC_AIR,
		BC_STONE, BC_AIR, BC_WOOD, BC_GRASS, BC_DIRT, BC_STONE, BC_STONE, BC_AIR,
		// 40 - 7f
		BC_AIR, BC_AIR, BC_AIR, BC_STONE, BC_AIR, BC_AIR, BC_STONE, BC_AIR,
		BC_WOOD, BC_STONE, BC_STONE, BC_AIR, BC_AIR, BC_AIR, BC_SNOW, BC_ICE,
		BC_SNOW, BC_FOLIAGE, BC_CLAY, BC_FOLIAGE, BC_AIR, BC_WOOD, BC_AIR, BC_BRICK,
		BC_STONE, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_WOOD,
		BC_WOOD, BC_AIR, BC_STONE, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_FOLIAGE, BC_FOLIAGE, BC_AIR, BC_WOOD, BC_BRICK, BC_STONE, BC_CLAY, BC_FOLIAGE,
		BC_BRICK, BC_BRICK, BC_BRICK, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_SAND, BC_AIR, BC_AIR, BC_AIR, BC_WOOD, BC_WOOD, BC_AIR,
		// 80 - bf
		BC_SSTONE, BC_STONE, BC_AIR, BC_AIR, BC_AIR, BC_GRASS, BC_WOOD, BC_WOOD,
		BC_WOOD, BC_AIR, BC_STONE, BC_AIR, BC_FOLIAGE, BC_FOLIAGE, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		// c0 - ff
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR,
		BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR, BC_AIR};
	
	// instances of components
	private MapWriterOverlay overlay = null;
	private MapWriterExecutor mapWriterExecutor = null;
	private MapWriterAnvil mapWriterAnvil = null;
	
	// region map (region coords as Long -> region)
	private HashMap<Long, MapWriterRegion> regionMap;
	
	public MapWriter() {
		MapWriter.instance = this;
		colours.genBlockColors();
	}
	
	// Called on login and after dimension change.
	public void enterDimension(WorldClient wc) {
		
		// the chunkloader saves chunks to the region files in the 'region' subdirectory.
		// it needs to be updated when the player changes dimension
		if (this.multiplayer) {
			this.mapWriterAnvil.updateChunkLoader(this.worldDir, wc.provider.dimensionId);
		}
		
		//this.overlay.setSpawnPos(wc.getWorldInfo().getSpawnX(), wc.getWorldInfo().getSpawnZ());
	}
	
	// Called before dimension change and before disconnect.
	public void leaveDimension() {
		if (this.multiplayer)
			this.mapWriterAnvil.closeChunkLoader();
	}
	
	/*
	Following methods are called from hooks in NetClientHandler, Chunk, and EntityRenderer.
	*/
	
	// Called from NetClientHandler.handleLogin
	public void hookHandleLogin(Minecraft mc, NetHandlerPlayClient nch,WorldClient wc) {
		// load components
		this.mapWriterExecutor = new MapWriterExecutor();
		this.mapWriterAnvil = new MapWriterAnvil();
		this.regionMap = new HashMap<Long, MapWriterRegion>();
		
		// init values
		this.tickCounter = 0;
		this.chunkCount = 0;
		
		this.mc = mc;
		this.multiplayer = !this.mc.isIntegratedServerRunning();
		
		// get hostname from socketAddress object
		/**
		InetSocketAddress sockAddr = (InetSocketAddress) nch.getNetworkManager().getSocketAddress();
		this.worldName = String.format("%s.%d", sockAddr.getHostName(), sockAddr.getPort());
		
		// create directories
		this.worldDir = new File(this.saveDir, this.worldName);
		this.imageDir = new File(this.worldDir, "images");
		this.imageDir.mkdirs();
		**/
		// worldName is MpServer by default on multiplayer servers
		if (this.multiplayer) {
			wc.worldInfo.setWorldName(this.worldName);
		}
		
		// read marker files
		ArrayList<Integer> markerList = this.readMarkerFile(new File(this.worldDir, "markers.txt"));
		
		// instance of the overlay object
		this.overlay = new MapWriterOverlay(this, mc, markerList);
		
		this.enterDimension(wc);
	}
	
	// Called from NetClientHandler.quitWithPacket
	// is called before mc.loadWorld(null) on quit
	public void hookQuitWithPacket() {
		// save chunks
		this.leaveDimension();
		
		// close all loaded regions, saving modified images
		for (MapWriterRegion region : this.regionMap.values()) {
			region.close();
		}
		this.regionMap.clear();
		
		// save world and player info to level.dat
		if (this.multiplayer) {
			this.mapWriterAnvil.saveWorld(this.worldDir, this.mc.theWorld.getWorldInfo(), this.mc.thePlayer);
		}
		
		// write marker files
		ArrayList<Integer> markerList = this.overlay.getMarkerList();
		if (markerList != null) {
			this.writeMarkerFile(new File(this.worldDir, "markers.txt"), markerList);
		}
		
		// close overlay
		this.overlay.close();
		
		// wait for file background thread to finish
		System.out.println("MapWriter: waiting for region io tasks to finish...");
		if (this.mapWriterExecutor.close()) {
			System.out.println("MapWriter: error: timeout waiting for region io tasks to finish");
		}
	}
	
	// Called from NetClientHandler.handleRespawn
	public void hookHandleRespawnPre(WorldClient wc, boolean dimensionChange) {
		// if changing dimensions the old World will be unloaded and the new World loaded in its place.
		// need to save any chunks that have not already been saved before that happens.
		if (dimensionChange) {
			// save all loaded chunks in anvil format
			this.leaveDimension();
		}
	}
	
	// Called from NetClientHandler.handleRespawn
	public void hookHandleRespawnPost(WorldClient wc, boolean dimensionChange) {
		// if dimension was changed then wc will hold the WorldClient for the new dimension	
		if (dimensionChange)
			this.enterDimension(wc);
	}
	
	// Called from Chunk.fillChunk
	public void hookFillChunk(Chunk chunk) {
		if (chunk != null) {
			if (chunk.worldObj.provider.dimensionId == 0) {
				MapWriterRegion region = this.getOrCreateRegion(chunk.xPosition << 4, chunk.zPosition << 4);
				//if ((chunk.xPosition == 0) && (chunk.zPosition == 0)) {
				//	System.out.format("MapWriter.hookFillChunk: chunk (0,0) calling region(%d, %d).update(chunk)\n", region.x, region.z);
				//}
				region.updateTexture(chunk);
			}
			if (this.multiplayer)
				this.mapWriterAnvil.addChunk(chunk);
		}
	}
	
	// Called from Chunk.onChunkUnload
	public void hookOnChunkUnload(Chunk chunk) {
		if ((chunk != null) && this.multiplayer) {
			this.mapWriterAnvil.unloadChunk(chunk);
		}
	}
	
	// EntityRenderer.updateCameraAndRender
	// Called every in game tick (but not while in menus).
	// It is called just after the in game overlay (HUD) has been rendered so the
	// graphics context is still set up for drawing to the overlay.
	public void hookUpdateCameraAndRender(Minecraft mc) {
		if (this.mc.thePlayer != null) {
			
			this.overlay.update(mc);
			
			this.overlay.draw();
			
			this.mapWriterExecutor.processTaskQueue();
			
			this.checkForKeyPresses();
			
			// every 32 ticks check if any unused regions can be unloaded
			if ((this.tickCounter & 0x1f) == 0) {
				this.closeOldRegions();
			}
			
			this.tickCounter++;
		}
	}
	
	// check for keys pressed this tick and perform appropriate action.
	private void checkForKeyPresses() {
		for (int i = 0; i < this.keyBindArray.length; i++) {
			boolean keyDown = Keyboard.isKeyDown(this.keyBindArray[i]);
			// key pressed if was not down last tick but is down this tick.
			// don't process key press if we are in a gui.
			if (!this.keyDownLastArray[i] && keyDown && (this.mc.currentScreen == null)) {
				switch (i) {
					case 0:
						this.overlay.nextOverlayMode();
						break;
					case 1:
						this.overlay.setMarkerAtPlayerPos();
						break;
					case 2:
						this.overlay.nextMarkerMode();
						break;
					case 3:
						this.overlay.panView(0, -PAN_INCREMENT);
						break;
					case 4:
						this.overlay.panView(0, PAN_INCREMENT);
						break;
					case 5:
						this.overlay.panView(-PAN_INCREMENT, 0);
						break;
					case 6:
						this.overlay.panView(PAN_INCREMENT, 0);
						break;
				}
			}
			this.keyDownLastArray[i] = keyDown;
		}
	}
	
	// checks regionMap to see if the region that contains the given coordinate is loaded.
	// if no region is loaded for the position create a new region and return that.
	public MapWriterRegion getOrCreateRegion(int x, int z) {
		int regionX = x & REGION_MASK;
		int regionZ = z & REGION_MASK;
		// Long key = new Long(((((long) regionZ) & 0xffffffffL) << 32) | (((long) regionX) & 0xffffffffL));
		Long key = new Long(ChunkCoordIntPair.chunkXZ2Int(regionX, regionZ));
		MapWriterRegion region = this.regionMap.get(key);
		if (region == null) {
			region = new MapWriterRegion(regionX, regionZ, this.imageDir, mapWriterExecutor);
			this.regionMap.put(key, region);
		}
		return region;
	}
	
	// close regions not accessed in a certain number of ticks (32 * 32 = 1024 ticks)
	public void closeOldRegions() {
		Iterator it = this.regionMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			MapWriterRegion region = (MapWriterRegion) entry.getValue();
			if (region.lastAccessed > 32) {
				region.close();
				it.remove();
			} else {
				region.lastAccessed++;
			}
		}
    }
	
	public ArrayList<Integer> readMarkerFile(File file) {
		Scanner s = null;
		ArrayList<Integer> intList = new ArrayList<Integer>();
		try {
            s = new Scanner(new BufferedReader(new FileReader(file)));
			boolean end = false;
			int x = 0;
			int z = 0;
			int color = 0;
            while (!end) {
				if (s.hasNextInt())
					// get x value
					x = s.nextInt();
				
				if (s.hasNextInt())
					// get x value
					z = s.nextInt();
				
				if (s.hasNextInt()) {
					// get color value
					int colourIndex = Math.min(Math.max(MapWriterOverlay.colourIndexMin, s.nextInt()), MapWriterOverlay.colourIndexMax);
					intList.add(x);
					intList.add(z);
					intList.add(colourIndex);
				} else
					end = true;
			}
		}
		catch (IOException e) {
			System.out.format("MapWriter.readMarkerFile: could not read marker file '%s'\n", file.getPath());
			//e.printStackTrace();
			intList = null;
		}
        finally {
            if (s != null) {
                s.close();
            }
        }
		return intList;
	}
	
	public boolean writeMarkerFile(File file, ArrayList<Integer> intList) {
		BufferedWriter f = null;
		boolean error = false;
		try {
			f = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i + 2 < intList.size(); i += 3) {
				f.write(String.format("%d %d %d\n", intList.get(i), intList.get(i + 1), intList.get(i + 2)));
			}
		}
		catch (IOException e) {
			error = true;
			System.out.format("MapWriter.writeMarkerFile: IO exception writing to '%s'\n", file.getPath());
			e.printStackTrace();
		}
		finally {
			if (f != null) {
				try { f.close(); }
				catch (IOException e) {}
			}
		}
		return error;
	}
}
