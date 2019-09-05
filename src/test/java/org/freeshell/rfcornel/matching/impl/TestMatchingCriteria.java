package org.freeshell.rfcornel.matching.impl;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.matching.MatchingCriteria;
import org.freeshell.rfcornel.util.Pair;
import org.freeshell.rfcornel.util.TestTreeNode;

import java.util.List;
import java.util.Optional;

/**
 * Created by reuben on 8/5/17.
 */

public class TestMatchingCriteria implements MatchingCriteria<TestTreeNode> {

    @Override
    public boolean matches(TestTreeNode node1, TestTreeNode node2, List<Pair<TestTreeNode, TestTreeNode>> matching) {
        // Leaf Node so just match labels
        if (!node1.getChildren().isPresent() && !node2.getChildren().isPresent()) {
            Optional<String> node1Label = node1.getLabel();
            Optional<String> node2Label = node2.getLabel();
            return (node1Label.isPresent() && node2Label.isPresent() && node1Label.get().equals(node2Label.get()));
        } else {
            // We need to find the set of children in the matching such that if (w,z) are a tuple in the matching
            // w is a child of x and z is a child of y
            int commonPairs = 0;
            Optional<String> node1Label = node1.getLabel();
            Optional<String> node2Label = node2.getLabel();
            if (node1Label.isPresent() && node2Label.isPresent() &&
                    !node1Label.get().equals(node2Label.get()))
                return false;
            for (Pair<TestTreeNode, TestTreeNode> matchingPair : matching) {
               if (isDescendentOf(matchingPair.first, node1) &&
                       isDescendentOf(matchingPair.second, node2)) {
                   commonPairs++;
               }
            }

            int childrenOfNode1 = numberOfLeaves(node1);
            int childrenOfNode2 = numberOfLeaves(node2);
            float result =  ((float)commonPairs / (float)Math.max(childrenOfNode1, childrenOfNode2));
            return result >= .5;
        }
    }

    private int numberOfLeaves(TestTreeNode root) {
        Optional<List<TestTreeNode>> children = root.getChildren();

        if (root.getChildren().isPresent()) {
            int numberOfChildren = 0;
            for (Node child : children.get()) {
                numberOfChildren += numberOfLeaves((TestTreeNode)child);
            }
            return numberOfChildren;
        } else {
            return 1;
        }
    }

    private boolean isDescendentOf(TestTreeNode child, TestTreeNode parent) {
        TestTreeNode node = child;
        if (child == parent) {
            return true;
        }

        do {
            node = (TestTreeNode)node.getParent().get();
            if (node == parent) {
                return true;
            }
        } while (node.getParent().isPresent());

        return false;
    }
}
