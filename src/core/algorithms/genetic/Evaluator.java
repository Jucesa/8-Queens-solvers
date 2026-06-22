package core.algorithms.genetic;

public class Evaluator {
    public static int calculateFitness(long strand) {
        int attacks = 0;
        int[][] queenCoords = new int[8][2];
        int count = 0;

        // 1. Extract coordinates of the 8 queens from the bitmask
        for (int i = 0; i < 64; i++) {
            if (((strand >> i) & 1L) == 1L) {
                queenCoords[count][0] = i / 8; // Row
                queenCoords[count][1] = i % 8; // Column
                count++;
            }
            if (count == 8) break;
        }

        // 2. Compare every pair of queens (Combination 8C2 = 28 comparisons)
        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 8; j++) {
                int r1 = queenCoords[i][0];
                int c1 = queenCoords[i][1];
                int r2 = queenCoords[j][0];
                int c2 = queenCoords[j][1];

                // Check Horizontal
                if (r1 == r2) {
                    attacks++;
                }
                // Check Vertical
                else if (c1 == c2) {
                    attacks++;
                }
                // If the absolute difference of rows equals the absolute difference of columns
                else if (Math.abs(r1 - r2) == Math.abs(c1 - c2)) {
                    attacks++;
                }
            }
        }
        return attacks;
    }
}
