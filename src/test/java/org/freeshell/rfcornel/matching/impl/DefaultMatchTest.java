package org.freeshell.rfcornel.matching.impl;

import com.google.common.collect.ImmutableSet;
import org.freeshell.rfcornel.util.Pair;
import org.freeshell.rfcornel.util.TestTreeNode;
import org.freeshell.rfcornel.util.TestingUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests to validate the default matching algorithm.
 *
 * @author reuben
 */
public class DefaultMatchTest {
    // Test to validate we can deserialize xml correctly into the test tree node
    // we care about.
    @Test
    public void simpleXmlParsingTest() throws ParserConfigurationException, SAXException, IOException {
        String simpleInput = "<test></test>";
        TestTreeNode root = TestingUtils.parseXmlTree(simpleInput);

        assertThat(root, notNullValue());
        assertThat(root.getLabel().get(), is("test"));
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


    // Write a test to validate matching tree with one matching node
    @Test
    public void testMatchingOneNode() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent></parent>";
        String tree2 = "<parent></parent>";

        int expectedNumberOfMatchings = 1;
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(tree1, tree2, expectedNumberOfMatchings);
        Set<Pair<String, String>> calculatedMatches = matching.stream().map(x -> Pair.of(x.first.getLabel().get(), x.second.getLabel().get())).collect(Collectors.toSet());
        Set<Pair<String, String>> expectedMatches = ImmutableSet.of(Pair.of("parent", "parent"));

        validateMatchingSet(calculatedMatches, expectedMatches);
    }

    // Write a test to validate matching tree with one matching node
    @Test
    public void testNonMatchingOneNode() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent></parent>";
        String tree2 = "<child></child>";

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(tree1, tree2, 0);
    }

    // Write a test to validate one parent node and one child node
    @Test
    public void testValidateOneParentAndOneChildNodeMatchingBoth() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+
                "</parent>";

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(tree1, tree2, 2);

        Set<Pair<String, String>> calculatedMatches = matching.stream().map(x -> Pair.of(x.first.getLabel().get(), x.second.getLabel().get())).collect(Collectors.toSet());
        Set<Pair<String, String>> expectedMatches = ImmutableSet.of(Pair.of("parent", "parent"),
                Pair.of("child", "child"));

        validateMatchingSet(calculatedMatches, expectedMatches);
    }

    @Test
    public void testValidateOneParentOneChildNodeWithChildNodesNotMatching() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>" +
                "</parent>";
        String tree2 = "<parent>" +
                "<child1></child1>" +
                "</parent>";

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(tree1, tree2, 0);
    }

    @Test
    public void testValidateOneParentOneChildNodeWithParentNodesNotMatching() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent1>" +
                "<child></child>" +
                "</parent1>";
        String tree2 = "<parent>" +
                "<child></child>" +
                "</parent>";

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(tree1, tree2, 1);

        Set<Pair<String, String>> calculatedMatches = matching.stream().map(x -> Pair.of(x.first.getLabel().get(), x.second.getLabel().get())).collect(Collectors.toSet());
        Set<Pair<String, String>> expectedMatches = ImmutableSet.of(Pair.of("child", "child"));

        validateMatchingSet(calculatedMatches, expectedMatches);
    }

    private void validateMatchingSet(Set<Pair<String, String>> calculatedMatches, Set<Pair<String, String>> expectedMatches) {
        for (Pair<String, String> match : calculatedMatches) {
            if (!expectedMatches.contains(match)) {
                fail(match + " does not exist in expectedMatches");
            }
        }

        for (Pair<String, String> match : expectedMatches) {
            if (!calculatedMatches.contains(match)) {
                fail(match + " does not exist in matches");
            }
        }
    }

    // write a test to validate one parent node and two children node
    // Write a test to validate where all the nodes match
    @Test
    public void testValidate1Parent2ChildrenAllMatching() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>" +
                "<child1></child1>" +
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>" +
                "<child1></child1>" +
                "</parent>";

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(tree1, tree2, 3);

        Set<Pair<String, String>> calculatedMatches = matching.stream().map(x -> Pair.of(x.first.getLabel().get(), x.second.getLabel().get())).collect(Collectors.toSet());
        Set<Pair<String, String>> expectedMatches = ImmutableSet.of(Pair.of("parent", "parent"),
                Pair.of("child", "child"),Pair.of("child1", "child1"));

        validateMatchingSet(calculatedMatches, expectedMatches);
    }

    // Write a test to validate where none of the nodes match
    @Test
    public void testValidate1Parent2ChildrenNoMatching() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>" +
                "<child></child>" +
                "</parent>";
        String tree2 = "<parent1>" +
                "<child1></child1>" +
                "<child1></child1>" +
                "</parent1>";

       TestingUtils.runMatchingTest(tree1, tree2, 0);
    }


    // Write a test to validate matching where tree1 parent has 2 children and tree2 has 1 child
    @Test
    public void validateTree1Parent2ChildrenTree2Has1Child() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>" +
                "<child1></child1>" +
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>" +
                "</parent>";

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(tree1, tree2, 2);

        Set<Pair<String, String>> calculatedMatches = matching.stream().map(x -> Pair.of(x.first.getLabel().get(), x.second.getLabel().get())).collect(Collectors.toSet());
        Set<Pair<String, String>> expectedMatches = ImmutableSet.of(Pair.of("parent", "parent"),
                Pair.of("child", "child"));

        validateMatchingSet(calculatedMatches, expectedMatches);
    }

    // Write a test to validate matching where tree1 parent has 2 children and tree2 has none
    @Test
    public void validateWhereTree1HasChildrenTree2HasNoChildren() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>" +
                "<child1></child1>" +
                "</parent>";
        String tree2 = "<parent>" +
                "</parent>";
        TestingUtils.runMatchingTest(tree1, tree2, 0);
    }
}
