package nbody;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class NBodyAdvanceFj extends RecursiveAction{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private static final int SURPLUS_THRESHOLD = 3;

	private NBody[] bodies;
	private int startIndex;
	private int endIndex;
	private double dt;
	private double[][] iBodyAarr;

	public NBodyAdvanceFj(NBody[] bodies, int startIndex, int endIndex, double dt, double[][] iBodyAarr) {
		this.bodies = bodies;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.dt = dt;
		this.iBodyAarr = iBodyAarr;
	}


	@Override
	protected void compute() {


		if (ForkJoinTask.getSurplusQueuedTaskCount() > SURPLUS_THRESHOLD || endIndex - startIndex <= 8) {
			//double[][] otherBodyAarr = new double[bodies.length][3];
			for (int j = startIndex; j < endIndex; ++j) {
				NBody iBody = bodies[j];				
				for (int k = j + 1; k < bodies.length; ++k) {
					final NBody otherBody = bodies[k];
					double dx = iBody.x - otherBody.x;
					double dy = iBody.y - otherBody.y;
					double dz = iBody.z - otherBody.z;

					double dSquared = dx * dx + dy * dy + dz * dz;
					double distance = Math.sqrt(dSquared);
					double mag = dt / (dSquared * distance);


					/*synchronized (iBody){						
						iBody.vx -= dx * otherBody.mass * mag;
						iBody.vy -= dy * otherBody.mass * mag;
						iBody.vz -= dz * otherBody.mass * mag;
					}*/

					iBodyAarr[j][0] += dx * otherBody.mass * mag;
					iBodyAarr[j][1] += dy * otherBody.mass * mag;
					iBodyAarr[j][2] += dz * otherBody.mass * mag;

					synchronized (otherBody){
						otherBody.vx += dx * iBody.mass * mag;
						otherBody.vy += dy * iBody.mass * mag;
						otherBody.vz += dz * iBody.mass * mag;
					}
					/*otherBodyAarr[k][0] += dx * iBody.mass * mag;
					otherBodyAarr[k][1] += dy * iBody.mass * mag;
					otherBodyAarr[k][2] += dz * iBody.mass * mag;*/

				}


			}
			/*for (int i = 0; i < bodies.length; i++) {
				NBody iBody = bodies[i];
				synchronized (iBody){	
					iBody.vx += otherBodyAarr[i][0];
					iBody.vy += otherBodyAarr[i][1];
					iBody.vz += otherBodyAarr[i][2];
				}
			}*/


		} else {
			int iteration = endIndex - startIndex;
			int middle = iteration / 2;
			NBodyAdvanceFj bodyAdvance1 = new NBodyAdvanceFj(bodies, startIndex, startIndex + middle, dt, iBodyAarr);
			NBodyAdvanceFj bodyAdvance2 = new NBodyAdvanceFj(bodies, startIndex+middle, endIndex, dt, iBodyAarr);

			bodyAdvance1.fork();
			bodyAdvance2.fork();

			bodyAdvance2.join();
			bodyAdvance1.join();
		}
	}
}
