package nbody;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class NBodySystem {

	public static final int DEFAULT_ITERATIONS = 5;
	public static final int DEFAULT_SIZE = 50000;

	public static final int ADVANCE_THRESHOLD = 1000;
	public static final int APPLY_THRESHOLD = 100;

	static final double PI = 3.141592653589793;
	static final double SOLAR_MASS = 4 * PI * PI;

	protected NBody[] bodies;
	private ForkJoinPool pool = new ForkJoinPool();

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
		double[][] iBodyAarr = new double[bodies.length][3];
		double[][] otherBodyAarr = new double[bodies.length][3];
		NBodyAdvanceFj bodyAdvance = new NBodyAdvanceFj(bodies, 0, bodies.length, dt, iBodyAarr, otherBodyAarr);
		bodyAdvance.compute();
		//pool.execute(bodyAdvance);
		
		//System.out.println("inForkJoinPool " + bodyAdvance.inForkJoinPool());
		//System.out.println(pool.toString());
		//bodyAdvance.join();
		for (int i = 0; i < bodies.length; i++) {
			bodies[i].vx -= iBodyAarr[i][0];
			bodies[i].vy -= iBodyAarr[i][1];
			bodies[i].vz -= iBodyAarr[i][2];
			bodies[i].vx += otherBodyAarr[i][0];
			bodies[i].vy += otherBodyAarr[i][1];
			bodies[i].vz += otherBodyAarr[i][2];
			bodies[i].x += dt * bodies[i].vx;
			bodies[i].y += dt * bodies[i].vy;
			bodies[i].z += dt * bodies[i].vz;
		}
		/*IntStream.range(0, bodies.length).parallel().forEach(i -> {				
			bodies[i].vx -= iBodyAarr[i][0];
			bodies[i].vy -= iBodyAarr[i][1];
			bodies[i].vz -= iBodyAarr[i][2];
			bodies[i].x += dt * bodies[i].vx;
			bodies[i].y += dt * bodies[i].vy;
			bodies[i].z += dt * bodies[i].vz;
		});*/
		/*for (NBody body : bodies) {
			body.x += dt * body.vx;
			body.y += dt * body.vy;
			body.z += dt * body.vz;
		}*/
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