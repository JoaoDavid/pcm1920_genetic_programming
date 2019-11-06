package nbody;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class NBodyAdvanceFj extends RecursiveAction{

	private static final int SURPLUS_THRESHOLD = 3;

	private NBody[] bodies;
	private int startIndex;
	private int endIndex;
	private double dt;
	private double[][] iBodyAarr;
	private double[][] otherBodyAarr;

	public NBodyAdvanceFj(NBody[] bodies, int startIndex, int endIndex, double dt, double[][] iBodyAarr, double[][] otherBodyAarr) {
		this.bodies = bodies;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.dt = dt;
		this.iBodyAarr = iBodyAarr;
		this.otherBodyAarr = otherBodyAarr;
	}


	@Override
	protected void compute() {
		if (ForkJoinTask.getSurplusQueuedTaskCount() > SURPLUS_THRESHOLD || endIndex - startIndex <= 8) {
			for (int j = startIndex; j < endIndex; ++j) {
				NBody iBody = bodies[j];
				int iNewIndex = 0;
				for (int k = j + 1; k < bodies.length; ++k) {
					final NBody otherBody = bodies[k];
					double dx = iBody.x - otherBody.x;
					double dy = iBody.y - otherBody.y;
					double dz = iBody.z - otherBody.z;

					double dSquared = dx * dx + dy * dy + dz * dz;
					double distance = Math.sqrt(dSquared);
					double mag = dt / (dSquared * distance);

					iBodyAarr[j][0] += dx * otherBody.mass * mag;
					iBodyAarr[j][1] += dy * otherBody.mass * mag;
					iBodyAarr[j][2] += dz * otherBody.mass * mag;
					
					//----------------------

					int otherIndex = bodies.length-1-j;
					double dx2 = bodies[iNewIndex].x - bodies[otherIndex].x;
					double dy2 = bodies[iNewIndex].y - bodies[otherIndex].y;
					double dz2 = bodies[iNewIndex].z - bodies[otherIndex].z;

					double dSquared2 = dx2 * dx2 + dy2 * dy2 + dz2 * dz2;
					double distance2 = Math.sqrt(dSquared2);
					double mag2 = dt / (dSquared2 * distance2);

					otherBodyAarr[otherIndex][0] += dx2 * bodies[iNewIndex].mass * mag2;
					otherBodyAarr[otherIndex][1] += dy2 * bodies[iNewIndex].mass * mag2;
					otherBodyAarr[otherIndex][2] += dz2 * bodies[iNewIndex].mass * mag2;

					iNewIndex++;
				}
			}
		} else {
			int iteration = endIndex - startIndex;
			int positions = (iteration * bodies.length) - ((iteration*iteration)-1);
			int division = iteration / 3;
			NBodyAdvanceFj bodyAdvance1 = new NBodyAdvanceFj(bodies, startIndex, startIndex + division, dt, iBodyAarr, otherBodyAarr);
			NBodyAdvanceFj bodyAdvance2 = new NBodyAdvanceFj(bodies, startIndex+division, endIndex, dt, iBodyAarr, otherBodyAarr);

			bodyAdvance1.fork();
			bodyAdvance2.fork();

			bodyAdvance2.join();
			bodyAdvance1.join();
		}
	}
}
