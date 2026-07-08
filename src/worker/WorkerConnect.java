package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.ClientConnect;
import server.Server;

public class WorkerConnect extends Thread{
	
	private Server server;
	private Socket socket;

	private ObjectInputStream in;
	private ObjectOutputStream out;

	private String tipoProcura;

	public WorkerConnect(Server server, Socket socket, ObjectInputStream in, ObjectOutputStream out, String tipoProcura){
		this.server = server;
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.tipoProcura = tipoProcura;
	}

	public void run(){
		try{
			serve();
		} catch(IOException e){
			System.err.println("ERRO WorkerConnect serve");
		} finally{
			try{
				socket.close();
			} catch(IOException e){
				System.err.println("ERRO WorkerConnect socket close");
			}
		}
	}

	private void serve() throws IOException{
		try{
			while(!interrupted()){

				Task task = server.removeTask(tipoProcura);
				out.writeObject(task);

				SearchResult searchResult = (SearchResult) in.readObject();
				ClientConnect cc = server.getClientConnect(task.getIDClient());
				cc.getResponse().add(task, searchResult);
			}
		} catch(InterruptedException e){
			System.err.println("ERRO WorkerConnect serve interrupted");
		} catch(ClassNotFoundException e){
			System.err.println("ERRO WorkerConnect class not found");
		}
	}

}
