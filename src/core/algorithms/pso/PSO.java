package core.algorithms.pso;

import core.NQueensSolver;
import java.util.function.BiConsumer;

public class PSO extends NQueensSolver {
    private final int numParticles;
    private final int iterations;
    private final double c1;
    private final double c2;
    private final double inertia;

    public PSO(int numParticles, int iterations, double c1, double c2, double inertia) {
        this.numParticles = numParticles;
        this.iterations = iterations;
        this.c1 = c1;
        this.c2 = c2;
        this.inertia = inertia;
    }

    @Override
    public String getName() {
        return "PSO (P=" + numParticles + ", K=" + iterations + ", c1=" + c1 + ", c2=" + c2 + ", w=" + inertia +")";
    }

    @Override
    public int solve(boolean[] board, BiConsumer<boolean[], Integer> onStep) {
        Particle[] swarm = new Particle[numParticles];
        double[] gBestPos = new double[BOARD_SIZE];
        int gBestFitness = Integer.MAX_VALUE;

        // 1. Inicialização do Enxame
        for (int i = 0; i < numParticles; i++) {
            double[] randomPos = new double[BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; j++) {
                randomPos[j] = rand.nextDouble() * (BOARD_SIZE - 1);
            }

            int fitness = calculateFitnessFromPso(randomPos);
            swarm[i] = new Particle(randomPos, fitness);

            if (fitness < gBestFitness) {
                gBestFitness = fitness;
                gBestPos = randomPos.clone();
            }
        }

        // 2. Loop de Evolução
        for (int step = 1; step <= iterations; step++) {
            if (gBestFitness == 0) break;

            for (Particle p : swarm) {
                p.move(inertia, c1, c2, gBestPos);

                int currentFitness = calculateFitnessFromPso(p.getPosition());

                // Atualiza Melhor Pessoal (pBest)
                if (currentFitness < p.getPBestFitness()) {
                    p.updatePBest(p.getPosition(), currentFitness);
                }

                // Atualiza Melhor Global (gBest)
                if (currentFitness < gBestFitness) {
                    gBestFitness = currentFitness;
                    gBestPos = p.getPosition().clone();

                    // Notifica a interface visual
                    onStep.accept(psoToBoard(gBestPos), step);
                }
            }
        }
        return gBestFitness;
    }

    private int calculateFitnessFromPso(double[] psoPos) {
        return calcularAtaques(psoToBoard(psoPos));
    }

    private boolean[] psoToBoard(double[] psoPos) {
        boolean[] b = new boolean[TOTAL_SQUARES];
        for (int row = 0; row < BOARD_SIZE; row++) {
            int col = (int) Math.round(psoPos[row]);
            // Clamp de segurança para o arredondamento
            col = Math.clamp(col, 0, BOARD_SIZE - 1);
            b[row * BOARD_SIZE + col] = true;
        }
        return b;
    }
}