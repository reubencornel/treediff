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

public class DeleteOperationTest {
    @Test
    public void testDeleteOperation() throws IOException, SAXException, ParserConfigurationException {
        String tree1 = "<parent>" +
                "<child>" +
                "<child01></child01>" +  // --> move this child to
                "</child>"+
                "<child1>" +
                "<child11></child11>" +
                "</child1>"+         // --> this parent
                "<child2></child2>"+
                "<child3></child3>"+
                "</parent>";
        TestTreeNode root1 = parseXmlTree(tree1);
        EditOperation operation = Operations.delete(root1.getChildren().get(0));

        operation.apply();
        assertThat(root1.getChildren().get(0).getLabel(), Matchers.is("child1"));
    }
}
