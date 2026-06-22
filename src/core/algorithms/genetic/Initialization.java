package core.algorithms.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Initialization {

    public static long initializeRandomQueens(int bitmapSize) {
        long strand = 0L;
        // We use a list to ensure we pick 8 UNIQUE positions
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < bitmapSize; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions);

        // Set the first 8 positions from the shuffled list
        for (int i = 0; i < 8; i++) {
            int pos = positions.get(i);
            strand |= (1L << pos);
        }
        return strand;
    }
}
