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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class PathChromosomeSingleGeneMutatorTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    private GeneticConfiguration conf;
    private ArrayList<EdgeGene> genes;
    private PathChromosome chromosome;
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
	
	EdgeGene firstToThirdGene = new EdgeGene(graphMock.getFirstToThirdEdge(), conf);
	this.genes = new ArrayList<EdgeGene>();
	this.genes.add(firstToThirdGene);
	this.chromosome = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());

	DirectedWeightedEdge firstToThird = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToThirdEdge().getCost() + 1);	
	graphMock.getGraph().addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThird);
    }

    @Test
    public void testDoMutation() throws Exception {
    }

    @Test
    public void testGetMutationPosition() {
    }
    
}
