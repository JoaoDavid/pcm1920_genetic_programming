package knapsack;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class KnapsackGA {
	private static final int N_GENERATIONS = 500;
	private static final int POP_SIZE = 100000;
	private static final double PROB_MUTATION = 0.5;	
	private Individual[] population;
	
	public KnapsackGA() {
		populateInitialPopulationRandomly();
	}

	private void populateInitialPopulationRandomly() {
		population = IntStream.range(0, POP_SIZE).parallel().mapToObj( num -> {
			return Individual.createRandom();
		}).toArray(Individual[]::new);
	}

	public void run() {
		for (int generation=0; generation<N_GENERATIONS; generation++) {

			// Step1 - Calculate Fitness
			Arrays.stream(population).parallel().forEach(Individual::measureFitness);
			
			
			// Step2 - Sort by Fitness descending			
			population = Arrays.stream(population).parallel().sorted(new Comparator<Individual>() {
				@Override
				public int compare(Individual o1, Individual o2) {
					if (o1.getFitness() > o2.getFitness()) return -1;
					if (o1.getFitness() < o2.getFitness()) return 1;
					return 0;
				}
			}).toArray(Individual[]::new);
			
						
			// Debug			
			Individual best = population[0];
			System.out.println("Best fitness at " + generation + " is " + best.getFitness());

			
			// Step3 - Find parents to mate (cross-over)			
			Stream<Individual> newStreamPop = IntStream.range(1, POP_SIZE).parallel().mapToObj( num -> {
				// The first elements in the population have higher probability of being selected
				int pos1 = (int) (- Math.log(ThreadLocalRandom.current().nextDouble()) * POP_SIZE) % POP_SIZE;
				int pos2 = (int) (- Math.log(ThreadLocalRandom.current().nextDouble()) * POP_SIZE) % POP_SIZE;
				return population[pos1].crossoverWith(population[pos2]);
			});
			
			
			// Step4 - Mutate			
			Individual[] newPopulation = newStreamPop.toArray(Individual[]::new);
			Stream.of(newPopulation).parallel().forEach(i -> {
		        if (ThreadLocalRandom.current().nextDouble() < PROB_MUTATION) {
		            i.mutate();
		        }
		    });
			
			population = Stream.concat(Stream.of(best),Stream.of(newPopulation)).toArray(Individual[]::new);
		}
	}

}
