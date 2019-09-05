package org.freeshell.rfcornel.treediff.impl;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.datastructure.impl.BreadthFirstTraversalIterator;
import org.freeshell.rfcornel.datastructure.impl.PostOrderTraversalIterator;
import org.freeshell.rfcornel.lcs.LCSAlgorithm;
import org.freeshell.rfcornel.treediff.EditOperation;
import org.freeshell.rfcornel.treediff.Operations;
import org.freeshell.rfcornel.treediff.TreeDiffAlgorithm;
import org.freeshell.rfcornel.util.Pair;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * This implementation is based on Change Detection in Hierarchically Structured Information
 *
 * @author reuben
 */
public class EditScriptAlgorithm<E extends Node<E>> implements TreeDiffAlgorithm<E> {
    private final Collection<Pair<E, E>> _matching;
    private final Map<E, E> _T2ToT1;
    private final Map<E, E> _T1ToT2;
    private final LCSAlgorithm<E> _lcsAlgorithm;
    private List<EditOperation> _operations;

    public EditScriptAlgorithm(Collection<Pair<E, E>> matching, LCSAlgorithm<E> algorithm) {
        this._matching = matching;
        this._lcsAlgorithm = algorithm;
        _T2ToT1 = matching.stream().collect(Collectors.toMap(x -> x.second, x -> x.first));
        _T1ToT2 = matching.stream().collect(Collectors.toMap(x -> x.first, x -> x.second));
        this._operations = new ArrayList<>();
    }

    @Override
    public List<EditOperation> calculateEditScript(E root1, E root2) {
        BreadthFirstTraversalIterator<E> breadthFirstTraversalIterator = new BreadthFirstTraversalIterator<>(root2);
        while(breadthFirstTraversalIterator.hasNext()) {
            //Let x be the current node in the breadth  rst search of T2
            E x = breadthFirstTraversalIterator.next();
            // Let w bet the partner of x in T1
            Optional<E> w = Optional.ofNullable(_T2ToT1.get(x));
            //let y =  p(x)
            Optional<E> y = x.getParent();
            //Let z be the partner of y in M
            Optional<E> z = y.map(parentNode -> _T2ToT1.get(parentNode));

            if (!w.isPresent()) {
                int k = findPosition(x);
                Node copiedNode = x.copyNode();
                copiedNode.setInOrder(true);
                x.setInOrder(true);
                EditOperation insertOp = Operations.insert(copiedNode, z.get(), k);
                _operations.add(insertOp);
                insertOp.apply();
                addToMatching((E) x, (E) copiedNode);
            } else if (y.isPresent()) { // x is not a root
                //if v(w) != v(x)
                if (!x.valuesEqual(w.get().getValue().get())) {
                    EditOperation operation = Operations.updateValueOperation(w.get(), x);
                    _operations.add(operation);
                    operation.apply();
                }

                E v = (E) w.get().getParent().get();
                // if the parent of node x in t2 is not the the parent of w
                if (_T2ToT1.get(y.get()) != v) {
                    int k = findPosition(x);
                    EditOperation moveOp = Operations.move(w.get(), z.get(), k);
                    _operations.add(moveOp);
                    moveOp.apply();
                    // This is not a part of the algorithm, but I put in here because it tends to give out better results for the cases with which I'm dealing
                    x.setInOrder(true);
                    w.get().setInOrder(true);
                }
            }
            if(w.isPresent()) {
                alignChildren((E)w.get(), (E)x);
            }
        }

        PostOrderTraversalIterator<Node> postOrderTraversalIterator = new PostOrderTraversalIterator<>(root1);
        List<EditOperation> operations = new ArrayList<>();
        while (postOrderTraversalIterator.hasNext()) {
            Node w = postOrderTraversalIterator.next();
            if (!_T1ToT2.containsKey(w)) {
                EditOperation delOP = Operations.delete(w);
                operations.add(delOP);
                _operations.add(delOP);
            }
        }

        operations.stream().forEach(op -> op.apply());

        return _operations;
    }

    private void addToMatching(E x, E copiedNode) {
        _matching.add(Pair.of(copiedNode, x));
        _T2ToT1.put(x, copiedNode);
        _T1ToT2.put(copiedNode, x);
    }

    void alignChildren(E w, E x){
        //Mark all children of w and all children of x  out of order
        markAllChildrenOutOfOrder(w);
        markAllChildrenOutOfOrder(x);

        List<E> s1 = (List<E>) w.getChildren()
                .flatMap(value -> Optional.of(value.stream().filter(y -> _T1ToT2.containsKey(y) && (_T1ToT2.get(y).getParent().get() == x))
                    .collect(Collectors.toList())))
                .orElse(new ArrayList());

        List<E> s2 = (List<E>) x.getChildren()
                .flatMap(value -> Optional.of(value.stream().filter(y -> _T2ToT1.containsKey(y) && (_T2ToT1.get(y).getParent().get() == w))
                        .collect(Collectors.toList())))
                .orElse(new ArrayList());

        BiPredicate<E, E> lcsPredicate = (node, node2) -> _T1ToT2.containsKey(node) && _T1ToT2.get(node) == node2;
        List<Pair<E, E>> s = this._lcsAlgorithm.getLCS(s1, s2, lcsPredicate);
        Map<Node, Node> lcsNodeMap = s.stream().collect(Collectors.toMap(z -> z.first,
                z -> z.second));

        for (Pair<E, E> inOrderNodes : s) {
            inOrderNodes.first.setInOrder(true);
            inOrderNodes.second.setInOrder(true);
        }

        //   For each tuple in the match that was not present in S
        //       We call find pos on the second sibling
        //       Validate we append and apply Mov to tree1
        //       Validate we mark a and b in order
        for (E a : s1) {
            for (E b : s2) {
                // (a, b) belongs to M but (a, b) does not belong to S
                if (_T1ToT2.get(a) == b &&
                        (!lcsNodeMap.containsKey(a) ||
                        lcsNodeMap.get(a) != b)) {
                    int pos = findPosition(b);
                    EditOperation moveOperation = Operations.move(a, w, pos);
                    this._operations.add(moveOperation);
                    a.setInOrder(true);
                    b.setInOrder(true);
                    moveOperation.apply();
                }
            }
        }
    }

    void markAllChildrenOutOfOrder(E x) {
        x.getChildren()
                .ifPresent(children -> children
                                    .stream()
                                    .forEach(child -> child.setInOrder(false)));
    }

    int findPosition(E nodeInTree2) {
        Optional<E> parent = nodeInTree2.getParent();

        if (!parent.isPresent()) {
            throw new IllegalStateException("Expected to find a parent for node: " + nodeInTree2);
        }
        return findPosition(nodeInTree2, parent.get());
    }

    int findPosition(E nodeInTree2, E parent) {
        // if x is the left most child of its parent that is marked
        // "in order" return 1 (0, off by one)
        List<E> children = (List<E>) parent.getChildren().get();
        int rightMostSiblingOfNodeMarkedInOrder = findRightMostSiblingOfNodeMarkedInOrder(nodeInTree2, children);

        if (rightMostSiblingOfNodeMarkedInOrder == -1) {
            return 0;
        }

        E v = children.get(rightMostSiblingOfNodeMarkedInOrder);
        E u = _T2ToT1.get(v);

        return findIndexOf(u) + 1;
    }

    private int findIndexOf(E u) {
        assert(u.getParent().isPresent());

        List<E> siblings = u.getParent().get().getChildren().get();
        int i = 0;
        while (siblings.get(i)  != u) {
            i++;
        }
        return i;
    }

    int findRightMostSiblingOfNodeMarkedInOrder(E nodeInTree2, List<E> children) {
        int index = -1;
        int i;
        for (i = 0; children.get(i) != nodeInTree2; i++) {
            E currentNode = children.get(i);
            if (currentNode.inOrder()) {
                index = i;
            }
        }

        return index;
    }
}
