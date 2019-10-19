package knapsack;

public class Main {
	public static void main(String[] args) {
		KnapsackGA ga = new KnapsackGA();
		long time = System.nanoTime();
		ga.run();
		System.out.println(System.nanoTime() - time + "nano sec");
	}
}
