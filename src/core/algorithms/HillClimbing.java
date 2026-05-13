package core.algorithms;

import core.NQueensSolver;

import java.util.function.BiConsumer;

public class HillClimbing extends NQueensSolver {
    private final int k;
    public HillClimbing(int k) { this.k = k; }

    @Override
    public String getName() { return "Hill Climbing (K=" + k + ")"; }

    @Override
    public int solve(boolean[] board, BiConsumer<boolean[], Integer> onStep) {
        int currentAttacks = calcularAtaques(board);
        onStep.accept(board, 0);

        for (int step = 1; step <= k; step++) {
            if (currentAttacks == 0) break;

            boolean improved = false;
            int bestAtq = currentAttacks;
            int bestSource = -1, bestDest = -1;

            // Busca exaustiva pela melhor vizinhança
            for (int i = 0; i < TOTAL_SQUARES; i++) {
                if (board[i]) {
                    for (int j = 0; j < TOTAL_SQUARES; j++) {
                        if (!board[j]) {
                            board[i] = false; board[j] = true;
                            int nAtq = calcularAtaques(board);
                            if (nAtq < bestAtq) {
                                bestAtq = nAtq;
                                bestSource = i;
                                bestDest = j;
                                improved = true;
                            }
                            board[i] = true; board[j] = false; // backtrack
                        }
                    }
                }
            }

            if (!improved) break;

            board[bestSource] = false;
            board[bestDest] = true;
            currentAttacks = bestAtq;
            onStep.accept(board, step);
        }
        return currentAttacks;
    }
}