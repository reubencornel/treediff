package org.freeshell.rfcornel.datastructure.impl;

import org.freeshell.rfcornel.datastructure.Node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * An iterator that traverses a subtree in breadth first fashion.
 *
 * @author reuben
 */
public class BreadthFirstTraversalIterator<E extends Node> implements Iterator {
    private final Queue<E> _queue;

    public BreadthFirstTraversalIterator(E root) {
        _queue = new LinkedList<>();
        _queue.add(root);
    }

    @Override
    public boolean hasNext() {
        return !_queue.isEmpty();
    }

    @Override
    public E next() {
        E currentElement = _queue.poll();
        _queue.addAll(currentElement.getChildren());
        return currentElement;
    }
}
