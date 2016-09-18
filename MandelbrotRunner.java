/*
 * Main class for the Mandelbrot program
 */
public class MandelbrotRunner {
	/*
	 * Either makes a new border frame or directly makes and saves the Mandelbrot image
	 */
	public static void main(String[] args){
		boolean save = false;
	
		if (save){
			Mandelbrot mand = new Mandelbrot(6400,3600,10);
			System.out.println("calculating");
			long time = System.currentTimeMillis();
			mand.generate();
			System.out.println("done calculating in " + stringTime(System.currentTimeMillis()-time));
			System.out.println("saving");
			time = System.currentTimeMillis();
			mand.save("Backgound2.png");
			System.out.println("done saving in " + stringTime(System.currentTimeMillis()-time));
		}
		else 
			new BorderFrame((int)(600),(int)(480));
	}
	
	/*
	 * nicely formats milliseconds into seconds, minutes, and hours
	 */
	public static String stringTime(long millis){
		double seconds = millis/1000.0;
		int minutes=0;
		int hours=0;
		if (seconds>60){
			minutes = (int) seconds/60;
			seconds-=minutes*60;
		}
		if (minutes>60){
			hours = (int) minutes/60;
			minutes-=hours*60;
		}
		return (hours + ":" + minutes + ":" + seconds);
	}
}
