package knapsack;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class KnapsackGA {
	private static final int N_GENERATIONS = 500;
	private static final int POP_SIZE = 100000;
	private static final double PROB_MUTATION = 0.5;
	
	private Random r = new Random();
	
	private Individual[] population = new Individual[POP_SIZE];
	private Stream<Individual> streamPop;// = Stream.empty();// = Arrays.stream(population);
	
	public KnapsackGA() {
		populateInitialPopulationRandomly();
	}

	private void populateInitialPopulationRandomly() {
		/*Stream.Builder<Individual> builder = Stream.builder();
		builder.add(Individual.createRandom());
		streamPop = builder.build();*/
		
		streamPop = IntStream.range(0, POP_SIZE).parallel().mapToObj( num -> {
			return Individual.createRandom();
		});
		System.out.println(streamPop.count());
		
		
		/*for (int i=0; i<POP_SIZE; i++) {
			population[i] = Individual.createRandom();
		}*/
		//streamPop.forEach(Individual.createRandom());
	}

	public void run() {
		for (int generation=0; generation<N_GENERATIONS; generation++) {

			// Step1 - Calculate Fitness
			/*for (int i=0; i<POP_SIZE; i++) {
				population[i].measureFitness();
			}*/
			//Stream<Individual> str = 
					//Arrays.stream(population).parallel().forEach(Individual::measureFitness);
			streamPop.parallel().forEach(Individual::measureFitness);
			
			// Step2 - Sort by Fitness descending
			
			/*Arrays.sort(population, new Comparator<Individual>() {
				@Override
				public int compare(Individual o1, Individual o2) {
					if (o1.getFitness() > o2.getFitness()) return -1;
					if (o1.getFitness() < o2.getFitness()) return 1;
					return 0;
				}
			});*/
			
			streamPop.sorted(new Comparator<Individual>() {
				@Override
				public int compare(Individual o1, Individual o2) {
					if (o1.getFitness() > o2.getFitness()) return -1;
					if (o1.getFitness() < o2.getFitness()) return 1;
					return 0;
				}
			});
			
			Optional<Individual> best = streamPop.findFirst();
			
			// Debug
			
			//System.out.println("Best fitness at " + generation + " is " + population[0].getFitness());
			System.out.println("Best fitness at " + generation + " is " + best.get());
			
			population = (Individual[]) streamPop.parallel().toArray();
			
			// Step3 - Find parents to mate (cross-over)
			Individual[] newPopulation = new Individual[POP_SIZE];
			newPopulation[0] = population[0]; // The best individual remains
			Stream<Individual> newStreamPop = Stream.of(best.get());
			
			
			/*for (int i=1; i<POP_SIZE; i++) {
				// The first elements in the population have higher probability of being selected
				int pos1 = (int) (- Math.log(r.nextDouble()) * POP_SIZE) % POP_SIZE;
				int pos2 = (int) (- Math.log(r.nextDouble()) * POP_SIZE) % POP_SIZE;
				
				newPopulation[i] = population[pos1].crossoverWith(population[pos2]);
			}*/
			
			newStreamPop = IntStream.range(1, POP_SIZE).parallel().mapToObj( num -> {
				int pos1 = (int) (- Math.log(r.nextDouble()) * POP_SIZE) % POP_SIZE;
				int pos2 = (int) (- Math.log(r.nextDouble()) * POP_SIZE) % POP_SIZE;
				return population[pos1].crossoverWith(population[pos2]);
			});
			
			// Step4 - Mutate
			for (int i=1; i<POP_SIZE; i++) {
				if (r.nextDouble() < PROB_MUTATION) {
					newPopulation[i].mutate();
				}
			}
			newStreamPop.parallel().forEach(Individual::mutate);
			streamPop = newStreamPop;
			population = newPopulation;
		}
	}

}
