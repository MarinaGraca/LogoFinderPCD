package worker;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

public class Worker extends Thread{
	
	private Socket socket;
	private String ip;
	private int porto;
	
	private String tipoProcura;

	private ObjectInputStream in;
	private ObjectOutputStream out;

	public Worker(String ip, int porto, String tipoProcura){
		this.ip = ip;
		this.porto = porto;
		this.tipoProcura = tipoProcura;
		
		System.out.println("Worker thread");
	}

	public void run(){
		try{
			connect();
			serve();
		} catch(IOException e){
			System.err.println("ERRO Worker connect");
		} finally{
			System.out.println("Worker stopping...");
			try{
				socket.close();
			} catch(IOException e){
				System.err.println("ERRO Worker socket close");
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

			out.writeObject("Worker");
			out.writeObject(tipoProcura);

		} catch(UnknownHostException e){
			System.err.println("ERRO Worker host");
		} catch(IOException e){
			System.err.println("ERRO Worker socket");
		}
	}

	private void serve(){
		while(!interrupted()){
			try{
				Task task = (Task) in.readObject();
				
				SearchResult searchResult = search(task);
				
				out.writeObject(searchResult);

			} catch(ClassNotFoundException e){
				System.err.println("ERRO Worker serve class not found");
			} catch(IOException e) {
				System.err.println("ERRO Worker serve out");
			}
		}
	}
	
	public SearchResult search(Task task) throws IOException{
		SearchResult searchResult = new SearchResult();

		System.out.println(task.getImgPath());

		BufferedImage logo = byteToBuff(task.getLogoBytes());

		int nRotacoes = 0; // Procura Simples
		
		if(tipoProcura.equals("Procura 90º")) 
			nRotacoes = 1;
		if(tipoProcura.equals("Procura 180º")) 
			nRotacoes = 2;

		for(int i = 0; i < nRotacoes; i++)
			logo = rotate90(logo);

		BufferedImage image = byteToBuff(task.getImgBytes());

		int largura = image.getWidth() - logo.getWidth();
		int altura = image.getHeight() - logo.getHeight();

		for(int xi = 0; xi <= largura; xi++){
			for(int yi = 0; yi <= altura; yi++){
				boolean pixeisIguais = true;
				for(int xl = 0; xl < logo.getWidth() && pixeisIguais; xl++)
					for(int yl = 0; yl < logo.getHeight() && pixeisIguais; yl++)
						if(logo.getRGB(xl, yl) != image.getRGB(xi + xl, yi + yl))
							pixeisIguais = false;
				
				if(pixeisIguais){
					searchResult.add(new Rectangle(xi, yi, logo.getWidth(), logo.getHeight()));
					System.out.println("(" + xi + ", " + yi + ")");
				}
			}
		}
		return searchResult;
	}

	public static BufferedImage rotate90(BufferedImage imgB){
		BufferedImage img = new BufferedImage(imgB.getHeight(), imgB.getWidth(), imgB.getType());  
		Graphics2D g = img.createGraphics();
		g.rotate(Math.toRadians(90), imgB.getWidth()/2, imgB.getHeight()/2);
		g.drawImage(imgB, null, 0, 0);
		g.dispose();
		return img;
	}

	private BufferedImage byteToBuff(byte[] bytes) throws IOException{
		InputStream stream = new ByteArrayInputStream(bytes);
		BufferedImage bi = ImageIO.read(stream);
		return bi;
	}

	public static void main(String[] args) throws IOException{
		new Worker(args[0], Integer.parseInt(args[1]), args[2]).start();
	}

}
