package com.andreistraut.pathsearch.datamodel.graph;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author straut
 */
public class DirectedWeightedEdgeFactoryTest {

    private Node source;
    private Node destination;
    private int cost = 100;

    public DirectedWeightedEdgeFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(DirectedWeightedEdgeFactoryTest.class.getName()).log(Level.INFO,
		DirectedWeightedEdgeFactoryTest.class.toString() + " TEST: Edge Factory");    
    }

    @Before
    public void setUp() {	
	source = new Node("1", "1");
	destination = new Node("2", "2");
    }

    @Test
    public void testCreateEdge_2args() {
	DirectedWeightedEdgeFactory factory = new DirectedWeightedEdgeFactory();
	DirectedWeightedEdge edge = factory.createEdge(source, destination);
	Assert.assertEquals(source, edge.getSource());
	Assert.assertEquals(destination, edge.getDestination());
    }

    @Test
    public void testCreateEdge_3args() {
	DirectedWeightedEdgeFactory factory = new DirectedWeightedEdgeFactory();
	DirectedWeightedEdge edge = factory.createEdge(source, destination, cost);
	Assert.assertEquals(source, edge.getSource());
	Assert.assertEquals(destination, edge.getDestination());
	Assert.assertEquals(cost, edge.getCost());
    }

}
