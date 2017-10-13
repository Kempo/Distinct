package kempodev.distinct.radar;

import net.minecraft.client.Minecraft;
import java.util.*;
import java.util.concurrent.*;

/*
This class handles executing and managing 'tasks'.
A single background thread runs tasks in the sequence they are added (via addTask()).
A linked list FIFO queue of every unfinished task is processed every time the processTaskQueue method is called.
processTaskQueue checks the task at the front of the queue to see if the background thread has processed it.
If it the task is complete processTaskQueue calls the onComplete() method of the task and removes it from the queue.
If it is not complete the task is added to the front of the queue again.
In this way the tasks are always processed sequentially, in the order they were added to the queue.

Tasks are extensions of the base Task class.
There are two abstract methods which must be overwritten by the extending class.
  void run()
    Is executed in the background thread when the executor reaches this task.
  void onComplete()
    Is called by processTaskQueue() when the task is done (after the run method is complete).
	This method runs in the main thread so is a good place to copy the results of the run() method.

The run() method of a task added to the queue is guaranteed to be run before the run() method of the next task
added. Likewise the onComplete() method of the first task is guaranteed to be run before the onComplete() of the second
task. However the run() method of any class added after a Task may be executed before the onComplete() method of
the earlier Task is called.

e.g. addTask(Task1)
     addTask(Task2)
	 addTask(Task3)
	 
may run in the order:
    Task1.run()
	Task2.run()
	  Task1.onComplete()
	Task3.run()
	  Task2.onComplete()
	  Task3.onComplete()
*/

public class MapWriterExecutor {
	
	private ExecutorService executor;
	private LinkedList<MapWriterTask> taskQueue;
	
	public MapWriterExecutor() {
		this.executor = Executors.newSingleThreadExecutor();
		this.taskQueue = new LinkedList<MapWriterTask>();
	}
	
	// add a task to the queue
	public void addTask(MapWriterTask task) {
		Future<?> future = this.executor.submit(task);
		task.setFuture(future);
		this.taskQueue.add(task);
	}
	
	// Pop a Task entry from the task queue and check if the task's thread has finished.
	// If it has completed then call onComplete for the task.
	// If it has not completed then push the task back on the queue.
	public void processTaskQueue() {
		MapWriterTask task = this.taskQueue.poll();
		if (task != null) {
			Future<?> future = task.getFuture();
			if (future.isDone()) {
				task.onComplete();
			} else {
				// put entry back on queue
				this.taskQueue.push(task);
			}
		}
	}
	
	public boolean close() {
		boolean error = true;
		try {
			// stop accepting new tasks
			this.executor.shutdown();
			// process current tasks
			while (this.taskQueue.size() > 0) {
				this.processTaskQueue();
			}
			// should already be terminated, but just in case...
			error = !this.executor.awaitTermination(10L, TimeUnit.SECONDS);
			error = false;
		} catch (InterruptedException e) {
			System.out.println("MapWriter: error: IO task was interrupted during shutdown");
			e.printStackTrace();
		}
		return error;
	}
}
