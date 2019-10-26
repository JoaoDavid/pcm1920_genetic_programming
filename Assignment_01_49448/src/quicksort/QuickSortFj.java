package quicksort;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class QuickSortFj extends RecursiveAction{

	public static void main(String[] args) {
		Random gen = new Random();
		int[] arr = new int[50000000];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = gen.nextInt();
		}
		
		////////////
		
		ForkJoinPool p = new ForkJoinPool();
		
		
		QuickSortFj qs = new QuickSortFj(arr, 0, arr.length-1);

		long tPar = System.nanoTime();
		//qs.compute();
		p.execute(qs);
		System.out.println("parallelComputation: \n" + ((System.nanoTime() - tPar)));
		

		System.out.println("DONE");
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
			if (ForkJoinTask.getSurplusQueuedTaskCount() > 3) {
				QuickSortSeq.quickSort(arr, low, high);
			} else {
				int pi = QuickSortSeq.partition(arr, low, high);

				QuickSortFj qs1 = new QuickSortFj(arr, low, pi - 1);  // Before pi
				QuickSortFj qs2 = new QuickSortFj(arr, pi + 1, high); // After pi

				qs1.compute();
				qs2.compute();
			}
		}
	}

}
