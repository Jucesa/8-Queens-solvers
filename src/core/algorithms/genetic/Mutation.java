package core.algorithms.genetic;
import java.util.Random;

public class Mutation {

    public static long mutate(long strand) {
        Random rand = new Random();
        int queenPos = -1;
        int emptyPos = -1;

        // 1. Find a random bit that currently contains a queen (1)
        while (queenPos == -1) {
            int r = rand.nextInt(64);
            if (((strand >> r) & 1L) == 1L) {
                queenPos = r;
            }
        }

        // 2. Find a random bit that is currently empty (0)
        while (emptyPos == -1) {
            int r = rand.nextInt(64);
            if (((strand >> r) & 1L) == 0L) {
                emptyPos = r;
            }
        }

        // 3. Perform the "move" using bitwise operators
        // Remove queen from old position: strand AND (NOT bitmask)
        strand &= ~(1L << queenPos);

        // Place queen in new position: strand OR bitmask
        strand |= (1L << emptyPos);

        return strand;
    }
}
