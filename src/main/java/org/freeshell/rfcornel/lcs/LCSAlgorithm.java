package org.freeshell.rfcornel.lcs;

import org.freeshell.rfcornel.util.Pair;

import java.util.List;
import java.util.function.BiPredicate;

public interface LCSAlgorithm<T> {
	LCSResult findLCS(List<T> seq1, List<T> seq2, BiPredicate<T, T> equalityPredicate);
	List<Pair<T, T>> interpretLCS(List<T> seq1, List<T> seq2, LCSResult result);

	default List<Pair<T, T>> getLCS(List<T> seq1, List<T> seq2, BiPredicate<T, T> equalityPredicate) {
		LCSResult result = findLCS(seq1, seq2, equalityPredicate);
		return interpretLCS(seq1, seq2, result);
	}
}
