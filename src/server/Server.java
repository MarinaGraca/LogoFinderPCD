package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import client.ClientConnect;
import client.SearchRequest;
import worker.Task;
import worker.WorkerConnect;

public class Server{
	
	private int porto;
	
	private ServerSocket ss;
	private Socket socket;

	private BlockingQueue queue0;
	private BlockingQueue queue90;
	private BlockingQueue queue180;
	
	private List<ClientConnect> clients;
	private List<WorkerConnect> workers;

	public Server(int porto){
		this.porto = porto;
		System.out.println("Server running");

		queue0 = new BlockingQueue();
		queue90 = new BlockingQueue();
		queue180 = new BlockingQueue();
		
		clients = new LinkedList<>();
		workers = new LinkedList<>();
	}

	private void serve() throws IOException, ClassNotFoundException{
		try{
			ss = new ServerSocket(porto);
			System.out.println("Server Socket: " + ss);

			while(true){
				socket = ss.accept();
				System.out.println("Conexao aceite na socket: " + socket);

				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

				String tipo = (String) in.readObject();

				if(tipo.equals("Client")){
					ClientConnect cc = new ClientConnect(this, socket, in, out);
					cc.start();
					clients.add(cc);
				}

				if(tipo.equals("Worker")){					
					String tipoProcura = (String) in.readObject();
					WorkerConnect wc = new WorkerConnect(this, socket, in, out, tipoProcura);
					wc.start();
					workers.add(wc);
				}
			}
		} finally{
			ss.close();
		}
	}

	public void addTasks(SearchRequest request, int idClient){
		for(Task task : request.getTasks()){

			task.setIDClient(idClient);

			if(task.getTipoProcura().equals("Procura Simples"))
				queue0.offer(task);

			else if(task.getTipoProcura().equals("Procura 90º"))
				queue90.offer(task);

			else if(task.getTipoProcura().equals("Procura 180º"))
				queue180.offer(task);
		}
	}

	public Task removeTask(String tipoProcura) throws InterruptedException{
		if(tipoProcura.equals("Procura Simples"))
			return queue0.poll();

		if(tipoProcura.equals("Procura 90º"))
			return queue90.poll();

		if(tipoProcura.equals("Procura 180º"))
			return queue180.poll();
			
		return null;
	}

	public ClientConnect getClientConnect(int idClient){
		for(int i = 0; i < clients.size(); i++){
			ClientConnect cc = clients.get(i);
			
			if(idClient == cc.getIDClient())
				return cc;
		}
		return null;
	}

	public BlockingQueue getQueue0(){
		return queue0;
	}

	public BlockingQueue getQueue90(){
		return queue90;
	}

	public BlockingQueue getQueue180(){
		return queue180;
	}
	
	public static void main(String[] args) throws IOException, NumberFormatException, ClassNotFoundException{
		new Server(Integer.parseInt(args[0])).serve();
	}

}
