import java.util.concurrent.Phaser;

public class PhaserExample implements Runnable {

	public static final int ITERATIONS = 3;

	public static void main(String[] args) {
		Phaser ph = new Phaser(1) {
			protected boolean onAdvance(int phase, int registeredParties) {
				return phase >= ITERATIONS || registeredParties == 0;
			}
		};
		int[] arr = new int[9];
		PhaserExample p1 = new PhaserExample(ph, "Porto", arr, 0, 3);
		PhaserExample p2 = new PhaserExample(ph, "Benfica", arr, 3, 6);
		PhaserExample p3 = new PhaserExample(ph, "Sporting", arr, 6, 9);

		while (!ph.isTerminated()) {
			ph.arriveAndAwaitAdvance();
		}
		/*ph.arriveAndAwaitAdvance();
		ph.arriveAndAwaitAdvance();
		ph.arriveAndAwaitAdvance();*/

		for(Integer i : arr) {
			System.out.print(i + " ");
		}

	}

	private String club;
	private Phaser ph;
	private int[] arr;
	private int start;
	private int end;

	public PhaserExample(Phaser ph, String club, int[] arr, int start, int end) {
		this.ph = ph;
		this.club = club;
		this.arr = arr;
		this.start = start;
		this.end = end;
		ph.register();
		new Thread(this).start();
	}

	@Override
	public void run() {

		for(int i = start; i < end; i++) {
			System.out.println(club + " " + ph.getPhase());
			ph.arriveAndAwaitAdvance();
			arr[i] = i;
		}
		ph.arriveAndDeregister();

	}

}
