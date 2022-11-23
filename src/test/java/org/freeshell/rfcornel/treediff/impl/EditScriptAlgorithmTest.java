package org.freeshell.rfcornel.treediff.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.freeshell.rfcornel.lcs.LCSAlgorithm;
import org.freeshell.rfcornel.lcs.LCSResult;
import org.freeshell.rfcornel.lcs.impl.SimpleLCSAlgorithm;
import org.freeshell.rfcornel.lcs.impl.SimpleLCSResult;
import org.freeshell.rfcornel.matching.MatchingEngine;
import org.freeshell.rfcornel.matching.impl.FastMatch;
import org.freeshell.rfcornel.matching.impl.SimpleXmlTestMatchingCriteria;
import org.freeshell.rfcornel.matching.impl.TestMatchingCriteria;
import org.freeshell.rfcornel.treediff.EditOperation;
import org.freeshell.rfcornel.treediff.editoperations.UpdateValueOperation;
import org.freeshell.rfcornel.util.Pair;
import org.freeshell.rfcornel.util.TestTreeNode;
import org.freeshell.rfcornel.util.TestingUtils;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static org.freeshell.rfcornel.util.TestingUtils.parseXmlTree;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

/**
 * Tests to validate the Algorithm impl in EditScriptAlgorithm
 *
 * @author reuben
 */
public class EditScriptAlgorithmTest {
    // Edit algorithm tests
    // ---------------------
    // The most basic test is the for the first conditino
    // If the node in the breadth first search(x) has no partner in M then
    // find its position in T2 and insert it in T1. In this test, we are going to
    // have a few matching nodes to let the parents match
    @Test
    public void case1() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+
                "<child1></child1>"+
                "</parent>";
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);

        MatchingEngine matchingEngine = new FastMatch();
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = matchingEngine.calculateMatching(root1, root2, new TestMatchingCriteria());
        assertThat(matching, notNullValue());
        int oldMatchingSize = matching.size();
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, new SimpleLCSAlgorithm<>());
        List<EditOperation> editOperations = nodeEditScriptAlgorithm.calculateEditScript(root1, root2);

        assertThat(!root1.getChildren().isEmpty(), is(true));
        assertThat(root1.getChildren().size(), is(2));
        assertThat(editOperations.size(), is(1));
        assertThat(matching.size() - oldMatchingSize, is(1));
        assertThat(matching.stream().map(x -> x.first.getLabel()
                + " " + x.second.getLabel()).filter(x -> x.equals("child1 child1")).count(), is(1l));
    }

    // If the node in T2 has a different value from the node in t1
    // update the value of the node in T1 and add the operation to the script.
    @Test
    public void case2() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child name='reuben'></child>"+
                "</parent>";
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 2, new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, new SimpleLCSAlgorithm<>());
        List<EditOperation> editOperations = nodeEditScriptAlgorithm.calculateEditScript(root1, root2);

        assertThat(editOperations.size(), is(1));
        assertThat(editOperations.get(0) instanceof UpdateValueOperation, is(true));
        assertThat(root1.getChildren().get(0).getValue().size() > 0, is(true));
        assertThat(root1.getChildren().get(0).getValue().get("name"), is("reuben"));
    }

    // In this case the parents of the matched node are not matched.
    // when this happens find the matched partner of the parent in 2 and move
    // the subtree to it.
    @Test
    public void case3() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+
                "<child1>" +
                   "<child12></child12>" +
                "</child1>" +
                "</parent>";
        String tree2 = "<parent>" +
                "<child>" +
                     "<child12 name='reuben'></child12>" +
                "</child>"+
                "<child1></child1>"+
                "</parent>";
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 3, new SimpleXmlTestMatchingCriteria(), new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, new SimpleLCSAlgorithm<>());
        List<EditOperation> editOperations = nodeEditScriptAlgorithm.calculateEditScript(root1, root2);
        assertThat(editOperations.size(), is(2));
    }

    // This test replicates the most common use case in the goldfiles: removing a property
    @Test
    public void case4() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child1>" +
                    "<child12></child12>" +
                    "<child13></child13>" +
                "</child1>" +
                "<child13></child13>" +
                "</parent>";
        String tree2 = "<parent>" +
                "<child1>" +
                    "<child12></child12>" +
                "</child1>"+
                "<child13></child13>" +
                "</parent>";
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 4, new SimpleXmlTestMatchingCriteria(), new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, new SimpleLCSAlgorithm<>());
        List<EditOperation> editOperations = nodeEditScriptAlgorithm.calculateEditScript(root1, root2);
        System.out.println(editOperations);
    }

    // TODO write a test where the roots aren't matched

    // We should start off by first testing alignChildren
    //   Validate that when we start the function all children of the nodes
    //   passed in are marked "out of order"

    //         Validate with children
    @Test
    public void validateWeMarkAllNodesOutOfOrder() throws IOException, SAXException, ParserConfigurationException {
        String tree = "<parent>" +
                "<child></child>"+
                "<child1></child1>"+
                "</parent>";
        TestTreeNode root2 = parseXmlTree(tree);

        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(new ArrayList<>(), null);
        nodeEditScriptAlgorithm.markAllChildrenOutOfOrder(root2);
        boolean value = root2.getChildren().stream().map(x -> x.inOrder()).reduce(false, (x, y) -> x || y);
        assertThat(value, is(false));
    }

    //         Validate what happens when we don't have any children

    //   Validate we Call LCS with equals defined as the tuple of nodes in S1 and S2
    // belong in the matching the result of this call is "S"
    @Test
    public void validateCollectionsPassedIntoLCS() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+
                "<child1></child1>"+
                "<child2></child2>"+
                "<child3></child3>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+
                "<child1></child1>"+
                "<child3></child3>"+
                "</parent>";
        String tree3 = "<parent>" +
                "<child2></child2>"+
                "</parent>";

        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);
        TestTreeNode root3 =  parseXmlTree(tree3);

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 4, new FastMatch());

        // Create a fake match
        Pair<TestTreeNode, TestTreeNode> fakeMatch = Pair.of(root1.getChildren().get(2), root3.getChildren().get(0));
        matching.add(fakeMatch);

        DummyLCSImpl<TestTreeNode> dummyLCS = new DummyLCSImpl<>();
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<TestTreeNode>(matching, dummyLCS);
        nodeEditScriptAlgorithm.alignChildren(root1, root2);

        Set<String> list1Set = dummyLCS.getList1().stream()
                .filter(x -> x.getParent().get() == root1)
                .map(x -> x.getLabel())
                .collect(Collectors.toSet());
        assertThat(list1Set.size(), is(3));
        Set<String> list2Set = dummyLCS.getList2().stream()
                .filter(x -> x.getParent().get() == root2)
                .map(x -> x.getLabel()).collect(Collectors.toSet());
        assertThat(list2Set.size(), is(3));

        Set<String> expectedList1 = ImmutableSet.of("child", "child1", "child3");
        assertTrue(list1Set.removeAll(expectedList1));
        assertThat(list1Set.size(), is(0));

        assertTrue(list2Set.removeAll(expectedList1));
        assertThat(list2Set.size(), is(0));
    }

    public static class DummyLCSImpl<E> implements LCSAlgorithm<E> {
        private List<E> seq1;
        private List<E> seq2;

        public List<E> getList1() {
            return seq1;
        }

        public List<E> getList2() {
            return seq2;
        }

        @Override
        public LCSResult findLCS(List<E> seq1, List<E> seq2, BiPredicate<E, E> equalityPredicate) {
            this.seq1 = seq1;
            this.seq2 = seq2;
            return new SimpleLCSResult(0, 0);
        }

        @Override
        public List<Pair<E, E>> interpretLCS(List<E> seq1, List<E> seq2, LCSResult result) {
            return ImmutableList.of();
        }
    }

    //   For each node in the result from S1 validate we mark them in order
    @Test
    public void markNodesFromLCSInOrder() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+     //--> should be in order
                "<child3></child3>"+
                "<child1></child1>"+   //--> should be in order
                "<child2></child2>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+    //--> should be in order
                "<child1></child1>"+  //--> should be in order
                "<child3></child3>"+
                "</parent>";
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 4, new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, new SimpleLCSAlgorithm<>());
        nodeEditScriptAlgorithm.alignChildren(root1, root2);

        assertThat(root1.getChildren().get(0).inOrder(), is(true));
        assertThat(root2.getChildren().get(0).inOrder(), is(true));

        assertThat(root1.getChildren().get(1).inOrder(), is(true));
        assertThat(root2.getChildren().get(2).inOrder(), is(true));
    }


    //   For each tuple in the match that was not present in S
    //       We call find pos on the second sibling
    //       Validate we append and apply Mov to tree1
    //       Validate we mark a and b in order
    @Test
    public void simpleAlignCase() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+     //--> should be in order
                "<child3></child3>"+
                "<child1></child1>"+   //--> should be in order
                "<child2></child2>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+    //--> should be in order
                "<child1></child1>"+  //--> should be in order
                "<child3></child3>"+
                "</parent>";
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 4, new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, new SimpleLCSAlgorithm<>());
        nodeEditScriptAlgorithm.alignChildren(root1, root2);

        assertThat(root1.getChildren().get(0).getLabel(), is("child"));
        assertThat(root1.getChildren().get(1).getLabel(), is("child1"));
        assertThat(root1.getChildren().get(2).getLabel(), is("child3"));
    }

    
    // Validate function findPos(x)
    //      validate that we return 1 if x is the left most child of its parent that is marked inorder
    @Test
    public void testCaseWhereNodeIsLeftMostChildMarkedInOrder() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+
                "<child1></child1>"+
                "</parent>";

        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 2, new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, null);
        TestTreeNode node = root2.getChildren().get(0);
        node.setInOrder(true);
        assertThat(nodeEditScriptAlgorithm.findPosition(node), is(0));
    }

    //      Validate we get an exception when pass in a root node.
    @Test(expected = IllegalStateException.class)
    public void testValidatePassingInRootNode() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child></child>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+
                "<child1></child1>"+
                "</parent>";

        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 2, new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, null);
        assertThat(nodeEditScriptAlgorithm.findPosition(root2), is(1));
    }

    // validate where v is the rightmost sibling of x that is to
    // the left of x and is marked  in order
    @Test
    public void findRightMostSiblingMarkedInOrder() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child3></child3>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+
                "<child1></child1>"+
                "<child2></child2>"+
                "<child3></child3>"+
                "</parent>";

        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);

        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 1, new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, null);
        root2.getChildren().get(1).setInOrder(true);
        int nodePosition = nodeEditScriptAlgorithm.findRightMostSiblingOfNodeMarkedInOrder(root2.getChildren().get(3), root2.getChildren());
        assertThat(nodePosition, is(1));
    }

    //      let u be the partner of v in T1
    //      validate we return i + 1 where i is the index of u in T1
    @Test
    public void validateIndexCalculationForRegularCase() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child1></child1>"+
                "<child3></child3>"+
                "</parent>";
        String tree2 = "<parent>" +
                "<child></child>"+
                "<child1></child1>"+ //==> marked in order
                "<child2></child2>"+
                "<child3></child3>"+
                "</parent>";

        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(root1, root2, 3, new FastMatch());
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, null);
        root2.getChildren().get(1).setInOrder(true);
        int nodePosition = nodeEditScriptAlgorithm.findPosition(root2.getChildren().get(3), root2);
        assertThat(nodePosition, is(1));
    }

    @Test
    public void workTest() throws IOException, SAXException, ParserConfigurationException {
        InputStream file1 = new BufferedInputStream(new FileInputStream("/home/reuben/Dropbox/CampaignInsights.xml"));
        InputStream file2 = new BufferedInputStream(new FileInputStream("/home/reuben/Dropbox/CampaignInsights1.xml"));

        TestTreeNode testTreeNode1 = parseXmlTree(file1);
        TestTreeNode testTreeNode2 = parseXmlTree(file2);

        long start = System.currentTimeMillis();
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = TestingUtils.runMatchingTest(testTreeNode1, testTreeNode2, -1, new FastMatch());
        System.out.println(matching);
        EditScriptAlgorithm<TestTreeNode> nodeEditScriptAlgorithm = new EditScriptAlgorithm<>(matching, new SimpleLCSAlgorithm<>());
        List<EditOperation> editOperations = nodeEditScriptAlgorithm.calculateEditScript(testTreeNode1, testTreeNode2);
        System.out.println(editOperations);
        System.out.println(System.currentTimeMillis() - start);
    }
}
