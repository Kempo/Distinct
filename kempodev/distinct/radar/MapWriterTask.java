package kempodev.distinct.radar;

import net.minecraft.client.Minecraft;
import java.util.*;
import java.util.concurrent.*;

public abstract class MapWriterTask implements Runnable {
	
	// the task stores its own future
	private Future<?> future;

	// called by processTaskQueue after the thread completes
	public abstract void onComplete();
	
	// the method that runs in a separate thread
	// must not access future in run()
	public abstract void run();
	
	// methods to access the tasks Future variable
	public final Future<?> getFuture() {
		return this.future;
	}
	
	public final void setFuture(Future<?> future) {
		this.future = future;
	}
}
