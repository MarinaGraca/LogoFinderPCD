package worker;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SearchResult implements Serializable{
	
	private List<Rectangle> rectangulos = new ArrayList<>();

	public SearchResult(){
		
	}
	
	public void add(Rectangle rectangulo){
		rectangulos.add(rectangulo);
	}

	public List<Rectangle> getListaRectangulos(){
		return rectangulos;
	}

}
