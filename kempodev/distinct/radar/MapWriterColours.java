package kempodev.distinct.radar;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

public class MapWriterColours {
	public HashMap<Integer,Integer> blockColors = new HashMap<Integer,Integer>();
	private int getIconMapColour(IIcon icon, Texture terrainTexture) {
		// flipped icons have the U and V coords reversed (minU > maxU, minV > maxV).
		// thanks go to taelnia for fixing this. 
		int iconX = (int) Math.round(((float) terrainTexture.w) * Math.min(icon.getMinU(), icon.getMaxU()));
		int iconY = (int) Math.round(((float) terrainTexture.h) * Math.min(icon.getMinV(), icon.getMaxV()));
		int iconWidth = (int) Math.round(((float) terrainTexture.w) * Math.abs(icon.getMaxU() - icon.getMinU()));
		int iconHeight = (int) Math.round(((float) terrainTexture.h) * Math.abs(icon.getMaxV() - icon.getMinV()));

		int[] pixels = new int[iconWidth * iconHeight];

		//MwUtil.log("(%d, %d) %dx%d", iconX, iconY, iconWidth, iconHeight);

		terrainTexture.getRGB(iconX, iconY, iconWidth, iconHeight, pixels, 0, iconWidth);

		// need to use custom averaging routine rather than scaling down to one pixel to
		// stop transparent pixel colours being included in the average.
		return getAverageColourOfArray(pixels);
	}
	public int getAverageColourOfArray(int[] pixels) {
		int count = 0;
		double totalA = 0.0;
		double totalR = 0.0;
		double totalG = 0.0;
		double totalB = 0.0;
		for (int pixel : pixels) {
			double a = (double) ((pixel >> 24) & 0xff);
			double r = (double) ((pixel >> 16) & 0xff);
			double g = (double) ((pixel >> 8)  & 0xff);
			double b = (double) ((pixel >> 0)  & 0xff);

			totalA += a;
			totalR += r * a / 255.0;
			totalG += g * a / 255.0;
			totalB += b * a / 255.0;

			count++;
		}

		totalR = totalR * 255.0 / totalA;
		totalG = totalG * 255.0 / totalA;
		totalB = totalB * 255.0 / totalA;
		totalA = totalA / ((double) count);

		return ((((int) (totalA)) & 0xff) << 24) |
				((((int) (totalR)) & 0xff) << 16) |
				((((int) (totalG)) & 0xff) << 8) |
				((((int) (totalB)) & 0xff));
	}
	public void genBlockColors() {
		double u1Last = 0;
		double u2Last = 0;
		double v1Last = 0;
		double v2Last = 0;
		int blockColourLast = 0;
		int e_count = 0;
		int b_count = 0;
		int s_count = 0;

		for (int blockID = 0; blockID < 4096; blockID++) { //TODO: replace hardcoded 4096 with actual registry size
			for (int dv = 0; dv < 16; dv++) {

				int blockAndMeta = ((blockID & 0xfff) << 4) | (dv & 0xf);
				Block block = (Block) Block.blockRegistry.getObjectForID(blockID);
				int blockColour = 0;

				if (block != null) {

					IIcon icon = null;
					try {
						icon = block.getIcon(1, dv);
					} catch (Exception e) {
						//MwUtil.log("genFromTextures: exception caught when requesting block texture for %03x:%x", blockID, dv);
						//e.printStackTrace();
						e_count++;
					}

					if (icon != null) {
						double u1 = icon.getMinU();
						double u2 = icon.getMaxU();
						double v1 = icon.getMinV();
						double v2 = icon.getMaxV();

						if ((u1 == u1Last) && (u2 == u2Last) && (v1 == v1Last) && (v2 == v2Last)) {
							blockColour = blockColourLast;
							s_count++;
						} else {
							blockColour = block.getMaterial().getMaterialMapColor().colorValue;
							u1Last = u1;
							u2Last = u2;
							v1Last = v1;
							v2Last = v2;
							blockColourLast = blockColour;
							b_count++;
						}
						//if (dv == 0)
						//	MwUtil.log("block %03x:%x colour = %08x", blockID, dv, blockColour);
					}

					// doesn't work as some leaves blocks aren't rendered using the biome
					// foliage colour
					//try {
					//	if (block.isLeaves(null, 0, 0, 0)) {
					//		bc.setBlockType(blockAndMeta, BlockType.LEAVES);
					//	}
					//} catch (NullPointerException e) {
					//}

					//blockColour = adjustBlockColourFromType(bc, blockAndMeta, blockColour);
				}
				//bc.setColour(blockAndMeta, blockColour);
				if(!blockColors.containsKey(blockID)) {
					blockColors.put(blockID, blockColour);
					System.out.println(blockID + " was put in with the color of " + blockColour);
				}
			}
		}
	}
}
