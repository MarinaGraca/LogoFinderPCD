package client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import worker.SearchResult;
import worker.Task;

@SuppressWarnings("serial")
public class SearchResponse implements Serializable{
	
private int createdTasks;
	
	private List<Task> tasks;
	private List<SearchResult> results;
	
	public SearchResponse(int createdTasks){
		this.createdTasks = createdTasks;
		tasks = new LinkedList<Task>();
		results = new LinkedList<SearchResult>();
	}
	
	public List<String> getUniquePaths(){
		List<String> uniquePath = new ArrayList<String>();
		
		for(Task task : tasks){
			String path = task.getImgPath();
			if(!uniquePath.contains(path))
				uniquePath.add(path);
		}
		return uniquePath;
	}
	
	public List<SearchResult> getSearchResultsForUniquePath(String path){
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		
		for(int i = 0; i < tasks.size(); i++){
			Task task = tasks.get(i);
			
			if(path.equals(task.getImgPath())){
				SearchResult sr = results.get(i);
				searchResults.add(sr);
			}
		}
		return searchResults;
	}
	
	public synchronized void add(Task task, SearchResult result){
		tasks.add(task);
		results.add(result);
		
		notifyAll();
	}
	
	public synchronized void awaitResults() throws InterruptedException{
		while(createdTasks != results.size())
			wait();
	}
	
	public List<Task> getTasks(){
		return tasks;
	}
	
	public List<SearchResult> getResults(){
		return results;
	}

}
