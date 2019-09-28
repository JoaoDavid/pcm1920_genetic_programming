
public class MultiplyingMatrices {

	public static void main(String[] args) {
		int[][] mA = new int[3][2];
		int[][] mB = new int[2][4];
		
		mA[0][0] = 2;
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
		mB[1][3] = 3;
		
		int[][] mC = sequentialComputation(mA, mB);
		printMatrix(mC);

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
	
	public static void printMatrix(int[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				System.out.println(matrix[i][j]);
			}
		}
	}

}
