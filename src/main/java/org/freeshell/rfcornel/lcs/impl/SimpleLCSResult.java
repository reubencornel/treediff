package org.freeshell.rfcornel.lcs.impl;

import org.freeshell.rfcornel.lcs.LCSCellResult;
import org.freeshell.rfcornel.lcs.LCSDirection;
import org.freeshell.rfcornel.lcs.LCSResult;
import org.freeshell.rfcornel.util.Pair;

import java.util.Optional;

public class SimpleLCSResult implements LCSResult {
	private LCSCellResult[][] results;

	public SimpleLCSResult(int x, int y) {
		// We always add +1 because when we execute LCS, the zeroth row and column are always empty
		this.results = new LCSCellResult[x+2][y+2];

	}

	public Optional<LCSCellResult> getCellResult(int x, int y) {
		return Optional.ofNullable(results[x+1][y+1]);
	}

	public void setCellResult(int x, int y, LCSDirection direction, int cost) {
		results[x+1][y+1] = new LCSCellResult(direction, cost);
	}

	public Pair<Integer, Integer> getSize() {
		return Pair.of(results.length - 1, results[0].length - 1);
	} 
}
