package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import client.Client;
import client.SearchRequest;
import client.SearchResponse;
import worker.SearchResult;
import worker.Task;

public class GUI{
	
	private JFrame frame;

	private DefaultListModel<String> angulos = new DefaultListModel<>(); // esquerda
	private DefaultListModel<SearchFile> files = new DefaultListModel<>(); // direita

	private JList<String> list1 = new JList<String>(angulos); // angulos
	private JList<SearchFile> list2 = new JList<SearchFile>(files); // imagens

	private JLabel label = new JLabel();
	private ImageIcon icon;
	private JScrollPane scroll;

	private JPanel painelTexto;

	private JFileChooser fc;
	private File onde; // pasta de imagens onde procurar
	private File logo; // imagem a procurar

	private JButton pasta;
	private JButton imagem;
	private JButton procura;

	private JTextField textCima; // onde
	private JTextField textBaixo; // logo

	private Listener listener = new Listener();

	private Client client;
	private String tipoProcura;

	public GUI(Client client){
		this.client = client;
		frame = new JFrame("Procura de sub-imagens");
		frame.setSize(1000, 700);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		buildGUI();
	}

	private void buildGUI(){
		icon = new ImageIcon();
		label.setIcon(icon);

		JScrollPane imgScroll = new JScrollPane(label);
		frame.add(imgScroll, BorderLayout.CENTER);

		// painel angulos esquerda
		angulos.addElement("Procura Simples");
		angulos.addElement("Procura 90º");
		angulos.addElement("Procura 180º");
		list1.addListSelectionListener(listener); // para escolher tipo de procura
		frame.add(list1, BorderLayout.WEST);

		// painel resultados direita
		list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list2.addListSelectionListener(listener);
		scroll = new JScrollPane(list2);
		scroll.setPreferredSize(new Dimension(100, 700));
		frame.add(scroll, BorderLayout.EAST);

		frame.add(painelSul(), BorderLayout.SOUTH);
	}

	private JPanel painelSul(){
		JPanel painel = new JPanel(new BorderLayout());

		JPanel painelSulNorte = new JPanel(new BorderLayout());
		painel.add(painelSulNorte);

		painelSulNorte.add(painelBotoes(), BorderLayout.EAST);

		painelTexto = new JPanel(new GridLayout(2, 1));
		painelSulNorte.add(painelTexto, BorderLayout.CENTER);

		textCima = new JTextField("");
		painelTexto.add(textCima);

		textBaixo = new JTextField("");
		painelTexto.add(textBaixo);

		JPanel painelSulSul = new JPanel(new GridLayout(1, 1));
		painel.add(painelSulSul, BorderLayout.SOUTH);

		procura = new JButton("Procura");
		painelSulSul.add(procura, BorderLayout.SOUTH);
		procura.addActionListener(listener); // para correr o programa

		return painel;
	}

	private JPanel painelBotoes(){
		JPanel painel = new JPanel(new GridLayout(2, 1));

		pasta = new JButton("Pasta");
		pasta.addActionListener(listener); // para escolher pasta de imagens onde procurar
		painel.add(pasta);

		imagem = new JButton("Imagem");
		imagem.addActionListener(listener); // para escolher imagem a procurar
		painel.add(imagem);

		return painel;
	}

	private class Listener implements ListSelectionListener, ActionListener{
		public void valueChanged(ListSelectionEvent e){			
			if(e.getSource() == list1){
				if(!e.getValueIsAdjusting()){
					tipoProcura = list1.getSelectedValue();
					System.out.println(tipoProcura);
				}
			}

			if(e.getSource() == list2){
				if(!e.getValueIsAdjusting()){
					SearchFile file = list2.getSelectedValue();
					System.out.println("Imagem: " + file.getName());
					label.setIcon(new ImageIcon(file.getBuff()));
				}
			}
		}

		public void actionPerformed(ActionEvent e){
			if(e.getSource() == pasta){
				fc = new JFileChooser("img");
				fc.setDialogTitle("Escolher pasta de imagens onde procurar");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int x = fc.showOpenDialog(pasta);
				if(x == JFileChooser.APPROVE_OPTION){
					onde = fc.getSelectedFile();
					textCima.setText(onde.getAbsolutePath());
				}
			} else if(e.getSource() == imagem){
				fc = new JFileChooser("img");
				fc.setDialogTitle("Escolher imagem a procurar");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setAcceptAllFileFilterUsed(false);
				fc.setFileFilter(new FileNameExtensionFilter("So extensoes .png", "png"));

				int x = fc.showOpenDialog(pasta);
				if(x == JFileChooser.APPROVE_OPTION){
					logo = fc.getSelectedFile();
					textBaixo.setText(logo.getAbsolutePath());
				}
			} else if(e.getSource() == procura){
				System.out.println("Nova procura...");
				procurar(list1.getSelectedValuesList(), textCima.getText(), textBaixo.getText());
			}
		}
	}

	private void procurar(List<String> list, String folderPath, String logoPath){
		files.clear();

		File logoFile = new File(logoPath);
		File folderFile = new File(folderPath);	
		File[] images = folderFile.listFiles();

		SearchRequest request = new SearchRequest();
		for(File imageFile : images){
			if(imageFile.isFile() && imageFile.getName().endsWith(".png")){
				try{
					byte[] imgBytes = buffToByte(imageFile);
					byte[] logoBytes = buffToByte(logoFile);

					for(String s : list){
						String tipo = s.toString();
						Task task = new Task(imageFile.getPath(), imgBytes, logoPath, logoBytes, tipo);
						request.add(task);
					}

				} catch(IOException e){
					System.err.println("ERRO GUI io file");
				}
			}
		}
		
		try{
			client.sendRequest(request);
		} catch(IOException e){
			System.err.println("ERRO GUI send request");
		}
	}

	public byte[] buffToByte(File file) throws IOException{
		BufferedImage bi = ImageIO.read(file);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", stream);
		stream.flush();
		byte[] bytes = stream.toByteArray();
		stream.close();
		return bytes;
	}

	public void drawRect(Rectangle rect, BufferedImage bi) throws IOException{
		int x = rect.x;
		int y = rect.y;
		int xx = rect.width;
		int yy = rect.height;
		Graphics2D g2d = bi.createGraphics();
		g2d.setColor(Color.RED);
		g2d.drawRect(x, y, xx, yy);
		g2d.dispose();
	}

	public void addResponse(SearchResponse response) throws IOException{
		files.clear();
		
		List<String> paths = response.getUniquePaths();
		
		for(String path : paths){
			BufferedImage bi = ImageIO.read(new File(path));
			
			List<SearchResult> results = response.getSearchResultsForUniquePath(path);
			
			for(SearchResult sr : results){
				List<Rectangle> rectangles = sr.getListaRectangulos();
				for(Rectangle rect : rectangles){
					drawRect(rect, bi);
				}
			}
			
			String name = new File(path).getName();
			SearchFile newFile = new SearchFile(name, bi);
			files.addElement(newFile);
		}
	}
	
	public void init(){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
