package sort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class QuickSort extends RecursiveTask<List<Integer>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		

		List<Integer> list = new ArrayList<Integer>();
		list.add(2);
		list.add(3);
		list.add(1);
		list.add(12);
		list.add(5);
		list.add(0);
		

		QuickSort f = new QuickSort(list);
		System.out.println("Sorted = " + f.compute());
	}

	
	private List<Integer> array;
	
	public QuickSort(List<Integer> array) {
		this.array = array;
	}


	@Override
	protected List<Integer> compute() {
		if (array.size() <= 1) {
			return array;
		}
		int pivot = array.remove(0);
		List<Integer> less = new ArrayList<Integer>();
		List<Integer> higher = new ArrayList<Integer>();
		for (Integer i : array) {
			if (i <= pivot) {
				less.add(i);
			}else {
				higher.add(i);
			}
		}
	
		QuickSort qs1 = new QuickSort(less);
		QuickSort qs2 = new QuickSort(higher);
		
		qs1.fork();
		qs2.fork();

		List<Integer> newList = new ArrayList<Integer>((List<Integer>) qs1.join());
		newList.add(pivot);
		newList.addAll((List<Integer>) qs2.join());
		return newList;
		
	}
	
	
}
