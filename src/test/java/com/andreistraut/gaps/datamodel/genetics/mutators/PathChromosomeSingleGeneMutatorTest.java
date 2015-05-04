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
import org.jgap.impl.StockRandomGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class PathChromosomeSingleGeneMutatorTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private ArrayList<EdgeGene> genes;
    private PathChromosome chromosome;
    private PathChromosome expected;
    private final int RUN_LIMIT = 10;
    
    public PathChromosomeSingleGeneMutatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeSingleGeneMutatorTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosomeSingleGeneMutator",
		PathChromosomeSingleGeneMutatorTest.class.toString());
    }
    
    @Before
    public void setUp() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();
	this.conf = new GeneticConfiguration("DirectedWeightedGraphMockThreeNodeThreeEdges", this.graphMock.getGraph());
	this.conf.setKeepPopulationSizeConstant(false);

	DirectedWeightedEdge firstToThirdMoreExpensive = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToThirdEdge().getCost() + 1);
	graphMock.getGraph().addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThirdMoreExpensive);
	
	EdgeGene firstToThirdGeneMoreExpensive = new EdgeGene(firstToThirdMoreExpensive, conf);
	this.genes = new ArrayList<EdgeGene>();
	this.genes.add(firstToThirdGeneMoreExpensive);
	this.chromosome = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	ArrayList<EdgeGene> expectedGenes = new ArrayList<EdgeGene>();
	expectedGenes.add(new EdgeGene(graphMock.getFirstToThirdEdge(), conf));
	expected = new PathChromosome(conf, expectedGenes,
		graphMock.getFirstNode(), graphMock.getThirdNode());
    }

    @Test
    public void testDoMutation100PercentBetterSection() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    this.setUp();
	    
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.chromosome);

	    PathChromosomeSingleGeneMutator mutator = new PathChromosomeSingleGeneMutator(conf, 100);
	    Assert.assertFalse(this.chromosome.equals(expected));
	    
	    mutator.doMutation(this.chromosome, candidates, new StockRandomGenerator());

	    Assert.assertTrue(candidates.size() == 1);
	    Assert.assertTrue(this.chromosome.equals(expected));
	    Assert.assertTrue(candidates.contains(this.chromosome));
	}
    }

    @Test
    public void testDoMutation0PercentBetterSection() throws Exception {
	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    this.setUp();
	    
	    ArrayList<PathChromosome> candidates = new ArrayList<PathChromosome>();
	    candidates.add(this.chromosome);

	    PathChromosomeSingleGeneMutator mutator = new PathChromosomeSingleGeneMutator(conf, 0);
	    Assert.assertFalse(this.chromosome.equals(expected));
	    
	    mutator.doMutation(this.chromosome, candidates, new StockRandomGenerator());

	    Assert.assertTrue(candidates.size() == 1);
	    Assert.assertFalse(this.chromosome.equals(expected));
	    Assert.assertTrue(candidates.contains(this.chromosome));
	}
    }    
}
