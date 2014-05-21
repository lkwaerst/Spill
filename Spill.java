import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.DataBufferInt;
import java.awt.*;
import java.awt.image.*;

public class Spill extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    
    public static int bredde = 300;
    public static int hoeyde = bredde / 16 * 9;
    public static int skala = 3;
    

    private Thread spillTraad;
    private JFrame vindu;
    private Keyboard key;
    private boolean kjoerer = false;
    private Skjerm skjerm;
    private int x, y;

    private BufferedImage image = new BufferedImage(bredde, hoeyde, BufferedImage.TYPE_INT_RGB); //her legges bildet
    private int[] pixler = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    public Spill() {
	Dimension str = new Dimension(bredde * skala, hoeyde * skala);
	setPreferredSize(str);
	skjerm = new Skjerm(bredde, hoeyde);
	vindu = new JFrame();
	key = new Keyboard();
	addKeyListener(key);
    }

    public void run() {
	//timer
	long sisteTid = System.nanoTime();
	long timer = System.currentTimeMillis(); //brukes til aa holde styr paa hvert sek
	final double ns = 1000000000 / 60.0; //etter saa mange ns skal neste frame vises
	double delta = 0;
	int frames = 0; //teller fps
	int updates = 0; //teller updates/s
	//spillloop
	while (kjoerer) {
	    long now = System.nanoTime();
	    delta += (now - sisteTid) / ns; //naar denne blir 1 skal spillet oppdateres
	    sisteTid = now;
	    while (delta >= 1) { //dette skal vel vaere en if test?
		update();  //begrenset for at alle datamaskiner skal kjøre like raskt
		updates++;
		delta--;
	    }
	    render();  //ikke begrenset
	    frames++;

	    if(System.currentTimeMillis() - timer > 1000) {
		timer += 1000; //naa har det gaatt ett sekund
		vindu.setTitle("Spill | " + updates + " ups, " + frames + " fps");
		updates = 0;
		frames = 0;
	    }
	}
	stop();
    }

    public void update() {
	key.update();
	if (key.down) y++;
	if (key.up) y--;
	if (key.left) x--;
	if (key.right) x++;
    }

    public void render() {
	//buffer strategy - regner noe uten faktisk aa bruke det med en gang, mellomlagring. Finner ut hvilken farge hver piksel skal ha og lagrer det.

	BufferStrategy bs = getBufferStrategy(); // i canvas
	if (bs == null) {
	    createBufferStrategy(3); //trippel buffering, fint med 3, 2 paa lager
	    return;
	}
	skjerm.clear(); //maa rendre paa nytt underlag
	skjerm.render(x, y);

	for (int i = 0; i < pixler.length; i++) {
	    pixler[i] = skjerm.pixler[i];
	}

	Graphics g = bs.getDrawGraphics(); //lenker dem sammen, grafisk context til buffer
	//her kommer grafikken
	g.setColor(Color.BLACK); //hvilken farge
	g.fillRect(0, 0, getWidth(), getHeight()); //hvor skal den
	g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	g.dispose(); //fjerner grafikken naar man er ferdig med den
	bs.show(); //viser neste
    }

    public synchronized void start() {
	kjoerer = true;
	spillTraad = new Thread(this, "Display");
	spillTraad.start();
    }

    public synchronized void stop() {
	kjoerer = false;
	try {
	    spillTraad.join();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	Spill spill = new Spill();
	spill.vindu.setResizable(false);
	spill.vindu.setTitle("Spill");
	spill.vindu.add(spill);
	spill.vindu.pack();   //fjerner tomrom
	spill.vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	spill.vindu.setLocationRelativeTo(null); //settes i midten
	spill.vindu.setVisible(true);

	spill.start();
    }
}