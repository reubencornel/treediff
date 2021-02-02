package org.freeshell.rfcornel.datastructure.impl;

import com.google.common.collect.ImmutableList;
import org.freeshell.rfcornel.datastructure.Node;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests to validate post order traversal
 */
public class PostOrderTraversalIteratorTest {

    @Test
    public void testTraversalWithOneNode() {
        Node mockedNode = Mockito.mock(Node.class);
        Mockito.when(mockedNode.getChildren()).thenReturn(Optional.empty());
        Mockito.when(mockedNode.getParent()).thenReturn(Optional.empty());

        PostOrderTraversalIterator iterator = new PostOrderTraversalIterator(mockedNode);
        assertThat(iterator.hasNext(), equalTo(true));
        assertThat(iterator.next(), equalTo(mockedNode));
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void testTraversalWithOneRootAndOneChild() {
        Node childNode = Mockito.mock(Node.class);
        Mockito.when(childNode.getChildren()).thenReturn(Optional.empty());

        Node rootNode = Mockito.mock(Node.class);
        Mockito.when(rootNode.getChildren()).thenReturn(Optional.of(ImmutableList.of(childNode)));
        Mockito.when(rootNode.getParent()).thenReturn(Optional.empty());

        Mockito.when(childNode.getParent()).thenReturn(Optional.of(rootNode));


        PostOrderTraversalIterator iterator = new PostOrderTraversalIterator(rootNode);
        assertThat(iterator.hasNext(), equalTo(true));
        Node nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == childNode);

        assertThat(iterator.hasNext(), equalTo(true));
        nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == rootNode);
        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void testTraversalWithOneRootAndTwoChildren() {
        Node childNode = Mockito.mock(Node.class);
        Mockito.when(childNode.getChildren()).thenReturn(Optional.empty());

        Node childNode1 = Mockito.mock(Node.class);
        Mockito.when(childNode.getChildren()).thenReturn(Optional.empty());

        Node rootNode = Mockito.mock(Node.class);
        Mockito.when(rootNode.getChildren()).thenReturn(Optional.of(ImmutableList.of(childNode, childNode1)));
        Mockito.when(rootNode.getParent()).thenReturn(Optional.empty());

        Mockito.when(childNode.getParent()).thenReturn(Optional.of(rootNode));
        Mockito.when(childNode.getParent()).thenReturn(Optional.of(rootNode));


        PostOrderTraversalIterator iterator = new PostOrderTraversalIterator(rootNode);
        assertThat(iterator.hasNext(), equalTo(true));
        Node nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == childNode);

        assertThat(iterator.hasNext(), equalTo(true));
        nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == childNode1);

        assertThat(iterator.hasNext(), equalTo(true));
        nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == rootNode);

        assertThat(iterator.hasNext(), equalTo(false));
    }

    @Test
    public void moreComplicatedCase(){
        Node childNode = Mockito.mock(Node.class);

        Node childNode1 = Mockito.mock(Node.class);
        Mockito.when(childNode.getChildren()).thenReturn(Optional.empty());

        Node childNode2 = Mockito.mock(Node.class);
        Mockito.when(childNode2.getParent()).thenReturn(Optional.of(childNode));
        Mockito.when(childNode2.getChildren()).thenReturn(Optional.empty());
        Mockito.when(childNode.getChildren()).thenReturn(Optional.of(ImmutableList.of(childNode2)));


        Node rootNode = Mockito.mock(Node.class);
        Mockito.when(rootNode.getChildren()).thenReturn(Optional.of(ImmutableList.of(childNode, childNode1)));
        Mockito.when(rootNode.getParent()).thenReturn(Optional.empty());

        Mockito.when(childNode.getParent()).thenReturn(Optional.of(rootNode));
        Mockito.when(childNode.getParent()).thenReturn(Optional.of(rootNode));

        PostOrderTraversalIterator iterator = new PostOrderTraversalIterator(rootNode);
        assertThat(iterator.hasNext(), equalTo(true));
        Node nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == childNode2);

        assertThat(iterator.hasNext(), equalTo(true));
        nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == childNode);

        assertThat(iterator.hasNext(), equalTo(true));
        nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == childNode1);

        assertThat(iterator.hasNext(), equalTo(true));
        nextNode = iterator.next();
        assertThat("next Node did not match expected Node: " + nextNode, nextNode == rootNode);

        assertThat(iterator.hasNext(), equalTo(false));

    }
}
