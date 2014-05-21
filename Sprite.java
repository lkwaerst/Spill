public class Sprite {

    private final int SIZE;
    private int x, y; //pixelkoordinatene til spriten i spritesheeten
    public int[] pixler;
    private SpriteSheet sheet;

    Sprite(int str, int x, int y, SpriteSheet s) {
	SIZE = str;
	pixler = new int[str * str];
	this.x = x * str; //x og y er hvilken sprite i spritesheet
	this.y = y * str;
	sheet = s;
    }

    //asossierer pixlene i pixeler[] til de rette pixlene i sheet.pixler[]
    private void load() {
	for (int i = 0; i < SIZE; i++) {
	    for (int j = 0; j < SIZE; j++) {
		pixler[x + y * SIZE] = sheet.pixler[(j + x) + (i + y) * sheet.SIZE];
	    }
	}
    }
}