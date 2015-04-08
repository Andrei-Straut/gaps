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

    private EdgeGene firstGene;
    private EdgeGene secondGene;
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

	firstGene = new EdgeGene(firstToSecondEdge, conf);
	secondGene = new EdgeGene(secondToThirdEdge, conf);

	genes = new ArrayList<EdgeGene>();
	genes.add(firstGene);
	genes.add(secondGene);
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeTest.class.getName()).log(Level.INFO,
		PathChromosomeTest.class.toString() + " TEST: PathChromosome");
    }

    @Test
    public void testGetLowestCostGene() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	Assert.assertTrue(firstGene == chromosome.getLowestCostGene());
    }

    @Test
    public void testGetHighestCostGene() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	Assert.assertTrue(secondGene == chromosome.getHighestCostGene());
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
	// Set a valid gene at the first position. Expected: gene is set, genes list size not modified
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
	// Set an invalid gene at the first position, without checking validity. Expected: gene is set
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
	// Set a valid gene at the last position. Expected: gene is set, genes list size not modified
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
	// Set an invalid gene at the last position. Expected: gene is set, genes list size not modified
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
	// Set a valid gene, and check validity. Expected: gene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(firstNode, secondNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, true);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneValidGeneSkipValidity() throws InvalidConfigurationException {
	// Set a valid gene, without checking validity. Expected: gene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(firstNode, secondNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneInvalidGeneSkipValidity() throws InvalidConfigurationException {
	// Set a invalid gene, without checking validity. Expected: gene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(secondNode, thirdNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneInvalidGeneCheckValidity() throws InvalidConfigurationException {
	// Set an invalid gene at the first position. Expected: gene is  not set, genes list continues unmodified
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
	// Set an valid gene at the last position. Expected: gene is  set
	PathChromosome chromosome = new PathChromosome(conf, genes, firstNode, thirdNode);
	DirectedWeightedEdge edge = new DirectedWeightedEdge(firstNode, secondNode);
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsLastGeneInvalidGeneCheckValidity() throws InvalidConfigurationException {
	// Set an invalid gene at the last position. Expected: gene is  not set
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
    public void testGetCyclicGeneSequence() {
    }

    @Test
    public void testIsLegal() {
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
