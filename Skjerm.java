import java.util.Random;

class Skjerm {

    private int bredde, hoeyde;
    public int[] pixler;
    public final int MAP_SIZE = 64; 
    public final int MAP_SIZE_MASK = MAP_SIZE - 1;

    public int[] tiles = new int[MAP_SIZE * MAP_SIZE];
    int teller;
    int teller2;

    private Random random = new Random();

    public Skjerm(int b, int h) {
	bredde = b;
	hoeyde = h;
	pixler = new int[bredde * hoeyde];

	//random farger til alle tiles
	for (int i = 0; i < tiles.length; i++) {
	    tiles[i] = random.nextInt(0xffffff);
	}

	
    }

    public void clear() {
	for (int i = 0; i < pixler.length; i++) {
	    pixler[i] = 0;
	}
    }

    public void render(int xOffset, int yOffset) {
	// if (teller == hoeyde) {
	//     teller = 0;
	// }
	for (int i = 0; i < hoeyde; i++) {
	    int ii = i + yOffset;
	    // if (ii < 0 || ii >= hoeyde) {
	    //     ii -= hoeyde;
	    // }
	    for (int j = 0; j < bredde; j++) {
		int jj = j + xOffset;
		// if (jj < 0 || jj >= bredde) {
		//     break;
		// }
		//j >> 4 samme som j / 16 (noe med bits), & 63 faar den til aa bli 0 om stoerre enn 63
		int tileIndex = ((jj >> 4) & MAP_SIZE_MASK) + ((ii >> 4) & MAP_SIZE_MASK) * MAP_SIZE ; //skal finne hvilken tile denne pixelen tilhoerer
		pixler[j + i * bredde] = tiles[tileIndex];;
	    }
	}
	// teller++;
    }
}