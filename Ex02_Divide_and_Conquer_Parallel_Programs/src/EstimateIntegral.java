import java.util.concurrent.RecursiveTask;

public class EstimateIntegral extends RecursiveTask<Double>{

	public static void main(String[] args) {
		EstimateIntegral integral = new EstimateIntegral(0,1);
		System.out.println("Integral 0 to 1 = " + integral.compute());
	}
	
	private double lowerBound;
	private double upperBound;
	public static final double RESOLUTION = 10e-7;
	
	

	public EstimateIntegral(double lowerBound, double upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	@Override
	protected Double compute() {
		double height = upperBound-lowerBound;
		if(height <= RESOLUTION) {
			double baseA = function(lowerBound);
			double baseB = function(lowerBound + height);
			return (baseA + baseB) * (height/2);
		}
		double middle = (lowerBound + upperBound) / 2; 
		  
		EstimateIntegral subtaskA = new EstimateIntegral(lowerBound, middle); 
		EstimateIntegral subtaskB = new EstimateIntegral(middle, upperBound); 

        subtaskA.fork(); 
        subtaskB.fork(); 

		return subtaskA.join() + subtaskB.join();
	}
	
	public static double function (double x) {
		return x * (x-1);
	}

}
