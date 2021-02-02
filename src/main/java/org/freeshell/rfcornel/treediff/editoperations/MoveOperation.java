package org.freeshell.rfcornel.treediff.editoperations;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.treediff.EditOperation;
import org.freeshell.rfcornel.treediff.Operations;
import org.freeshell.rfcornel.treediff.editoperations.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 *  This class implements logic required to move a subtree from one _parent to another.
 */
public class MoveOperation<E extends Node<E>> implements EditOperation {
    private final E _nodeToBeMoved;
    private final E _parent;
    private final int _position;
    private  E _oldParent;
    private int _oldPosition;

    public MoveOperation(E nodeToBeMoved, E parent, int position) {
        this._nodeToBeMoved = nodeToBeMoved;
        this._parent = parent;
        this._position = position;
    }

    @Override
    public Operation getOperation() {
        return Operation.Move;
    }

    @Override
    public void apply() {
        E oldParent = _nodeToBeMoved.getParent().get();
        int oldPosition = Operations.childPosition(oldParent, _nodeToBeMoved);
        if (oldPosition == -1) {
            throw new IllegalStateException("Could not find child " + _nodeToBeMoved + "  in _parent: " + oldParent);
        }

        _oldParent = oldParent;
        _oldPosition = oldPosition;
        oldParent.getChildren().get().remove(oldPosition);
        _nodeToBeMoved.setParent(_parent);

        if (_parent.getChildren().isPresent()) {
            List<E> children = _parent.getChildren().get();
            children.add(_position, _nodeToBeMoved);
        } else {
            List<E> children = new ArrayList<>();
            children.add(_position, _nodeToBeMoved);
            _parent.setChildren(children);
        }
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visit(this, _nodeToBeMoved, _oldParent, _oldPosition, _parent, _position);
    }

    @Override
    public String toString() {
        return new StringBuffer("[MOVE ")
                .append(_nodeToBeMoved.toString())
                .append(" UNDER ")
                .append(_parent.toString())
                .append("[")
                .append(_position)
                .append("]]").toString();
    }
}
