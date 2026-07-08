package client;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import gui.GUI;
import worker.SearchResult;
import worker.Task;

public class Client extends Thread{
	
	private Socket socket;
	private String ip;
	private int porto;
	
	private GUI gui;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private List<SearchResult> results = new LinkedList<SearchResult>();
	private List<Rectangle> rectangulos = new LinkedList<Rectangle>();
	
	public Client(String ip, int porto){
		this.ip = ip;
		this.porto = porto;
		
		gui = new GUI(this);
		gui.init();
		
		System.out.println("Client thread");
	}
	
	public void run(){
		try{
			connect();
			serve();
		} catch(IOException e){
			System.err.println("ERRO Client connect");
		} finally{
			System.out.println("Client stopping...");
			try{
				socket.close();
			} catch(IOException e){
				System.err.println("ERRO Client socket close");
			}
		}
	}
	
	private void connect() throws IOException{
		try{
			InetAddress a = InetAddress.getByName(ip);
			socket = new Socket(a, porto);
			System.out.println("Connected to server socket: " + socket);
			System.out.println("Address: " + a);
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			out.writeObject("Client");
			
		} catch(UnknownHostException e){
			System.err.println("ERRO Client host");
		} catch(IOException e){
			System.err.println("ERRO Client socket");
		}
	}
	
	private void serve(){
		while(!interrupted()){
			try{
				SearchResponse response = (SearchResponse) in.readObject();
				gui.addResponse(response);
				
			} catch(ClassNotFoundException e){
				System.err.println("ERRO Client serve class not found");
			} catch(IOException e){
				System.err.println("ERRO Client serve in");
			}
		}
	}
	
	public void sendRequest(SearchRequest request) throws IOException{
		out.writeObject(request);
	}
	
	public List<Rectangle> getRect(Task task){
		for(SearchResult sr : results)
			rectangulos = sr.getListaRectangulos();
		return rectangulos;
	}
	
	public static void main(String[] args) throws IOException{
		new Client(args[0], Integer.parseInt(args[1])).start();
	}

}
