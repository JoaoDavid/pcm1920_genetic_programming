package nbody;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NBodySystem {

	public static final int DEFAULT_ITERATIONS = 5;
	public static final int DEFAULT_SIZE = 50000;

	public static final int ADVANCE_THRESHOLD = 1000;
	public static final int APPLY_THRESHOLD = 100;

	static final double PI = 3.141592653589793;
	static final double SOLAR_MASS = 4 * PI * PI;

	protected NBody[] bodies;
	private final int NTHREADS = Runtime.getRuntime().availableProcessors();
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final CyclicBarrier barrier = new CyclicBarrier(NTHREADS);
	private volatile int i;

	public NBodySystem(int n, long seed) {
		Random random = new Random(seed);
		bodies = new NBody[n];

		double px = 0.0;
		double py = 0.0;
		double pz = 0.0;
		for (int i = 0; i < n; i++) {
			bodies[i] = new NBody(random);
			px += bodies[i].vx * bodies[i].mass;
			py += bodies[i].vy * bodies[i].mass;
			pz += bodies[i].vz * bodies[i].mass;
		}
		bodies[0].offsetMomentum(px, py, pz);
	}

	public void advance(double dt) {

		/*for (int i = 0; i < bodies.length; ++i) {
			NBody iBody = bodies[i];*/


		Thread[] ts = new Thread[NTHREADS];
		for (int j=0; j<NTHREADS; j++) {
			final int tid = j;
			final int iVar = i + 1;
			ts[j] = new Thread( () -> {
				while(i < bodies.length) {
					/*try {
						barrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	*/					
					int tidLocal = tid;
					int iterations = bodies.length - (i + 1);
					int startIndex = (tid * iterations / NTHREADS) + iVar;
					int endIndex = ((tid+1) * iterations / NTHREADS) + iVar;
					//					
					for (int k = startIndex; k < endIndex; ++k) {
						//reading only
						//rwl.readLock().lock();
						//System.out.println("IM READING "+tid);
						NBody otherBody = bodies[k];
						double dx = bodies[i].x - otherBody.x;
						double dy = bodies[i].y - otherBody.y;
						double dz = bodies[i].z - otherBody.z;

						double dSquared = dx * dx + dy * dy + dz * dz;
						double distance = Math.sqrt(dSquared);
						double mag = dt / (dSquared * distance);
						/*try {
								barrier.await();
							} catch (InterruptedException | BrokenBarrierException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
						/*rwl.readLock().unlock();
						rwl.writeLock().lock();*/
						//System.out.println("IM WRITING "+tid);
						//read and writing
						bodies[i].vx -= dx * otherBody.mass * mag;
						bodies[i].vy -= dy * otherBody.mass * mag;
						bodies[i].vz -= dz * otherBody.mass * mag;

						otherBody.vx += dx * bodies[i].mass * mag;
						otherBody.vy += dy * bodies[i].mass * mag;
						otherBody.vz += dz * bodies[i].mass * mag;
						//rwl.writeLock().unlock();
					}
					if(tidLocal == 0) {//thread with id 0 has special controls
						//System.out.println(i);
						bodies[i].x += dt * bodies[i].vx;
						bodies[i].y += dt * bodies[i].vy;
						bodies[i].z += dt * bodies[i].vz;
						i++;
					}
				}

				//

			});
			ts[j].start();
		}

		for (int j=0; j<NTHREADS; j++) {
			try {
				ts[j].join();
			} catch (InterruptedException e) {
				// Class code : no exceptions
			}
		}



		/*for (int j = i + 1; j < bodies.length; ++j) {
				//reading only
				final NBody otherBody = bodies[j];
				double dx = iBody.x - otherBody.x;
				double dy = iBody.y - otherBody.y;
				double dz = iBody.z - otherBody.z;

				double dSquared = dx * dx + dy * dy + dz * dz;
				double distance = Math.sqrt(dSquared);
				double mag = dt / (dSquared * distance);

				//read and writing
				iBody.vx -= dx * otherBody.mass * mag;
				iBody.vy -= dy * otherBody.mass * mag;
				iBody.vz -= dz * otherBody.mass * mag;

				otherBody.vx += dx * iBody.mass * mag;
				otherBody.vy += dy * iBody.mass * mag;
				otherBody.vz += dz * iBody.mass * mag;
			}*/
		//iBody.x += dt * iBody.vx;
		//iBody.y += dt * iBody.vy;
		//iBody.z += dt * iBody.vz;
		//}
	}

	public double energy() {
		double dx, dy, dz, distance;
		double e = 0.0;

		for (int i = 0; i < bodies.length; ++i) {
			NBody iBody = bodies[i];
			e += 0.5 * iBody.mass * (iBody.vx * iBody.vx + iBody.vy * iBody.vy + iBody.vz * iBody.vz);

			for (int j = i + 1; j < bodies.length; ++j) {
				NBody jBody = bodies[j];
				dx = iBody.x - jBody.x;
				dy = iBody.y - jBody.y;
				dz = iBody.z - jBody.z;

				distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
				e -= (iBody.mass * jBody.mass) / distance;
			}
		}
		return e;
	}
}