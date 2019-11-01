package knapsack;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class KnapsackGA {
	private static final int N_GENERATIONS = 500;
	private static final int POP_SIZE = 100000;
	private static final double PROB_MUTATION = 0.5;	
	private Individual[] population = new Individual[POP_SIZE];

	public KnapsackGA() {
		populateInitialPopulationRandomly();
	}

	private void populateInitialPopulationRandomly() {
		IntStream.range(0, POP_SIZE).parallel().forEach(i -> {
			population[i] = Individual.createRandom();
		});

	}

	public void run() {
		for (int generation=0; generation<N_GENERATIONS; generation++) {

			// Step1 - Calculate Fitness
			Arrays.stream(population).parallel().forEach(Individual::measureFitness);

			// Step2 - Sort by Fitness descending			
			Arrays.parallelSort(population, new Comparator<Individual>() {
				@Override
				public int compare(Individual o1, Individual o2) {
					if (o1.getFitness() > o2.getFitness()) return -1;
					if (o1.getFitness() < o2.getFitness()) return 1;
					return 0;
				}});

			// Debug
			//population[0] is the individual with best fitness
			System.out.println("Best fitness at " + generation + " is " + population[0].getFitness());
			
			Individual[] newPopulation = new Individual[POP_SIZE];
			newPopulation[0] = population[0];
			IntStream.range(1, POP_SIZE).parallel().forEach(i -> {				
				// Step3 - Find parents to mate (cross-over)
				// The first elements in the population have higher probability of being selected
				int pos1 = (int) (- Math.log(ThreadLocalRandom.current().nextDouble()) * POP_SIZE) % POP_SIZE;
				int pos2 = (int) (- Math.log(ThreadLocalRandom.current().nextDouble()) * POP_SIZE) % POP_SIZE;
				newPopulation[i] = population[pos1].crossoverWith(population[pos2]);
				
				// Step4 - Mutate
				if (ThreadLocalRandom.current().nextDouble() < PROB_MUTATION) {
					newPopulation[i].mutate();
				}
			});

			population = newPopulation;
		}
	}

}
