package org.freeshell.rfcornel.treediff;

import org.freeshell.rfcornel.datastructure.Node;
import org.freeshell.rfcornel.treediff.editoperations.DeleteOperation;
import org.freeshell.rfcornel.treediff.editoperations.InsertOperation;
import org.freeshell.rfcornel.treediff.editoperations.MoveOperation;
import org.freeshell.rfcornel.treediff.editoperations.UpdateValueOperation;

import java.util.List;


/**
 * Created by reuben on 11/20/17.
 */
public class Operations{
    public static InsertOperation insert(Node newNode, Node parent, int position) {
        return new InsertOperation(newNode, parent, position);
    }

    public static UpdateValueOperation updateValueOperation(Node oldNode, Node newNode) {
        return new UpdateValueOperation(oldNode, newNode);
    }

    public static EditOperation move(Node nodeToBeMoved, Node parent, int position) {
        return new MoveOperation(nodeToBeMoved, parent, position);
    }

    public static EditOperation delete(Node nodeToBeDeleted) {
        return new DeleteOperation(nodeToBeDeleted);
    }

     public static  <E extends Node> int childPosition(Node parent, Node childToFind) {
        int position = 0;
        for (E child : (List<E>) parent.getChildren()) {
            if(child == childToFind) {
                return position;
            }
            position++;
        }
        return -1;
    }
}
