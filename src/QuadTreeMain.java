import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class QuadTreeMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedImage img1 = null;
		BufferedImage img2 = null;
		QuadTreeNode<Colour> parentNode1 = new QuadTreeNode<Colour>();
		QuadTreeNode<Colour> parentNode2 = new QuadTreeNode<Colour>();
		
		try {
			
			img1 = ImageIO.read(new File("./src/door1024.png"));
			img2 = ImageIO.read(new File("./src/fruit.png"));
			
			int imageWidth1 = img1.getWidth();
			int imageHeight1 = img1.getHeight();
			
			int imageWidth2 = img2.getWidth();
			int imageHeight2 = img2.getHeight();
			
			if(imageWidth1 == imageHeight1 && imageWidth2 == imageHeight2) {
				if(!MathematicalOperations.checkLogIsInteger(imageWidth1, 2) && !MathematicalOperations.checkLogIsInteger(imageWidth2, 2)) {
					System.out.println("Scale the image and run the algorithm again");
					return;
				}
			}
			else {
				System.out.println("Scale the image and run the algorithm again");
				return;
			}
			
			QuadTree quadtree = new QuadTree();
			
			parentNode1 = quadtree.quadTree(img1, 
					new Coordinate(0, 0), 
					new Coordinate(img1.getWidth(), img1.getHeight()), 
					img1.getHeight(), 
					img1.getWidth());
			
			parentNode2 = quadtree.quadTree(img2, 
					new Coordinate(0, 0), 
					new Coordinate(img2.getWidth(), img2.getHeight()), 
					img1.getHeight(), 
					img1.getWidth());
			
			int baseNodeCount = QuadTree.baseNodeCount;
			int count = QuadTree.count;
			
			// System.out.println(parentNode1);
			
			int quadTreeHeight1 = MathematicalOperations.log(imageWidth1, 2) + 1;
			System.out.println("The height of the quadtree 1: " + quadTreeHeight1);
			
			int quadTreeHeight2 = MathematicalOperations.log(imageWidth2, 2) + 1;
			System.out.println("The height of the quadtree 2: " + quadTreeHeight2);
			
			BufferedImage outputImage = new BufferedImage(imageWidth1, imageHeight1, BufferedImage.TYPE_INT_RGB);
			
			for(int i = 0; i < imageWidth1; i++) {
				for(int j = 0; j < imageHeight1; j++) {
					outputImage.setRGB(i, j, parentNode1.colour.getPixelColour());
				}
			}
			
			File outputfile = new File("imageavg" + 1 + ".jpg");
			ImageIO.write(outputImage, "jpg", outputfile);
			
			BufferedImage outputImage2 = new BufferedImage(imageWidth1, imageHeight1, BufferedImage.TYPE_INT_RGB);
			
			for(int i = 0; i < imageWidth1; i++) {
				for(int j = 0; j < imageHeight1; j++) {
					outputImage2.setRGB(i, j, parentNode2.colour.getPixelColour());
				}
			}
			
			File outputfile2 = new File("imageavg" + 2 + ".jpg");
			ImageIO.write(outputImage2, "jpg", outputfile2);
			
			quadtree.display(parentNode1, imageWidth1, imageHeight1, "image1");
			quadtree.display(parentNode2, imageWidth1, imageHeight1, "image2");
			
			int level = 1;
			
			long startTime = System.nanoTime();
			if(quadtree.compareQuadTree(parentNode1, parentNode2, level)) {
				System.out.println("The quadtree are same at level " + level);
			}
			else {
				System.out.println("The quadtree are not same at level " + level);
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.println("Time taken for quadtree comparision: " + duration);
			
			startTime = System.nanoTime();
			if(CompareImages.compareImages(img1, img2, imageWidth1, imageHeight1)) {
				System.out.println("The images are same");
			}
			else {
				System.out.println("The images are not same");
			}
			endTime = System.nanoTime();
			duration = (endTime - startTime);
			System.out.println("Time taken for pixel level comparision: " + duration);
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
	
	public void display(QuadTreeNode quadtree, int width, int height, String filename) {
		if(quadtree.children.size() > 0) {
			QuadTreeNode[] children = new QuadTreeNode[4];
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
			
			File outputfile = new File(filename + ".png");
			try {
				ImageIO.write(outputImage, "png", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean compareQuadTree(QuadTreeNode quadtree1, QuadTreeNode quadtree2, int level) {
		if(quadtree1.children.size() > 0 && quadtree2.children.size() > 0) {
			QuadTreeNode[] children1 = new QuadTreeNode[4];
			QuadTreeNode[] children2 = new QuadTreeNode[4];
			
			Iterator iterator = quadtree1.children.iterator();
			int k = 0;
			
			while(iterator.hasNext()) {
				children1[k++] = (QuadTreeNode) iterator.next();
			}
			
			iterator = quadtree2.children.iterator();
			k = 0;
			
			while(iterator.hasNext()) {
				children2[k++] = (QuadTreeNode) iterator.next();
			}
			
			if(level > 0) {
				boolean child1 = compareQuadTree(children1[0], children2[0], level-1);
				boolean child2 = compareQuadTree(children1[1], children2[1], level-1);
				boolean child3 = compareQuadTree(children1[2], children2[2], level-1);
				boolean child4 = compareQuadTree(children1[3], children2[3], level-1);
				return (child1 && child2 && child3 && child4);
			}
			
			
			if(children1[0].getColour(children1[0].colour) != children2[0].getColour(children2[0].colour)) {
				return false;
			}
			
			if(children1[1].getColour(children1[1].colour) != children2[1].getColour(children2[1].colour)) {
				return false;
			}
			
			if(children1[2].getColour(children1[2].colour) != children2[2].getColour(children2[2].colour)) {
				return false;
			}
			
			if(children1[3].getColour(children1[3].colour) != children2[3].getColour(children2[3].colour)) {
				return false;
			}
			
		}
		else {
			return false;
		}
		return true;
	}
	
}

class CompareImages {
	public static boolean compareImages(BufferedImage img1, BufferedImage img2, int width, int height) {
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(img1.getRGB(i, j) != img2.getRGB(i, j)) {
					return false;
				}
			}
		}
		return true;
	}
}
