package com.andreistraut.gaps.datamodel.graph;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectedWeightedGraphSemiRandomTest {

    private final int RUN_LIMIT_SMALL = 10000;
    private final int NUMBER_OF_NODES_SMALL = 5;
    private final int NUMBER_OF_EDGES_SMALL = 7;
    
    private final int RUN_LIMIT_MEDIUM = 500;
    private final int NUMBER_OF_NODES_MEDIUM = 100;
    private final int NUMBER_OF_EDGES_MEDIUM = 500;
    
    private final int RUN_LIMIT_LARGE = 10;
    private final int NUMBER_OF_NODES_LARGE = 1000;
    private final int NUMBER_OF_EDGES_LARGE = 3000;

    public DirectedWeightedGraphSemiRandomTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger(DirectedWeightedGraphSemiRandomTest.class.getName()).log(Level.INFO,
                "{0} TEST: Graph",
                DirectedWeightedGraphSemiRandomTest.class.toString());
    }

    @Test
    public void testInitNodes() {
        DirectedWeightedGraphSemiRandom graph = new DirectedWeightedGraphSemiRandom(10, 30);
        ArrayList<Node> nodes = graph.initNodes();

        Assert.assertTrue(nodes.size() == 10);
        Assert.assertTrue(graph.getNodes().size() == 10);
        Assert.assertTrue(graph.vertexSet().size() == 10);
    }

    @Test
    public void testGraphGenerationSmallGraph() {
        boolean allEqual = true;
        
        for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
            DirectedWeightedGraphSemiRandom first = new DirectedWeightedGraphSemiRandom(
                    this.NUMBER_OF_NODES_SMALL, this.NUMBER_OF_EDGES_SMALL);
            DirectedWeightedGraphSemiRandom second = new DirectedWeightedGraphSemiRandom(
                    this.NUMBER_OF_NODES_SMALL, this.NUMBER_OF_EDGES_SMALL);

            first.initNodes();
            second.initNodes();

            ArrayList<DirectedWeightedEdge> firstEdges = first.initEdges();
            ArrayList<DirectedWeightedEdge> secondEdges = second.initEdges();
            
            allEqual = firstEdges.equals(secondEdges);
            if(!allEqual) {
                break;
            }
        }
        
        Assert.assertFalse(allEqual);
    }

    @Test
    public void testGraphGenerationMediumGraph() {
        boolean allEqual = true;
        
        for (int i = 0; i < RUN_LIMIT_MEDIUM; i++) {
            DirectedWeightedGraphSemiRandom first = new DirectedWeightedGraphSemiRandom(
                    this.NUMBER_OF_NODES_MEDIUM, this.NUMBER_OF_EDGES_MEDIUM);
            DirectedWeightedGraphSemiRandom second = new DirectedWeightedGraphSemiRandom(
                    this.NUMBER_OF_NODES_MEDIUM, this.NUMBER_OF_EDGES_MEDIUM);

            first.initNodes();
            second.initNodes();

            ArrayList<DirectedWeightedEdge> firstEdges = first.initEdges();
            ArrayList<DirectedWeightedEdge> secondEdges = second.initEdges();
            
            allEqual = firstEdges.equals(secondEdges);
            if(!allEqual) {
                break;
            }
        }
        
        Assert.assertFalse(allEqual);
    }

    @Test
    public void testGraphGenerationLargeGraph() {
        boolean allEqual = true;
        
        for (int i = 0; i < RUN_LIMIT_LARGE; i++) {
            DirectedWeightedGraphSemiRandom first = new DirectedWeightedGraphSemiRandom(
                    this.RUN_LIMIT_LARGE, this.RUN_LIMIT_LARGE);
            DirectedWeightedGraphSemiRandom second = new DirectedWeightedGraphSemiRandom(
                    this.RUN_LIMIT_LARGE, this.RUN_LIMIT_LARGE);

            first.initNodes();
            second.initNodes();

            ArrayList<DirectedWeightedEdge> firstEdges = first.initEdges();
            ArrayList<DirectedWeightedEdge> secondEdges = second.initEdges();
            
            allEqual = firstEdges.equals(secondEdges);
            if(!allEqual) {
                break;
            }
        }
        
        Assert.assertFalse(allEqual);
    }
}
