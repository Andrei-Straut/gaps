package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomePopulation;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.StockRandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PathChromosomeCycleRemoveMutatorTest {

    private DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private ArrayList<EdgeGene> genes;
    private PathChromosome circularChromosome;
    private PathChromosome expected;
    private final int RUN_LIMIT = 10;

    public PathChromosomeCycleRemoveMutatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeCycleRemoveMutatorTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosomeCycleRemoveMutator",
		PathChromosomeCycleRemoveMutatorTest.class.toString());
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("PathChromosomeCycleRemoveMutatorTest", this.graphMock.getGraph());
	this.conf.setKeepPopulationSizeConstant(false);

	DirectedWeightedEdge firstToSecond = graphMock.getFirstToSecondEdge();
	DirectedWeightedEdge secondToFirst = new DirectedWeightedEdge(
		graphMock.getSecondNode(),
		graphMock.getFirstNode(),
		graphMock.getFirstToSecondEdgeCost());
	DirectedWeightedEdge firstToThird = graphMock.getFirstToThirdEdge();

	EdgeGene firstToSecondGene = new EdgeGene(firstToSecond, conf);
	EdgeGene secondToFirstGene = new EdgeGene(secondToFirst, conf);
	EdgeGene firstToThirdGene = new EdgeGene(firstToThird, conf);

	this.genes = new ArrayList<EdgeGene>();
	this.genes.add(firstToSecondGene);
	this.genes.add(secondToFirstGene);
	this.genes.add(firstToThirdGene);

	this.circularChromosome = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	ArrayList<EdgeGene> expectedGenes = new ArrayList<EdgeGene>();
	expectedGenes.add(new EdgeGene(
		new DirectedWeightedEdge(
			graphMock.getFirstNode(),
			graphMock.getThirdNode(),
			1),
		conf));
	expected = new PathChromosome(conf, expectedGenes,
		graphMock.getFirstNode(), graphMock.getThirdNode());
    }

    @Test
    public void testDoMutationOneCandidateValidCircular100Percent() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.circularChromosome);

	    PathChromosomeCycleRemoveMutator mutator = new PathChromosomeCycleRemoveMutator(conf, 100);
	    
	    mutator.doMutation(this.circularChromosome, candidates, new StockRandomGenerator());

	    Assert.assertTrue(candidates.size() == 1);
	    Assert.assertTrue(this.circularChromosome.equals(expected));
	    Assert.assertTrue(candidates.contains(this.circularChromosome));
	}
    }

    @Test
    public void testDoMutationOneCandidateValidCircular0Percent() throws Exception {
	ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	candidates.add(this.circularChromosome);

	PathChromosomeCycleRemoveMutator mutator = new PathChromosomeCycleRemoveMutator(conf, 0);
	mutator.doMutation(this.circularChromosome, candidates, new StockRandomGenerator());

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertTrue(candidates.size() == 1);
	    Assert.assertFalse(this.circularChromosome.equals(expected));
	    Assert.assertTrue(candidates.contains(this.circularChromosome));
	    Assert.assertFalse(candidates.contains(expected));
	}
    }

    @Test
    public void testOperateOneCandidateValidCircular100PercentKeepPopulationSizeConstant() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.circularChromosome);

	    PathChromosomePopulation population = new PathChromosomePopulation(conf, candidates);
	    conf.setKeepPopulationSizeConstant(true);
	    PathChromosomeCycleRemoveMutator mutator = new PathChromosomeCycleRemoveMutator(conf, 100);
	    mutator.operate(population, candidates);
	    
	    Assert.assertTrue(candidates.size() == 1);
	    Assert.assertFalse(this.circularChromosome.equals(expected));
	    Assert.assertTrue(candidates.contains(this.circularChromosome));
	    Assert.assertTrue(!candidates.contains(expected));
	}
    }

    @Test
    public void testOperateOneCandidateValidCircular0PercentKeepPopulationSizeConstant() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.circularChromosome);

	    PathChromosomePopulation population = new PathChromosomePopulation(conf, candidates);
	    conf.setKeepPopulationSizeConstant(true);
	    PathChromosomeCycleRemoveMutator mutator = new PathChromosomeCycleRemoveMutator(conf, 0);
	    mutator.operate(population, candidates);
	    
	    Assert.assertTrue(candidates.size() == 1);
	    Assert.assertFalse(this.circularChromosome.equals(expected));
	    Assert.assertTrue(candidates.contains(this.circularChromosome));
	    Assert.assertTrue(!candidates.contains(expected));
	}
    }

    @Test
    public void testOperateOneCandidateValidCircular100Percent() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.circularChromosome);

	    PathChromosomePopulation population = new PathChromosomePopulation(conf, candidates);
	    PathChromosomeCycleRemoveMutator mutator = new PathChromosomeCycleRemoveMutator(conf, 100);
	    mutator.operate(population, candidates);
	    
	    Assert.assertTrue(candidates.size() == 2);
	    Assert.assertFalse(this.circularChromosome.equals(expected));
	    Assert.assertTrue(candidates.contains(this.circularChromosome));
	    Assert.assertTrue(candidates.contains(expected));
	}
    }

    @Test
    public void testOperateOneCandidateValidCircular0Percent() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.circularChromosome);

	    PathChromosomePopulation population = new PathChromosomePopulation(conf, candidates);
	    PathChromosomeCycleRemoveMutator mutator = new PathChromosomeCycleRemoveMutator(conf, 0);
	    mutator.operate(population, candidates);
	    
	    Assert.assertTrue(candidates.size() == 2);
	    Assert.assertFalse(this.circularChromosome.equals(expected));
	    Assert.assertTrue(candidates.contains(this.circularChromosome));
	    Assert.assertFalse(candidates.contains(expected));
	}
    }
}
