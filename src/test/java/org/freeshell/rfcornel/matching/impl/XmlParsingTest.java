package org.freeshell.rfcornel.matching.impl;

import org.freeshell.rfcornel.util.TestTreeNode;
import org.freeshell.rfcornel.util.TestingUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class XmlParsingTest {
    // Test to validate we can deserialize xml correctly into the test tree node
    // we care about.
    @Test
    public void simpleXmlParsingTest() throws ParserConfigurationException, SAXException, IOException {
        String simpleInput = "<test></test>";
        TestTreeNode root = TestingUtils.parseXmlTree(simpleInput);

        assertThat(root, notNullValue());
        assertThat(root.getLabel(), is("test"));
        assertThat(root.getChildren().isPresent(), is(false));
    }

    @Test
    public void treeXmlParsingTest() throws ParserConfigurationException, SAXException, IOException {
        String treeInput = "<parent>" +
                "<child1></child1>" +
                "<child2></child2>" +
                "</parent>";
        TestTreeNode root = TestingUtils.parseXmlTree(treeInput);

        assertThat(root, notNullValue());
        assertThat(root.getChildren().isPresent(), is(true));
        assertThat(root.getChildren().get().size(), is(2));
    }

    @Test
    public void xmlParsingWithAttributes () throws IOException, SAXException, ParserConfigurationException {
        String treeInput = "<parent>" +
                "<child1 name='reuben'></child1>" +
                "<child2></child2>" +
                "</parent>";
        TestTreeNode root = TestingUtils.parseXmlTree(treeInput);

        assertThat(root, notNullValue());
        assertThat(root.getChildren().isPresent(), is(true));
        assertThat(root.getChildren().get().size(), is(2));
        assertThat(root.getChildren().get().get(0).getValue().get().keySet().contains("name"), is(true));
    }

}
