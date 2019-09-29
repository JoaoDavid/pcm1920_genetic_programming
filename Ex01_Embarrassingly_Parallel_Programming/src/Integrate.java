
public class Integrate {
	
	static final double errorTolerance = 10e-7;
	static double start = 0;
    static double end = 1;
    
	// the function to integrate
    static double computeFunction(double x)  {
        return x * (x-1);
    }
	public static void main(String[] args) throws Exception {

		double integrate = 0;
		
		for (double d=Integrate.start; d<=end; d += errorTolerance) {
			double h = errorTolerance;
			double b = computeFunction(d);
			double B = computeFunction(d + errorTolerance);
			integrate += (b + B) * h / 2;
		}
		
		System.out.println("Integrate: " + integrate);
		// Integrate: 2.500500000140081E7
		
		
	}
}
