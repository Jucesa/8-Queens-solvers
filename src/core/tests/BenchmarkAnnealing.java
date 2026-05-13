package core.tests;

import core.NQueensSolver;
import core.algorithms.SimulatedAnnealing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BenchmarkAnnealing {

    private record AnnealingResult(
            int k,
            double temp,
            double cooling,
            double avgAttacks,
            double successRate
    ) {}

    public static void main(String[] args) {
        int numSimulationsPerConfig = 1000; // Aumentado para estabilizar a média
        int numQueens = 8;

        // Variações de parâmetros para o Grid Search
        int[] kOptions = {1000};
        double[] tempOptions = {50.0, 100.0, 200.0};
        double[] coolingOptions = {0.5, 0.9, 0.98}; // Incluí o seu 0.5 para comparação

        // Gerar base de testes fixa
        boolean[][] testBase = new boolean[numSimulationsPerConfig][NQueensSolver.TOTAL_SQUARES];
        for (int i = 0; i < numSimulationsPerConfig; i++) {
            testBase[i] = NQueensSolver.gerarTabuleiro(numQueens);
        }

        System.out.println("Iniciando Benchmark do Simulated Annealing...");
        System.out.println("Aguarde o processamento...\n");

        List<AnnealingResult> allResults = new ArrayList<>();

        for (int k : kOptions) {
            for (double temp : tempOptions) {
                for (double cooling : coolingOptions) {
                    
                    SimulatedAnnealing solver = new SimulatedAnnealing(k, temp, cooling);
                    int totalAttacks = 0;
                    int successCount = 0;

                    for (int i = 0; i < numSimulationsPerConfig; i++) {
                        boolean[] boardClone = testBase[i].clone();
                        int finalAtq = solver.solve(boardClone, (b, step) -> {});
                        
                        totalAttacks += finalAtq;
                        if (finalAtq == 0) successCount++;
                    }

                    double avgAttacks = (double) totalAttacks / numSimulationsPerConfig;
                    double successRate = ((double) successCount / numSimulationsPerConfig) * 100;

                    allResults.add(new AnnealingResult(k, temp, cooling, avgAttacks, successRate));
                }
            }
        }

        // Ordenação por Média de Ataques (Ascendente)
        allResults.sort(Comparator.comparingDouble(AnnealingResult::avgAttacks));

        exibirTabela(allResults);
    }

    private static void exibirTabela(List<AnnealingResult> results) {
        System.out.println("====================================================================");
        System.out.println(" RANKING SIMULATED ANNEALING | ORDENADO POR MÉDIA DE ATAQUES");
        System.out.println("====================================================================");
        System.out.printf("| %-7s | %-6s | %-8s | %-15s | %-12s |\n", "Iter (K)", "Temp", "Cooling", "Média Ataques", "Sucesso (0)");
        System.out.println("|---------|--------|----------|-----------------|--------------|");

        for (AnnealingResult res : results) {
            System.out.printf("| %-7d | %-6.1f | %-8.2f | %-15.2f | %-11.1f%% |\n", 
                              res.k, res.temp, res.cooling, res.avgAttacks, res.successRate);
        }
        System.out.println("====================================================================");
        
        AnnealingResult melhor = results.get(0);
        System.out.printf("\n>>> MELHOR CONFIGURAÇÃO: K=%d, Temp=%.1f, Cooling=%.2f\n", 
                          melhor.k, melhor.temp, melhor.cooling);
    }
}