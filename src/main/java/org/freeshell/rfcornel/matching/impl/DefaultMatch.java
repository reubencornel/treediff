package org.freeshell.rfcornel.matching.impl;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.datastructure.impl.PostOrderTraversalIterator;
import org.freeshell.rfcornel.matching.MatchingCriteria;
import org.freeshell.rfcornel.matching.MatchingEngine;
import org.freeshell.rfcornel.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class implements a simple matching.
 *
 * @author reuben 
 */
public class DefaultMatch<E extends Node> implements MatchingEngine<E> {
    @Override
    public Collection<Pair<E, E>> calculateMatching(E root1, E root2, MatchingCriteria<E> criteria) {
        List<Pair<E, E>> matches = new ArrayList<>();
        PostOrderTraversalIterator<E> iterator1 = new PostOrderTraversalIterator<>(root1);

        while(iterator1.hasNext()) {
            E x = iterator1.next();
            PostOrderTraversalIterator<E> iterator2 = new PostOrderTraversalIterator<>(root2);
            while(iterator2.hasNext()) {
                E y = iterator2.next();
                if (!y.isMatched() && criteria.matches(x, y, matches)) {
                    matches.add(Pair.of(x, y));
                    x.setMatched(true);
                    y.setMatched(true);
                    break;
                }
            }
        }
        return matches;
    }

}
