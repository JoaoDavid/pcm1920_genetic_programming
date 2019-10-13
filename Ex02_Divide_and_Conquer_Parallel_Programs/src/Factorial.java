import java.util.concurrent.RecursiveTask;

public class Factorial extends RecursiveTask<Integer> {

	

	public static void main(String[] args) {

		Factorial f = new Factorial(5);
		System.out.println("Factorial 5 = " + f.compute());
	}
	
	private int n;
	
	public Factorial(int n) {
		this.n = n;
	}

	@Override
	protected Integer compute() {
		if (n <= 1) return 1;
		
		Factorial fac = new Factorial(n-1);
		
		fac.fork();
		
		return n * fac.join();
	}

}
