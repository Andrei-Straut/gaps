package com.andreistraut.pathsearch.datamodel.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgrapht.GraphPath;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class DirectedWeightedGraphTest {
    private Node first;
    private Node second;
    private Node third;
    private DirectedWeightedEdge firstToSecond;
    private DirectedWeightedEdge secondToThird;
    private int firstToSecondCost = 1;
    private int secondToThirdCost = 2;
    private DirectedWeightedGraph graph;
    
    public DirectedWeightedGraphTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(DirectedWeightedGraph.class.getName()).log(Level.INFO,
		DirectedWeightedGraphTest.class.toString() + " TEST: Graph");
    }
    
    @Before
    public void setUp() {
	first = new Node("1", "1");
	second = new Node("2", "2");
	third = new Node("3", "3");
	
	firstToSecond = new DirectedWeightedEdge(first, second, firstToSecondCost, true);
	secondToThird = new DirectedWeightedEdge(second, third, secondToThirdCost, true);
	
	graph = new DirectedWeightedGraph(3, 2);
	graph.addVertex(first);
	graph.addVertex(second);
	graph.addVertex(third);	
	graph.addEdge(first, second, firstToSecond);
	graph.addEdge(second, third, secondToThird);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetNumberOfNodes() {
	Assert.assertTrue(graph.getNodes().size() == 3);
	Assert.assertTrue(graph.vertexSet().size() == 3);
    }

    @Test
    public void testGetNumberOfEdges() {
	Assert.assertTrue(graph.getEdges().size() == 2);
	Assert.assertTrue(graph.edgeSet().size() == 2);
    }

    @Test
    public void testGetNodes() {
	ArrayList<Node> nodes = graph.getNodes();
	Assert.assertTrue(nodes.size() == 3);
	
	Assert.assertTrue(nodes.contains(first));
	Assert.assertTrue(nodes.contains(second));
	Assert.assertTrue(nodes.contains(third));
    }

    @Test
    public void testGetEdges() {
	ArrayList<DirectedWeightedEdge> edges = graph.getEdges();
	Assert.assertTrue(edges.size() == 2);
	
	Assert.assertTrue(edges.contains(firstToSecond));
	Assert.assertTrue(edges.contains(secondToThird));
    }

    @Test
    public void testInitNodes() {
	graph= new DirectedWeightedGraph(10, 30);
	ArrayList<Node> nodes = graph.initNodes();
	
	Assert.assertTrue(nodes.size() == 10);
	Assert.assertTrue(graph.getNodes().size() == 10);
	Assert.assertTrue(graph.vertexSet().size() == 10);
    }

    @Test
    public void testInitEdges() {
	graph = new DirectedWeightedGraph(10, 30);
	graph.initNodes();
	
	ArrayList<DirectedWeightedEdge> edges = graph.initEdges();
	Assert.assertTrue(edges.size() == 30);
	Assert.assertTrue(graph.getEdges().size() == 30);
	Assert.assertTrue(graph.edgeSet().size() == 30);
    }

    @Test
    public void testGetKShortestPaths() {
	Node fourth = new Node("4", "4");
	DirectedWeightedEdge firstToFourth = new DirectedWeightedEdge(first, fourth, 3, true);
	DirectedWeightedEdge fourthToThird = new DirectedWeightedEdge(fourth, third, 4, true);
	graph.addVertex(fourth);
	graph.addEdge(first, fourth, firstToFourth);
	graph.addEdge(fourth, third, fourthToThird);
	
	/**
	 * Now graph structure should be
	 * 
	 * 1 -> 2 --> 3
	 * |          |
	 *  --> 4 --> |
	 * 
	 * KShortestPaths should return 2
	 */
	List<GraphPath<Node, DirectedWeightedEdge>> kShortestOnePath = graph.getKShortestPaths(first, third, 1);
	Assert.assertTrue(kShortestOnePath.size() == 1);
	
	List<GraphPath<Node, DirectedWeightedEdge>> kShortestTwoPath = graph.getKShortestPaths(first, third, 2);
	Assert.assertTrue(kShortestTwoPath.size() == 2);
	
	/**
	 * Also test the case where we only have two paths, but we are requesting more
	 */
	List<GraphPath<Node, DirectedWeightedEdge>> kShortestUnlimited = graph.getKShortestPaths(first, third, 1000);
	Assert.assertTrue(kShortestUnlimited.size() == 2);
    }
    
    @Test
    public void testGetKPathsDepthFirst() {
	Node fourth = new Node("4", "4");
	DirectedWeightedEdge firstToFourth = new DirectedWeightedEdge(first, fourth, 3, true);
	DirectedWeightedEdge fourthToThird = new DirectedWeightedEdge(fourth, third, 4, true);
	graph.addVertex(fourth);
	graph.addEdge(first, fourth, firstToFourth);
	graph.addEdge(fourth, third, fourthToThird);
	
	ArrayList<DirectedWeightedGraphPath> kOnePath = graph.getKPathsDepthFirst(first, third, 1);
	Assert.assertTrue(kOnePath.size() == 1);
	
	ArrayList<DirectedWeightedGraphPath> kTwoPath = graph.getKPathsDepthFirst(first, third, 2);
	Assert.assertTrue(kTwoPath.size() == 2);
	Assert.assertFalse(kTwoPath.get(0).equals(kTwoPath.get(1)));
	
	List<DirectedWeightedGraphPath> kUnlimited = graph.getKPathsDepthFirst(first, third, 1000);
	Assert.assertTrue(kUnlimited.size() == 2);
	Assert.assertFalse(kTwoPath.get(0).equals(kTwoPath.get(1)));
    }

    @Test
    public void testGetLowestCostEdge() {
	DirectedWeightedEdge lowest = graph.getLowestCostEdge();
	Assert.assertEquals(firstToSecond, lowest);
	
	Node fourth = new Node("4", "4");
	DirectedWeightedEdge firstToFourth = new DirectedWeightedEdge(first, fourth, firstToSecondCost - 1, true);
	
	graph.addVertex(fourth);
	graph.addEdge(first, fourth, firstToFourth);
	lowest = graph.getLowestCostEdge();
	Assert.assertEquals(firstToFourth, lowest);
	
	graph.removeEdge(first, second);
	lowest = graph.getLowestCostEdge();
	Assert.assertEquals(firstToFourth, lowest);
    }

    @Test
    public void testGetHighestCostEdge() {
	DirectedWeightedEdge highest = graph.getHighestCostEdge();
	Assert.assertEquals(secondToThird, highest);	
	
	Node fourth = new Node("4", "4");
	DirectedWeightedEdge firstToFourth = new DirectedWeightedEdge(first, fourth, secondToThirdCost + 1, true);
	
	graph.addVertex(fourth);
	graph.addEdge(first, fourth, firstToFourth);
	highest = graph.getHighestCostEdge();
	Assert.assertEquals(firstToFourth, highest);
	
	graph.removeEdge(first, fourth);
	highest = graph.getHighestCostEdge();
	Assert.assertEquals(secondToThird, highest);
    }

    @Test
    public void testToJson() {
	JsonObject graphJson = graph.toJson();
	
	Assert.assertTrue(graphJson.has("graph"));
	Assert.assertTrue(graphJson.has("edges"));
	Assert.assertTrue(graphJson.has("statistics"));
	
	JsonArray nodesJson = graphJson.get("graph").getAsJsonArray();
	Assert.assertTrue(nodesJson.size() == 3);
	
	JsonArray edgesJson = graphJson.get("edges").getAsJsonArray();
	Assert.assertTrue(edgesJson.size() == 2);
	
	JsonObject statisticsJson = graphJson.get("statistics").getAsJsonObject();
	Assert.assertTrue(statisticsJson.has("numberOfNodes"));
	Assert.assertTrue(statisticsJson.has("numberOfEdges"));
	Assert.assertTrue(statisticsJson.has("minimumEdgeCost"));
	Assert.assertTrue(statisticsJson.has("maximumEdgeCost"));
	Assert.assertTrue(statisticsJson.has("totalEdgeCost"));
	Assert.assertTrue(statisticsJson.has("averageEdgeCost"));
	Assert.assertTrue(statisticsJson.has("averageEdgesPerNode"));
    }
    
}
