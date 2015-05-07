package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomePopulation;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockOneGene;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.StockRandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class PathChromosomeOnePointCrossoverTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private PathChromosome firstChromosome;
    private EdgeGene firstToThirdSecondGene;
    private ArrayList<EdgeGene> secondChromosomeGenes;
    private PathChromosome secondChromosome;
    private final int RUN_LIMIT = 10;

    public PathChromosomeOnePointCrossoverTest() {
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("DirectedWeightedGraphMockThreeNodeThreeEdges", this.graphMock.getGraph());
	this.conf.setKeepPopulationSizeConstant(false);
	this.firstChromosome = new PathChromosomeMockOneGene(conf).getChromosome();

	DirectedWeightedEdge firstToThirdSecondEdge = new DirectedWeightedEdge(
		graphMock.getFirstNode(),
		graphMock.getThirdNode(),
		graphMock.getFirstToSecondEdgeCost() + 1);
	graphMock.getGraph().addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThirdSecondEdge);

	this.firstToThirdSecondGene = new EdgeGene(firstToThirdSecondEdge, conf);
	this.secondChromosomeGenes = new ArrayList<EdgeGene>();
	this.secondChromosomeGenes.add(firstToThirdSecondGene);
	this.secondChromosome = new PathChromosome(conf, this.secondChromosomeGenes, graphMock.getFirstNode(), graphMock.getThirdNode());
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeOnePointCrossoverTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosomeOnePointCrossoverTest",
		PathChromosomeOnePointCrossoverTest.class.toString());
    }

    @Test
    public void testDoCrossover100PercentValid() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    this.setUp();

	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.firstChromosome);
	    candidates.add(this.secondChromosome);

	    PathChromosomeOnePointCrossover mutator = new PathChromosomeOnePointCrossover(conf, 100);
	    mutator.setPrintCrossoverStatistics(true);
	    Assert.assertFalse(this.firstChromosome.equals(this.secondChromosome));
	    Assert.assertTrue(this.firstChromosome.isLegal());
	    Assert.assertTrue(this.secondChromosome.isLegal());

	    mutator.doCrossover(this.firstChromosome, this.secondChromosome, candidates, new StockRandomGenerator());

	    Assert.assertTrue(this.firstChromosome.isLegal());
	    Assert.assertTrue(this.secondChromosome.isLegal());
	    Assert.assertFalse(this.firstChromosome.equals(secondChromosome));

	    boolean containsGene = false;	    
	    for (EdgeGene gene : this.firstChromosome.getGenesList()) {
		if (gene.equals(this.firstToThirdSecondGene)) {
		    containsGene = true;
		    break;
		}
	    }
	    Assert.assertTrue(containsGene);

	    containsGene = false;
	    for (EdgeGene gene : this.secondChromosome.getGenesList()) {
		if (gene.equals(this.firstToThirdSecondGene)) {
		    containsGene = true;
		    break;
		}
	    }
	    Assert.assertFalse(containsGene);
	}
    }

    @Test
    public void testDoCrossover0PercentValid() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    this.setUp();

	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.firstChromosome);
	    candidates.add(this.secondChromosome);

	    PathChromosomeOnePointCrossover mutator = new PathChromosomeOnePointCrossover(conf, 0);
	    Assert.assertFalse(this.firstChromosome.equals(this.secondChromosome));
	    Assert.assertTrue(this.firstChromosome.isLegal());
	    Assert.assertTrue(this.secondChromosome.isLegal());

	    mutator.doCrossover(this.firstChromosome, this.secondChromosome, candidates, new StockRandomGenerator());

	    Assert.assertTrue(this.firstChromosome.isLegal());
	    Assert.assertTrue(this.secondChromosome.isLegal());
	    Assert.assertFalse(this.firstChromosome.equals(secondChromosome));

	    boolean containsGene = false;
	    for (EdgeGene gene : this.firstChromosome.getGenesList()) {
		if (gene.equals(this.firstToThirdSecondGene)) {
		    containsGene = true;
		    break;
		}
	    }
	    Assert.assertFalse(containsGene);

	    containsGene = false;
	    for (EdgeGene gene : this.secondChromosome.getGenesList()) {
		if (gene.equals(this.firstToThirdSecondGene)) {
		    containsGene = true;
		    break;
		}
	    }
	    Assert.assertTrue(containsGene);
	}
    }    

    @Test
    public void testOperateTwoCandidatesValid100PercentKeepPopulationSizeConstant() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    this.setUp();
	    
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.firstChromosome);
	    candidates.add(this.secondChromosome);

	    PathChromosomeOnePointCrossover mutator = new PathChromosomeOnePointCrossover(conf, 100);
	    conf.setKeepPopulationSizeConstant(true);
	    Assert.assertFalse(this.firstChromosome.equals(this.secondChromosome));
	    Assert.assertTrue(this.firstChromosome.isLegal());
	    Assert.assertTrue(this.secondChromosome.isLegal());

	    PathChromosomePopulation population = new PathChromosomePopulation(conf, candidates);
	    mutator.operate(population, candidates);
	    
	    Assert.assertTrue(this.firstChromosome.isLegal());
	    Assert.assertTrue(this.secondChromosome.isLegal());
	    Assert.assertFalse(this.firstChromosome.equals(secondChromosome));
	}
    }

    @Test
    public void testOperateTwoCandidatesValid00PercentKeepPopulationSizeConstant() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    this.setUp();
	    
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.firstChromosome);
	    candidates.add(this.secondChromosome);

	    PathChromosomeOnePointCrossover mutator = new PathChromosomeOnePointCrossover(conf, 0);
	    conf.setKeepPopulationSizeConstant(true);
	    Assert.assertFalse(this.firstChromosome.equals(this.secondChromosome));
	    Assert.assertTrue(this.firstChromosome.isLegal());
	    Assert.assertTrue(this.secondChromosome.isLegal());

	    PathChromosomePopulation population = new PathChromosomePopulation(conf, candidates);
	    mutator.operate(population, candidates);
	    
	    Assert.assertTrue(this.firstChromosome.isLegal());
	    Assert.assertTrue(this.secondChromosome.isLegal());
	    Assert.assertFalse(this.firstChromosome.equals(secondChromosome));
	}
    }
}
