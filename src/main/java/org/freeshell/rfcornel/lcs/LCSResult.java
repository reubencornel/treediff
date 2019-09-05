package org.freeshell.rfcornel.lcs;

import org.freeshell.rfcornel.util.Pair;

import java.util.Optional;

public interface LCSResult {
	Optional<LCSCellResult> getCellResult(int x, int y);
	void setCellResult(int x, int y, LCSDirection direction, int cost);
	Pair<Integer, Integer> getSize();
}
