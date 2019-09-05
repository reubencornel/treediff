package org.freeshell.rfcornel.datastructure.impl;

import org.freeshell.rfcornel.util.TestTreeNode;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for breadth first search iterator
 */
public class BreadthFirstTraversalIteratorTest {
    @Test
    public void testIteratorWithOnlyRoot() {
        TestTreeNode root = new TestTreeNode();
        BreadthFirstTraversalIterator iterator = new BreadthFirstTraversalIterator(root);
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is(root));
        assertThat(iterator.hasNext(), Matchers.is(false));
    }

    @Test
    public void testIteratorWithTwoChildren() {
        TestTreeNode root = new TestTreeNode();
        TestTreeNode child1 = new TestTreeNode();
        TestTreeNode child2 = new TestTreeNode();
        root.addChild(child1);
        root.addChild(child2);

        BreadthFirstTraversalIterator iterator = new BreadthFirstTraversalIterator(root);
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is(root));
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is(child1));
        assertThat(iterator.hasNext(), Matchers.is(true));
        assertThat(iterator.next(), Matchers.is(child2));
        assertThat(iterator.hasNext(), Matchers.is(false));
    }
}
