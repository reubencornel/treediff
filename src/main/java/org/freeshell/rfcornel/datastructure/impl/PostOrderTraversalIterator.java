package org.freeshell.rfcornel.datastructure.impl;

import com.google.common.collect.ImmutableList;
import org.freeshell.rfcornel.datastructure.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Created by rcornel on 6/16/17.
 */
public class PostOrderTraversalIterator<E extends Node> implements Iterator{

    private final E _root;
    private boolean _justInitialized;
    private E _currentNode;
    private List<PostTraversalNodeInfo> _postTraversalStack;
    private PostTraversalNodeInfo _currentNodeInfo;

    public PostOrderTraversalIterator(E root) {
        this._root = root;
        _currentNode = root;
        _justInitialized = true;
        _postTraversalStack = new ArrayList<>();
        _postTraversalStack.add(PostTraversalNodeInfo.newPointer(root));
    }

    @Override
    public boolean hasNext() {
        return  (_justInitialized || _currentNode != _root);
    }

    @Override
    public E next() {
        _justInitialized = false;
        _currentNodeInfo = _postTraversalStack.get(_postTraversalStack.size() - 1);

        if (hasUnvisitedChildren()) {
            // traverse down till we hit the first unvisited child.
            visitUnvisitedChildren();
        } else {
            visitParent();
        }

        return _currentNode;
    }

    private boolean hasUnvisitedChildren() {
        return _currentNodeInfo.lastVisitedChild < _currentNodeInfo.numberOfChildren;
    }

    private void visitParent() {
        if (_postTraversalStack.size() -1  <= 0) {
            // We are at the root element
            _currentNodeInfo = _postTraversalStack.get(0);
            _currentNode = _root;
        } else {
            _postTraversalStack.remove(_postTraversalStack.size() - 1);
            _currentNodeInfo = _postTraversalStack.get(_postTraversalStack.size() - 1);
            _currentNode = (E) _currentNode.getParent().orElse(_root);
        }
        if (hasUnvisitedChildren()) {
            visitUnvisitedChildren();
        }
    }

    private void visitUnvisitedChildren() {
        while (hasChildren(_currentNode) && hasUnvisitedChildren()) {
            E nextNode =(E) getChildren(_currentNode).get(_currentNodeInfo.lastVisitedChild);
            _currentNodeInfo.lastVisitedChild++;
            _currentNodeInfo = PostTraversalNodeInfo.newPointer(nextNode);
            _postTraversalStack.add(_currentNodeInfo);
            _currentNode = nextNode;
        }
    }

    private boolean hasChildren(E currentNode) {
        return getChildren(currentNode).size() > 0;
    }

    private static <E extends Node> List<E> getChildren(E currentNode) {
        return ((Optional<List<E>>)currentNode.getChildren()).orElse(ImmutableList.of());
    }

    static class PostTraversalNodeInfo {
        public Integer numberOfChildren;
        public Integer lastVisitedChild;

        private PostTraversalNodeInfo(){

        }

        public static <T, E extends Node>PostTraversalNodeInfo newPointer(E node) {
            PostTraversalNodeInfo p = new PostTraversalNodeInfo();
            p.lastVisitedChild = 0;
            p.numberOfChildren = getChildren(node).size();
            return p;
        }
    }
}
