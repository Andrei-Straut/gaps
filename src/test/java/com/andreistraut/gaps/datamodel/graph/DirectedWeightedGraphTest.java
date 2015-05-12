package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgrapht.GraphPath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectedWeightedGraphTest {

    private Node first;
    private Node second;
    private Node third;
    private DirectedWeightedEdge firstToSecond;
    private DirectedWeightedEdge secondToThird;
    private final int firstToSecondCost = 1;
    private final int secondToThirdCost = 2;
    private DirectedWeightedGraphSemiRandom graph;

    public DirectedWeightedGraphTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(DirectedWeightedGraphTest.class.getName()).log(Level.INFO,
		"{0} TEST: DirectedWeightedGraph",
		DirectedWeightedGraphTest.class.toString());
    }

    @Before
    public void setUp() {
	first = new Node("1", "1");
	second = new Node("2", "2");
	third = new Node("3", "3");

	firstToSecond = new DirectedWeightedEdge(first, second, firstToSecondCost, true);
	secondToThird = new DirectedWeightedEdge(second, third, secondToThirdCost, true);

	graph = new DirectedWeightedGraphSemiRandom(3, 2);
	graph.addVertex(first);
	graph.addVertex(second);
	graph.addVertex(third);
	graph.addEdge(first, second, firstToSecond);
	graph.addEdge(second, third, secondToThird);
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
    public void testGetNodeByIdExistentNode() {
	Assert.assertTrue(graph.getNodeById("1").equals(first));
	Assert.assertTrue(graph.getNodeById("2").equals(second));
	Assert.assertTrue(graph.getNodeById("3").equals(third));
    }

    @Test
    public void testGetNodeByIdAfterAddNode() {
	Node fourth = new Node("Fourth", "Fourth");
	graph.addVertex(fourth);
	Assert.assertTrue(graph.getNodeById("Fourth").equals(fourth));
    }

    @Test
    public void testGetNodeByIdInexistentNode() {
	Assert.assertTrue(graph.getNodeById("4") == null);
	Assert.assertTrue(graph.getNodeById("5") == null);
	Assert.assertTrue(graph.getNodeById("6") == null);
    }

    @Test
    public void testGetNodeByIdEmptyVertexSet() {
	DirectedWeightedGraphSemiRandom emptyGraph = new DirectedWeightedGraphSemiRandom(5, 5);
	Assert.assertTrue(emptyGraph.getNodeById("1") == null);
    }

    @Test
    public void testGetNodeByNameExistentNode() {
	Assert.assertTrue(graph.getNodeByName("1").equals(first));
	Assert.assertTrue(graph.getNodeByName("2").equals(second));
	Assert.assertTrue(graph.getNodeByName("3").equals(third));
    }

    @Test
    public void testGetNodeByNameAfterAddNode() {
	Node fourth = new Node("Fourth", "Fourth");
	graph.addVertex(fourth);
	Assert.assertTrue(graph.getNodeByName("Fourth").equals(fourth));
    }

    @Test
    public void testGetNodeByNameInexistentNode() {
	Assert.assertTrue(graph.getNodeByName("4") == null);
	Assert.assertTrue(graph.getNodeByName("5") == null);
	Assert.assertTrue(graph.getNodeByName("6") == null);
    }

    @Test
    public void testGetNodeByNameEmptyVertexSet() {
	DirectedWeightedGraphSemiRandom emptyGraph = new DirectedWeightedGraphSemiRandom(5, 5);
	Assert.assertTrue(emptyGraph.getNodeByName("1") == null);
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
	 * 1 -> 2 --> 3 | | --> 4 --> |
	 *
	 * KShortestPaths should return 2
	 */
	List<GraphPath<Node, DirectedWeightedEdge>> kShortestOnePath = graph.getKShortestPaths(first, third, 1);
	Assert.assertTrue(kShortestOnePath.size() == 1);

	List<GraphPath<Node, DirectedWeightedEdge>> kShortestTwoPath = graph.getKShortestPaths(first, third, 2);
	Assert.assertTrue(kShortestTwoPath.size() == 2);

	/**
	 * Also test the case where we only have two paths, but we are
	 * requesting more
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
    public void testGetLowestCostEdgeOneEdgedGraph() {
	DirectedWeightedGraphSemiRandom gr = new DirectedWeightedGraphSemiRandom(2, 1);

	Node firstNode = new Node("1", "1");
	Node secondNode = new Node("2", "2");
	DirectedWeightedEdge firstToSecondEdge = new DirectedWeightedEdge(firstNode, secondNode, 1, true);
	gr.addVertex(firstNode);
	gr.addVertex(secondNode);
	gr.addEdge(firstNode, secondNode, firstToSecondEdge);

	DirectedWeightedEdge lowest = gr.getLowestCostEdge();
	Assert.assertTrue(lowest.equals(firstToSecondEdge));
    }

    @Test
    public void testGetLowestCostEdgeEmptyGraph() {
	DirectedWeightedGraphSemiRandom gr = new DirectedWeightedGraphSemiRandom(3, 2);

	try {
	    DirectedWeightedEdge lowest = gr.getLowestCostEdge();
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().equals("Edge list is empty"));
	}
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
    public void testGetHighestCostEdgeOneEdgedGraph() {
	DirectedWeightedGraphSemiRandom gr = new DirectedWeightedGraphSemiRandom(2, 1);

	Node firstNode = new Node("1", "1");
	Node secondNode = new Node("2", "2");
	DirectedWeightedEdge firstToSecondEdge = new DirectedWeightedEdge(firstNode, secondNode, 1, true);
	gr.addVertex(firstNode);
	gr.addVertex(secondNode);
	gr.addEdge(firstNode, secondNode, firstToSecondEdge);

	DirectedWeightedEdge highest = gr.getHighestCostEdge();
	Assert.assertTrue(highest.equals(firstToSecondEdge));
    }

    @Test
    public void testGetHighestCostEdgeEmptyGraph() {
	DirectedWeightedGraphSemiRandom gr = new DirectedWeightedGraphSemiRandom(3, 2);

	try {
	    DirectedWeightedEdge highest = gr.getHighestCostEdge();
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().equals("Edge list is empty"));
	}
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
