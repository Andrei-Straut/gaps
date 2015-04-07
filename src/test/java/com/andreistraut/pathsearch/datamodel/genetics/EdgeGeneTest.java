package com.andreistraut.pathsearch.datamodel.genetics;

import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.pathsearch.datamodel.graph.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.InvalidConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class EdgeGeneTest {

    private Node firstNode;
    private Node secondNode;
    private DirectedWeightedEdge firstToSecondEdge;
    private DirectedWeightedEdge secondToFirstEdge;
    private final int firstToSecondEdgeCost = 1;
    private final int secondToFirstEdgeCost = 2;
    private DirectedWeightedGraph graph;
    private GeneticConfiguration conf;

    public EdgeGeneTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(DirectedWeightedGraph.class.getName()).log(Level.INFO,
		GeneticConfigurationTest.class.toString() + " TEST: Edge Gene");
    }

    @Before
    public void setUp() {
	firstNode = new Node("1", "1");
	secondNode = new Node("2", "2");

	firstToSecondEdge = new DirectedWeightedEdge(firstNode, secondNode, firstToSecondEdgeCost, true);
	secondToFirstEdge = new DirectedWeightedEdge(secondNode, firstNode, secondToFirstEdgeCost, true);

	graph = new DirectedWeightedGraph(3, 2);
	graph.addVertex(firstNode);
	graph.addVertex(secondNode);
	graph.addEdge(firstNode, secondNode, firstToSecondEdge);

	conf = new GeneticConfiguration("EdgeGeneTest", graph);
    }

    @Test
    public void testCopy() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	EdgeGene second = first.copy();

	Assert.assertFalse(first == second);
	Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testGetAllele() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	Assert.assertEquals(firstToSecondEdge, first.getAllele());
    }

    @Test
    public void testSetAllele() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	first.setAllele(secondToFirstEdge);

	Assert.assertEquals(secondToFirstEdge, first.getAllele());
    }

    @Test
    public void testGetConfiguration() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	Assert.assertTrue(conf == first.getConfiguration());
    }

    @Test
    public void testGetInternalValue() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	Assert.assertEquals(firstToSecondEdge, (DirectedWeightedEdge) first.getInternalValue());
    }

    @Test
    public void testNewGene() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	EdgeGene second = (EdgeGene) first.newGene();

	Assert.assertFalse(first == second);
	Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testNewGeneInternal() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	EdgeGene second = (EdgeGene) first.newGene();

	Assert.assertFalse(first == second);
	Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testApplyMutationGuaranteed() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	int originalCost = first.getAllele().getCost();

	/*
	 * Mutation in this case is guaranteed. Values should be different 
	 */
	first.applyMutation(originalCost, 100);
	int newCost = first.getAllele().getCost();
	Assert.assertFalse(originalCost == newCost);
    }

    @Test
    public void testApplyMutationNoOp() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	int originalCost = first.getAllele().getCost();

	first.applyMutation(originalCost, 0);
	Assert.assertTrue(originalCost == first.getAllele().getCost());
    }

    @Test
    public void testCompareToSmallerThan() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	EdgeGene second = new EdgeGene(secondToFirstEdge, conf);
	Assert.assertTrue(
		first.compareTo(second) == first.getAllele().getCost() - second.getAllele().getCost()
		&& first.compareTo(second) < 0);
    }

    @Test
    public void testCompareToLargerThan() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(secondToFirstEdge, conf);
	EdgeGene second = new EdgeGene(firstToSecondEdge, conf);
	Assert.assertTrue(
		first.compareTo(second) == first.getAllele().getCost() - second.getAllele().getCost()
		&& first.compareTo(second) > 0);
    }

    @Test
    public void testCompareToEquals() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	EdgeGene second = new EdgeGene(firstToSecondEdge, conf);
	Assert.assertTrue(first.compareTo(second) == 0);
    }

    @Test
    public void testCompareToAgainstNullAllele() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(firstToSecondEdge, conf);
	EdgeGene second = new EdgeGene(null, conf);
	Assert.assertTrue(
		first.compareTo(second) == first.getAllele().getCost()
		&& first.compareTo(second) > 0);
    }

    @Test
    public void testCompareToAgainstNull() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(secondToFirstEdge, conf);
	EdgeGene second = null;
	Assert.assertTrue(
		first.compareTo(second) == first.getAllele().getCost()
		&& first.compareTo(second) > 0);
    }

    @Test
    public void testCompareToAgainstObject() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(secondToFirstEdge, conf);
	Object secondObject = new Object();
	Assert.assertTrue(
		first.compareTo(secondObject) == first.getAllele().getCost()
		&& first.compareTo(secondObject) > 0);
    }

    @Test
    public void testToString() {
	//TODO: Finish this
    }

    @Test
    public void testHashCode() {
	//TODO: Finish this
    }

    @Test
    public void testEquals() {
	//TODO: Finish this
    }

}
