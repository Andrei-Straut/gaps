package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.Node;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.InvalidConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class PathChromosomeTest {

    private Node firstNode;
    private Node secondNode;
    private Node thirdNode;
    private final int firstToSecondEdgeCost = 1;
    private final int secondToThirdEdgeCost = 2;
    private DirectedWeightedEdge firstToSecondEdge;
    private DirectedWeightedEdge secondToThirdEdge;
    private DirectedWeightedGraph graph;

    private EdgeGene firstToSecondGene;
    private EdgeGene secondToThirdGene;
    private ArrayList<EdgeGene> genes;
    private GeneticConfiguration conf;

    public PathChromosomeTest() {
    }

    @Before
    public void setUp() throws InvalidConfigurationException {

	firstNode = new Node("1", "1");
	secondNode = new Node("2", "2");
	thirdNode = new Node("3", "3");

	firstToSecondEdge = new DirectedWeightedEdge(firstNode, secondNode, firstToSecondEdgeCost, true);
	secondToThirdEdge = new DirectedWeightedEdge(secondNode, thirdNode, secondToThirdEdgeCost, true);

	graph = new DirectedWeightedGraph(3, 2);
	graph.addVertex(firstNode);
	graph.addVertex(secondNode);
	graph.addVertex(thirdNode);
	graph.addEdge(firstNode, secondNode, firstToSecondEdge);
	graph.addEdge(secondNode, thirdNode, secondToThirdEdge);

	conf = new GeneticConfiguration("EdgeGeneTest", graph);

	firstToSecondGene = new EdgeGene(firstToSecondEdge, conf);
	secondToThirdGene = new EdgeGene(secondToThirdEdge, conf);

	genes = new ArrayList<EdgeGene>();
	genes.add(firstToSecondGene);
	genes.add(secondToThirdGene);
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeTest.class.getName()).log(Level.INFO,
		PathChromosomeTest.class.toString() + " TEST: PathChromosome");
    }

    @Test
    public void testConstructorNullConfiguration() throws InvalidConfigurationException {
	//It's ok, this is supposed to throw exception. In fact, we are testing if indeed it does
	PathChromosome chromosome;
	try {
	    chromosome = new PathChromosome(null, genes, firstNode, thirdNode);
	} catch (InvalidConfigurationException ex) {
	    Assert.assertTrue(ex.getClass() == InvalidConfigurationException.class);
	    Assert.assertTrue(ex.getMessage().equals("Configuration to be set must not be null!"));
	}
    }

    @Test
    public void testGetLowestCostGene() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	Assert.assertTrue(firstToSecondGene == chromosome.getLowestCostGene());
    }

    @Test
    public void testGetHighestCostGene() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	Assert.assertTrue(secondToThirdGene == chromosome.getHighestCostGene());
    }

    @Test
    public void testGetLowestCostGeneIndex() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	Assert.assertTrue(0 == chromosome.getLowestCostGeneIndex());
    }

    @Test
    public void testGetHighestCostGeneIndex() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	Assert.assertTrue(1 == chromosome.getHighestCostGeneIndex());
    }

    @Test
    public void testSetGene_2argsFirstGeneValidGene() throws InvalidConfigurationException {
	// Set a valid secondToFirstGene at the chromosome position. Expected: secondToFirstGene is set, genes list size not modified
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(firstNode, secondNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene);
	int modifiedNoOfGenes = chromosome.getGenesList().size();

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
	Assert.assertTrue(originalNoOfGenes == modifiedNoOfGenes);
    }

    @Test
    public void testSetGene_2argsFirstGeneInvalidGene() throws InvalidConfigurationException {
	// Set an invalid secondToFirstGene at the chromosome position, without checking validity. Expected: secondToFirstGene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(secondNode, thirdNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene);
	int modifiedNoOfGenes = chromosome.getGenesList().size();

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
	Assert.assertTrue(originalNoOfGenes == modifiedNoOfGenes);
    }

    @Test
    public void testSetGene_2argsLastGeneValidGene() throws InvalidConfigurationException {
	// Set a valid secondToFirstGene at the last position. Expected: secondToFirstGene is set, genes list size not modified
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(secondNode, thirdNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(originalNoOfGenes - 1, gene);
	int modifiedNoOfGenes = chromosome.getGenesList().size();

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(originalNoOfGenes - 1) == gene);
	Assert.assertTrue(originalNoOfGenes == modifiedNoOfGenes);
    }

    @Test
    public void testSetGene_2argsLastGeneInvalidGene() throws InvalidConfigurationException {
	// Set an invalid secondToFirstGene at the last position. Expected: secondToFirstGene is set, genes list size not modified
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(secondNode, thirdNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene);
	int modifiedNoOfGenes = chromosome.getGenesList().size();

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
	Assert.assertTrue(originalNoOfGenes == modifiedNoOfGenes);
    }

    @Test
    public void testSetGene_3argsFirstGeneValidGeneCheckValidity() throws InvalidConfigurationException {
	// Set a valid secondToFirstGene, and check validity. Expected: secondToFirstGene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(firstNode, secondNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, true);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneValidGeneSkipValidity() throws InvalidConfigurationException {
	// Set a valid secondToFirstGene, without checking validity. Expected: secondToFirstGene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(firstNode, secondNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneInvalidGeneSkipValidity() throws InvalidConfigurationException {
	// Set a invalid secondToFirstGene, without checking validity. Expected: secondToFirstGene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(secondNode, thirdNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneInvalidGeneCheckValidity() throws InvalidConfigurationException {
	// Set an invalid secondToFirstGene at the chromosome position. Expected: secondToFirstGene is  not set, genes list continues unmodified
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);

	DirectedWeightedEdge edge = new DirectedWeightedEdge(secondNode, thirdNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, true);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertFalse(chromosome.getGene(0) == gene);
	Assert.assertTrue(genes.equals(chromosome.getGenesList()));
    }

    @Test
    public void testSetGene_3argsLastGeneValidGeneSkipValidity() throws InvalidConfigurationException {
	// Set an valid secondToFirstGene at the last position. Expected: secondToFirstGene is  set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(firstNode, secondNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsLastGeneInvalidGeneCheckValidity() throws InvalidConfigurationException {
	// Set an invalid secondToFirstGene at the last position. Expected: secondToFirstGene is  not set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(firstNode, secondNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(originalNoOfGenes - 1, gene, true);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertFalse(chromosome.getGene(originalNoOfGenes - 1) == gene);
	Assert.assertTrue(genes.equals(chromosome.getGenesList()));
    }

    @Test
    public void testGetCost_0argsValidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost() == firstToSecondEdgeCost + secondToThirdEdgeCost);
    }

    @Test
    public void testGetCost_0argsInvalidChromosomeNullEdges() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	chromosome.setGenesList(null);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost() == Integer.MAX_VALUE);
    }

    @Test
    public void testGetCost_0argsInvalidChromosomeEmptyEdges() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	chromosome.setGenesList(new ArrayList<EdgeGene>());

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost() == Integer.MAX_VALUE);
    }

    @Test
    public void testGetCost_0argsInvalidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(secondNode, thirdNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost() == Integer.MAX_VALUE);
    }

    @Test
    public void testGetCost_2argsValidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(0, 1) == firstToSecondEdgeCost + secondToThirdEdgeCost);
    }

    @Test
    public void testGetCost_2argsValidChromosomeSameStartEndIndex() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(0, 0) == firstToSecondEdgeCost);
	Assert.assertTrue(chromosome.getCost(1, 1) == secondToThirdEdgeCost);
    }

    @Test
    public void testGetCost_2argsValidChromosomeStartIndexLowerThanZero() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(-1, 2) == firstToSecondEdgeCost + secondToThirdEdgeCost);
	Assert.assertTrue(chromosome.getCost(-1, 1) == firstToSecondEdgeCost + secondToThirdEdgeCost);
	Assert.assertTrue(chromosome.getCost(-1, 0) == firstToSecondEdgeCost);
    }

    @Test
    public void testGetCost_2argsValidChromosomeStartIndexLargerThanSize() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(3, 4) == firstToSecondEdgeCost + secondToThirdEdgeCost);
	Assert.assertTrue(chromosome.getCost(2, 4) == firstToSecondEdgeCost + secondToThirdEdgeCost);
    }

    @Test
    public void testGetCost_2argsValidChromosomeEndIndexLargerThanSize() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(0, 3) == firstToSecondEdgeCost + secondToThirdEdgeCost);
	Assert.assertTrue(chromosome.getCost(0, 2) == firstToSecondEdgeCost + secondToThirdEdgeCost);
	Assert.assertTrue(chromosome.getCost(0, 1) == firstToSecondEdgeCost + secondToThirdEdgeCost);
    }

    @Test
    public void testGetCost_2argsValidChromosomeStartIndexLargerThanEndIndex() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(5, 4) == firstToSecondEdgeCost + secondToThirdEdgeCost);
	Assert.assertTrue(chromosome.getCost(5, 2) == firstToSecondEdgeCost + secondToThirdEdgeCost);
	Assert.assertTrue(chromosome.getCost(5, 1) == firstToSecondEdgeCost + secondToThirdEdgeCost);
	Assert.assertTrue(chromosome.getCost(5, 0) == firstToSecondEdgeCost);
    }

    @Test
    public void testIsLegalValidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);

	Assert.assertTrue(chromosome.isLegal());
    }

    @Test
    public void testIsLegalInvalidChromosome() throws InvalidConfigurationException {
	DirectedWeightedEdge edge1 = new DirectedWeightedEdge(secondNode, firstNode);
	EdgeGene secondToFirstGene = new EdgeGene(edge1, conf);

	ArrayList<EdgeGene> edgeGenes = new ArrayList<EdgeGene>();
	edgeGenes.add(firstToSecondGene);
	edgeGenes.add(secondToFirstGene);
	edgeGenes.add(firstToSecondGene);

	PathChromosome chromosome = new PathChromosome(conf, edgeGenes, firstNode, thirdNode);

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsLegalInvalidChromosomeEmptyEdges() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(), firstNode, thirdNode);

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsLegalValidChromosomeNullSource() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, null, thirdNode);

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsLegalValidChromosomeNullDestination() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, null);

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsLegalValidChromosomeNullSourceAndDestination() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, null, null);

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsFitterThanHigherFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, firstNode, thirdNode);

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(firstNode, thirdNode, firstToSecondEdgeCost);
	graph.addEdge(firstNode, thirdNode, firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, firstNode, thirdNode);

	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanLowerFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, firstNode, thirdNode);

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(firstNode, thirdNode, firstToSecondEdgeCost + secondToThirdEdgeCost + 1);
	graph.addEdge(firstNode, thirdNode, firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, firstNode, thirdNode);

	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanEqualFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, firstNode, thirdNode);

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(firstNode, thirdNode, firstToSecondEdgeCost + secondToThirdEdgeCost);
	graph.addEdge(firstNode, thirdNode, firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, firstNode, thirdNode);

	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanInvalidChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, firstNode, thirdNode);

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(firstNode, thirdNode, firstToSecondEdgeCost + secondToThirdEdgeCost);
	graph.addEdge(firstNode, thirdNode, firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, firstNode, secondNode);

	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanEmptyChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, firstNode, thirdNode);
	PathChromosome second = new PathChromosome(conf, new ArrayList<EdgeGene>(), firstNode, thirdNode);

	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanNullChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, firstNode, thirdNode);
	PathChromosome second = null;

	Assert.assertTrue(first.isFitterThan(second));
    }

    @Test
    public void testGetCyclicGeneSequenceValidChromosome() throws InvalidConfigurationException {
	DirectedWeightedEdge edge1 = new DirectedWeightedEdge(secondNode, firstNode);
	EdgeGene secondToFirstGene = new EdgeGene(edge1, conf);
	DirectedWeightedEdge edge2 = new DirectedWeightedEdge(firstNode, thirdNode);
	EdgeGene firstToThirdGene = new EdgeGene(edge2, conf);

	ArrayList<EdgeGene> edgeGenes = new ArrayList<EdgeGene>();
	edgeGenes.add(firstToSecondGene);
	edgeGenes.add(secondToFirstGene);
	edgeGenes.add(firstToThirdGene);

	PathChromosome chromosome = new PathChromosome(conf, edgeGenes, firstNode, thirdNode);
	ArrayList<EdgeGene> cycle = chromosome.getCyclicGeneSequence();

	Assert.assertTrue(chromosome.isLegal());

	System.out.println(cycle.toString());
	Assert.assertTrue(cycle.get(0) == firstToSecondGene);
	Assert.assertTrue(cycle.get(1) == secondToFirstGene);
    }

    @Test
    public void testGetCyclicGeneSequenceInvalidChromosome() throws InvalidConfigurationException {
	DirectedWeightedEdge edge1 = new DirectedWeightedEdge(secondNode, firstNode);
	EdgeGene secondToFirstGene = new EdgeGene(edge1, conf);

	ArrayList<EdgeGene> edgeGenes = new ArrayList<EdgeGene>();
	edgeGenes.add(firstToSecondGene);
	edgeGenes.add(secondToFirstGene);
	edgeGenes.add(firstToSecondGene);

	PathChromosome chromosome = new PathChromosome(conf, edgeGenes, firstNode, thirdNode);
	ArrayList<EdgeGene> cycle = chromosome.getCyclicGeneSequence();

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(cycle.get(0) == firstToSecondGene);
	Assert.assertTrue(cycle.get(1) == secondToFirstGene);
    }

    @Test
    public void testGetCyclicGeneSequenceInvalidChromosomeCycleOnly() throws InvalidConfigurationException {
	DirectedWeightedEdge edge1 = new DirectedWeightedEdge(secondNode, firstNode);
	EdgeGene secondToFirstGene = new EdgeGene(edge1, conf);

	ArrayList<EdgeGene> edgeGenes = new ArrayList<EdgeGene>();
	edgeGenes.add(firstToSecondGene);
	edgeGenes.add(secondToFirstGene);

	PathChromosome chromosome = new PathChromosome(conf, edgeGenes, firstNode, thirdNode);
	ArrayList<EdgeGene> cycle = chromosome.getCyclicGeneSequence();

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(cycle.get(0) == firstToSecondGene);
	Assert.assertTrue(cycle.get(1) == secondToFirstGene);
    }

    @Test
    public void testCompareTo() {
    }

    @Test
    public void testToJson() {
    }

    @Test
    public void testToString() {
    }

    @Test
    public void testEquals() {
    }

    @Test
    public void testHashCode() {
    }

    @Test
    public void testClone() {
    }

}
