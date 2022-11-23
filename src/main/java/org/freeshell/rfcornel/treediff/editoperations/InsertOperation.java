package org.freeshell.rfcornel.treediff.editoperations;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.treediff.EditOperation;
import org.freeshell.rfcornel.treediff.editoperations.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reuben on 11/20/17.
 */
public class InsertOperation<E extends Node<E>> implements EditOperation {
    private final Node _newNode;
    private final Node _parent;
    private final int position;

    public InsertOperation(Node newNode, Node parent, int position) {
        this._newNode = newNode;
        this._parent = parent;
        this.position = position;
    }

    @Override
    public Operation getOperation() {
        return Operation.Insert;
    }

    @Override
    public void apply() {
        if (!_parent.getChildren().isEmpty()) {
            List<Node> nodes = (List<Node>) _parent.getChildren();
            nodes.add(position, _newNode);
        } else {
            List<Node> nodes = new ArrayList<>();
            nodes.add(_newNode);
            _parent.setChildren(nodes);
        }
        _newNode.setParent(_parent);
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visit(this,_newNode, _parent, position);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        return buffer.append("[INSERT ")
                .append(_newNode.toString())
                .append(" UNDER " )
                .append(_parent.toString())
                .append(" AT POSITION ")
                .append(position)
                .append("]")
                .toString();
    }
}
