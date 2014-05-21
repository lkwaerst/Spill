import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

public class SpriteSheet {

    private String path;
    public final int SIZE;
    public int[] pixler;

    public SpriteSheet(String path, int str) {
	this.path = path;
	SIZE = str;
	pixler = new int[str * str];
    }

    private void load() {
	//laster bildet inn i programmet
	try {
	    BufferedImage img = ImageIO.read(SpriteSheet.class.getResource(path));
	    //legger individuelle pixelverdier in i pixler[]
	    int w = img.getWidth();
	    int h = img.getHeight();
	    img.getRGB(0, 0, w, h, pixler, 0, w);
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }
}