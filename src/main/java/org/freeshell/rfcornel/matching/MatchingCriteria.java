package org.freeshell.rfcornel.matching;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.util.Pair;

import java.util.List;

/**
 * This interface defines the behavior of Matching Criteria.
 *
 * @author reuben
 */
public interface MatchingCriteria<E extends Node> {
    boolean matches(E node1, E node2, List<Pair<E, E>> matching);
}
