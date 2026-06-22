package core.algorithms.genetic;


public class RNA {
    private long strand;
    private int fitness;
    private final int strandSize;

    public RNA(int strandSize) {
        this.strandSize = strandSize;
        this.strand = Initialization.initializeRandomQueens(this.strandSize);
        this.fitness = Evaluator.calculateFitness(strand);
    }

    public void boardToString() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int row = 0; row < 8; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < 8; col++) {
                int bitIndex = row * 8 + col;
                boolean hasQueen = ((strand >> bitIndex) & 1L) == 1L;

                if (hasQueen) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println("Fitness: " + fitness + "\n");
    }

    // Getters and Setters
    public long getStrand() {
        return strand;
    }

    public void setStrand(long strand) {
        this.strand = strand;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}