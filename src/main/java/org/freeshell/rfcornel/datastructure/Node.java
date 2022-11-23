package org.freeshell.rfcornel.datastructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This interface defines the behavior for nodes in a tree. This lets me wrap any type of tree in this datastructure
 * so that I can apply the algorithm generically to it.
 *
 * @author reuben
 */
public interface Node<E extends Node<E>> {
    /**
     * This method returns the label for a given node.
     * @return
     */
    String getLabel();

    /**
     * This method returns the parent of a given node.
     *
     * @return returns empty if the node has no parent in which case this node is the root of the tree
     */
    Optional<E> getParent();

    /**
     * This method lets you set a parent for a node.
     */
    void setParent(E parent);

    /**
     * This method returns all the children of a given node
     *
     * @return returns empty if the node has no children.
     */
    List<E> getChildren();

    /**
     * This method lets you set the children for a node.
     *
     * @param children
     */
    void setChildren(List<E> children);

    /**
     * Returns true if a node is marked "in order"
     */
    boolean inOrder();

    /**
     * Sets the property
     * @param inOrder
     */
    void setInOrder(boolean inOrder);

    /**
     * @return true iff this node is matched with a node in tree 2
     */
    boolean isMatched();

    /**
     * Set the matched property to indicate that this node has been matched with a node in the other tree.
     * @param matched
     */
    void setMatched(boolean matched);

    /**
     * This method lets a node update its value to that of another node passed into it.
     */
    void updateValue(E newNode);


    /**
     * This method lets you retrieve the "value" of a node. In case of xml it could
     * be the attributes of the node
     * @return
     */
    Optional<Map<String, Object>> getValue();

    /**
     * This requires nodes to implement a method that helps create a new node with the same contents.
     * It is required when we try to create a new node with the same content to insert when we dont find a node
     * in T1
     *
     * @return new instance of the copied node.
     */
    Node<E> copyNode();

    /**
     * This method lets you set a value for a node.
     * @param value
     */
    void setValue(Map<String, Object> value);

    /**
     * This method helps identify if a value passed into a node is equal to its value.
     *
     * @return true iff the values are equal
     */
    boolean valuesEqual(Map<String, Object> value);

    /**
     * this method should return the old value only if this node has been updated. else it returns Optional.empty()
     *
     * @return
     */
    Optional<String> getOldValueString();
}
