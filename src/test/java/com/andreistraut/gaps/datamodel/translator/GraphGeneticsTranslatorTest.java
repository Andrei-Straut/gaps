package com.andreistraut.gaps.datamodel.translator;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.Node;
import java.util.ArrayList;
import org.jgrapht.GraphPath;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GraphGeneticsTranslatorTest {
    
    public GraphGeneticsTranslatorTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testToPathChromosome() throws Exception {
	System.out.println("toPathChromosome");
	GraphPath<Node, DirectedWeightedEdge> path = null;
	GeneticConfiguration configuration = null;
	PathChromosome expResult = null;
	PathChromosome result = GraphGeneticsTranslator.toPathChromosome(path, configuration);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    @Test
    public void testToEdgeGeneList() throws Exception {
	System.out.println("toEdgeGeneList");
	GraphPath<Node, DirectedWeightedEdge> edgeList = null;
	GeneticConfiguration configuration = null;
	ArrayList<EdgeGene> expResult = null;
	ArrayList<EdgeGene> result = GraphGeneticsTranslator.toEdgeGeneList(edgeList, configuration);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
