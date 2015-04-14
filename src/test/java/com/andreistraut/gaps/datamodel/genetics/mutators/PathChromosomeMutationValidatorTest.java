package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockTwoGene;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.InvalidConfigurationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class PathChromosomeMutationValidatorTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private PathChromosome twoGeneChromosome;
    private final int RUN_LIMIT = 10;

    public PathChromosomeMutationValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeMutationValidatorTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosomeMutationValidatorTest",
		PathChromosomeMutationValidatorTest.class.toString());
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("PathChromosomeCrossoverValidatorTest", this.graphMock.getGraph());
	conf.setKeepPopulationSizeConstant(false);

	this.twoGeneChromosome = new PathChromosomeMockTwoGene(this.conf).getChromosome();
    }

    @Test
    public void testCanMutate100PercentValid() {
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(this.twoGeneChromosome, 100);

	// make sure it works in repeated runs
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertTrue(validator.canMutate());
	}
    }

    @Test
    public void testCanMutate100PercentValidUseChanceCheck() {
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(this.twoGeneChromosome, 100);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertTrue(validator.canMutate(false));
	}
    }

    @Test
    public void testCanMutate100PercentValidSkipChanceCheck() {
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(twoGeneChromosome, 100);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertTrue(validator.canMutate(true));
	}
    }

    @Test
    public void testCanMutate100PercentInvalidEmptyPath() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(),
		this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 100);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate());
	}
    }

    @Test
    public void testCanMutate100PercentInvalidEmptyPathUseChanceCheck() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(),
		this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 100);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(true));
	}
    }

    @Test
    public void testCanMutate100PercentInvalidEmptyPathSkipChanceCheck() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(),
		this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 100);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(false));
	}
    }

    @Test
    public void testCanMutate100PercentNull() throws InvalidConfigurationException {
	PathChromosome chromosome = null;
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 100);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate());
	}
    }

    @Test
    public void testCanMutate100PercentNullUseChanceCheck() throws InvalidConfigurationException {
	PathChromosome chromosome = null;
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 100);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(true));
	}
    }

    @Test
    public void testCanMutate100PercentNullSkipChanceCheck() throws InvalidConfigurationException {
	PathChromosome chromosome = null;
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 100);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(false));
	}
    }

    @Test
    public void testCanMutate0PercentValid() {
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(this.twoGeneChromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate());
	}
    }

    @Test
    public void testCanMutate0PercentValidUseChanceCheck() {
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(this.twoGeneChromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(false));
	}
    }

    @Test
    public void testCanMutate0PercentValidSkipChanceCheck() {
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(twoGeneChromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertTrue(validator.canMutate(true));
	}
    }

    @Test
    public void testCanMutate0PercentInvalidEmptyPath() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(),
		this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate());
	}
    }

    @Test
    public void testCanMutate0PercentInvalidEmptyPathUseChanceCheck() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(),
		this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(true));
	}
    }

    @Test
    public void testCanMutate0PercentInvalidEmptyPathSkipChanceCheck() throws InvalidConfigurationException {
	PathChromosome chromosome = new PathChromosome(conf, new ArrayList<EdgeGene>(),
		this.graphMock.getFirstNode(), this.graphMock.getThirdNode());
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(false));
	}
    }

    @Test
    public void testCanMutate0PercentNull() throws InvalidConfigurationException {
	PathChromosome chromosome = null;
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate());
	}
    }

    @Test
    public void testCanMutate0PercentNullUseChanceCheck() throws InvalidConfigurationException {
	PathChromosome chromosome = null;
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(true));
	}
    }

    @Test
    public void testCanMutate0PercentNullSkipChanceCheck() throws InvalidConfigurationException {
	PathChromosome chromosome = null;
	PathChromosomeMutationValidator validator = new PathChromosomeMutationValidator(chromosome, 0);

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    Assert.assertFalse(validator.canMutate(false));
	}
    }
}
