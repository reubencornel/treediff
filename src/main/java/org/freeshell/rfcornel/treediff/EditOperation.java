package org.freeshell.rfcornel.treediff;

import org.freeshell.rfcornel.treediff.editoperations.Operation;
import org.freeshell.rfcornel.treediff.editoperations.visitor.Visitor;

/**
 * This interface represents a single edit operation that had been
 * applied to the old tree so that its converted to the new tree.
 *
 * @author reuben
 */
public interface EditOperation {

    /**
     * Helps identify the type of operation.
     *
     * @return
     */
    Operation getOperation();

    /**
     * Applies the operation to the operand.
     */
    void apply();

    void visit(Visitor visitor);
}
