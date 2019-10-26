package quicksort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class QuickSort<E extends Comparable<E>> extends RecursiveTask<List<E>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		
		Random gen = new Random();
		List<Integer> listPar = new ArrayList<Integer>();
		List<Integer> listSeq = new ArrayList<Integer>();
		for(int i = 0; i < 50000000; i++) {
			Integer integer = gen.nextInt(50000)-50000;
			listPar.add(integer);
			listSeq.add(integer);
		}
		/*listA.add(2);
		listA.add(3);
		listA.add(1);
		listA.add(12);
		listA.add(5);
		listA.add(0);
		listA.add(-10);*/
		System.out.println(listPar.size());
		
		QuickSort<Integer> f = new QuickSort<Integer>(listPar);
		long tPar = System.nanoTime();
		List<Integer> sortedPar = f.compute();
		//System.out.println("Sorted Par. = " + f.compute());
		System.out.println("parallelComputation (sec): \n" + ((System.nanoTime() - tPar)));
		System.out.println();

		long tSeq = System.nanoTime();
		List<Integer> sortedSeq = quicksort(listSeq);
		//System.out.println("Sorted Seq. = " + quicksort(listB));
		System.out.println("sequentialComputation (sec): \n" + ((System.nanoTime() - tSeq)));

		System.out.println(sortedPar.equals(sortedSeq));
		
	}

	
	private List<E> list;
	
	public QuickSort(List<E> array) {
		this.list = array;
	}


	@Override
	protected List<E> compute() {
		if (ForkJoinTask.getSurplusQueuedTaskCount() > 3) {
			return quicksort(list);
		}
		/*if (list.size() <= 1) {
			return list;
		}*/
		if (list.size() <= 50000) {
			return quicksort(list);
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
