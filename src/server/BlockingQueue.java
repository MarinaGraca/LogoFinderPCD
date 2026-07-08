package server;

import java.util.LinkedList;
import java.util.Queue;

import worker.Task;

public class BlockingQueue{
	
	private Queue<Task> queue;
	
	public BlockingQueue(){
		queue = new LinkedList<>();
	}
	
	public synchronized void offer(Task task){
		queue.offer(task);	
		notifyAll();
	}
	
	public synchronized Task poll() throws InterruptedException{
		while(queue.size() == 0)
			wait();
		return queue.poll();
	}

}
