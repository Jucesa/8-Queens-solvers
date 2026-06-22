package core.algorithms.genetic;

import java.util.Random;

public class GeneticOperators {
    private static final Random rand = new Random();

    public static long crossover(long p1, long p2) {
        // 1. Pick a random row (1-7) to be the "cut point"
        // Each row is 8 bits, so we cut at a multiple of 8
        int cutRow = rand.nextInt(7) + 1; 
        int cutBit = cutRow * 8;

        // 2. Create a mask for the first 'cutBit' bits
        // Example: if cutBit is 16, mask is sixteen 1s at the end
        long mask = (1L << cutBit) - 1;

        // 3. Combine: take the top rows from p1 and bottom rows from p2
        long childStrand = (p1 & mask) | (p2 & ~mask);

        // 4. Repair the strand to ensure it has exactly 8 queens
        return repair(childStrand);
    }

    private static long repair(long strand) {
        int count = Long.bitCount(strand);

        // Too many queens? Remove random ones.
        while (count > 8) {
            int pos = rand.nextInt(64);
            if (((strand >> pos) & 1L) == 1L) {
                strand &= ~(1L << pos);
                count--;
            }
        }

        // Too few queens? Add random ones.
        while (count < 8) {
            int pos = rand.nextInt(64);
            if (((strand >> pos) & 1L) == 0L) {
                strand |= (1L << pos);
                count++;
            }
        }

        return strand;
    }
}