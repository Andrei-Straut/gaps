package com.andreistraut.gaps.datamodel.graph;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectedWeightedGraphStaticTest {

    private final int RUN_LIMIT_SMALL = 10000;
    private final int NUMBER_OF_NODES_SMALL = 5;
    private final int NUMBER_OF_EDGES_SMALL = 10;
    
    private final int RUN_LIMIT_MEDIUM = 500;
    private final int NUMBER_OF_NODES_MEDIUM = 100;
    private final int NUMBER_OF_EDGES_MEDIUM = 500;
    
    private final int RUN_LIMIT_LARGE = 10;
    private final int NUMBER_OF_NODES_LARGE = 1000;
    private final int NUMBER_OF_EDGES_LARGE = 3000;

    public DirectedWeightedGraphStaticTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger(DirectedWeightedGraphStaticTest.class.getName()).log(Level.INFO,
                "{0} TEST: Graph Static Generation",
                DirectedWeightedGraphStaticTest.class.toString());
    }

    @Test
    public void testInitNodes() {
        DirectedWeightedGraphStatic graph = new DirectedWeightedGraphStatic(10, 30);
        ArrayList<Node> nodes = graph.initNodes();

        Assert.assertTrue(nodes.size() == 10);
        Assert.assertTrue(graph.getNodes().size() == 10);
        Assert.assertTrue(graph.vertexSet().size() == 10);
    }

    @Test
    public void testGraphGenerationSmallGraph() {
        for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
            DirectedWeightedGraphStatic first = new DirectedWeightedGraphStatic(
                    this.NUMBER_OF_NODES_SMALL, this.NUMBER_OF_EDGES_SMALL);
            DirectedWeightedGraphStatic second = new DirectedWeightedGraphStatic(
                    this.NUMBER_OF_NODES_SMALL, this.NUMBER_OF_EDGES_SMALL);

            first.initNodes();
            second.initNodes();

            ArrayList<DirectedWeightedEdge> firstEdges = first.initEdges();
            ArrayList<DirectedWeightedEdge> secondEdges = second.initEdges();

            Assert.assertTrue(first.equals(second));
            Assert.assertTrue(firstEdges.equals(secondEdges));
        }
    }

    @Test
    public void testGraphGenerationMediumGraph() {
        for (int i = 0; i < RUN_LIMIT_MEDIUM; i++) {
            DirectedWeightedGraphStatic first = new DirectedWeightedGraphStatic(
                    this.NUMBER_OF_NODES_MEDIUM, this.NUMBER_OF_EDGES_MEDIUM);
            DirectedWeightedGraphStatic second = new DirectedWeightedGraphStatic(
                    this.NUMBER_OF_NODES_MEDIUM, this.NUMBER_OF_EDGES_MEDIUM);

            first.initNodes();
            second.initNodes();

            ArrayList<DirectedWeightedEdge> firstEdges = first.initEdges();
            ArrayList<DirectedWeightedEdge> secondEdges = second.initEdges();

            Assert.assertTrue(first.equals(second));
            Assert.assertTrue(firstEdges.equals(secondEdges));
        }
    }

    @Test
    public void testGraphGenerationLargeGraph() {
        for (int i = 0; i < RUN_LIMIT_LARGE; i++) {
            DirectedWeightedGraphStatic first = new DirectedWeightedGraphStatic(
                    this.NUMBER_OF_NODES_LARGE, this.NUMBER_OF_EDGES_LARGE);
            DirectedWeightedGraphStatic second = new DirectedWeightedGraphStatic(
                    this.NUMBER_OF_NODES_LARGE, this.NUMBER_OF_EDGES_LARGE);

            first.initNodes();
            second.initNodes();

            ArrayList<DirectedWeightedEdge> firstEdges = first.initEdges();
            ArrayList<DirectedWeightedEdge> secondEdges = second.initEdges();

            Assert.assertTrue(first.equals(second));
            Assert.assertTrue(firstEdges.equals(secondEdges));
        }
    }
}
