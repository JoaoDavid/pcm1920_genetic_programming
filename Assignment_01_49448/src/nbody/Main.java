package nbody;


public class Main {
	public static void main(String[] args) {
		NBodySystem bodies = new NBodySystem(NBodySystem.DEFAULT_SIZE, 1L);
		System.out.printf("%.9f\n", bodies.energy());
		long start = System.nanoTime();		
		for (int i = 0; i < NBodySystem.DEFAULT_ITERATIONS; ++i) {
			bodies.advance(0.01);
			System.out.printf("%.9f\n", bodies.energy());
		}
		long end = System.nanoTime();
		System.out.println(end - start + " nano sec | " + (end - start) * 1E-9);
	}
}
