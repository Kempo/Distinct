package kempodev.distinct.radar;

import java.util.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;

import java.awt.image.BufferedImage;

import kempodev.distinct.main.Distinct;

/*
	MapWriter.Region class
	Represents a 32x32 chunk area (512x512 blocks).
	Contains the GL texture reference for the texture containing the map pixels.
	Also contains the backing image file to load or save the texture to.
*/
public class MapWriterRegion {
	public int x;
	public int z;
	private int texture;
	//private int[] pixels = new int[MapWriter.REGION_SIZE * MapWriter.REGION_SIZE];
	private File imageFile;
	private MapWriterExecutor mapWriterExecutor;
	public int lastAccessed = 0;
	public int updateCount = 0;
	
	public MapWriterRegion(int x, int z, File imageDir, MapWriterExecutor mapWriterExecutor) {
		this.x = x;
		this.z = z;
		this.mapWriterExecutor = mapWriterExecutor;
		
		// generate the filename based on the x and z coords
		String filename = String.format("%d.%d.png", x, z);
		this.imageFile = new File(imageDir, filename);
		
		// create a blank texture initially
		this.texture = MapWriterRender.allocateAndFillTexture(MapWriter.REGION_SIZE, MapWriter.REGION_SIZE, 0x80000000);
		
		System.out.format("MapWriterOverlay.Region: new region at (%d, %d) with texture %d\n", x, z, this.texture);
		
		// load the backing image file if it exists
		this.mapWriterExecutor.addTask(new TaskLoadRegion(this));
	}
	
	// update the calculated pixels in the GL texture
	public void updateTexture(Chunk chunk) {
		this.mapWriterExecutor.addTask(new TaskUpdateRegion(this, chunk));
		this.lastAccessed = 0;
		this.updateCount++;
	}
	
	public int getTexture() {	
		return this.texture;
	}
	
	public void touch() {
		this.lastAccessed = 0;
	}
	
	public void deleteTexture() {
		if (this.texture != 0) {
			MapWriterRender.deleteTexture(this.texture);
			this.texture = 0;
		}
	}
	
	// schedule close, write texture to backing image, deallocate texture
	public void close() {
		if (this.updateCount == 0) {
			System.out.format("MapWriterOverlay.Region: closing region at (%d, %d) with texture %d\n", x, z, this.texture);
			this.deleteTexture();
		} else {
			System.out.format("MapWriterOverlay.Region: saving and closing region at (%d, %d) with texture %d, and %d chunk updates\n", x, z, this.texture, this.updateCount);
			this.mapWriterExecutor.addTask(new TaskCloseRegion(this));
		}
	}
	
	/*
	Tasks for loading, closing, and updating regions.
	The run() method of each Task is run in a background thread.
	The onComplete() method runs after the background thread has processed this task.
	*/
	
	public class TaskLoadRegion extends MapWriterTask {
		private MapWriterRegion region;
		private BufferedImage img = null;
		private File imageFile;
		private int texture;
		
		// read from image file when given img is null.
		// otherwise write the given image to 
		public TaskLoadRegion(MapWriterRegion region) {
			this.region = region;
			this.imageFile = region.imageFile;
			this.texture = region.getTexture();
		}
		
		public void run() {
			// read from the image file
			try {
				this.img = ImageIO.read(this.imageFile);
				// check dimensions are ok
				if ((this.img.getWidth() != MapWriter.REGION_SIZE) || (this.img.getHeight() != MapWriter.REGION_SIZE)) {
					this.img = null;
				}
			}
			catch (IOException e) {
				this.img = null;
			}
		}
		
		public void onComplete() {
			if (img != null) {
				MapWriterRender.updateTextureFromImage(this.region.getTexture(), img);
			}
		}
	}
	
	public class TaskCloseRegion extends MapWriterTask {
		private BufferedImage img;
		private MapWriterRegion region;
		private File imageFile;
		
		public TaskCloseRegion(MapWriterRegion region) {
			this.region = region;
			this.imageFile = region.imageFile;	// copy imageFile in case it changes before the thread starts
			this.img = MapWriterRender.createImageFromTexture(region.getTexture(), MapWriter.REGION_SIZE, MapWriter.REGION_SIZE);
		}
		
		public void run() {
			// write the given image to the image file
			try {
				ImageIO.write(this.img, "png", this.imageFile);
			}
			catch (IOException e) {
				System.out.format("MapWriterExecutor: error: could not write image to %s\n", imageFile.getName());
			}
		}
		
		public void onComplete() {
			// delete region texture, freeing up GL resources
			this.region.deleteTexture();
		}
	}
	
	// Update a region with data from a chunk.
	// This is mainly done in a task so that it occurs after the backing image is loaded for
	// the region. Otherwise updated chunks would be overwritten when the image finishes loading.
	//
	// Not sure if really need (or should) do the pixel calculations in the background thread.
	// The chunk block data may change while the thread is running, causing corruption.
	// Could either create a copy of the needed block data before the thread starts, or just
	// calculate the pixels in the main thread. Then would pass pixels[256] instead of chunk.
	//
	// A better alternative is to calculate the pixels in the onComplete method, the advantage being
	// only one onComplete is called per tick, which should prevent stuttering when many chunks
	// are loaded in the same tick.
	//
	public class TaskUpdateRegion extends MapWriterTask {
		private MapWriterRegion region;
		private Chunk chunk;
		 
		public TaskUpdateRegion(MapWriterRegion region, Chunk chunk) {
			this.region = region;
			this.chunk = chunk;
		}
		
		public void run() {
			// could put pixel calculation here, but would risk corruption
		}
		
		private void shadeChunkPixels(int[] pixels) {
			for (int z = MapWriter.CHUNK_SIZE - 1; z >= 0; z--) {
				for (int x = MapWriter.CHUNK_SIZE - 1; x >= 0; x--) {
					int offset = z * MapWriter.CHUNK_SIZE + x;
					int height = (pixels[offset] >> 24) & 0xff;
					
					int heightW = height;
					if ((x - 1) >= 0) {
						heightW = (pixels[z * MapWriter.CHUNK_SIZE + (x - 1)] >> 24) & 0xff;
					}
					
					int heightS = height;
					if ((z - 1) >= 0) {
						heightS = (pixels[(z - 1) * MapWriter.CHUNK_SIZE + x] >> 24) & 0xff;
					}
					
					// heightDiff between -510 and 510.
					int heightDiff = (height - heightW) + (height - heightS);
					int heightShading = Math.min(Math.max(-2, heightDiff), 2) * 8;
					heightShading += Math.min(Math.max(-16, heightDiff), 8) * 2;
					int r = ((pixels[offset] >> 16) & 0xff);
					int g = ((pixels[offset] >> 8) & 0xff);
					int b = ((pixels[offset] >> 0) & 0xff);
					r = Math.min(Math.max(0, r + heightShading), 0xff);
					g = Math.min(Math.max(0, g + heightShading), 0xff);
					b = Math.min(Math.max(0, b + heightShading), 0xff);
					pixels[offset] = 0xff000000 | (r << 16) | (g << 8) | (b);
				}
			}
		}
		
		// calculate the color of the topmost pixel at each (x,z) location in the chunk.
		// update the region GL texture once pixels are calculated.
		public void onComplete() {
			if (this.region.getTexture() != 0) {
				int[] pixels = new int[MapWriter.CHUNK_SIZE * MapWriter.CHUNK_SIZE];
				for (int z = 0; z < MapWriter.CHUNK_SIZE; z++) {
					for (int x = 0; x < MapWriter.CHUNK_SIZE; x++) {
						int y = getHeightValue(x, z,chunk);
						int colour = 0;
						int waterCount = 0;
						//Block block = Distinct.getInstance().getWorld().getBlock(x, y, z);
						Block block = chunk.func_150810_a(x, y,z);
						int blockID = Block.getIdFromBlock(block);
						// search in a column downwards for the first opaque block (alpha = 0xff).
						/**
						for (y = 255; (y > 0) && ((colour & 0xff000000) != 0xff000000); y--) {
							int blockID = this.chunk.func_150808_b(x, y, z);
							if(blockID != 255) {
							colour = (blockID <= 0xff) ? MapWriter.blockColourArray[blockID] : MapWriter.BC_AIR;
								if (colour == MapWriter.BC_WATER) {
									waterCount++;
								}
							}
						}
						// if the block is underwater it should be the colour of water.
						if (waterCount > 0) {
							colour = MapWriter.BC_WATER;
						}
						**/

						//colour = (blockID <= 0xff) ? MapWriter.blockColourArray[blockID] : MapWriter.BC_AIR;
						if(blockID != 0) {
						colour = block.getMaterial().getMaterialMapColor().colorValue;
						// the height of the block goes into the alpha channel.
						// the shadeChunkPixels uses this function to shade differences in height appropriately.
						//System.out.println(blockID + "     " + colour);
						pixels[z * MapWriter.CHUNK_SIZE + x] = ((y & 0xff) << 24)/** | (colour & 0xffffff)**/;
						}
					}
				}
				// shade the pixels
				//this.shadeChunkPixels(pixels);

				// copy the calculated pixels to the region texture
				int textureX = (this.chunk.xPosition << 4) - this.region.x;
				int textureZ = (this.chunk.zPosition << 4) - this.region.z;

				if ((textureX >= 0) && (textureX < MapWriter.REGION_SIZE) && (textureZ >= 0) && (textureZ < MapWriter.REGION_SIZE)) {
					GL11.glDisable(GL11.GL_LIGHTING);
					MapWriterRender.copyPixelsToBuffer(pixels);
					
					MapWriterRender.updateTexture(this.region.getTexture(), textureX, textureZ, MapWriter.CHUNK_SIZE, MapWriter.CHUNK_SIZE);
					GL11.glEnable(GL11.GL_LIGHTING);
				}
			} else {
				System.out.format("MapWriter.TaskUpdateRegion: error: region (%d, %d) has no texture (already closed?)\n", this.region.x, this.region.z);
			}
			this.region.lastAccessed = 0;
		}
	}
	 public int getHeightValue(int par1, int par2,Chunk chunk)
	    {
	        if (par1 >= -30000000 && par2 >= -30000000 && par1 < 30000000 && par2 < 30000000)
	        {
	                //Chunk var3 = this.getChunkFromChunkCoords(par1 >> 4, par2 >> 4);
	                return chunk.getHeightValue((par1 >> 4) & 15, (par2 >> 4) & 15);
	        }
	        else
	        {
	            return 64;
	        }
	    }
}
