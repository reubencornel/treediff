package org.freeshell.rfcornel.lcs.impl;

import org.freeshell.rfcornel.lcs.LCSCellResult;
import org.freeshell.rfcornel.lcs.LCSDirection;
import org.freeshell.rfcornel.lcs.LCSResult;
import org.freeshell.rfcornel.util.Pair;

import java.util.Arrays;
import java.util.Optional;

public class ShortBasedLCSResult implements LCSResult {
    private byte[][] results;
    private int[][] costs;

    public ShortBasedLCSResult(int x, int y){
        this.results = new byte[x+2][y+2];
        for (int i = 0; i < x + 2; i++) {
            Arrays.fill(results[i], (byte)-1);
        }
        this.costs = new int[x+2][y+2];
    }

    @Override
    public Optional<LCSCellResult> getCellResult(int x, int y) {
        if (results[x+1][y+1] == -1) {
            return Optional.empty();
        } else {
            return Optional.of(new LCSCellResult(LCSDirection.valueOf(results[x+1][y+1]), costs[x+1][y+1]));
        }
    }

    @Override
    public void setCellResult(int x, int y, LCSDirection direction, int cost) {
        results[x+1][y+1] = direction.value;
        costs[x+1][y+1] = cost;
    }

    @Override
    public Pair<Integer, Integer> getSize() {
        return Pair.of(results.length - 1, results[0].length - 1);
    }
}
