package gui;

import java.awt.image.BufferedImage;

public class SearchFile{
	
	private String name;
	private BufferedImage buff;
	
	public SearchFile(String name, BufferedImage buff){
		this.name = name;
		this.buff = buff;
	}
	
	public String getName(){
		return name;
	}
	
	public BufferedImage getBuff(){
		return buff;
	}
	
	public String toString(){
		return name;
	}

}
