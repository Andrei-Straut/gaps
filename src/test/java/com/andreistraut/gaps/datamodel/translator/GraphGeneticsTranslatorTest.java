package com.andreistraut.gaps.datamodel.translator;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.gaps.datamodel.graph.Node;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgrapht.GraphPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphGeneticsTranslatorTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private GraphPath<Node, DirectedWeightedEdge> path;

    public GraphGeneticsTranslatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(GraphGeneticsTranslatorTest.class.getName()).log(Level.INFO,
		"{0} TEST: GraphGeneticsTranslatorTest",
		GraphGeneticsTranslatorTest.class.toString());
    }

    @Before
    public void setUp() {
	this.graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("PathChromosomePopulationTest", this.graphMock.getGraph());
	this.path = graphMock.getGraph().getKShortestPaths(graphMock.getFirstNode(), graphMock.getThirdNode(), 2).get(1);
    }

    @Test
    public void testToPathChromosomeValidPath() throws Exception {
	PathChromosome chromosome = GraphGeneticsTranslator.toPathChromosome(path, conf);

	Assert.assertTrue(chromosome.getGenesList().size() == 2);
	Assert.assertTrue(chromosome.getGene(0).getAllele().equals(graphMock.getFirstToSecondEdge()));
	Assert.assertTrue(chromosome.getGene(1).getAllele().equals(graphMock.getSecondToThirdEdge()));
    }

    @Test
    public void testToPathChromosomeEmptyPath() throws Exception {
	GraphPath<Node, DirectedWeightedEdge> invalidPath
		= new DirectedWeightedGraphPath(graphMock.getGraph());

	//Negative test, should fail for empty paths
	try {
	    PathChromosome chromosome = GraphGeneticsTranslator.toPathChromosome(invalidPath, conf);
	    Assert.assertTrue(false);
	} catch (NullPointerException e) {
	    Assert.assertTrue(true);
	}
    }

    @Test
    public void testToEdgeGeneListValidPath() throws Exception {
	ArrayList<EdgeGene> expected = new ArrayList<>();
	expected.add(new EdgeGene(graphMock.getFirstToSecondEdge(), conf));
	expected.add(new EdgeGene(graphMock.getSecondToThirdEdge(), conf));

	ArrayList<EdgeGene> actual = GraphGeneticsTranslator.toEdgeGeneList(path, conf);
	Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void testToEdgeGeneListEmptyPath() throws Exception {

	GraphPath<Node, DirectedWeightedEdge> invalidPath
		= new DirectedWeightedGraphPath(graphMock.getGraph());

	//Negative test, should fail for empty paths
	try {
	    ArrayList<EdgeGene> actual = GraphGeneticsTranslator.toEdgeGeneList(invalidPath, conf);
	    Assert.assertTrue(false);
	} catch (NullPointerException e) {
	    Assert.assertTrue(true);
	}
    }

}
