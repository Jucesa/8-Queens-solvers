package core.algorithms;

import core.NQueensSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class RandomSearch extends NQueensSolver {
    private final int k;

    public RandomSearch(int k) {
        this.k = k;
    }

    @Override
    public String getName() {
        return "Random Search (K=" + k + ")";
    }

    @Override
    public int solve(boolean[] board, BiConsumer<boolean[], Integer> onStep) {
        int currentAttacks = calcularAtaques(board);
        onStep.accept(board, 0);

        for (int i = 1; i <= k; i++) {
            if (currentAttacks == 0) break;

            List<Integer> queens = new ArrayList<>();
            List<Integer> empty = new ArrayList<>();
            for (int idx = 0; idx < TOTAL_SQUARES; idx++) {
                if (board[idx]) queens.add(idx);
                else empty.add(idx);
            }

            int qIdx = queens.get(rand.nextInt(queens.size()));
            int destIdx = empty.get(rand.nextInt(empty.size()));

            board[qIdx] = false;
            board[destIdx] = true;
            int newAtq = calcularAtaques(board);

            if (newAtq <= currentAttacks) {
                currentAttacks = newAtq;
                onStep.accept(board, i);
            } else {
                board[qIdx] = true;
                board[destIdx] = false;
            }
        }
        return currentAttacks;
    }
}
