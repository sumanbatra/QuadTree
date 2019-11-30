import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuadTreeNode<T> {
	Colour colour;
	QuadTreeNode<T> parent = null;
	List<QuadTreeNode<T>> children;
	
	QuadTreeNode() {
		this.children = new ArrayList<QuadTreeNode<T>>();
		this.colour = new Colour();
	}
	
	public void setAverageColour() {
		Iterator iterator = this.children.iterator();
		int alpha = 0, red = 0, green = 0, blue = 0;
		
		while(iterator.hasNext()) {
			QuadTreeNode<T> node = (QuadTreeNode<T>) iterator.next();
			alpha += node.colour.alpha;
			red += node.colour.red;
			green += node.colour.green;
			blue += node.colour.blue;
		}
		
		this.colour.alpha = alpha / 4;
		this.colour.red = red / 4;
		this.colour.green = green / 4;
		this.colour.blue = blue / 4;
	}
	
	public int getColour(Colour colour) {
		return colour.getPixelColour();
	}
	
	public boolean colourCompare() {
		for(int i = 0; i < this.children.size() - 1; i++) {
			for(int j = i+1; j < this.children.size(); j++) {
				if(this.children.get(i).colour.getPixelColour() != this.children.get(j).colour.getPixelColour()) {
					return false;
				}
			}
		}
		return true;
	}
	
}
