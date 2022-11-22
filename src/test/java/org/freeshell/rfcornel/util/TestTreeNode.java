package org.freeshell.rfcornel.util;

import org.freeshell.rfcornel.datastructure.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents a node in a tree used for testing. I use this because I can write
 * my test tree representations in json, de-serialize them and run tests with them.
 *
 * Beware when using this in test classes, I keep track of the all the nodes in a static map.
 * Make sure you use unique labels in different tests so that there are no clashes. I use the static
 * map so that I can identify the parent node when jackson tries to deserialize the value to it.
 *
 * The @JsonIdentifyInfoNode below defines what property of the object is the id. This lets me
 * assign the right parents to the right children.
 *
 * @author reuben
 */
public class TestTreeNode implements Node<TestTreeNode> {
    private String _label;
    private List<TestTreeNode> _children;
    private TestTreeNode _parent;
    private boolean _matched = false;
    private boolean _inOrder;
    private Map<String, Object> _value;

    public TestTreeNode() {
    }

    @Override
    public String getLabel() {
        return _label;
    }

    public void setLabel(String label) {
        this._label = label;
    }

    @Override
    public Optional<TestTreeNode> getParent() {
        return Optional.ofNullable(_parent);
    }


    public void setParent(TestTreeNode parent) {
        this._parent = parent;
    }

    @Override
    public Optional<List<TestTreeNode>> getChildren() {
        return Optional.ofNullable(_children);
    }

    @Override
    public void setChildren(List<TestTreeNode> children) {
        if (children == null) {
            return ;
        }
        this._children = children.stream().collect(Collectors.toList());
    }

    public void addChild(TestTreeNode child) {
        if (this._children == null) {
            this._children = new ArrayList<>();
        }
        this._children.add(child);
        child.setParent(this);
    }

    @Override
    public boolean inOrder() {
        return this._inOrder;
    }

    @Override
    public void setInOrder(boolean inOrder) {
        this._inOrder = inOrder;
    }

    @Override
    public boolean isMatched() {
        return _matched;
    }

    @Override
    public void setMatched(boolean matched) {
        this._matched = matched;
    }

    @Override
    public void updateValue(TestTreeNode newNode) {
        // For now this is a test Node.
        // ideally we should do a deep copy. But this is test code
      newNode.getValue().ifPresent(x -> this._value = x);
    }

    @Override
    public Optional<Map<String, Object>> getValue() {
        return Optional.of(_value);
    }

    @Override
    public Node copyNode() {
        TestTreeNode newNode = new TestTreeNode();
        newNode.setLabel(this.getLabel());
        return newNode;
    }

    @Override
    public void setValue(Map<String, Object> value) {
        this._value = value;
    }

    @Override
    public boolean valuesEqual(Map<String, Object> value) {
        if (value == null && _value == value) {
            return true;
        }

        for (String key : _value.keySet()) {
            if (value.get(key) == null ||
                    !value.get(key).equals(_value.get(key))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Optional<String> getOldValueString() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "[" + getLabel() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof TestTreeNode)) {
            return false;
        }

        TestTreeNode castedObj = (TestTreeNode)obj;
        return (this.getLabel() != null && this.getLabel().equals(castedObj.getLabel()))
                || (this.getLabel() == null && castedObj.getLabel() == null);
    }
}
