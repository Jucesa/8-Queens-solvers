package core.tests;

import core.NQueensSolver;
import core.algorithms.HillClimbing;
import core.algorithms.RandomSearch;
import core.algorithms.SimulatedAnnealing;
import core.algorithms.pso.PSO;

import java.util.*;

public class Simulation {
    public static void main(String[] args) {
        int numSimulations = 10; // Aumentado para maior precisão estatística
        int numQueens = 8;

        // Definindo a lista de solvers com diferentes configurações (K) conforme a imagem
        List<NQueensSolver> solvers = Arrays.asList(
                new HillClimbing(1000),
                new SimulatedAnnealing(1000, 100, 0.5),
                new RandomSearch(1000),
                new PSO(50, 1000, 2.5, 0.5, 0.7),
                new PSO(50, 1000, 1.5, 0.5, 0.9),
                new PSO(50, 1000, 2.5, 1.5, 0.7),
                new PSO(50, 1000, 2.5, 0.5, 0.4)

        );

        // 1. Gerar base de teste única para todos
        boolean[][] bateriaDeTestes = new boolean[numSimulations][NQueensSolver.TOTAL_SQUARES];
        for (int i = 0; i < numSimulations; i++) {
            bateriaDeTestes[i] = NQueensSolver.gerarTabuleiro(numQueens);
        }

        Map<String, Double> resultados = new LinkedHashMap<>();

        System.out.println("Executando simulações... Aguarde.");

        // 2. Execução silenciosa
        for (NQueensSolver solver : solvers) {
            int totalAttacks = 0;

            for (int i = 0; i < numSimulations; i++) {
                boolean[] boardClone = bateriaDeTestes[i].clone();

                // Passamos um callback vazio (b, step) -> {} para não imprimir nada durante a busca
                int finalAtq = solver.solve(boardClone, (b, step) -> {});
                totalAttacks += finalAtq;
            }

            double media = (double) totalAttacks / numSimulations;
            resultados.put(solver.getName(), media);
        }

        // 3. Exibição do Relatório Final
        exibirRelatorioFinal(resultados);
    }

    private static void exibirRelatorioFinal(Map<String, Double> resultados) {
        // Limpa o terminal antes de mostrar a tabela
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("================================================");
        System.out.println("   COMPARATIVO FINAL (MESMA BASE DE TESTE)  ");
        System.out.println("================================================");
        System.out.printf("| %-32s | %-9s |\n", "Algoritmo", "Média Atq");
        System.out.println("|----------------------------------|-----------|");

        for (Map.Entry<String, Double> entry : resultados.entrySet()) {
            System.out.printf("| %-32s | %-9.2f |\n", entry.getKey(), entry.getValue());
        }

        System.out.println("================================================");
    }
}