package core.tests;

import core.NQueensSolver;
import core.algorithms.pso.PSO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BenchmarkPSO {

    // Estrutura imutável para armazenar os resultados de cada teste
    private record BenchmarkResult(
            double c1,
            double c2,
            double inertia,
            double avgAttacks,
            double successRate
    ) {}

    public static void main(String[] args) {
        int numParticles = 50;
        int iterations = 1000;
        int numSimulationsPerConfig = 100;
        int numQueens = 8;

        double[] c1Options = {0.5, 1.5, 2.5};
        double[] c2Options = {0.5, 1.5, 2.5};
        double[] inertiaOptions = {0.4, 0.7, 0.9};

        boolean[][] testBase = new boolean[numSimulationsPerConfig][NQueensSolver.TOTAL_SQUARES];
        for (int i = 0; i < numSimulationsPerConfig; i++) {
            testBase[i] = NQueensSolver.gerarTabuleiro(numQueens);
        }

        System.out.println("Iniciando Benchmark do PSO...");
        System.out.println("Processando combinações (Aguarde)...\n");

        List<BenchmarkResult> allResults = new ArrayList<>();

        // 1. Coleta de dados (Grid Search)
        for (double c1 : c1Options) {
            for (double c2 : c2Options) {
                for (double inertia : inertiaOptions) {

                    PSO solver = new PSO(numParticles, iterations, c1, c2, inertia);
                    int totalAttacks = 0;
                    int successCount = 0;

                    for (int i = 0; i < numSimulationsPerConfig; i++) {
                        boolean[] boardClone = testBase[i].clone();
                        // Callback vazio para execução silenciosa
                        int finalAtq = solver.solve(boardClone, (b, step) -> {});

                        totalAttacks += finalAtq;
                        if (finalAtq == 0) successCount++;
                    }

                    double avgAttacks = (double) totalAttacks / numSimulationsPerConfig;
                    double successRate = ((double) successCount / numSimulationsPerConfig) * 100;

                    allResults.add(new BenchmarkResult(c1, c2, inertia, avgAttacks, successRate));
                }
            }
        }

        // 2. Ordenação por Média de Ataques (Menor para o maior)
        allResults.sort(Comparator.comparingDouble(BenchmarkResult::avgAttacks));

        // 3. Impressão da Tabela Ordenada
        exibirTabela(allResults, numParticles, iterations);
    }

    private static void exibirTabela(List<BenchmarkResult> results, int p, int k) {
        System.out.println("====================================================================");
        System.out.printf(" RANKING PSO | P=%d | K=%d | ORDENADO POR MÉDIA\n", p, k);
        System.out.println("====================================================================");
        System.out.printf("| %-5s | %-5s | %-7s | %-15s | %-12s |\n", "C1", "C2", "Inércia", "Média Ataques", "Sucesso (0)");
        System.out.println("|-------|-------|---------|-----------------|--------------|");

        for (BenchmarkResult res : results) {
            System.out.printf("| %-5.1f | %-5.1f | %-7.1f | %-15.2f | %-11.1f%% |\n",
                    res.c1, res.c2, res.inertia, res.avgAttacks, res.successRate);
        }
        System.out.println("====================================================================");

        BenchmarkResult melhor = results.get(0);
        System.out.printf("\n>>> MELHOR CONFIGURAÇÃO ENCONTRADA: C1=%.1f, C2=%.1f, Inércia=%.1f\n",
                melhor.c1, melhor.c2, melhor.inertia);
    }
}