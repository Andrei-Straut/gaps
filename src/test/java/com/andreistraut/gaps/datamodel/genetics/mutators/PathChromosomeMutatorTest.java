package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PathChromosomeMutatorTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private ArrayList<EdgeGene> genes;
    private PathChromosome chromosome;
    private PathChromosomeMutator mutator;
    private final int RUN_LIMIT = 10;
    
    public PathChromosomeMutatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeMutatorTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosomeMutator",
		PathChromosomeMutatorTest.class.toString());
    }

    
    @Before
    public void setUp() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("PathChromosomeCycleRemoveMutatorTest", this.graphMock.getGraph());
	this.conf.setKeepPopulationSizeConstant(false);

	DirectedWeightedEdge firstToSecond = graphMock.getFirstToSecondEdge();
	DirectedWeightedEdge secondToThird = graphMock.getSecondToThirdEdge();

	EdgeGene firstToSecondGene = new EdgeGene(firstToSecond, conf);
	EdgeGene secondToThirdGene = new EdgeGene(secondToThird, conf);

	this.genes = new ArrayList<EdgeGene>();
	this.genes.add(firstToSecondGene);
	this.genes.add(secondToThirdGene);

	this.chromosome = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());
	
	/*
	 * We wont't test these here, instead they'll be tested along with subclasses, so we won't implement them.
	 * Also, operate() will be tested in child classes
	 */
	this.mutator = new PathChromosomeMutator(conf) {
	    @Override
	    int getMutationPosition(RandomGenerator generator, PathChromosome chromosome) {
		return 0;
	    }
	    
	    @Override
	    void doMutation(PathChromosome chromosome, List candidateChromosomes, RandomGenerator generator) throws InvalidConfigurationException {
	    }
	};
    }

    @Test
    public void testConvertMutationRateLargerThan100() {
	double mutationRate = this.mutator.convertMutationRate(101);
	Assert.assertTrue(mutationRate == 1.0);
    }

    @Test
    public void testConvertMutationRate100() {
	double mutationRate = this.mutator.convertMutationRate(100);
	Assert.assertTrue(mutationRate == 1.0);
    }

    @Test
    public void testConvertMutationRateLowerThan100() {
	double mutationRate = this.mutator.convertMutationRate(50);
	Assert.assertTrue(mutationRate == 0.5);
    }

    @Test
    public void testConvertMutationRate1() {
	double mutationRate = this.mutator.convertMutationRate(1);
	Assert.assertTrue(mutationRate == 0.01);
    }

    @Test
    public void testConvertMutationRateLowerThan1() {
	double mutationRate = this.mutator.convertMutationRate((int) 0.4);
	Assert.assertTrue(mutationRate == 0.0);
	
	mutationRate = this.mutator.convertMutationRate((int) 0.5);
	Assert.assertTrue(mutationRate == 0.0);
	
	mutationRate = this.mutator.convertMutationRate((int) 0.6);
	Assert.assertTrue(mutationRate == 0.0);
    }

    @Test
    public void testConvertMutationRateNegative() {
	double mutationRate = this.mutator.convertMutationRate(-50);
	Assert.assertTrue(mutationRate == 0.0);
    }

    @Test
    public void testSetMutationRateIntLargerThan100() {
	this.mutator.setMutationRate(101);
	Assert.assertTrue(this.mutator.mutationRate == 1.0);
    }

    @Test
    public void testSetMutationIntRate100() {
	this.mutator.setMutationRate(100);
	Assert.assertTrue(this.mutator.mutationRate == 1.0);
    }

    @Test
    public void testSetMutationRateIntLowerThan100() {
	this.mutator.setMutationRate(50);
	Assert.assertTrue(this.mutator.mutationRate == 0.5);
    }

    @Test
    public void testSetMutationIntRate1() {
	this.mutator.setMutationRate(1);
	Assert.assertTrue(this.mutator.mutationRate == 0.01);
    }

    @Test
    public void testSetMutationRateIntLowerThan1() {
	this.mutator.setMutationRate((int) 0.4);
	Assert.assertTrue(this.mutator.mutationRate == 0.0);
	
	this.mutator.setMutationRate((int) 0.5);
	Assert.assertTrue(this.mutator.mutationRate == 0.0);
	
	this.mutator.setMutationRate((int) 0.6);
	Assert.assertTrue(this.mutator.mutationRate == 0.0);
    }

    @Test
    public void testSetMutationRateIntNegative() {
	this.mutator.setMutationRate(-50);
	Assert.assertTrue(this.mutator.mutationRate == 0.0);
    }

    @Test
    public void testGetMutationRateIntLargerThan100() {
	this.mutator.setMutationRate(101);
	Assert.assertTrue(this.mutator.getMutationRate() == 100);
    }

    @Test
    public void testGetMutationIntRate100() {
	this.mutator.setMutationRate(100);
	Assert.assertTrue(this.mutator.getMutationRate() == 100);
    }

    @Test
    public void testGetMutationRateIntLowerThan100() {
	this.mutator.setMutationRate(50);
	Assert.assertTrue(this.mutator.getMutationRate() == 50);
    }

    @Test
    public void testGetMutationIntRate1() {
	this.mutator.setMutationRate(1);
	Assert.assertTrue(this.mutator.getMutationRate() == 1);
    }

    @Test
    public void testGetMutationRateIntLowerThan1() {
	this.mutator.setMutationRate((int) 0.4);
	Assert.assertTrue(this.mutator.getMutationRate() == 0);
	
	this.mutator.setMutationRate((int) 0.5);
	Assert.assertTrue(this.mutator.getMutationRate() == 0);
	
	this.mutator.setMutationRate((int) 0.6);
	Assert.assertTrue(this.mutator.getMutationRate() == 0);
    }

    @Test
    public void testGetMutationRateIntNegative() {
	this.mutator.setMutationRate(-50);
	Assert.assertTrue(this.mutator.getMutationRate() == 0);
    }
}
