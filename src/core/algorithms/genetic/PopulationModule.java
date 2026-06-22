package core.algorithms.genetic;

import java.util.Random;

public class PopulationModule {
    public static RNA[] randomPopulation(int popSize, int strandSize){
        RNA[] population = new RNA[popSize];

        for (int i = 0; i < popSize; i++) {
            population[i] = new RNA(strandSize);
        }
        return population;
    }

    public static void apllyMutate(RNA[] population, double mutationRate) {
        Random rand = new Random();

        for (RNA rna : population) {
            // Only mutate if the random roll is less than the mutation rate
            if (rand.nextDouble() < mutationRate) {

                // 1. Get the current board state
                long currentStrand = rna.getStrand();

                // 2. Apply the bit-swap mutation (using the method we wrote earlier)
                long mutatedStrand = Selection.mutate(currentStrand);

                // 3. Update the RNA object with the new strand
                rna.setStrand(mutatedStrand);

                // 4. CRITICAL: Recalculate fitness immediately after mutation
                // Otherwise, the GA will use the old fitness for the new board
                rna.setFitness(Evaluator.calculateFitness(rna.getStrand()));
            }
        }
    }
}
