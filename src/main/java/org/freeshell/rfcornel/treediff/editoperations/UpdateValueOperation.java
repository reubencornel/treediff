package org.freeshell.rfcornel.treediff.editoperations;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.treediff.EditOperation;
import org.freeshell.rfcornel.treediff.editoperations.visitor.Visitor;

/**
 * This class implements an operation that lets an old node update its value with the value of the node passed in.
 *
 * @author reuben
 */
public class UpdateValueOperation<E extends Node> implements EditOperation{

    private final E _oldValue;
    private final E _newValue;
    private Node _originalValue;

    public UpdateValueOperation(E oldValue, E newValue) {
        this._oldValue = oldValue;
        this._newValue = newValue;
    }

    @Override
    public Operation getOperation() {
        return Operation.Update;
    }

    @Override
    public void apply() {
        _originalValue = _oldValue.copyNode();
        _oldValue.updateValue(_newValue);
    }

    @Override
    public void visit(Visitor visitor) {
        visitor.visit(this, _oldValue, _originalValue, _newValue);
    }

    @Override
    public String toString() {
        return new StringBuffer()
                .append("[UPDATE ")
                .append(this._oldValue.toString())
                .append("<old value=")
                .append(this._oldValue.getOldValueString().orElse(""))
                .append(">")
                .append(" TO ")
                .append(this._newValue.toString())
                .append("]").toString();
    }
}
