
public class MultiplyingMatrices {

	public static void main(String[] args) {
		int[][] mA = createMatrix(1000, 10000);//new int[3][2];
		int[][] mB = createMatrix(10000, 1000);//new int[2][4];

		/*mA[0][0] = 2;
		mA[1][0] = 0;
		mA[2][0] = -1;
		mA[0][1] = 3;
		mA[1][1] = 1;
		mA[2][1] = 4;

		mB[0][0] = 1;
		mB[1][0] = -2;
		mB[0][1] = 2;
		mB[1][1] = 0;
		mB[0][2] = 3;
		mB[1][2] = 4;
		mB[0][3] = 0;
		mB[1][3] = 3;*/

		long tSeq = System.nanoTime();
		int[][] mC = sequentialComputation(mA, mB);
		System.out.println("sequentialComputation : " + (System.nanoTime() - tSeq));
		System.out.println("----------------------");
		
		long tPar = System.nanoTime();
		int[][] mD = parallelComputation(mA, mB);
		System.out.println("parallelComputation :   " + (System.nanoTime() - tPar));
		System.out.println("----------------------");


		//printMatrix(mC);
		System.out.print(equalMatrices(mC, mD));

	}

	public static int[][] sequentialComputation(int[][] mA, int[][] mB){
		final int M = mA.length;
		final int N = mA[0].length;
		final int O = mB[0].length;
		int[][] mC = new int [M][O];

		for(int i = 0; i < M; i++) {
			for(int j = 0; j < O; j++) {
				int v = 0;
				for(int k = 0; k < N; k++) {
					v += mA[i][k] * mB[k][j];
				}
				mC[i][j] = v;
			}
		}		
		return mC;
	}

	public static int[][] parallelComputation(int[][] mA, int[][] mB){
		final int M = mA.length;
		final int N = mA[0].length;
		final int O = mB[0].length;
		int[][] mC = new int [M][O];

		
		int NTHREADS = 4;

		Thread[] threads = new Thread[NTHREADS];


		for (int tid=0; tid < NTHREADS; tid++ ) {
			final int tidInside = tid;
			Runnable r = () -> {

				/*try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/

				int startIndex = tidInside * M / NTHREADS;
				int endIndex = (tidInside + 1) * M / NTHREADS ;
				
				for(int i = startIndex; i < endIndex; i++) {
					for(int j = 0; j < O; j++) {
						int v = 0;
						for(int k = 0; k < N; k++) {
							v += mA[i][k] * mB[k][j];
						}
						mC[i][j] = v;
					}
				}
			};

			threads[tidInside] = new Thread(r);
			threads[tidInside].start();
		}
		
		for (Thread t :threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		return mC;
	}

	public static void printMatrix(int[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				System.out.println(matrix[i][j]);
			}
		}
	}
	
	private static int[][] createMatrix(int m, int n) {
		int[][] mat = new int[m][n];
		for (int i=0; i<m; i++) {
			for (int j=0; j<n; j++) {
				mat[i][j] = i + j;
			}
		}
		return mat;
	}
	
	public static boolean equalMatrices(int[][] a, int[][] b) {
		if(a.length != b.length) {
			return false;
		}
		for(int i = 0; i < a.length; i++) {
			if(a[i].length != b[i].length) {
				return false;
			}
			for(int j = 0; j < a[0].length; j++) {
				if(a[i][j] != b[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

}
