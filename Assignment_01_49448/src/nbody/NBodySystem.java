package nbody;

import java.util.Random;

public class NBodySystem {
	
	public static final int DEFAULT_ITERATIONS = 5;
	public static final int DEFAULT_SIZE = 50000;

	public static final int ADVANCE_THRESHOLD = 1000;
	public static final int APPLY_THRESHOLD = 100;

	static final double PI = 3.141592653589793;
	static final double SOLAR_MASS = 4 * PI * PI;
	
	protected NBody[] bodies;

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

		for (int i = 0; i < bodies.length; ++i) {
			NBody iBody = bodies[i];
			for (int j = i + 1; j < bodies.length; ++j) {
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
			}
			iBody.x += dt * iBody.vx;
			iBody.y += dt * iBody.vy;
			iBody.z += dt * iBody.vz;
		}
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