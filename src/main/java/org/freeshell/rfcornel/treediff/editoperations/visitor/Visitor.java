package org.freeshell.rfcornel.treediff.editoperations.visitor;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.treediff.editoperations.DeleteOperation;
import org.freeshell.rfcornel.treediff.editoperations.InsertOperation;
import org.freeshell.rfcornel.treediff.editoperations.MoveOperation;
import org.freeshell.rfcornel.treediff.editoperations.UpdateValueOperation;

public interface Visitor<E extends Node<E>> {
    void visit(DeleteOperation<E> deleteOperation, E nodeToBeDeleted);
    void visit(InsertOperation<E> insertOperation, E newNode, E Parent, int position);
    void visit(MoveOperation<E> moveOperation, E nodeToBeMoved, E oldParent, int oldPosition, E newParent, int position);
    void visit(UpdateValueOperation updateValueOperation, E nodeToBeUpdated, E oldValue, E newValue);
}
