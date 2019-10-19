package knapsack;

public class Main {
	public static void main(String[] args) {
		KnapsackGA ga = new KnapsackGA();
		long start = System.nanoTime();
		ga.run();
		long end = System.nanoTime();
		System.out.println(end - start + " nano sec | " + (end - start) * 1E-9);
	}
}
