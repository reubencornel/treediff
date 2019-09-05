package org.freeshell.rfcornel.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Basic sax parser for testing.ul
 *
 * @author reuben
 */
public class SAXHandler extends DefaultHandler {
    private final Stack<TestTreeNode> treeStack = new Stack<>();
    private TestTreeNode lastNodePopped = null;
    private boolean captureText = false;
    private StringBuffer buffer = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        TestTreeNode node = new TestTreeNode();
        node.setLabel(qName);
        Map attributeMap = new HashMap<String, Object>();
        for(int i = 0; i < attributes.getLength(); i++) {
            attributeMap.put(attributes.getQName(i), attributes.getValue(i));
        }
        node.setValue(attributeMap);

        captureText = true;
        buffer = new StringBuffer();
        treeStack.push(node);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(captureText){
            for (int i = start; i < start + length; i++){
                buffer.append(ch[i]);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        captureText = false;

        lastNodePopped = treeStack.pop();
        if (treeStack.size() > 0) {
            treeStack.peek().addChild(lastNodePopped);
        }

        if (buffer.length() > 0) {
            lastNodePopped.getValue().get().put("text", buffer.toString());
            buffer = new StringBuffer();
        }
    }

    public TestTreeNode getRoot() {
        return lastNodePopped;
    }

}
