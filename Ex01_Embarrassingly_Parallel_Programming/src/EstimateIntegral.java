/*
 * 2. Gathering trapezoids together
--------------------------------
Write a program that estimates the integral of a given function f, using the trapezoid rule.
2. The lower bound of the integral (0.0)
3. The upper bound of the integral (1.0)
4. The resolution  (10^-7)
*/
public class EstimateIntegral {
	
	static final double LOWER_BOUND = 0.0;
	static final double UPPER_BOUND = 100000.0;
	static final double RESOLUTION = 10e-7;

	public static void main(String[] args) {
		long tSeq = System.nanoTime();
		//System.out.println(sequentialComputation());
		System.out.println("sequentialComputation : " + (System.nanoTime() - tSeq));
		System.out.println("----------------------");
		
		long tPar = System.nanoTime();
		System.out.println(parallelComputation());
		System.out.println("parallelComputation : " + (System.nanoTime() - tPar));
		System.out.println("----------------------");
		

	}
	
	public static double function (double x) {
		return x * (x-1);
	}
	
	public static double sequentialComputation(){
		double res = 0;
		double i = LOWER_BOUND;
		while(i < UPPER_BOUND) {
			double height = RESOLUTION;
			if(i + RESOLUTION > UPPER_BOUND) {
				height = UPPER_BOUND - i;
			}
			double baseA = Math.abs(function(i));
			double baseB = Math.abs(function(i + height));
			res += (baseA + baseB) * (height/2);
			i += height;
		}
		return res;
	}
	
	public static double parallelComputation(){
		int NTHREADS = 16;

		Thread[] threads = new Thread[NTHREADS];
		double[] sum = new double[NTHREADS];
		double res = 0;


		for (int tid=0; tid < NTHREADS; tid++ ) {
			final int tidInside = tid;
			Runnable r = () -> {
				double startIndex = tidInside * UPPER_BOUND / NTHREADS;
				double endIndex = (tidInside + 1) * UPPER_BOUND / NTHREADS ;

				double i = startIndex;
				while(i < endIndex) {
					double height = RESOLUTION;
					if(i + RESOLUTION > endIndex) {
						height = endIndex - i;
					}
					double baseA = Math.abs(function(i));
					double baseB = Math.abs(function(i + height));
					sum[tidInside] += (baseA + baseB) * (height/2);
					i += height;
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
		return res;
	}

}
