package core.algorithms.genetic;

public class RNASolver {

    public static void solve(int strandSize, int popSize, int tournamentSize, double mutationRate, int maxGenerations) {
        // 1. Initialize Population
        RNA[] population = PopulationModule.randomPopulation(popSize, strandSize);

        // Initial fitness evaluation for the starting population
        for (RNA rna : population) {
            rna.setFitness(Evaluator.calculateFitness(rna.getStrand()));
        }

        int bestFitness = Integer.MAX_VALUE; // We want to minimize (aiming for 0)
        int generation = 0;
        RNA globalBest = null;

        // Fix: Continue ONLY if we haven't found a solution AND haven't hit the limit
        while (bestFitness > 0 && generation < maxGenerations) {
            RNA[] Paux = new RNA[popSize];

            // --- ELITISM: Keep the best one from the previous generation ---
            Paux[0] = findBest(population);

            for (int i = 1; i < popSize; i++) {
                // 2. SELECTION
                RNA parent1 = Selection.tournamentN(population, tournamentSize);
                RNA parent2 = Selection.tournamentN(population, tournamentSize);

                // 3. CROSSOVER
                long childStrand = GeneticOperators.crossover(parent1.getStrand(), parent2.getStrand());

                // 4. MUTATION (Chance-based)
                if (Math.random() < mutationRate) {
                    childStrand = Mutation.mutate(childStrand);
                }

                // 5. CREATE NEW INDIVIDUAL
                RNA child = new RNA(strandSize);
                child.setStrand(childStrand);
                child.setFitness(Evaluator.calculateFitness(childStrand));
                Paux[i] = child;
            }

            // 6. UPDATE POPULATION
            population = Paux;

            // 7. TRACK PROGRESS
            globalBest = findBest(population);
            bestFitness = globalBest.getFitness();
            generation++;

            if (generation % 100 == 0 || bestFitness == 0) {
                System.out.println("Generation: " + generation + " | Best Fitness: " + bestFitness);
            }
        }

        // --- FINAL RESULT ---
        if (bestFitness == 0) {
            System.out.println("Solution found in generation " + generation + "!");
        } else {
            System.out.println("Reached max generations without perfect solution.");
        }
        assert globalBest != null;
        globalBest.boardToString();
    }

    /**
     * Helper to find the individual with the lowest fitness (fewer attacks)
     */
    private static RNA findBest(RNA[] population) {
        RNA best = population[0];
        for (RNA rna : population) {
            if (rna.getFitness() < best.getFitness()) {
                best = rna;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        int strandSize = 8;
        int popSize = 100;
        int tournamentSize = 4;
        double mutationRate = 0.01;
        int maxGenerations = 1000;
        RNASolver.solve(strandSize, popSize, tournamentSize, mutationRate, maxGenerations);
    }

}
