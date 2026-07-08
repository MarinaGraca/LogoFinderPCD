package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Server;

public class ClientConnect extends Thread{
	
	private static int clientCounter = 0;

	private int idClient;

	private Server server;
	private Socket socket;

	private ObjectInputStream in;
	private ObjectOutputStream out;

	private SearchResponse searchResponse;

	public ClientConnect(Server server, Socket socket, ObjectInputStream in, ObjectOutputStream out){
		this.server = server;
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.idClient = clientCounter++;
	}

	public void run(){
		try{
			serve();
		} catch(IOException e){
			System.err.println("ERRO ClientConnect serve");
		} finally{
			try{
				socket.close();
			} catch(IOException e){
				System.err.println("ERRO ClientConnect socket close");
			}
		}	
	}

	private void serve() throws IOException{
		try{
			while(!interrupted()){
				SearchRequest request = (SearchRequest) in.readObject();

				searchResponse = new SearchResponse(request.getTasks().size());
				server.addTasks(request, idClient);
				searchResponse.awaitResults();

				out.writeObject(searchResponse);
			}
		} catch(ClassNotFoundException e){
			System.err.println("ERRO ClientConnect serve class not found");
		} catch(InterruptedException e){
			System.err.println("ERRO ClientConnect serve interrupt");
		}
	}

	public int getIDClient(){
		return idClient;
	}

	public SearchResponse getResponse(){
		return searchResponse;
	}

}
