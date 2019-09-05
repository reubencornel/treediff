package org.freeshell.rfcornel.util;

import org.freeshell.rfcornel.matching.MatchingCriteria;
import org.freeshell.rfcornel.matching.MatchingEngine;
import org.freeshell.rfcornel.matching.impl.DefaultMatch;
import org.freeshell.rfcornel.matching.impl.TestMatchingCriteria;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by reuben on 11/17/17.
 */
public class TestingUtils {
    public static TestTreeNode parseXmlTree(String treeInput) throws ParserConfigurationException, SAXException, IOException {
        return parseXmlTree(new ByteArrayInputStream(treeInput.getBytes()));
    }

    public static TestTreeNode parseXmlTree(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser parser = saxParserFactory.newSAXParser();
        SAXHandler handler = new SAXHandler();
        parser.parse(inputStream, handler);
        return handler.getRoot();
    }

    public static Collection<Pair<TestTreeNode, TestTreeNode>> runMatchingTest(String tree1, String tree2, int expectedNumberOfMatchings) throws ParserConfigurationException, SAXException, IOException {
        TestTreeNode root1 = parseXmlTree(tree1);
        TestTreeNode root2 = parseXmlTree(tree2);
        return runMatchingTest(root1, root2, expectedNumberOfMatchings);
    }

    public static Collection<Pair<TestTreeNode, TestTreeNode>> runMatchingTest(TestTreeNode root1, TestTreeNode root2, int expectedNumberOfMatchings) {
        return runMatchingTest(root1, root2, expectedNumberOfMatchings, new TestMatchingCriteria());
    }

    public static Collection<Pair<TestTreeNode, TestTreeNode>> runMatchingTest(TestTreeNode root1, TestTreeNode root2, int expectedNumberOfMatchings, MatchingCriteria criteria) {
        MatchingEngine matchingEngine = new DefaultMatch();
        Collection<Pair<TestTreeNode, TestTreeNode>> matching = matchingEngine.calculateMatching(root1, root2, criteria);
        assertThat(matching, notNullValue());
        if(expectedNumberOfMatchings > 0) {
            assertThat(matching.size(), is(expectedNumberOfMatchings));
        }
        return matching;
    }
}
