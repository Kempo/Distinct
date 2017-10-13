package kempodev.distinct.utilities;

import java.awt.Color;

public class ColorUtil {
	private int hex;
	
	public int setHex(int h) {
		return hex = h;
	}
	public double getRed() {
		Color c = new Color(this.hex);
		double red = c.getRed();
		return red / 255.0D;
	}
	public double getBlue() {
		Color c = new Color(this.hex);
		double blue = c.getBlue();
		return blue / 255.0D;
	}
	public double getGreen() {
		Color c = new Color(this.hex);
		double green = c.getGreen();
		return green / 255.0D;
	}
}
