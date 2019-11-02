package quicksort;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class QuickSortFj extends RecursiveAction{

	private static final int SURPLUS_THRESHOLD = 3;

	public static void main(String[] args) {
		//Filling array with random numbers
		Random gen = new Random();
		int[] arr = new int[QuickSortSeq.ARR_SIZE];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = gen.nextInt();
		}
		
		int[] arr2 = arr.clone();
		int[] arr3 = arr.clone();
		long tPar2 = System.nanoTime();
		arr2 = Arrays.stream(arr2).parallel().sorted().toArray();
		System.out.println("parallelComputation stream: \n" + ((System.nanoTime() - tPar2)));

		//***************************************//
		
		ForkJoinPool pool = new ForkJoinPool();
		System.out.println(pool.toString());
		QuickSortFj qs = new QuickSortFj(arr, 0, arr.length-1);

		long tPar = System.nanoTime();
		//qs.compute();
		pool.execute(qs);
		System.out.println("is terminated " + pool.isTerminated());
		
		//pool.invoke(qs);
		
		System.out.println(pool.toString());
		System.out.println("parallelComputation: \n" + ((System.nanoTime() - tPar)));
		System.out.println("DONE");
		
		qs.join();
		pool.shutdown();
		System.out.println("is terminated 2 " + pool.isTerminated());
		System.out.println(pool.toString());
		System.out.println(Arrays.equals(arr, arr2));
		Arrays.parallelSort(arr3);
		System.out.println(Arrays.equals(arr, arr3));
	}

	private int[] arr;
	private int low;
	private int high;

	public QuickSortFj(int[] arr, int low, int high) {
		this.arr = arr;
		this.low = low;
		this.high = high;
	}

	@Override
	protected void compute() {
		if (low < high) {
			if (ForkJoinTask.getSurplusQueuedTaskCount() > SURPLUS_THRESHOLD || high - low < 1000) {
				//System.out.println("entrou");
				QuickSortSeq.quickSort(arr, low, high);
			} else {
				//fp represents the index of the pivot chosen in partition
				//that is in the correct final position
				int fp = QuickSortSeq.partition(arr, low, high);

				QuickSortFj qs1 = new QuickSortFj(arr, low, fp - 1);  // Before fp
				QuickSortFj qs2 = new QuickSortFj(arr, fp + 1, high); // After fp
				

				/*qs1.invoke();				
				qs2.invoke();*/

				qs1.fork();
				qs2.fork();
				
				qs2.join();
				qs1.join();
			}
		}
	}

}
