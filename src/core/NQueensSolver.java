package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public abstract class NQueensSolver {

    public static final int BOARD_SIZE = 8;
    public static final int TOTAL_SQUARES = 64;
    protected static final Random rand = new Random();

    // --- INTERFACE E ESTRUTURAS ---

    public abstract int solve(boolean[] board, BiConsumer<boolean[], Integer> onStep);

    public abstract String getName();


    // --- FUNÇÕES DE APOIO ---

    private static int[] indexToCoords(int idx) {
        return new int[]{idx / BOARD_SIZE, idx % BOARD_SIZE};
    }

    public static boolean[] gerarTabuleiro(int numQueens) {
        boolean[] board = new boolean[TOTAL_SQUARES];
        int count = 0;
        while (count < numQueens) {
            int pos = rand.nextInt(TOTAL_SQUARES);
            if (!board[pos]) {
                board[pos] = true;
                count++;
            }
        }
        return board;
    }

    public static int calcularAtaques(boolean[] b) {
        int attacks = 0;
        List<Integer> queens = new ArrayList<>();
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            if (b[i]) queens.add(i);
        }

        for (int i = 0; i < queens.size(); i++) {
            int[] c1 = indexToCoords(queens.get(i));
            for (int j = i + 1; j < queens.size(); j++) {
                int[] c2 = indexToCoords(queens.get(j));
                if (c1[0] == c2[0] || c1[1] == c2[1] ||
                        Math.abs(c1[0] - c2[0]) == Math.abs(c1[1] - c2[1])) {
                    attacks++;
                }
            }
        }
        return attacks;
    }

    private static int ataquesNaPosicao(boolean[] b, int row, int col) {
        int count = 0;
        for (int i = 0; i < TOTAL_SQUARES; i++) {
            if (b[i]) {
                int[] q = indexToCoords(i);
                if (q[0] == row || q[1] == col ||
                        Math.abs(q[0] - row) == Math.abs(q[1] - col)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void renderFrame(boolean[] b, int numQueens, int attacks, int step, String solverName) {
        StringBuilder sb = new StringBuilder();

        // Move o cursor para o topo
        sb.append("\033[H");

        sb.append("====================================================\n");
        sb.append(String.format(" RAINHAS: %d | ATAQUES: %d | SOLVER: %s\n", numQueens, attacks, solverName));
        sb.append(String.format(" PASSO: %d\n", step));
        sb.append("====================================================\n");

        sb.append("\n   A  B  C  D  E  F  G  H\n");
        for (int r = 0; r < BOARD_SIZE; r++) {
            sb.append((r + 1)).append(" ");
            for (int c = 0; c < BOARD_SIZE; c++) {
                int atq = ataquesNaPosicao(b, r, c);

                String color = "\033[0m";
                if (atq >= 5) color = "\033[31;1m"; // Vermelho
                else if (atq >= 3) color = "\033[33m";    // Amarelo
                else if (atq >= 1) color = "\033[36m";    // Ciano
                else color = "\033[90m";                   // Cinza

                sb.append(color).append(" ").append(atq).append(" \033[0m");
            }
            sb.append("\n");
        }
        sb.append("\n====================================================\n");
        sb.append("\033[J");

        System.out.print(sb.toString());

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}