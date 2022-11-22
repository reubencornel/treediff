package org.freeshell.rfcornel.treediff.editoperations;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.treediff.EditOperation;
import org.freeshell.rfcornel.treediff.Operations;
import org.freeshell.rfcornel.treediff.editoperations.visitor.Visitor;

import java.util.Optional;

/**
 * Implements the delete operation
 *
 * @author reuben
 */
public class DeleteOperation<E extends Node<E>> implements EditOperation {
    private final Node _nodeToBeDeleted;
    private String parentLabel;
    private int parentIdentityHash;
    private final Optional<Node<E>> _parent;

    public DeleteOperation(Node nodeToBeDeleted) {
        this._nodeToBeDeleted = nodeToBeDeleted;
        this._parent = nodeToBeDeleted.getParent();
    }

    @Override
    public Operation getOperation() {
        return Operation.Delete;
    }

    @Override
    public void apply() {
        E parent = (E) _nodeToBeDeleted.getParent().get();
        parentLabel = parent.getLabel();
        parentIdentityHash = System.identityHashCode(parent);
        int position = Operations.childPosition(parent, _nodeToBeDeleted);
        parent.getChildren().get().remove(position);
        // Remove the child from under the parent, but keep a reference to the parent in case we need to use it further.
//        _nodeToBeDeleted.setParent(null);
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visit(this, this._nodeToBeDeleted);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        return buffer.append("[DELETE ")
                .append(_nodeToBeDeleted.toString())
                .append(" FROM UNDER ")
                .append(parentLabel)
                .append("@")
                .append(parentIdentityHash)
                .append("]").toString();
    }
}
