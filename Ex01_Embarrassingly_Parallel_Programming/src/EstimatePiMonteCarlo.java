import java.util.Random;

/*
 * 3. Throwing darts in MonteCarlo to find pi.
-------------------------------------------

Write a program that estimates the value of PI using a Monte Carlo simulation.

Consider a circle with radius 1 centered at the origin of a square defined by the opposing vertices (-1,-1) and (1,1).

By throwing randomly darts inside the square (following a uniform distribution), 
it is possible to obtain the ratio of darts that fell inside the circle and the total number of darts. From this ratio, you should derive pi.
*/
public class EstimatePiMonteCarlo {
	
	public static final int THROWS = 100000000;

	public static void main(String[] args) {
		System.out.println(sequentialComputation());

	}
	
	public static double sequentialComputation(){
		int hits = 0;
		Random r = new Random();
		for(int i= 0; i < THROWS; i++) {
			if(pointInside((r.nextDouble()*2) - 1, (r.nextDouble()*2) - 1)) {
				hits++;
			}
		}
		System.out.println("hits: " + hits);
		return ((double)hits/THROWS) * 4;
	}
	
	public static boolean pointInside(double x, double y) {
		return Math.pow(x,2) + Math.pow(y,2) <= 1;
	}

}
