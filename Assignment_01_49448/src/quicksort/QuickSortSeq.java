package quicksort;

import java.util.Random;

public class QuickSortSeq {
	
	public static void main(String[] args) {
		Random gen = new Random();
		int[] arr = new int[50000000];
		for(int i = 0; i < arr.length; i++) {
			arr[i] = gen.nextInt();
		}
		
		long tPar = System.nanoTime();
		quickSort(arr, 0, arr.length-1);
		System.out.println("seqComputation: \n" + ((System.nanoTime() - tPar)));
		
	}
	
	/**
	 * @param arr
	 * @param low
	 * @param high
	 * @returns arr sorted
	 */
	public static void quickSort(int[] arr, int low, int high)	{
	    if (low < high) {
	        int pi = partition(arr, low, high);

	        quickSort(arr, low, pi - 1);  // Before pi
	        quickSort(arr, pi + 1, high); // After pi
	    }
	}

	public static int partition(int[] arr, int low, int high) {
		// pivot (Element to be placed at right position)
	    int pivot = arr[high];  
	 
	    int i = (low - 1);  // Index of smaller element

	    for (int j = low; j <= high- 1; j++) {
	        // If current element is smaller than the pivot
	        if (arr[j] < pivot){
	            i++;    // increment index of smaller element
	            int temp = arr[i];
	            arr[i] = arr[j];
	            arr[j] = temp;
	        }
	    }
	    int temp2 = arr[i + 1];
	    arr[i + 1] = arr[high];
	    arr[high] = temp2;
	    return (i + 1);
	}
	

	    


}