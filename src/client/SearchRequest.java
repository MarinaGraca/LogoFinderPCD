package client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import worker.Task;

@SuppressWarnings("serial")
public class SearchRequest implements Serializable{
	
	private List<Task> tasks;
	
	public SearchRequest(){
		tasks = new LinkedList<Task>();
	}
	
	public void add(Task task){
		tasks.add(task);
	}
	
	public List<Task> getTasks(){
		return tasks;
	}

}
