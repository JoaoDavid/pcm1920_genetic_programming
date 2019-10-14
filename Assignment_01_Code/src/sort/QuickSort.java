package sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

public class QuickSort<E extends Comparable<E>> extends RecursiveTask<List<E>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		
		Random gen = new Random();
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < 5000; i++) {
			list.add(gen.nextInt(5000)-5000);
		}
		/*list.add(2);
		list.add(3);
		list.add(1);
		list.add(12);
		list.add(5);
		list.add(0);
		list.add(-10);*/
		
		
		QuickSort<Integer> f = new QuickSort<Integer>(list);
		long tPar = System.nanoTime();
		System.out.println("Sorted Par. = " + f.compute());
		System.out.println("parallelComputation (sec): " + ((System.nanoTime() - tPar)));
		
		long tSeq = System.nanoTime();
		System.out.println("Sorted Seq. = " + quicksort(list));
		System.out.println("sequentialComputation (sec): " + ((System.nanoTime() - tSeq)));
		
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
		/*if (array.size() <= 1000) {
			return quicksort(array);
	    }*/
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
	
		QuickSort<E> qs1 = new QuickSort<E>(less);
		QuickSort<E> qs2 = new QuickSort<E>(higher);
		
		qs1.fork();
		qs2.fork();

		List<E> newList = new ArrayList<E>((List<E>) qs1.join());
		newList.add(pivot);
		newList.addAll((List<E>) qs2.join());
		return newList;
		
	}
	
	public static <E extends Comparable<E>> List<E> quicksort(List<E> list) {
		if (list.size() <= 1) {
			return list;
		}
		E pivot = list.remove(0);
		List<E> less = new ArrayList<E>();
		List<E> higher = new ArrayList<E>();
		for (E i : list) {
			if (i.compareTo(pivot) <= 0) {
				less.add(i);
			}else {
				higher.add(i);
			}
		}
	
		List<E> newList = new ArrayList<E>(quicksort(less));
		newList.add(pivot);
		newList.addAll(quicksort(higher));
		return newList;
	}
	
	
}
