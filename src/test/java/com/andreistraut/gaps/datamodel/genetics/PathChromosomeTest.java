package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.InvalidConfigurationException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PathChromosomeTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;

    private EdgeGene firstToSecondGene;
    private EdgeGene secondToThirdGene;
    private ArrayList<EdgeGene> genes;
    private GeneticConfiguration conf;

    public PathChromosomeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeTest.class.getName()).log(Level.INFO, 
		"{0} TEST: PathChromosome", 
		PathChromosomeTest.class.toString());
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();

	conf = new GeneticConfiguration("EdgeGeneTest", graphMock.getGraph());

	firstToSecondGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	secondToThirdGene = new EdgeGene(graphMock.getSecondToThirdEdge(), conf);

	genes = new ArrayList<EdgeGene>();
	genes.add(firstToSecondGene);
	genes.add(secondToThirdGene);
    }

    @Test
    public void testConstructorNullConfiguration() throws InvalidConfigurationException {
	//It's ok, this is supposed to throw exception. In fact, we are testing if indeed it does
	PathChromosome chromosome;
	try {
	    chromosome = new PathChromosome(null, genes, 
		    graphMock.getFirstNode(), graphMock.getThirdNode());
	} catch (InvalidConfigurationException ex) {
	    Assert.assertTrue(ex.getClass() == InvalidConfigurationException.class);
	    Assert.assertTrue(ex.getMessage().equals("Configuration to be set must not be null!"));
	}
    }

    @Test
    public void testGetLowestCostGene() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	Assert.assertTrue(firstToSecondGene == chromosome.getLowestCostGene());
    }

    @Test
    public void testGetHighestCostGene() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	Assert.assertTrue(secondToThirdGene == chromosome.getHighestCostGene());
    }

    @Test
    public void testGetLowestCostGeneIndex() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	Assert.assertTrue(0 == chromosome.getLowestCostGeneIndex());
    }

    @Test
    public void testGetHighestCostGeneIndex() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	Assert.assertTrue(1 == chromosome.getHighestCostGeneIndex());
    }

    @Test
    public void testSetGene_2argsFirstGeneValidGene() throws InvalidConfigurationException {
	// Set a valid secondToFirstGene at the chromosome position. Expected: secondToFirstGene is set, genes list size not modified
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getFirstNode(), graphMock.getSecondNode());
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
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getSecondNode(), graphMock.getThirdNode());
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
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getSecondNode(), graphMock.getThirdNode());
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
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getSecondNode(), graphMock.getThirdNode());
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
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getFirstNode(), graphMock.getSecondNode());
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, true);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneValidGeneSkipValidity() throws InvalidConfigurationException {
	// Set a valid secondToFirstGene, without checking validity. Expected: secondToFirstGene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getFirstNode(), graphMock.getSecondNode());
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneInvalidGeneSkipValidity() throws InvalidConfigurationException {
	// Set a invalid secondToFirstGene, without checking validity. Expected: secondToFirstGene is set
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getSecondNode(), graphMock.getThirdNode());
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsFirstGeneInvalidGeneCheckValidity() throws InvalidConfigurationException {
	// Set an invalid secondToFirstGene at the chromosome position. Expected: secondToFirstGene is  not set, genes list continues unmodified
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getSecondNode(), graphMock.getThirdNode());
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, true);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertFalse(chromosome.getGene(0) == gene);
	Assert.assertTrue(genes.equals(chromosome.getGenesList()));
    }

    @Test
    public void testSetGene_3argsLastGeneValidGeneSkipValidity() throws InvalidConfigurationException {
	// Set an valid secondToFirstGene at the last position. Expected: secondToFirstGene is  set
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getFirstNode(), graphMock.getSecondNode());
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getGene(0) == gene);
    }

    @Test
    public void testSetGene_3argsLastGeneInvalidGeneCheckValidity() throws InvalidConfigurationException {
	// Set an invalid secondToFirstGene at the last position. Expected: secondToFirstGene is  not set
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	int originalNoOfGenes = chromosome.getGenesList().size();

	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getFirstNode(), graphMock.getSecondNode());
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(originalNoOfGenes - 1, gene, true);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertFalse(chromosome.getGene(originalNoOfGenes - 1) == gene);
	Assert.assertTrue(genes.equals(chromosome.getGenesList()));
    }

    @Test
    public void testGetCost_0argsValidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost() == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
    }

    @Test
    public void testGetCost_0argsInvalidChromosomeNullEdges() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	chromosome.setGenesList(null);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost() == Integer.MAX_VALUE);
    }

    @Test
    public void testGetCost_0argsInvalidChromosomeEmptyEdges() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	chromosome.setGenesList(new ArrayList<EdgeGene>());

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost() == Integer.MAX_VALUE);
    }

    @Test
    public void testGetCost_0argsInvalidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	DirectedWeightedEdge edge = new DirectedWeightedEdge(
		graphMock.getSecondNode(), graphMock.getThirdNode());
	EdgeGene gene = new EdgeGene(edge, conf);
	chromosome.setGene(0, gene, false);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost() == Integer.MAX_VALUE);
    }

    @Test
    public void testGetCost_2argsValidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(0, 1) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
    }

    @Test
    public void testGetCost_2argsValidChromosomeSameStartEndIndex() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(0, 0) == 
		graphMock.getFirstToSecondEdgeCost());
	Assert.assertTrue(chromosome.getCost(1, 1) == 
		graphMock.getSecondToThirdEdgeCost());
    }

    @Test
    public void testGetCost_2argsValidChromosomeStartIndexLowerThanZero() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(-1, 2) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	Assert.assertTrue(chromosome.getCost(-1, 1) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	Assert.assertTrue(chromosome.getCost(-1, 0) == 
		graphMock.getFirstToSecondEdgeCost());
    }

    @Test
    public void testGetCost_2argsValidChromosomeStartIndexLargerThanSize() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(3, 4) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	Assert.assertTrue(chromosome.getCost(2, 4) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
    }

    @Test
    public void testGetCost_2argsValidChromosomeEndIndexLargerThanSize() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(0, 3) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	Assert.assertTrue(chromosome.getCost(0, 2) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	Assert.assertTrue(chromosome.getCost(0, 1) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
    }

    @Test
    public void testGetCost_2argsValidChromosomeStartIndexLargerThanEndIndex() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(chromosome.getCost(5, 4) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	Assert.assertTrue(chromosome.getCost(5, 2) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	Assert.assertTrue(chromosome.getCost(5, 1) == 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	Assert.assertTrue(chromosome.getCost(5, 0) == 
		graphMock.getFirstToSecondEdgeCost());
    }

    @Test
    public void testIsLegalValidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(chromosome.isLegal());
    }

    @Test
    public void testIsLegalInvalidChromosome() throws InvalidConfigurationException {
	DirectedWeightedEdge edge1 = new DirectedWeightedEdge(
		graphMock.getSecondNode(), graphMock.getFirstNode());
	EdgeGene secondToFirstGene = new EdgeGene(edge1, conf);

	ArrayList<EdgeGene> edgeGenes = new ArrayList<EdgeGene>();
	edgeGenes.add(firstToSecondGene);
	edgeGenes.add(secondToFirstGene);
	edgeGenes.add(firstToSecondGene);

	PathChromosome chromosome = new PathChromosome(conf, edgeGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsLegalInvalidChromosomeEmptyEdges() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(), 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsLegalValidChromosomeNullSource() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		null, graphMock.getThirdNode());

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsLegalValidChromosomeNullDestination() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), null);

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsLegalValidChromosomeNullSourceAndDestination() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes, null, null);

	Assert.assertFalse(chromosome.isLegal());
    }

    @Test
    public void testIsFitterThanHigherFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost());
	
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanLowerFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost() + 1);
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanEqualFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanInvalidChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, 
		graphMock.getFirstNode(), graphMock.getSecondNode());

	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanEmptyChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = new PathChromosome(conf, new ArrayList<EdgeGene>(), 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(first.isFitterThan(second));
    }

    @Test
    public void testIsFitterThanNullChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = null;

	Assert.assertTrue(first.isFitterThan(second));
    }

    @Test
    public void testGetCyclicGeneSequenceValidChromosome() throws InvalidConfigurationException {
	DirectedWeightedEdge edge1 = new DirectedWeightedEdge(graphMock.getSecondNode(), graphMock.getFirstNode());
	EdgeGene secondToFirstGene = new EdgeGene(edge1, conf);
	DirectedWeightedEdge edge2 = new DirectedWeightedEdge(graphMock.getFirstNode(), graphMock.getThirdNode());
	EdgeGene firstToThirdGene = new EdgeGene(edge2, conf);

	ArrayList<EdgeGene> edgeGenes = new ArrayList<EdgeGene>();
	edgeGenes.add(firstToSecondGene);
	edgeGenes.add(secondToFirstGene);
	edgeGenes.add(firstToThirdGene);

	PathChromosome chromosome = new PathChromosome(conf, edgeGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	ArrayList<EdgeGene> cycle = chromosome.getCyclicGeneSequence();

	Assert.assertTrue(chromosome.isLegal());

	Assert.assertTrue(cycle.get(0) == firstToSecondGene);
	Assert.assertTrue(cycle.get(1) == secondToFirstGene);
    }

    @Test
    public void testGetCyclicGeneSequenceInvalidChromosome() throws InvalidConfigurationException {
	DirectedWeightedEdge edge1 = new DirectedWeightedEdge(graphMock.getSecondNode(), graphMock.getFirstNode());
	EdgeGene secondToFirstGene = new EdgeGene(edge1, conf);

	ArrayList<EdgeGene> edgeGenes = new ArrayList<EdgeGene>();
	edgeGenes.add(firstToSecondGene);
	edgeGenes.add(secondToFirstGene);
	edgeGenes.add(firstToSecondGene);

	PathChromosome chromosome = new PathChromosome(conf, edgeGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	ArrayList<EdgeGene> cycle = chromosome.getCyclicGeneSequence();

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(cycle.get(0) == firstToSecondGene);
	Assert.assertTrue(cycle.get(1) == secondToFirstGene);
    }

    @Test
    public void testGetCyclicGeneSequenceInvalidChromosomeCycleOnly() throws InvalidConfigurationException {
	DirectedWeightedEdge edge1 = new DirectedWeightedEdge(graphMock.getSecondNode(), graphMock.getFirstNode());
	EdgeGene secondToFirstGene = new EdgeGene(edge1, conf);

	ArrayList<EdgeGene> edgeGenes = new ArrayList<EdgeGene>();
	edgeGenes.add(firstToSecondGene);
	edgeGenes.add(secondToFirstGene);

	PathChromosome chromosome = new PathChromosome(conf, edgeGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	ArrayList<EdgeGene> cycle = chromosome.getCyclicGeneSequence();

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(cycle.get(0) == firstToSecondGene);
	Assert.assertTrue(cycle.get(1) == secondToFirstGene);
    }

    @Test
    public void testCompareToSmallerThan() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost());
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(first.compareTo(second) < 0);
	Assert.assertTrue(first.compareTo(second) == first.getFitnessValue() - second.getFitnessValue());
    }

    @Test
    public void testCompareToLargerThan() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost() + 1);
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(first.compareTo(second) > 0);
	Assert.assertTrue(first.compareTo(second) == first.getFitnessValue() - second.getFitnessValue());
    }

    @Test
    public void testCompareToEquals() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(),
		graphMock.getThirdNode(),
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(first.compareTo(second) == 0);
	Assert.assertTrue(first.compareTo(second) == first.getFitnessValue() - second.getFitnessValue());
    }

    @Test
    public void testCompareToEmptyChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = new PathChromosome(conf, new ArrayList<EdgeGene>(), 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(first.compareTo(second) == Integer.MAX_VALUE);
    }

    @Test
    public void testCompareToObject() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());
	Object second = new Object();

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(first.compareTo(second) == Integer.MAX_VALUE);
    }

    @Test
    public void testCompareToNull() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = null;

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(first.compareTo(second) == Integer.MAX_VALUE);
    }

    @Test
    public void testToJson() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());
	JsonObject firstJson = first.toJson();

	Assert.assertTrue(firstJson.has("nodeFrom"));
	Assert.assertTrue(firstJson.has("nodeTo"));
	Assert.assertTrue(firstJson.has("fitness"));
	Assert.assertTrue(firstJson.has("cost"));
	Assert.assertTrue(firstJson.has("isLegal"));
	Assert.assertTrue(firstJson.has("isSelectedForNextGeneration"));
	Assert.assertTrue(firstJson.has("age"));
	Assert.assertTrue(firstJson.has("path"));

	Assert.assertTrue(first.getGenesList().get(0).getAllele().getSource().toJson().equals(firstJson.get("nodeFrom")));
	Assert.assertTrue(first.getGenesList().get(first.getGenesList().size() - 1).getAllele().getDestination().toJson().equals(firstJson.get("nodeTo")));
	Assert.assertTrue(first.getFitnessValue() == firstJson.get("fitness").getAsDouble());
	Assert.assertTrue(first.getCost() == firstJson.get("cost").getAsDouble());
	Assert.assertTrue(first.isLegal() == firstJson.get("isLegal").getAsBoolean());
	Assert.assertTrue(first.isSelectedForNextGeneration() == firstJson.get("isSelectedForNextGeneration").getAsBoolean());
	Assert.assertTrue(first.getAge() == firstJson.get("age").getAsInt());
	Assert.assertTrue(first.getGenesList().size() == firstJson.getAsJsonArray("path").size());

	for (int i = 0; i < first.getGenesList().size(); i++) {
	    Assert.assertTrue(first.getGenesList().get(i).getAllele().toJson()
		    .equals(firstJson.getAsJsonArray(("path")).get(i)));
	}
    }

    @Test
    public void testToStringEquality() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(first.toString().equals(second.toString()));
    }

    @Test
    public void testToStringDifference() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(first.toString().equals(second.toString()));
    }

    @Test
    public void testEqualsEquality() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testEqualsDifference() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(first.equals(second));
    }

    @Test
    public void testEqualsNull() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = null;

	Assert.assertTrue(first.isLegal());
	Assert.assertFalse(first.equals(second));
    }

    @Test
    public void testEqualsObject() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	Object second = new Object();

	Assert.assertTrue(first.isLegal());
	Assert.assertFalse(first.equals(second));
    }

    @Test
    public void testHashCodeEquality() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(first.hashCode() == second.hashCode());
    }

    @Test
    public void testHashCodeDifference() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());
	DirectedWeightedGraph graph = graphMock.getGraph();
	graph.addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(firstToThirdGene);
	PathChromosome second = new PathChromosome(conf, secondGenes, graphMock.getFirstNode(), graphMock.getThirdNode());

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(first.hashCode() == second.hashCode());
    }

    @Test
    public void testClone() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, genes, 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosome second = first.clone();

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(first == second);
	Assert.assertTrue(first.equals(second));
    }

}
