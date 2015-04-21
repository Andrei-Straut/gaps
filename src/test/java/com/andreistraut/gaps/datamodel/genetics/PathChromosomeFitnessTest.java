package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockTwoGene;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.InvalidConfigurationException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PathChromosomeFitnessTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private ArrayList<EdgeGene> genes;

    public PathChromosomeFitnessTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeFitnessTest.class.getName()).log(Level.INFO, 
		"{0} TEST: PathChromosome Fitness", 
		PathChromosomeFitnessTest.class.toString());
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();
	conf = new GeneticConfiguration("PathChromosomeFitnessTest", graphMock.getGraph());
	EdgeGene firstToSecondGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondToThirdGene = new EdgeGene(graphMock.getSecondToThirdEdge(), conf);

	genes = new ArrayList<EdgeGene>();
	genes.add(firstToSecondGene);
	genes.add(secondToThirdGene);
    }

    @Test
    public void testGetLastComputedFitnessValue() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosomeMockTwoGene(conf).getChromosome();
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double originalFitnessValue = fitness.getLastComputedFitnessValue();

	Assert.assertTrue(originalFitnessValue == 0);
	fitness.evaluate(chromosome);

	Assert.assertTrue(originalFitnessValue != fitness.getLastComputedFitnessValue());
	Assert.assertTrue(fitness.getLastComputedFitnessValue() == chromosome.getFitnessValue());
    }

    @Test
    public void testEvaluateValidChromosome() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosomeMockTwoGene(conf).getChromosome();
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(chromosome);

	Assert.assertTrue(chromosome.isLegal());
	Assert.assertTrue(fitnessValue == chromosome.getFitnessValue());
	Assert.assertTrue(fitness.getFitnessValue(chromosome) == chromosome.getFitnessValue());
    }

    @Test
    public void testEvaluateInvalidChromosomeEmptyPath() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosomeMockTwoGene(conf).getChromosome();
	chromosome.setGenesList(new ArrayList<EdgeGene>());
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(chromosome);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(fitnessValue == chromosome.getFitnessValue());
	Assert.assertTrue(fitnessValue == chromosome.getMinFitnessValue());
	Assert.assertTrue(fitness.getFitnessValue(chromosome) == chromosome.getMinFitnessValue());
    }

    @Test
    public void testEvaluateInvalidChromosomeIncompletePath() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosomeMockTwoGene(conf).getChromosome();
	chromosome.getGenesList().remove(0);
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(chromosome);

	Assert.assertFalse(chromosome.isLegal());
	Assert.assertTrue(fitnessValue == chromosome.getFitnessValue());
	Assert.assertTrue(fitnessValue == chromosome.getMinFitnessValue());
	Assert.assertTrue(fitness.getFitnessValue(chromosome) == chromosome.getMinFitnessValue());
    }

    @Test
    public void testEvaluateObject() throws InvalidConfigurationException {
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(new org.jgap.Chromosome(conf));

	Assert.assertTrue(fitnessValue == Integer.MIN_VALUE);
	Assert.assertTrue(fitness.getFitnessValue(new org.jgap.Chromosome(conf)) == Integer.MIN_VALUE);
    }

    @Test
    public void testEvaluateNull() throws InvalidConfigurationException {
	PathChromosomeFitness fitness = new PathChromosomeFitness();
	double fitnessValue = fitness.evaluate(null);

	Assert.assertTrue(fitnessValue == Integer.MIN_VALUE);
	Assert.assertTrue(fitness.getFitnessValue(null) == Integer.MIN_VALUE);
    }

}
