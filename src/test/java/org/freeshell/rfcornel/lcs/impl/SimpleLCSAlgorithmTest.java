package org.freeshell.rfcornel.lcs.impl;


import com.google.common.collect.ImmutableList;
import org.freeshell.rfcornel.lcs.LCSAlgorithm;
import org.freeshell.rfcornel.lcs.LCSCellResult;
import org.freeshell.rfcornel.lcs.LCSDirection;
import org.freeshell.rfcornel.lcs.LCSResult;
import org.freeshell.rfcornel.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SimpleLCSAlgorithmTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {
                    ImmutableList.of('c'),
                        ImmutableList.of('c'),
                        ImmutableList.of(Pair.of(0, 0)),
                        ImmutableList.of(getCellResult(LCSDirection.NORTHWEST, 1)),
                        ImmutableList.of(Pair.of('c', 'c'))
                },
                {
                        ImmutableList.of('c', 'c'),
                        ImmutableList.of('c', 'c'),
                        ImmutableList.of(Pair.of(0, 0), Pair.of(1, 1), Pair.of(0, 1)),
                        ImmutableList.of(getCellResult(LCSDirection.NORTHWEST, 1), getCellResult(LCSDirection.NORTHWEST, 2), getCellResult(LCSDirection.NORTHWEST, 1), getCellResult(LCSDirection.NORTHWEST, 1)),
                        ImmutableList.of(Pair.of('c', 'c'), Pair.of('c', 'c'))

                },
                {
                        ImmutableList.of('c', 'd'),
                        ImmutableList.of('c', 'c'),
                        ImmutableList.of(Pair.of(0, 0), Pair.of(0, 1), Pair.of(1, 0), Pair.of(1, 1)),
                        ImmutableList.of(getCellResult(LCSDirection.NORTHWEST, 1), getCellResult(LCSDirection.WEST, 1), getCellResult(LCSDirection.NORTHWEST, 1), getCellResult(LCSDirection.WEST, 1)),
                        ImmutableList.of(Pair.of('c', 'c'))
                }
        });
    }

    @Parameterized.Parameter
    public List<Character> charSeq1;

    @Parameterized.Parameter(1)
    public List<Character> charSeq2;

    @Parameterized.Parameter(2)
    public List<Pair<Integer, Integer>> coords;

    @Parameterized.Parameter(3)
    public List<Optional<LCSCellResult>> expectedValues;

    @Parameterized.Parameter(4)
    public List<Pair<Character, Character>> expectedSeqElements;

    public static BiPredicate<Character, Character> equalityPredicate = (character, character2) -> character.equals(character2);

    @Test
	public void testSimpleLCSResult() {
		SimpleLCSResult result = new SimpleLCSResult(4, 4);
		result.setCellResult(0, 0,LCSDirection.NORTHWEST, 1);
		assertThat(result.getCellResult(0, 0), equalTo(Optional.of(new LCSCellResult(LCSDirection.NORTHWEST, 1))));
		assertEquals(result.getCellResult(1, 1), Optional.empty());
	}
	
	@Test
	public void testLCS(){
		LCSAlgorithm<Character> a = new SimpleLCSAlgorithm<>();
		LCSResult result = a.findLCS(charSeq1, charSeq2, equalityPredicate);

		int i = 0;
		for (Pair<Integer, Integer> pair : coords) {
		    assertThat("Failure count: " + i, result.getCellResult(pair.first, pair.second),
                    equalTo(expectedValues.get(i)));
		    i++;
        }
	}

	@Test
    public void testLCSInterpretation() {
        LCSAlgorithm<Character> a = new SimpleLCSAlgorithm<>();
        LCSResult result = a.findLCS(charSeq1, charSeq2, equalityPredicate);
        List<Pair<Character, Character>> lcs = a.interpretLCS(charSeq1, charSeq2, result);
        assertEquals(expectedSeqElements, lcs);
    }

	private static Optional<LCSCellResult> getCellResult(LCSDirection direction, int cost) {
	    return Optional.of(new LCSCellResult(direction, cost));
    }

}
