package sort;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class QuickSort extends RecursiveTask {

	
	private int[] array;
	private int pivot;
	private boolean hasPivot;
	
	public QuickSort(int[] array) {
		this.array = array;
		hasPivot = false;
	}
	
	private QuickSort(int[] array, int pivot) {
		this.array = array;
		this.pivot = pivot;
		hasPivot = true;
	}

	@Override
	protected int[] compute() {
		if (array.length == 1) {
			return array;
		}
		int middle = array.length / 2;
		Stream.concat(Arrays.stream(array), Arrays.stream(array))
        .toArray(int[]::new);
		QuickSort qs1 = new QuickSort(Arrays.copyOfRange(array, 0, middle), array[0]);
		QuickSort qs2 = new QuickSort(Arrays.copyOfRange(array, middle + 1, array.length - 1), array[0]);
	}
	
	
}
