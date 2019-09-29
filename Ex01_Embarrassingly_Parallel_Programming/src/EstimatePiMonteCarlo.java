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
	
	public static final int THROWS = 2000900000;

	public static void main(String[] args) {		
		long tSeq = System.nanoTime();
		System.out.println(sequentialComputation());
		System.out.println("sequentialComputation : " + (System.nanoTime() - tSeq));
		System.out.println("----------------------");
		
		long tPar = System.nanoTime();
		System.out.println(parallelComputation());
		System.out.println("parallelComputation :   " + (System.nanoTime() - tPar));
		System.out.println("----------------------");
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
	
	public static double parallelComputation(){
		int NTHREADS = 8;

		Thread[] threads = new Thread[NTHREADS];
		double[] sum = new double[NTHREADS];
		int res = 0;


		for (int tid=0; tid < NTHREADS; tid++ ) {
			final int tidInside = tid;
			Runnable r = () -> {
				int startIndex = tidInside * THROWS / NTHREADS;
				int endIndex = (tidInside + 1) * THROWS / NTHREADS ;				
				Random random = new Random();
				for(int i= startIndex; i < endIndex; i++) {
					if(pointInside((random.nextDouble()*2) - 1, (random.nextDouble()*2) - 1)) {
						sum[tidInside]++;
					}
				}				
			};
			threads[tidInside] = new Thread(r);
			threads[tidInside].start();
		}
		
		for (int tid=0; tid < NTHREADS; tid++ ) {
			try {
				threads[tid].join();
				res += sum[tid];
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("hits: " + res);
		return ((double)res/THROWS) * 4;
	}
	
	
	public static boolean pointInside(double x, double y) {
		return Math.pow(x,2) + Math.pow(y,2) <= 1;
	}

}
