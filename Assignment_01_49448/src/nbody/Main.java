package nbody;


public class Main {
	public static void main(String[] args) {
		NBodySystem bodies = new NBodySystem(NBodySystem.DEFAULT_SIZE, 1L);
		System.out.printf("%.9f\n", bodies.energy());
		long total = 0;
		double[] time = new double[NBodySystem.DEFAULT_ITERATIONS];
		for (int i = 0; i < NBodySystem.DEFAULT_ITERATIONS; ++i) {
			long start = System.nanoTime();
			bodies.advance(0.01);
			long end = System.nanoTime();
			//long duration = end - start;
			//System.out.println(duration + " nano sec | " + duration * 1E-9 + " sec");
			//total += duration;
			System.out.printf("%.9f\n", bodies.energy());
			time[i] = end - start;
		}
		for (int i = 0; i < NBodySystem.DEFAULT_ITERATIONS; ++i) {
			System.out.println("i=" + i + ":" + String.format("%.0f", time[i]));
			total += time[i];
		}
		System.out.println("average time (sec) " + (total/NBodySystem.DEFAULT_ITERATIONS) * 1E-9);
	}
}
