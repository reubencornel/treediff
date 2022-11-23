package org.freeshell.rfcornel.treediff.editoperations;

import org.freeshell.rfcornel.treediff.EditOperation;
import org.freeshell.rfcornel.treediff.Operations;
import org.freeshell.rfcornel.util.TestTreeNode;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.freeshell.rfcornel.util.TestingUtils.parseXmlTree;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by reuben on 11/20/17.
 */
public class InsertOperationTest {
    @Test
    public void testInsert() throws IOException, SAXException, ParserConfigurationException {
        // When we insert a new node.
        // we have to validate that the parent and the child nodes have been modified correctly.
        String tree1 = "<parent>" +
                "<child></child>"+     //--> should be in order
                "<child3></child3>"+
                "<child1></child1>"+   //--> should be in order
                "<child2></child2>"+
                "</parent>";
        String  node = "<child4></child4>";
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode newNode = parseXmlTree(node);
        EditOperation operation = Operations.insert(newNode, root1, 2);
        operation.apply();

        assertThat(newNode.getParent().get(), Matchers.is(root1));
        assertThat(root1.getChildren().get(2), Matchers.is(newNode));
    }


    @Test
    public void testInsertWithNoChildren() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent></parent>";
        String  node = "<child4></child4>";
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode newNode = parseXmlTree(node);
        EditOperation operation = Operations.insert(newNode, root1, 2);
        operation.apply();
        assertThat(newNode.getParent().get(), Matchers.is(root1));
        assertThat(root1.getChildren().get(0), Matchers.is(newNode));
    }
}
