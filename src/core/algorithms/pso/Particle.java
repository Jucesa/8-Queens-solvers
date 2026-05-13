package core.algorithms.pso;

import java.util.Random;

public class Particle {
    private double[] position;
    private double[] velocity;
    private double[] pBestPos;
    private int pBestFitness;
    private final Random rand = new Random();

    public Particle(double[] initialPos, int initialFitness) {
        this.position = initialPos.clone();
        this.pBestPos = initialPos.clone();
        this.velocity = new double[initialPos.length];
        this.pBestFitness = initialFitness;
    }

    /**
     * Atualiza a velocidade e posição da partícula com base nas constantes e no gBest.
     */
    public void move(double inertia, double c1, double c2, double[] gBestPos) {
        for (int i = 0; i < position.length; i++) {
            double r1 = rand.nextDouble();
            double r2 = rand.nextDouble();

            // V(t+1) = w*V(t) + c1*r1*(pBest - X) + c2*r2*(gBest - X)
            velocity[i] = (inertia * velocity[i]) +
                    (c1 * r1 * (pBestPos[i] - position[i])) +
                    (c2 * r2 * (gBestPos[i] - position[i]));

            position[i] += velocity[i];

            // Garante que a rainha permaneça dentro dos limites da linha (0-7)
            if (position[i] < 0) position[i] = 0;
            if (position[i] > 7) position[i] = 7;
        }
    }

    // Getters e Setters para controle do Solver
    public double[] getPosition() { return position; }
    public double[] getPBestPos() { return pBestPos; }
    public int getPBestFitness() { return pBestFitness; }

    public void updatePBest(double[] pos, int fitness) {
        this.pBestPos = pos.clone();
        this.pBestFitness = fitness;
    }
}