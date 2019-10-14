package sort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class QuickSort<E extends Comparable<E>> extends RecursiveTask<List<E>> {
	
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
		

		QuickSort<Integer> f = new QuickSort(list);
		System.out.println("Sorted = " + f.compute());
	}

	
	private List<E> array;
	
	public QuickSort(List<E> array) {
		this.array = array;
	}


	@Override
	protected List<E> compute() {
		if (array.size() <= 1) {
			return array;
		}
		E pivot = array.remove(0);
		List<E> less = new ArrayList<E>();
		List<E> higher = new ArrayList<E>();
		for (E i : array) {
			if (i.compareTo(pivot) <= 0) {
				less.add(i);
			}else {
				higher.add(i);
			}
		}
	
		QuickSort<E> qs1 = new QuickSort(less);
		QuickSort<E> qs2 = new QuickSort(higher);
		
		qs1.fork();
		qs2.fork();

		List<E> newList = new ArrayList<E>((List<E>) qs1.join());
		newList.add(pivot);
		newList.addAll((List<E>) qs2.join());
		return newList;
		
	}
	
	
}
