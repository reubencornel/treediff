package org.freeshell.rfcornel.matching.impl;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.matching.MatchingCriteria;
import org.freeshell.rfcornel.util.Pair;
import org.freeshell.rfcornel.util.TestTreeNode;

import java.util.List;
import java.util.Optional;

public class SimpleXmlTestMatchingCriteria implements MatchingCriteria<TestTreeNode>  {
    @Override
    public boolean matches(TestTreeNode node1, TestTreeNode node2, List<Pair<TestTreeNode, TestTreeNode>> matching) {
        Optional<String> node1Label = node1.getLabel();
        Optional<String> node2Label = node2.getLabel();
        // Let check the parents all the way up to the top
        Optional<TestTreeNode> parent1 = node1.getParent();
        Optional<TestTreeNode> parent2 = node2.getParent();

        while(parent1.isPresent() && parent2.isPresent()) {
            if (!parent1.get().getLabel().equals(parent2.get().getLabel()))
                return false;
            parent1 = parent1.get().getParent();
            parent2 = parent2.get().getParent();
        }

        if (parent1.isPresent() || parent2.isPresent()) {
            return false;
        }

        return (node1Label.isPresent() && node2Label.isPresent() && node1Label.get().equals(node2Label.get()));    }
}
