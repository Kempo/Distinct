package kempodev.distinct.radar;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.*;
import java.io.File;
import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

import kempodev.distinct.main.Distinct;

public class MapWriterOverlay {
	private Minecraft mc;
	private MapWriter mw;
	
	// regionArray holds the 4 nearest regions to the player
	// (regionArrayX, regionArrayZ) is the coordinate of the top left corner.
	private MapWriterRegion[] regionArray = new MapWriterRegion[4];
	private int regionArrayX = 0;
	private int regionArrayZ = 0;
	private int regionArrayW = MapWriter.REGION_SIZE * 2;
	private int regionArrayH = MapWriter.REGION_SIZE * 2;
	
	// 0 = disabled, 1 = small, 2 = large
	private int overlayMode = 0;
	
	private ArrayList<Integer> markerList = null;
	public static final int[] markerColours = {0xffffffff, 0xffffffff, 0xffff8000, 0xff00ffff, 0xffff00ff, 0xffffff00, 0xff0000a0};
	public static final String[] markerModes = {"all", "hidden", "orange", "cyan", "magenta", "yellow", "dark blue"};
	public static final int colourIndexMin = 2;
	public static final int colourIndexMax = markerColours.length - 1;
	private int markerMode = 0;
	
	// scaled screen width and height
	private double sw = 320.0D;
	private double sh = 240.0D;
	
	// overlay geometry in terms of screen coordinates
	private double overlayX = 0.0D;
	private double overlayY = 0.0D;
	private double overlayW = 90.0D;
	private double overlayH = 90.0D;
	
	// player position and heading
	private double playerX = 0.0D;
	private double playerZ = 0.0D;
	private double playerY = 0.0D;
	private int playerXInt = 0;
	private int playerYInt = 0;
	private int playerZInt = 0;
	private double playerXUnscaled = 0;
	private double playerYUnscaled = 0;
	private double playerZUnscaled = 0;
	private double playerHeading = 0.0D;
	
	// the geometry of the 'view' of the map using game (block) coordinates
	private double viewX = 0.0;
	private double viewZ = 0.0;
	// if the view dimensions are greater than the region size the view will clamp to
	// the region array borders somewhat before the region array is reloaded.
	// this is because more than 4 regions are needed when the viewW or viewH is greater
	// than the region size.
	// if the view dimensions are set to region_size * 2 the map view will not pan at all
	// as the player moves. it will only change when the player nears a region border.
	private double viewW = (double) MapWriter.REGION_SIZE;
	private double viewH = (double) MapWriter.REGION_SIZE;
	
	// view geometry within the region array.
	// used to calculate which portions of each region to display (using texture UV coords).
	private double viewRelX = 0.0D;
	private double viewRelZ = 0.0D;
	private double viewRelW = 1.0D;
	private double viewRelH = 1.0D;
	
	// used to stop the map snapping back to the player when it has been manually panned.
	public boolean keepViewCentredOnPlayer = true;
	
	// pixels and texture for underground view
	int[] pixels = new int[16 * 16];
	int undergroundTexture;
	
	public MapWriterOverlay(MapWriter mw, Minecraft mc, ArrayList<Integer> markerList) {
		this.mw = mw;
		this.mc = mc;
		this.markerList = markerList;
		
		this.undergroundTexture = MapWriterRender.allocateAndFillTexture(16, 16, 0xff000000);
		
		// initialize overlayX/Y/W/H variables, and set mode to small map
		this.nextOverlayMode();
	}
	
	public void close() {
		if (this.undergroundTexture != 0) {
			MapWriterRender.deleteTexture(this.undergroundTexture);
		}
		this.regionArray = null;
	}
	
	public void panView(double relX, double relZ) {
		this.keepViewCentredOnPlayer = false;
		this.viewX += relX;
		this.viewZ += relZ;
	}
	
	public void setViewCentre(double x, double z) {
		this.viewX = x - (this.viewW / 2.0);
		this.viewZ = z - (this.viewH / 2.0);
	}
	
	public void nextMarkerMode() {
		this.markerMode = (this.markerMode + 1) % this.markerModes.length;
		Distinct.getInstance().getChat().sendClientMessage(String.format("marker mode set to: %s", markerModes[this.markerMode]));
	}
	
	public int getNearestMarker(int x, int z, int maxDistance) {
		int nearestDistance = maxDistance * maxDistance;
		int nearestIndex = -1;
		if (this.markerList != null) {
			for (int i = 0; i + 2 < this.markerList.size(); i += 3) {
				int dx = x - this.markerList.get(i);
				int dz = z - this.markerList.get(i + 1);
				int d = (dx * dx) + (dz * dz);
				if (d < nearestDistance) {
					nearestIndex = i;
					nearestDistance = d;
				}
			}
		}
		return nearestIndex;
	}
	
	public void setMarker(int x, int z) {
		Distinct.getInstance().getChat().sendClientMessage(String.format("%s marker set at (%d, %d)", markerModes[this.markerMode], x, z));
		this.markerList.add(x);
		this.markerList.add(z);
		this.markerList.add(this.markerMode);
	}
	
	public void setMarkerChecked(int x, int z) {
		if (this.markerList == null) {
			this.markerList = new ArrayList<Integer>();
		}
		int i = this.getNearestMarker(x, z, 30);
		if (i < 0) {
			// set a new marker if no visible marker is already present
			if (this.markerMode >= 2) {
				this.setMarker(x, z);
			} else {
				Distinct.getInstance().getChat().sendClientMessage("no colour selected, press the marker mode key to select a colour");
			}
		} else {
			// if a marker is visible nearby remove it
			if ((this.markerMode == 0) || (this.markerMode == this.markerList.get(i + 2))) {
				Distinct.getInstance().getChat().sendClientMessage(String.format("removed marker at (%d, %d)", this.markerList.get(i), this.markerList.get(i + 1)));
				this.markerList.subList(i, i + 3).clear();
			} else {
				this.setMarker(x, z);
			}
		}
	}
	
	public void setMarkerAtPlayerPos() {
		this.setMarkerChecked(this.playerXInt, this.playerZInt);
	}
	
	public ArrayList<Integer> getMarkerList() {
		return this.markerList;
	}
	
	// toggle between small map, large map and no map
	public void nextOverlayMode() {
		this.overlayMode = (this.overlayMode + 1) % 4;
		this.keepViewCentredOnPlayer = true;
		/**
		if (this.overlayMode == 3) {
			this.updateUndergroundPixels();
		}
		**/
	}
	/**
	private void updateUndergroundPixels() {
		int px = (int) Math.floor(this.playerXUnscaled);
		int py = (int) Math.floor(this.playerYUnscaled);
		int pz = (int) Math.floor(this.playerZUnscaled);
		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				int airCount = 0;
				int lavaCount = 0;
				int waterCount = 0;
				for (int y = 0; y < 8; y++) {
					Block block = this.mc.theWorld.getBlock(x + px - 8, y + py - 4, z + pz - 8);
					int blockID = Block.getIdFromBlock(block);
					if (MapWriter.blockColourArray[blockID] == MapWriter.BC_AIR) {
							airCount++;
					} else if (MapWriter.blockColourArray[blockID] == MapWriter.BC_WATER) {
							waterCount++;
					} else if (MapWriter.blockColourArray[blockID] == MapWriter.BC_LAVA) {
							lavaCount++;
					}
				}
				int colour = 0;
				if (lavaCount > 0) {
					colour = 0xff000000 | ((lavaCount * 32) << 16);
				} else if (waterCount > 0) {
					colour = 0xff000000 | ((waterCount * 32));
				} else {
					colour = 0xff000000 | ((airCount * 20) << 8);
				}
				this.pixels[z * 16 + x] = colour;
			}
		}
		MapWriterRender.copyPixelsToBuffer(this.pixels);
		MapWriterRender.updateTexture(this.undergroundTexture, 0, 0, 16, 16);
	}
	**/
	// update the screen resolution, and recalculate the overlay position and dimensions.
	public void updateScreenResolution() {
		// scaled screen resolution, used for drawing overlay at different scales depending on the resolution
		ScaledResolution sRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		this.sw = sRes.getScaledWidth_double();
		this.sh = sRes.getScaledHeight_double();
		
		if (this.overlayMode == 2) {
			// draw large map, 210x210, 10 from top, centred horizontally
			this.overlayX = (this.sw / 2.0D) - 96.0D;
			this.overlayY = 10.0D;
			this.overlayW = 192.0D;
			this.overlayH = 192.0D;
			this.viewW = (double) MapWriter.REGION_SIZE;
			this.viewH = (double) MapWriter.REGION_SIZE;
		} else {
			// draw small map, 80x80, 10 from top, 10 from right side
			this.overlayX = this.sw - 90.0D;
			this.overlayY = 10.0D;
			this.overlayW = 80.0D;
			this.overlayH = 80.0D;
			this.viewW = (double) MapWriter.REGION_SIZE / 2.0;
			this.viewH = (double) MapWriter.REGION_SIZE / 2.0;
		}
	}
	
	public void update(Minecraft mc) {
		this.mc = mc;
		this.playerXUnscaled = (double) mc.thePlayer.posX;
		this.playerYUnscaled = (double) mc.thePlayer.posY;
		this.playerZUnscaled = (double) mc.thePlayer.posZ;
		
		// account for nether scaling
		if (mc.theWorld.provider.dimensionId != 0) {
			this.playerX = playerXUnscaled * 8.0;
			this.playerZ = playerZUnscaled * 8.0;
		} else {
			this.playerX = playerXUnscaled;
			this.playerZ = playerZUnscaled;
		}
		this.playerY = this.playerYUnscaled;
		
		// player pos as integer, rounded towards negative infinity
		int px = (int) Math.floor(this.playerX);
		int py = (int) Math.floor(this.playerY);
		int pz = (int) Math.floor(this.playerZ);
		
		// rotationYaw of 0 points due north, we want it to point due east instead
		// so add pi/2 radians (90 degrees)
		this.playerHeading = Math.toRadians(mc.thePlayer.rotationYaw) + (Math.PI / 2.0D);
		
		// update the underground view if the player moves to a new block.
		if ((px != this.playerXInt) || (py != this.playerYInt) || (pz != this.playerZInt)) {
			this.playerXInt = px;
			this.playerYInt = py;
			this.playerZInt = pz;
			/**
			if (overlayMode == 3) {
				this.updateUndergroundPixels();
			}
			**/
		}
		
		// centre the map if necessary
		if (this.keepViewCentredOnPlayer) {
			setViewCentre(this.playerX, this.playerZ);
		}
		
		// region_coords_of(map_centre - half_region_size)
		// calculates the region coords of the upper left region in the overlay
		int rSize = MapWriter.REGION_SIZE;
		int viewCentreX = (int) (this.viewX + (this.viewW / 2));
		int viewCentreZ = (int) (this.viewZ + (this.viewH / 2));
		int rX = (viewCentreX - (rSize / 2)) & MapWriter.REGION_MASK;
		int rZ = (viewCentreZ - (rSize / 2)) & MapWriter.REGION_MASK;
		
		// check if we need to update the region array
		if ((rX != this.regionArrayX) || (rZ != this.regionArrayZ)) {
			this.regionArrayX = rX;
			this.regionArrayZ = rZ;
			this.updateRegionArray();
		}
		
		// reset last accessed counter for each region being displayed
		for (int i = 0; i < this.regionArray.length; i++) {
			if (this.regionArray[i] == null) {
				this.updateRegionArray();
			} else {
				this.regionArray[i].touch();
			}
		}
		
		// clamp view to region array limits
		this.viewX = Math.min(Math.max((double) this.regionArrayX, this.viewX), (double) (this.regionArrayX + this.regionArrayW) - this.viewW);
		this.viewZ = Math.min(Math.max((double) this.regionArrayZ, this.viewZ), (double) (this.regionArrayZ + this.regionArrayH) - this.viewH);
		
		// calculate view coords relative to regionArray
		this.viewRelX = (this.viewX - (double) this.regionArrayX) / (double) this.regionArrayW;
		this.viewRelZ = (this.viewZ - (double) this.regionArrayZ) / (double) this.regionArrayH;
		this.viewRelW = (this.viewW) / (double) this.regionArrayW;
		this.viewRelH = (this.viewH) / (double) this.regionArrayH;
		
		// update overlay size and position
		this.updateScreenResolution();
	}
	
	// fill the regionArray with the 4 regions surrounding the player
	private void updateRegionArray() {
		int rSize = MapWriter.REGION_SIZE;
		int rX = this.regionArrayX;
		int rZ = this.regionArrayZ;
		System.out.format("top left region is (%d, %d)\n", rX, rZ);
		this.regionArray[0] = this.mw.getOrCreateRegion(rX,         rZ);
		this.regionArray[1] = this.mw.getOrCreateRegion(rX + rSize, rZ);
		this.regionArray[2] = this.mw.getOrCreateRegion(rX,         rZ + rSize);
		this.regionArray[3] = this.mw.getOrCreateRegion(rX + rSize, rZ + rSize);
	}
	
	// calculates the texture coordinates to display the correct part of the region.
	// i = index of region in regionArray
	// iX = 0.0 for left regions, 0.5 for right regions
	// iZ = 0.0 for upper regions, 0.5 for lower regions
	public void drawRegionPart(double x, double y, double w, double h, int i, double iX, double iZ) {
		if ((w > 0.0) && (h > 0.0)) {
			double u1 = Math.max(0.0, (this.viewRelX - iX) * 2.0);
			double v1 = Math.max(0.0, (this.viewRelZ - iZ) * 2.0);
			double u2 = Math.min((this.viewRelX + this.viewRelW - iX) * 2.0, 1.0);
			double v2 = Math.min((this.viewRelZ + this.viewRelH - iZ) * 2.0, 1.0);
			
			MapWriterRender.drawTexturedRect(x, y, w, h, u1, v1, u2, v2, this.regionArray[i].getTexture());
		}
	}
	
	// draw the map overlay, player arrow, and markers
	public void draw() {
		if (this.overlayMode != 0) {
			double arrowX;
			double arrowZ;
			if (this.overlayMode != 3) {
				// for 'Rel' variables 0.0 is regionArrayX and 1.0 is regionArrayX + regionArrayW.
				// 0.5 is where region[0] and region[1] join.
				// '0.5 - viewRelX' is the width of the section of region[0] to display.
				// multiply by overlayW / viewRelW to get the width of the section to display in screen coordinates.
				// limit to overlayW (will be greater if the view is entirely within one region).
				// if it is limited w2 will be 0 meaning that region[1] will not be displayed.
				double scalingX = this.overlayW / this.viewRelW;
				double scalingZ = this.overlayH / this.viewRelH;
				double w1 = Math.min(Math.max(0.0, (0.5 - this.viewRelX) * scalingX), this.overlayW);
				double h1 = Math.min(Math.max(0.0, (0.5 - this.viewRelZ) * scalingZ), this.overlayH);
				double w2 = this.overlayW - w1;
				double h2 = this.overlayH - h1;
					
				this.drawRegionPart(this.overlayX,      this.overlayY,      w1, h1, 0, 0.0, 0.0);
				this.drawRegionPart(this.overlayX + w1, this.overlayY,      w2, h1, 1, 0.5, 0.0);
				this.drawRegionPart(this.overlayX,      this.overlayY + h1, w1, h2, 2, 0.0, 0.5);
				this.drawRegionPart(this.overlayX + w1, this.overlayY + h1, w2, h2, 3, 0.5, 0.5);
				
				// the position of the player within the region
				arrowX = this.overlayX + ((this.playerX - this.viewX) * this.overlayW / this.viewW);
				arrowZ = this.overlayY + ((this.playerZ - this.viewZ) * this.overlayH / this.viewH);
				
				// draw markers
				this.drawMarkers();
				
			} else {
				// underground view mode
				double withinBlockX = this.playerXUnscaled - Math.floor(this.playerXUnscaled);
				double withinBlockZ = this.playerZUnscaled - Math.floor(this.playerZUnscaled);
				double tu1 = withinBlockX / 16.0;
				double tv1 = withinBlockZ / 16.0;
				double tu2 = (withinBlockX + 15.0) / 16.0;
				double tv2 = (withinBlockZ + 15.0) / 16.0;
				MapWriterRender.drawTexturedRect(this.overlayX, this.overlayY, this.overlayW, this.overlayH, tu1, tv1, tu2, tv2, this.undergroundTexture);
				// only 15 blocks of the 16 block texture are shown, so rather than the player arrow being
				// drawn in the exact centre of the overlay area, it needs to be drawn 8/15 of the way in.
				arrowX = this.overlayX + this.overlayW * 8.0 / 15.0;
				arrowZ = this.overlayY + this.overlayH * 8.0 / 15.0;
			}
			GL11.glPushMatrix();
			//GL11.glDisable(GL11.GL_LIGHTING);
			// clamp player arrow to edge of overlay if outside
			arrowX = Math.max(this.overlayX, Math.min(arrowX, this.overlayX + this.overlayW));
			arrowZ = Math.max(this.overlayY, Math.min(arrowZ, this.overlayY + this.overlayH));
			
			// draw player arrow
			MapWriterRender.drawArrow(arrowX, arrowZ, this.playerHeading, 4.0D, 0xffff0000);
			//GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}
	
	private void drawMarkers() {
		if ((this.markerMode != 1) && (this.markerList != null)) {
			for (int i = 0; i + 2 < this.markerList.size(); i += 3) {
				int colourIndex = this.markerList.get(i + 2);
				if ((this.markerMode == 0) || (this.markerMode == colourIndex)) {
					int colour = this.markerColours[colourIndex];
					this.drawMarker(this.markerList.get(i), this.markerList.get(i + 1), colour);
				}
			}
		}
	}
	
	private void drawMarker(int dstX, int dstZ, int colour) {
		// the marker position relative to the top left corner of the view
		double xRel = (double) (dstX - this.viewX);
		double zRel = (double) (dstZ - this.viewZ);
		
		// where to draw the marker in screen coordinates
		double x = (xRel / this.viewW);
		double y = (zRel / this.viewH);
		
		// clamp marker to be between 0 and 1 (edges of view)
		x = Math.max(0.0D, Math.min(x, 1.0D));
		y = Math.max(0.0D, Math.min(y, 1.0D));
		
		// multiply by the overlay size and add the overlay position to
		// get the position within the overlay in screen coordinates
		x = this.overlayX + this.overlayW * x;
		y = this.overlayY + this.overlayH * y;
		
		// draw a coloured 2x2 rectangle centred on the calculated (x, y)
		MapWriterRender.drawRect(x - 1.0D, y - 1.0D, 2.0D, 2.0D, colour);
	}
}