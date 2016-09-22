import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

/*
 * Class that actually calculates the Mandelbrot
 */
public class Mandelbrot {
	BufferedImage image;
	final int WIDTH,HEIGHT;
	final int MAX_ITER = 10000, ESCAPE_RADIUS = 1<<4;
	final double X_MAX,X_MIN,Y_MAX,Y_MIN;
	final int RAND;
	private final boolean TIMING = true, AXES = false;
	Color[] mapping = {new Color(66, 30, 15),new Color(25, 7, 26),new Color(9, 1, 47),
			new Color(4, 4, 73),new Color(0, 7, 100),new Color(12, 44, 138),
			new Color(24, 82, 177),new Color(57, 125, 209),new Color(134, 181, 229),
			new Color(211, 236, 248),new Color(241, 233, 191),new Color(248, 201, 95),
			new Color(255, 170, 0),new Color(204, 128, 0),new Color(153, 87, 0),
			new Color(106, 52, 3),new Color(66, 30, 15)};
	private boolean running = true;
    
	/*
	 * Use this constructor for to make the whole Mandelbrot
	 * Calculates bounds based on the ratio of width to height so the whole thing is shown
	 */
	public Mandelbrot(int w, int h, int rand){
		WIDTH = w;
		HEIGHT = h;
		RAND = rand;
		image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
		if (HEIGHT/2.4>WIDTH/3){
			X_MIN = -2.4;
			X_MAX = 0.6;
			Y_MAX = (double)HEIGHT/2*3/WIDTH;
			Y_MIN = -(double)HEIGHT/2*3/WIDTH;
		}
		else{
			Y_MAX = 1.2;
			Y_MIN = -1.2;
			X_MIN = -((double)WIDTH-HEIGHT/4)/HEIGHT*12/5;
			X_MAX = 0.6;
		}
	}
	
	/*
	 * Use this constructor if you want to specify the bounds manually
	 */
	public Mandelbrot(int w, int h, double X_MAX, double X_MIN, double Y_MAX, double Y_MIN, int rand){
		WIDTH = w;
		HEIGHT = h;
		this.X_MAX=X_MAX;
		this.X_MIN=X_MIN;
		this.Y_MAX=Y_MAX;
		this.Y_MIN=Y_MIN;
		this.RAND=rand;
		image = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
	}
	
	/*
	 * Creates four threads, each calculates a fourth of the image
	 * If TIMING it also prints out how long it took to calculate
	 */
	public void generate(){
		long start;
		if (TIMING){
			System.out.println("Started caclulation");
			 start = System.currentTimeMillis();
		}
		
		MandelThread a = new MandelThread(0,WIDTH/2,0,HEIGHT/2);
        MandelThread b = new MandelThread(0,WIDTH/2,HEIGHT/2,HEIGHT);
        MandelThread c = new MandelThread(WIDTH/2,WIDTH,0,HEIGHT/2);
        MandelThread d = new MandelThread(WIDTH/2,WIDTH,HEIGHT/2,HEIGHT);
        
        a.start();
        b.start();
        c.start();
        d.start();
        
        while(a.isAlive() || b.isAlive() || c.isAlive() || d.isAlive()){}
        if (TIMING)
        	System.out.println("Finished caclulation in " + MandelbrotRunner.stringTime(System.currentTimeMillis()-start));
	}
	
	/*
	 * Linear interpolation for smoothing out the colors
	 */
	public double lerp(double c1, double c2, double t){
		return (1-t)*c1 + t*c2;
	}
	
	/*
	 * saves the image to file as a .png
	 */
	public void save(String file){
		try{
			FileOutputStream f = new FileOutputStream(file);
			ImageIO.write(image, "png", f);
		}catch (Exception e){System.out.println(e.getMessage());}
	}
	
	/*
	 * stops the generation of the Mandelbrot
	 */
	public void stop(){
		running  = false;
	}
	
	/*
	 * the calculation thread class
	 */
	class MandelThread extends Thread{
		private final int X_START,X_END,Y_START,Y_END, NUM_POINTS;
		private int count = 0;
		public MandelThread(int xs,int xe,int ys,int ye){
			X_START = xs;
			X_END = xe;
			Y_START = ys;
			Y_END = ye;
			NUM_POINTS=(int) RAND;
		}
		
		/*
		 * Calculates the color for each point
		 * epsilon is the radius of each pixel so any point within epsilon of the center of the pixel is in the pixel
		 * if the center is in the set then it doesn't calculate an other points in the pixel
		 */
		public void run(){
			Complex c;
			double epsilon = (X_MAX-X_MIN)/WIDTH;
			for (int x = X_START;x<X_END;x++){
				for (int y = Y_START; y<Y_END;y++){
					if (!running) return;
					c = new Complex((((double)x/WIDTH*(X_MAX-X_MIN))+X_MIN),(((double)y/HEIGHT*(Y_MAX-Y_MIN))+Y_MIN));
					
					int col = doPoint(c);
					if (col==0)
						image.setRGB(x, y, col);
					else{
						int[] cols = new int[1+NUM_POINTS];
						cols[0]=col;
						for (int i = 1; i<=NUM_POINTS;i++){
							double xShift = Math.random()*2*epsilon-epsilon; 
							double yShift = Math.random()*2*epsilon-epsilon;
							cols[i]=doPoint(new Complex(c.getR()+xShift,c.getI()+yShift));
						}
						
						image.setRGB(x, y, averageCols(cols));
						
					}
					if (AXES)
						if (c.getI()<0.01 && c.getI()>-0.01 || c.getR()<0.01 && c.getR()>-0.01)
							image.setRGB(x, y, 0xffffff);
				}
			}
		}
		
		/*
		 * separates, averages, and recombines the rbg values of the color
		 */
		private int averageCols(int[]cols){
			int red=0;
			int green=0;
			int blue=0;
			for (int i = 0; i < cols.length; i++){
				red+=(cols[i]>>16)&0xff;
				green+=(cols[i]>>8)&0xff;
				blue+=cols[i]&0xff;
			}
			red=red/cols.length;
			green=green/cols.length;
			blue=blue/cols.length;
			return (red<<16)+(green<<8)+blue;
		}
		
		/*
		 * calculates whether a point is in the set and if not calculates the color of that point
		 */
		private int doPoint(Complex c){
			Complex z = new Complex(0,0);
			count = 0;
			double p = Math.sqrt((c.getR() - 0.25)*(c.getR() - 0.25) + c.getI()*c.getI());
		    if (c.getR()<p-2*p*p+0.25 || (c.getR()+1)*(c.getR()+1) + c.getI()*c.getI() < 1.0/16){
		        return 0;
		    }
		    else{
		    	while (z.magnitudeSquared()<ESCAPE_RADIUS && count<MAX_ITER ){
		    		count++;
		    		z=z.times(z).plus(c);
		    	}				
		    	if (count==MAX_ITER){
		    		return 0;
		    	}
		    	else{
		    		double nu = Math.log(Math.log(z.magnitudeSquared())/2/Math.log(2))/Math.log(2);
		    		double i = count+1-nu;
		    		i = Math.pow(i,.65);
		    		double hue = i%(mapping.length-1);
		    		Color c1 = mapping[(int)hue];
		    		Color c2 = mapping[(int)hue+1];
		    		Color col = new Color((int)lerp(c1.getRed(),c2.getRed(),i%1), (int)lerp(c1.getGreen(),c2.getGreen(),i%1), (int)lerp(c1.getBlue(),c2.getBlue(),i%1));
		    		return col.getRGB();
		    	}
		    }
		}
	}
}
