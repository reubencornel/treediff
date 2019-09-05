package org.freeshell.rfcornel.matching;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.util.Pair;

import java.util.Collection;

/**
 * This interface defines the behavior of MatchingEngines. Matching Engines are bits of code
 * that accept two trees and return a set of pairs of nodes such that the nodes are "matched".
 *
 * @author reuben
 */
public interface MatchingEngine<E extends Node> {
    Collection<Pair<E, E>> calculateMatching(E root1, E root2, MatchingCriteria<E> criteria);
}
