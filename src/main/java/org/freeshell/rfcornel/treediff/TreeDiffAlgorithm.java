package org.freeshell.rfcornel.treediff;

import org.freeshell.rfcornel.datastructure.Node;

import java.util.List;

/**
 * This interface represents the algorithm for diffing trees. The implementation is based on
 * Change Detection in Hierarchically Structured Information - Chawathe et al.
 *
 * @author reuben
 */
public interface TreeDiffAlgorithm<E extends Node> {
    List<EditOperation> calculateEditScript(E root1, E root2);
}
