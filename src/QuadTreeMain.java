import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class QuadTreeMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedImage img = null;
		QuadTreeNode<Colour> parentNode = new QuadTreeNode<Colour>();
		try {
			int imageWidth = 0;
			int imageHeight = 0;
			
			img = ImageIO.read(new File("/Users/ramkeerthyathinarayanan/Documents/Ram/Eclipse/QuadTree/src/1024x1024-png-7.png"));
			imageWidth = img.getWidth();
			imageHeight = img.getHeight();
			
			while(imageHeight > 0 || imageWidth > 0) {
				System.out.println("Image: " + img + "\nImage weight: " + imageWidth + "\nImage height: " + imageHeight);
				
				imageHeight /= 2;
				imageWidth /= 2;
				
			}
			
			QuadTree quadtree = new QuadTree();
			parentNode = quadtree.quadTree(img, 
					new Coordinate(0, 0), 
					new Coordinate(img.getWidth(), img.getHeight()), 
					img.getHeight(), 
					img.getWidth());
			
			int baseNodeCount = QuadTree.baseNodeCount;
			int count = QuadTree.count;
			System.out.println(parentNode);
			
			BufferedImage outputImage = new BufferedImage(1920, 1200, BufferedImage.TYPE_INT_RGB);
			
			for(int i = 0; i < 1920; i++) {
				for(int j = 0; j < 1200; j++) {
					outputImage.setRGB(i, j, parentNode.colour.getPixelColour());
				}
			}
			
			File outputfile = new File("image.jpg");
			ImageIO.write(outputImage, "jpg", outputfile);
			
			imageWidth = img.getWidth();
			imageHeight = img.getHeight();
			
			quadtree.display(parentNode, imageWidth, imageHeight);
			
		}
		catch(IOException e) {
			System.out.println("Error in opening image: " + e);
		}
	}

}

class QuadTree {
	public static int baseNodeCount = 0;
	public static int count = 0;
	
	public QuadTreeNode<Colour> quadTree(BufferedImage img, Coordinate topLeft, Coordinate bottomRight, int height, int width) {
		
		if(height == 1 && width == 1) {
			baseNodeCount++;
			QuadTreeNode<Colour> node = new QuadTreeNode<Colour>();
			int colorInt = img.getRGB(topLeft.x, topLeft.y);
			Colour pixelColour = new Colour(colorInt);
			node.colour = pixelColour;
			return node;
		}
		else {
			
			count++;
			
			/* if(width == 1) {
				width *= 2;
			}
			
			if(height == 1) {
				height *= 2;
			} */
			
			System.out.println(count);
			
			QuadTreeNode<Colour> northWestNode = this.quadTree(img, 
					new Coordinate(topLeft.x, topLeft.y), 
					new Coordinate(width / 2, height / 2), 
					height / 2, 
					width / 2);
			
			QuadTreeNode<Colour> southWestNode = this.quadTree(img, 
					new Coordinate(topLeft.x + (width / 2), topLeft.y), 
					new Coordinate(width / 2, bottomRight.y),
					height / 2, 
					width - (width / 2));
			
			QuadTreeNode<Colour> northEastNode = this.quadTree(img, 
					new Coordinate(topLeft.x, topLeft.y + (height / 2)), 
					new Coordinate(bottomRight.x / 2, height / 2), 
					height - (height / 2), 
					width / 2);
			
			
			QuadTreeNode<Colour> sountEastNode = this.quadTree(img, 
					new Coordinate(topLeft.x + (width / 2), topLeft.y + (height / 2)), 
					new Coordinate(bottomRight.x, bottomRight.y), 
					height - (height / 2), 
					width - (width / 2));
			
			QuadTreeNode<Colour> parent = new QuadTreeNode<Colour>();
			parent.children.add(northWestNode);
			parent.children.add(northEastNode);
			parent.children.add(southWestNode);
			parent.children.add(sountEastNode);
			parent.setAverageColour();
			
			if(parent.colourCompare()) {
				parent.children.clear();
			}
			
			if(parent.children.size() > 4) {
				System.out.println("too many children");
			}
			return parent;
		}
	}
	
	public void display(QuadTreeNode quadtree, int width, int height) {
		if(quadtree.children.size() > 0) {
			QuadTreeNode[] children = new QuadTreeNode[4]; // = (QuadTreeNode[]) quadtree.children.toArray();
			Iterator iterator = quadtree.children.iterator();
			int k = 0;
			
			while(iterator.hasNext()) {
				children[k++] = (QuadTreeNode) iterator.next();
			}
				
 			BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for(int i = 0; i < width; i++) {
				for(int j = 0; j < height; j++) {
					if(i < width / 2) {
						if(j < height / 2) {
							outputImage.setRGB(i, j, children[0].getColour(children[0].colour));
						}
						else {
							outputImage.setRGB(i, j, children[0].getColour(children[1].colour));
						}
					}
					else {
						if(j < height / 2) {
							outputImage.setRGB(i, j, children[0].getColour(children[2].colour));
						}
						else {
							outputImage.setRGB(i, j, children[0].getColour(children[3].colour));
						}
					}
				}
			}
			
			File outputfile = new File("image1.png");
			try {
				ImageIO.write(outputImage, "png", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
