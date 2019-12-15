import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author ramkeerthyathinarayanan
 *
 */

public class ImageScaling {
	
	/*
	 * method borrowed from https://www.codejava.net/java-se/graphics/how-to-resize-images-in-java
	 */
	public static void resize(String inputImagePath, String outputImagePath, int scaledWidth, int scaledHeight) throws IOException {
       
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
        
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);

        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }
}