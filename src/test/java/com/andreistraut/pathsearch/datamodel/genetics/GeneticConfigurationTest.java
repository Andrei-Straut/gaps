package com.andreistraut.pathsearch.datamodel.genetics;

import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.pathsearch.datamodel.graph.Node;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class GeneticConfigurationTest {

    private Node first;
    private Node second;
    private DirectedWeightedEdge firstToSecond;
    private final int firstToSecondCost = 1;
    private DirectedWeightedGraph graph;

    public GeneticConfigurationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(GeneticConfigurationTest.class.getName()).log(Level.INFO,
		GeneticConfigurationTest.class.toString() + " TEST: Genetic Configuration");    
    }

    @Before
    public void setUp() {
	first = new Node("1", "1");
	second = new Node("2", "2");

	firstToSecond = new DirectedWeightedEdge(first, second, firstToSecondCost, true);

	graph = new DirectedWeightedGraph(3, 2);
	graph.addVertex(first);
	graph.addVertex(second);
	graph.addEdge(first, second, firstToSecond);	
    }

    @Test
    public void testGetGraph() {
	GeneticConfiguration config = new GeneticConfiguration("GenTest", graph);
	Assert.assertEquals(graph, config.getGraph());
    }

    @Test
    public void testSetGraph() {
	GeneticConfiguration config = new GeneticConfiguration("GenTest", null);
	Assert.assertNull(config.getGraph());
	
	config.setGraph(graph);
	Assert.assertEquals(graph, config.getGraph());
    }
}
