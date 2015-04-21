package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockOneGene;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockTwoGene;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.InvalidConfigurationException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PathChromosomePopulationTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private PathChromosome oneGeneChromosome;
    private PathChromosome twoGeneChromosome;
    ArrayList<PathChromosome> chromosomes;

    public PathChromosomePopulationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomePopulationTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosome Population",
		PathChromosomePopulationTest.class.toString());
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("PathChromosomePopulationTest", this.graphMock.getGraph());

	this.oneGeneChromosome = new PathChromosomeMockOneGene(this.conf).getChromosome();
	this.twoGeneChromosome = new PathChromosomeMockTwoGene(this.conf).getChromosome();
	this.chromosomes = new ArrayList<PathChromosome>();
	this.chromosomes.add(this.oneGeneChromosome);
	this.chromosomes.add(this.twoGeneChromosome);
    }

    @Test
    public void testDetermineFittestChromosomeTwoValidChromosomes() throws InvalidConfigurationException {
	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, this.chromosomes);
	Assert.assertTrue(pop.determineFittestChromosome() == this.oneGeneChromosome);
    }

    @Test
    public void testDetermineFittestChromosomeOneValidChromosome() throws InvalidConfigurationException {
	PathChromosome nullChromosome = null;
	PathChromosome notNullChromosome = new PathChromosome(this.conf, new ArrayList<EdgeGene>(),
		this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	ArrayList<PathChromosome> chromos = new ArrayList<PathChromosome>();
	chromos.add(nullChromosome);
	chromos.add(notNullChromosome);
	chromos.add(this.oneGeneChromosome);

	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, chromos);
	Assert.assertTrue(pop.determineFittestChromosome() == this.oneGeneChromosome);
    }

    @Test
    public void testDetermineFittestChromosomeNoValidChromosomes() throws InvalidConfigurationException {

	PathChromosome notNullChromosome = new PathChromosome(this.conf, new ArrayList<EdgeGene>(),
		this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosome nullChromosome = null;
	ArrayList<PathChromosome> chromos = new ArrayList<PathChromosome>();
	chromos.add(notNullChromosome);
	chromos.add(nullChromosome);

	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, chromos);
	Assert.assertTrue(pop.determineFittestChromosome() == notNullChromosome);
    }

    @Test
    public void testDetermineFittestChromosomeValidStartEndIndex() throws InvalidConfigurationException {
	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, this.chromosomes);
	Assert.assertTrue(pop.determineFittestChromosome(0, 1) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(0, 2) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(1, 2) == this.twoGeneChromosome);
    }

    @Test
    public void testDetermineFittestChromosomeStartIndexEqualsEndIndex() throws InvalidConfigurationException {
	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, this.chromosomes);
	Assert.assertTrue(pop.determineFittestChromosome(0, 0) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(1, 1) == this.twoGeneChromosome);
    }

    @Test
    public void testDetermineFittestChromosomeStartIndexLargerThanSize() throws InvalidConfigurationException {
	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, this.chromosomes);
	Assert.assertTrue(pop.determineFittestChromosome(3, 1) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(2, 1) == this.oneGeneChromosome);
    }

    @Test
    public void testDetermineFittestChromosomeEndIndexLargerThanSize() throws InvalidConfigurationException {
	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, this.chromosomes);
	Assert.assertTrue(pop.determineFittestChromosome(0, 4) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(0, 3) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(0, 2) == this.oneGeneChromosome);
    }

    @Test
    public void testDetermineFittestChromosomeStartEndIndexLargerThanSize() throws InvalidConfigurationException {
	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, this.chromosomes);
	Assert.assertTrue(pop.determineFittestChromosome(2, 4) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(3, 4) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(4, 4) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(5, 4) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(6, 4) == this.oneGeneChromosome);
	Assert.assertTrue(pop.determineFittestChromosome(6, 5) == this.oneGeneChromosome);
    }

    @Test
    public void testDetermineFittestChromosomes() throws InvalidConfigurationException {
	this.chromosomes = new ArrayList<PathChromosome>();
	this.chromosomes.add(null);
	this.chromosomes.add(null);
	this.chromosomes.add(new PathChromosome(this.conf));
	this.chromosomes.add(new PathChromosome(this.conf));
	this.chromosomes.add(new PathChromosomeMockOneGene(this.conf).getChromosome());
	this.chromosomes.add(new PathChromosomeMockOneGene(this.conf).getChromosome());
	this.chromosomes.add(new PathChromosomeMockTwoGene(this.conf).getChromosome());
	this.chromosomes.add(new PathChromosomeMockTwoGene(this.conf).getChromosome());
	
	PathChromosomePopulation pop = new PathChromosomePopulation(this.conf, this.chromosomes);
	List<PathChromosome> fittest = pop.determineFittestChromosomes(1);
	Assert.assertTrue(fittest.size() == 1);
	Assert.assertTrue(fittest.get(0).equals(this.oneGeneChromosome));
	
	fittest = pop.determineFittestChromosomes(2);
	Assert.assertTrue(fittest.size() == 2);
	Assert.assertTrue(fittest.get(0).equals(this.oneGeneChromosome));
	Assert.assertTrue(fittest.get(1).equals(this.oneGeneChromosome));
	
	fittest = pop.determineFittestChromosomes(3);
	Assert.assertTrue(fittest.size() == 3);
	Assert.assertTrue(fittest.get(0).equals(this.oneGeneChromosome));
	Assert.assertTrue(fittest.get(1).equals(this.oneGeneChromosome));
	Assert.assertTrue(fittest.get(2).equals(this.twoGeneChromosome));
    }
}
