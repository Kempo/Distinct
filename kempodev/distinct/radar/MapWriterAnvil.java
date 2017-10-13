package kempodev.distinct.radar;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class MapWriterAnvil
{
	private IChunkLoader chunkLoader = null;
	private HashSet<Chunk> chunkSet = null;
	
    public MapWriterAnvil() {
	}
	
	// writes level.dat to directory 'MapWriter.saveDir/worldName/'
	public void saveWorld(File worldDir, WorldInfo worldInfo, EntityClientPlayerMP player) {
		SaveHandler saveHandler = (SaveHandler) new AnvilSaveHandler(worldDir.getParentFile(), worldDir.getName(), false);
		NBTTagCompound nbtPlayer = new NBTTagCompound();
		player.writeToNBT(nbtPlayer);
		saveHandler.saveWorldInfoWithPlayer(worldInfo, nbtPlayer);
		// wait for IO operations to finish
		saveHandler.flush();
	}
	
	// region files written to 'MapWriter.worldDir/worldName/<.|DIM-1|DIM1>
	public void updateChunkLoader(File worldDir, int dimensionId) {
		this.chunkSet = new HashSet<Chunk>();
		File dimDir;
		if (dimensionId == -1) {
			dimDir = new File(worldDir, "DIM-1");
		}
		else if (dimensionId == 1) {
			dimDir = new File(worldDir, "DIM1");
		}
		else {
			dimDir = worldDir;
		}
		if(dimDir != null) {
		dimDir.mkdirs();
		this.chunkLoader = new AnvilChunkLoader(dimDir);
		}
	}
	
	// write all currently loaded chunks to anvil region file
	public void closeChunkLoader() {
		for (Chunk chunk : this.chunkSet) {
			this.saveChunk(chunk);
		}
		this.chunkLoader = null;
		this.chunkSet = null;
	}
	
	// write chunk to anvil region file
	private void saveChunk(Chunk chunk) {
		try {
			//System.out.format("MapWriter.Region.saveChunk: saving chunk (%d, %d) cl %b wc %b\n", chunk.xPosition, chunk.zPosition, (MapWriter.this.chunkLoader == null), (MapWriter.this.wc == null));
			if (this.chunkLoader != null) {
				this.chunkLoader.saveChunk(chunk.worldObj, chunk);
			}
		}
		catch(IOException e) {
			System.out.format("MapWriter.saveChunk: IO exception saving chunk (%d, %d)\n", chunk.xPosition, chunk.zPosition);
		}
		catch(MinecraftException e) {
			System.out.format("MapWriter.saveChunk: Minecraft exception saving chunk (%d, %d)\n", chunk.xPosition, chunk.zPosition);
		}
	}
	
	// add filled chunk to the set of loaded chunks.
	// called from hookFillChunk.
	public void addChunk(Chunk chunk) {
		this.chunkSet.add(chunk);
	}
	
	// save chunk if it is in the chunk set.
	// this prevents unfilled chunks from being written (only filled chunks are added to the chunk set).
	public void unloadChunk(Chunk chunk) {
		if (this.chunkSet.contains(chunk)) {
			this.saveChunk(chunk);
			this.chunkSet.remove(chunk);
		}
	}
}