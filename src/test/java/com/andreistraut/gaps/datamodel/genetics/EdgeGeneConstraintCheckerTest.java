package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges;
import com.andreistraut.gaps.datamodel.mock.PathChromosomeMockTwoGene;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.Chromosome;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.BooleanGene;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EdgeGeneConstraintCheckerTest {
    private GeneticConfiguration conf;
    private DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges graphMock;
    private PathChromosome chromosome;
    
    public EdgeGeneConstraintCheckerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(EdgeGeneConstraintCheckerTest.class.getName()).log(Level.INFO, 
		"{0} TEST: EdgeGeneConstraintCheckerTest", 
		EdgeGeneConstraintCheckerTest.class.toString());
    }
    
    @Before
    public void setUp() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("EdgeGeneConstraintCheckerTest", graphMock.getGraph());
	this.chromosome = new PathChromosomeMockTwoGene(conf).getChromosome();
    }

    @Test
    public void testVerifyValidChromosomeValidAlleleValidPosition() throws InvalidConfigurationException {
	DirectedWeightedEdge allele = graphMock.getFirstToSecondEdge();
	EdgeGene gene = new EdgeGene(allele, conf);	
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertTrue(checker.verify(gene, allele, chromosome, 0));
    }

    @Test
    public void testVerifyValidChromosomeValidAlleleInvalidPosition() throws InvalidConfigurationException {
	DirectedWeightedEdge allele = graphMock.getFirstToSecondEdge();
	EdgeGene gene = new EdgeGene(allele, conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 1));
    }

    @Test
    public void testVerifyValidChromosomeInvalidAlleleNullValues() throws InvalidConfigurationException {
	DirectedWeightedEdge allele = new DirectedWeightedEdge(null, null);
	EdgeGene gene = new EdgeGene(graphMock.getSecondToThirdEdge(), conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 1));
    }

    @Test
    public void testVerifyValidChromosomeInvalidAlleleNullSource() throws InvalidConfigurationException {
	DirectedWeightedEdge allele = new DirectedWeightedEdge(null, graphMock.getFirstNode());
	EdgeGene gene = new EdgeGene(graphMock.getSecondToThirdEdge(), conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 1));
    }

    @Test
    public void testVerifyValidChromosomeInvalidAlleleNullDestination() throws InvalidConfigurationException {
	DirectedWeightedEdge allele = new DirectedWeightedEdge(graphMock.getFirstNode(), null);
	EdgeGene gene = new EdgeGene(graphMock.getSecondToThirdEdge(), conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 1));
    }

    @Test
    public void testVerifyValidChromosomeInvalidAlleleObject() throws InvalidConfigurationException {
	Object allele = new Object();
	EdgeGene gene = new EdgeGene(graphMock.getSecondToThirdEdge(), conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 1));
    }

    @Test
    public void testVerifyValidChromosomeValidAlleleInvalidIndex() throws InvalidConfigurationException {
	DirectedWeightedEdge allele = graphMock.getFirstToSecondEdge();
	EdgeGene gene = new EdgeGene(allele, conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertTrue(checker.verify(gene, allele, chromosome, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, -1));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 1));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 2));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 3));
    }

    @Test
    public void testVerifyInvalidChromosomeEmptyPath() throws InvalidConfigurationException {
	PathChromosome chromo = new PathChromosome(conf, new ArrayList<EdgeGene>(), 
		graphMock.getFirstNode(), graphMock.getThirdNode());
	DirectedWeightedEdge allele = graphMock.getFirstToSecondEdge();
	EdgeGene gene = new EdgeGene(allele, conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertFalse(checker.verify(gene, allele, chromo, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromo, 1));
    }

    @Test
    public void testVerifyInvalidChromosomeObject() throws InvalidConfigurationException {
	Chromosome chromo = new Chromosome(conf);
	DirectedWeightedEdge allele = graphMock.getFirstToSecondEdge();
	EdgeGene gene = new EdgeGene(allele, conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertFalse(checker.verify(gene, allele, chromo, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromo, 1));
    }

    @Test
    public void testVerifyInvalidChromosomeNull() throws InvalidConfigurationException {
	PathChromosome chromo = null;
	DirectedWeightedEdge allele = graphMock.getFirstToSecondEdge();
	EdgeGene gene = new EdgeGene(allele, conf);
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertFalse(checker.verify(gene, allele, chromo, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromo, 1));
    }

    @Test
    public void testVerifyValidChromosomeNullGene() throws InvalidConfigurationException {
	EdgeGene gene = null;
	DirectedWeightedEdge allele = graphMock.getFirstToSecondEdge();
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertTrue(checker.verify(gene, allele, chromosome, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 1));
    }

    @Test
    public void testVerifyValidChromosomeInvalidGeneObject() throws InvalidConfigurationException {
	Gene gene = new BooleanGene(conf);
	DirectedWeightedEdge allele = graphMock.getFirstToSecondEdge();
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();
	
	Assert.assertTrue(checker.verify(gene, allele, chromosome, 0));
	Assert.assertFalse(checker.verify(gene, allele, chromosome, 1));
    }
}
