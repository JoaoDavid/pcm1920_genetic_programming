import java.util.concurrent.Phaser;

public class Heat implements Runnable {

	public static final int NX = 2048;
	public static final int NY = 2048;
	public static final int ITERATIONS = 50;
	
	public static double[][] oldm = new double[NX][NY];
	public static double[][] newm = new double[NX][NY];

	public static void main(String[] args) {
		//double[][] oldm = new double[NX][NY];
		//double[][] newm = new double[NX][NY];
		Phaser ph = new Phaser(1) {
			protected boolean onAdvance(int phase, int registeredParties) {
				return phase >= ITERATIONS || registeredParties == 1;
			}
		};
		// Everything else is 0
		oldm[NX/2][NY/2] = 100000;

		int NTHREADS = Runtime.getRuntime().availableProcessors();
		Heat[] arrH = new Heat[NTHREADS];
		for(int i = 0; i < arrH.length; i++) {
			if(i==0) {
				arrH[i] = new Heat(ph, (i * (NX-1) / NTHREADS)+1, ((i+1) * (NX-1) / NTHREADS));
			} else if(i==arrH.length-1) {
				arrH[i] = new Heat(ph,(i * (NX-1) / NTHREADS), ((i+1) * (NX-1) / NTHREADS));
			}else {
				arrH[i] = new Heat(ph,(i * (NX-1) / NTHREADS), ((i+1) * (NX-1) / NTHREADS));

			}
		}


		//for (int timestep = 0; timestep <= ITERATIONS; timestep++) {

			/*for (int i=1; i< NX-1; i++) {
				for (int j=1; j< NY-1; j++) {
					double current = newm[i][j];
					newm[i][j] = (current + oldm[i-1][j] + oldm[i+1][j] + oldm[i][j-1] + oldm[i][j+1]) / 5; 
				}
			}*/
			while (!ph.isTerminated()) {
				ph.arriveAndAwaitAdvance();
				ph.arriveAndAwaitAdvance();
				double[][] tmp = newm;
				newm = oldm;
				oldm = tmp;
			}
			



			// Swap matrices
			/*double[][] tmp = newm;
			newm = oldm;
			oldm = tmp;*/
		//}

		System.out.println(newm[NX/2 - 10][NY/2 - 10]); // ~3.77

	}

	private Phaser ph;
	private int start;
	private int end;



	private Heat(Phaser ph, int start, int end) {
		this.ph = ph;
		this.start = start;
		this.end = end;
		ph.register();
		new Thread(this).start();
	}



	@Override
	public void run() {
		int counter = 0;
		for (int i=start; i< end; i++) {
			for (int j=1; j< NY-1; j++) {
				counter++;
				ph.arriveAndAwaitAdvance();
				double current = newm[i][j];
				ph.arriveAndAwaitAdvance();
				newm[i][j] = (current + oldm[i-1][j] + oldm[i+1][j] + oldm[i][j-1] + oldm[i][j+1]) / 5;
				
			}
		}
		System.out.println(counter);
		ph.arriveAndDeregister();
	}
}
