package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.mock.ThreeNodeTwoEdgesDirectedWeightedGraphMock;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.InvalidConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class PathChromosomeFitnessTest {

    private ThreeNodeTwoEdgesDirectedWeightedGraphMock graphMock;
    private GeneticConfiguration conf;
    private ArrayList<EdgeGene> genes;

    public PathChromosomeFitnessTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeFitnessTest.class.getName()).log(Level.INFO,
		PathChromosomeFitnessTest.class.toString() + " TEST: PathChromosome Fitness");
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	graphMock = new ThreeNodeTwoEdgesDirectedWeightedGraphMock();
	conf = new GeneticConfiguration("EdgeGeneTest", graphMock.getGraph());
	EdgeGene firstToSecondGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondToThirdGene = new EdgeGene(graphMock.getSecondToThirdEdge(), conf);

	genes = new ArrayList<EdgeGene>();
	genes.add(firstToSecondGene);
	genes.add(secondToThirdGene);
    }

    @Test
    public void testGetLastComputedFitnessValue() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes,
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double originalFitnessValue = fitness.getLastComputedFitnessValue();

	Assert.assertTrue(originalFitnessValue == 0);
	fitness.evaluate(chromosome);

	Assert.assertTrue(originalFitnessValue != fitness.getLastComputedFitnessValue());
	Assert.assertTrue(fitness.getLastComputedFitnessValue() == chromosome.getFitnessValue());
    }

    @Test
    public void testEvaluateValidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, genes,
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(chromosome);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(fitnessValue == chromosome.getFitnessValue());
    }

    @Test
    public void testEvaluateInvalidChromosomeEmptyPath() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(),
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(chromosome);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(fitnessValue == chromosome.getFitnessValue());
	Assert.assertTrue(fitnessValue == chromosome.getMinFitnessValue());
    }

    @Test
    public void testEvaluateInvalidChromosomeIncompletePath() throws InvalidConfigurationException {
	genes.remove(genes.get(0));

	PathChromosome chromosome = new PathChromosome(conf, genes,
		graphMock.getFirstNode(), graphMock.getThirdNode());
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(chromosome);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(fitnessValue == chromosome.getFitnessValue());
	Assert.assertTrue(fitnessValue == chromosome.getMinFitnessValue());
    }

    @Test
    public void testEvaluateObject() throws InvalidConfigurationException {
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(new org.jgap.Chromosome(conf));

	Assert.assertTrue(fitnessValue == Integer.MIN_VALUE);
    }

    @Test
    public void testEvaluateNull() throws InvalidConfigurationException {
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(null);

	Assert.assertTrue(fitnessValue == Integer.MIN_VALUE);
    }

}
