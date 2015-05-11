package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockOneGene;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockTwoGene;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.InvalidConfigurationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PathChromosomeCrossoverValidatorTest {

    private DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private PathChromosome oneGeneChromosome;
    private PathChromosome twoGeneChromosome;
    private final int RUN_LIMIT = 10;

    public PathChromosomeCrossoverValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeCrossoverValidatorTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosomeCrossoverValidatorTest",
		PathChromosomeCrossoverValidatorTest.class.toString());
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("PathChromosomeCrossoverValidatorTest", this.graphMock.getGraph());

	this.oneGeneChromosome = new PathChromosomeMockOneGene(this.conf).getChromosome();
	this.twoGeneChromosome = new PathChromosomeMockTwoGene(this.conf).getChromosome();
    }

    @Test
    public void testCanCrossover100PercentTwoValid() {
	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(this.oneGeneChromosome, this.twoGeneChromosome, 100);

	// make sure it works in repeated runs
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertTrue(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover100PercentValidWithInvalidEmptyPath() throws InvalidConfigurationException {
	PathChromosome second = new PathChromosome(conf, new ArrayList<EdgeGene>(), this.graphMock.getFirstNode(), this.graphMock.getThirdNode());

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(this.oneGeneChromosome, second, 100);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover100PercentValidWithNull() throws InvalidConfigurationException {
	PathChromosome second = null;

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(this.oneGeneChromosome, second, 100);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover100PercentInvalidEmptyPathWithValid() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, new ArrayList<EdgeGene>(), this.graphMock.getFirstNode(), this.graphMock.getThirdNode());

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(first, this.oneGeneChromosome, 100);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover100PercentNullWithValid() throws InvalidConfigurationException {
	PathChromosome first = null;

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(first, this.oneGeneChromosome, 100);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover100PercentInvalidEmptyPathWithInvalidEmptyPath() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, new ArrayList<EdgeGene>(), this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosome second = new PathChromosome(conf, new ArrayList<EdgeGene>(), this.graphMock.getFirstNode(), this.graphMock.getThirdNode());

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(first, second, 100);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover100PercentNullWithNull() throws InvalidConfigurationException {
	PathChromosome first = null;
	PathChromosome second = null;

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(first, second, 100);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover0PercentTwoValid() {
	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(this.oneGeneChromosome, this.twoGeneChromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover0PercentValidWithInvalidEmptyPath() throws InvalidConfigurationException {
	PathChromosome second = new PathChromosome(conf, new ArrayList<EdgeGene>(), this.graphMock.getFirstNode(), this.graphMock.getThirdNode());

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(this.oneGeneChromosome, second, 0);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover0PercentValidWithNull() throws InvalidConfigurationException {
	PathChromosome second = null;

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(this.oneGeneChromosome, second, 0);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover0PercentInvalidEmptyPathWithValid() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, new ArrayList<EdgeGene>(), this.graphMock.getFirstNode(), this.graphMock.getThirdNode());

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(first, this.oneGeneChromosome, 0);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover0PercentNullWithValid() throws InvalidConfigurationException {
	PathChromosome first = null;

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(first, this.oneGeneChromosome, 0);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover10PercentInvalidEmptyPathWithInvalidEmptyPath() throws InvalidConfigurationException {
	PathChromosome first = new PathChromosome(conf, new ArrayList<EdgeGene>(), this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosome second = new PathChromosome(conf, new ArrayList<EdgeGene>(), this.graphMock.getFirstNode(), this.graphMock.getThirdNode());

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(first, second, 0);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

    @Test
    public void testCanCrossover0PercentNullWithNull() throws InvalidConfigurationException {
	PathChromosome first = null;
	PathChromosome second = null;

	PathChromosomeCrossoverValidator validator = new PathChromosomeCrossoverValidator(first, second, 0);
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canCrossover());
	}
    }

}
