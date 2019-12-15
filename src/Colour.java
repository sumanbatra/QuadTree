/**
 * @author ramkeerthyathinarayanan
 *
 */

public class Colour {
	int alpha;
	int red;
	int green;
	int blue;
	
	Colour() {
		this.alpha = 0;
		this.red = 0;
		this.green = 0;
		this.blue = 0;
	}
	
	Colour(int rgb) {
		this.alpha = (rgb & 0xff000000) >> 24;
		this.red = (rgb & 0x00ff0000) >> 16;
		this.green = (rgb & 0x0000ff00) >> 8;
		this.blue  =  rgb & 0x000000ff;
	}
	
	int getPixelColour() {
		int p = (this.alpha<<24) | (this.red<<16) | (this.green<<8) | this.blue;
		return p;
	}
}
