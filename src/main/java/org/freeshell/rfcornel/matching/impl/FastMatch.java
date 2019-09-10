package org.freeshell.rfcornel.matching.impl;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.datastructure.impl.PostOrderTraversalIterator;
import org.freeshell.rfcornel.lcs.LCSAlgorithm;
import org.freeshell.rfcornel.lcs.impl.SimpleLCSAlgorithm;
import org.freeshell.rfcornel.matching.MatchingCriteria;
import org.freeshell.rfcornel.matching.MatchingEngine;
import org.freeshell.rfcornel.util.Pair;

import java.util.*;

public class FastMatch<E extends Node> implements MatchingEngine<E> {

    @Override
    public Collection<Pair<E, E>> calculateMatching(E root1, E root2, MatchingCriteria<E> criteria) {
        Map<String, List<E>> chain1 = chain(root1);
        Map<String, List<E>> chain2 = chain(root2);

        List<Pair<E, E>> matching = new ArrayList<>();
        LCSAlgorithm<E> algorithm = new SimpleLCSAlgorithm<>();

        chain1.forEach((label, list) -> {
            List<E> list2 = chain2.get(label);

            if (list2 != null) {
                List<Pair<E, E>> lcs = algorithm.getLCS(list, list2, (x, y) -> criteria.matches(x, y, matching));
                matching.addAll(lcs);

            }
        });

        return matching;
    }

    private LinkedHashMap<String, List<E>> chain(E root1) {
        LinkedHashMap<String, List<E>> returnMap = new LinkedHashMap<>();
        PostOrderTraversalIterator<E> iterator = new PostOrderTraversalIterator<>(root1);
        while(iterator.hasNext()) {
            E next = iterator.next();
            if (returnMap.containsKey((String)next.getLabel().get())) {
                returnMap.get(next.getLabel().get()).add(next);
            } else {
                List<E> newList =  new ArrayList<>();
                newList.add(next);
                returnMap.put((String)next.getLabel().get(), newList);
            }
        }

        return returnMap;
    }
}
