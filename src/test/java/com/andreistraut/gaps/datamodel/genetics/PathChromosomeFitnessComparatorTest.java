package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockOneGene;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockTwoGene;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.Chromosome;
import org.jgap.InvalidConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class PathChromosomeFitnessComparatorTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private ArrayList<EdgeGene> genes;

    public PathChromosomeFitnessComparatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeFitnessComparatorTest.class.getName()).log(Level.INFO, 
		"{0} TEST: PathChromosome Fitness", 
		PathChromosomeFitnessComparatorTest.class.toString());
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();
	conf = new GeneticConfiguration("PathChromosomeFitnessEvaluatorTest", graphMock.getGraph());
	EdgeGene firstToSecondGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondToThirdGene = new EdgeGene(graphMock.getSecondToThirdEdge(), conf);

	genes = new ArrayList<EdgeGene>();
	genes.add(firstToSecondGene);
	genes.add(secondToThirdGene);
    }

    @Test
    public void testIsValidFitterThanValidBetterFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();
	second.setFitnessValue(first.getFitnessValue() - 1);

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(comparator.isFitter(first, second));
    }

    @Test
    public void testIsValidFitterThanValidWorseFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockTwoGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockOneGene(conf).getChromosome();

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(comparator.isFitter(first, second));
    }

    @Test
    public void testIsValidFitterThanInvalid() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();
	second.setGenesList(new ArrayList<EdgeGene>());
	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(comparator.isFitter(first, second));
    }

    @Test
    public void testIsInvalidFitterThanInvalidFalse() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();
	first.setGenesList(new ArrayList<EdgeGene>());
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(new PathChromosomeMockTwoGene(conf).getFirstToSecondGene());
	second.setGenesList(secondGenes);

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertFalse(first.isLegal());
	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(first.compareTo(second) == 0);
	Assert.assertFalse(comparator.isFitter(first, second));
    }

    @Test
    public void testIsChromosomeFitterThanObject() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(comparator.isFitter(first, new Chromosome(conf)));
    }

    @Test
    public void testIsChromosomeFitterThanNull() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(comparator.isFitter(first, null));
    }

    @Test
    public void testIsObjectFitterThanChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertFalse(comparator.isFitter(new Chromosome(conf), first));
    }

    @Test
    public void testIsNullFitterThanChromosome() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertFalse(comparator.isFitter(null, first));
    }

    @Test
    public void testCompareValidToValidBetterFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(comparator.compare(first, second) > 0);
	Assert.assertTrue(comparator.compare(first, second) == first.getFitnessValue() - second.getFitnessValue());
    }

    @Test
    public void testCompareValidToValidWorseFitness() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockTwoGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockOneGene(conf).getChromosome();

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertFalse(comparator.compare(first, second) > 0);
	Assert.assertTrue(comparator.compare(first, second) == first.getFitnessValue() - second.getFitnessValue());
    }

    @Test
    public void testCompareValidToInvalidEmptyPath() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();
	second.setGenesList(new ArrayList<EdgeGene>());

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(comparator.compare(first, second) > 0);
	Assert.assertTrue(comparator.compare(first, second) == Integer.MAX_VALUE);
    }

    @Test
    public void testCompareValidToInvalidIncompletePath() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();
	ArrayList<EdgeGene> secondGenes = new ArrayList<EdgeGene>();
	secondGenes.add(new PathChromosomeMockTwoGene(conf).getFirstToSecondGene());
	second.setGenesList(secondGenes);

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertFalse(second.isLegal());
	Assert.assertTrue(comparator.compare(first, second) > 0);
	Assert.assertTrue(comparator.compare(first, second) == Integer.MAX_VALUE);
    }

    @Test
    public void testCompareValidToNull() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosome second = null;

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(comparator.compare(first, second) > 0);
	Assert.assertTrue(comparator.compare(first, second) == Integer.MAX_VALUE);
    }

    @Test
    public void testCompareValidToObject() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(first.isLegal());
	Assert.assertTrue(comparator.compare(first, new Object()) > 0);
	Assert.assertTrue(comparator.compare(first, new Object()) == Integer.MAX_VALUE);
    }

    @Test
    public void testCompareInvalidIncompletePathToValid() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockTwoGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockOneGene(conf).getChromosome();
	ArrayList<EdgeGene> firstGenes = new ArrayList<EdgeGene>();
	firstGenes.add(new PathChromosomeMockTwoGene(conf).getFirstToSecondGene());
	first.setGenesList(firstGenes);

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertFalse(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(comparator.compare(first, second) < 0);
	Assert.assertTrue(comparator.compare(first, second) == Integer.MIN_VALUE);
    }

    @Test
    public void testCompareInvalidEmptyPathToValid() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosomeMockOneGene(conf).getChromosome();
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();
	first.setGenesList(new ArrayList<EdgeGene>());

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertFalse(first.isLegal());
	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(comparator.compare(first, second) < 0);
	Assert.assertTrue(comparator.compare(first, second) == Integer.MIN_VALUE);
    }

    @Test
    public void testCompareNullToValid() throws InvalidConfigurationException {
	PathChromosome first = null;
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(comparator.compare(first, second) < 0);
	System.out.println(comparator.compare(first, second));
	Assert.assertTrue(comparator.compare(first, second) == Integer.MIN_VALUE);
    }

    @Test
    public void testCompareObjectToValid() throws InvalidConfigurationException {
	PathChromosome second = new PathChromosomeMockTwoGene(conf).getChromosome();

	PathChromosomeFitnessComparator comparator = new PathChromosomeFitnessComparator();

	Assert.assertTrue(second.isLegal());
	Assert.assertTrue(comparator.compare(new Object(), second) < 0);
	Assert.assertTrue(comparator.compare(new Object(), second) == Integer.MIN_VALUE);
    }
}
