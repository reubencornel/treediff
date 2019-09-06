package org.freeshell.rfcornel.lcs.impl;

import org.freeshell.rfcornel.lcs.LCSAlgorithm;
import org.freeshell.rfcornel.lcs.LCSCellResult;
import org.freeshell.rfcornel.lcs.LCSDirection;
import org.freeshell.rfcornel.lcs.LCSResult;
import org.freeshell.rfcornel.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class SimpleLCSAlgorithm<T> implements LCSAlgorithm<T> {

	private static final LCSCellResult EMPTY_CELL = new LCSCellResult(LCSDirection.NORTHWEST, 0);

	@Override
	public LCSResult findLCS(List<T> seq1, List<T> seq2, BiPredicate<T, T> equalityPredicate) {
		LCSResult returnResult = new ShortBasedLCSResult(seq1.size() - 1 , seq2.size() - 1);
		for (int i = 0; i < seq2.size(); i++) {
			for (int j = 0; j < seq1.size(); j++) {
				if (equalityPredicate.test(seq1.get(j), seq2.get(i))) {
					LCSCellResult prevResult = returnResult
							.getCellResult(i - 1, j - 1)
							.orElse(EMPTY_CELL);

					returnResult.setCellResult(i, j, LCSDirection.NORTHWEST, prevResult.cost + 1);
				} else {
					LCSCellResult westCell = returnResult.getCellResult(i, j - 1).orElse(EMPTY_CELL);
					LCSCellResult northCell = returnResult.getCellResult(i - 1, j).orElse(EMPTY_CELL);

					if (northCell.cost > westCell.cost) {
						returnResult.setCellResult(i, j, LCSDirection.NORTH, northCell.cost);
					} else {
						returnResult.setCellResult(i, j, LCSDirection.WEST, westCell.cost);
					}
				}
			}
		}
		return returnResult;
	}

	@Override
	public List<Pair<T, T>> interpretLCS(List<T> seq1, List<T> seq2, LCSResult result) {
		int i = seq2.size() - 1;
		int j = seq1.size() - 1;
		List<Pair<T, T>> returnList = new ArrayList<>();

		while (i >= 0 && j >= 0) {
			// Unclear error message clean up
			LCSCellResult cellResult = result.getCellResult(i, j).get();
			switch (cellResult.direction) {
				case WEST:
					j --;
					break;
				case NORTH:
					i--;
					break;
				case NORTHWEST:
					returnList.add(0, Pair.of(seq1.get(j), seq2.get(i)));
					j--;
					i--;
					break;
			}
		}
		return returnList;
	}

}

