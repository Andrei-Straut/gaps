package com.andreistraut.gaps.datamodel.graph;

import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class DirectedWeightedGraphImportedTest {

    private final int RUN_LIMIT_SMALL = 10000;
    private final int NUMBER_OF_NODES_SMALL = 5;
    private final int NUMBER_OF_EDGES_SMALL = 10;

    private JsonArray graphJson;

    public DirectedWeightedGraphImportedTest() {
    }

    @Before
    public void setUp() {
	DirectedWeightedGraph graph = new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges().getGraph();
	this.graphJson = graph.toJson().get("graph").getAsJsonArray();
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(DirectedWeightedGraphImportedTest.class.getName()).log(Level.INFO,
		"{0} TEST: Graph JSON Import",
		DirectedWeightedGraphImportedTest.class.toString());
    }

    @Test
    public void testFromJsonValidGraph() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJson.getAsJsonArray());
	    DirectedWeightedGraph expected = new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges().getGraph();

	    ArrayList<Node> mockNodes = expected.getNodes();
	    ArrayList<Node> importedNodes = graph.getNodes();
	    Assert.assertTrue(mockNodes.equals(importedNodes));

	    ArrayList<DirectedWeightedEdge> mockEdges = expected.getEdges();
	    ArrayList<DirectedWeightedEdge> importedEdges = graph.getEdges();

	    Assert.assertTrue(mockEdges.size() == importedEdges.size());
	    for (DirectedWeightedEdge edge : mockEdges) {
		Assert.assertTrue(importedEdges.contains(edge));
	    }
	}
    }

    @Test
    public void testFromJsonValidGraphTwoNodesTwoEdges() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");

	DirectedWeightedEdge firstToSecondEdge = new DirectedWeightedEdge(first, second, 100, true);
	DirectedWeightedEdge secondToFirstEdge = new DirectedWeightedEdge(second, first, 100, true);
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(firstToSecondEdge.toJson());
	JsonObject secondNodeJson = second.toJson();
	secondNodeJson.get("adjacencies").getAsJsonArray().add(secondToFirstEdge.toJson());
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().size() == 2);
	
	Assert.assertTrue(graph.getEdge(first, second).equals(firstToSecondEdge));
	Assert.assertTrue(graph.getEdge(second, first).equals(secondToFirstEdge));
    }

    @Test
    public void testFromJsonValidGraphThreeNodesTwoEdgesLinear() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	Node third = new Node("Third", "Third");

	DirectedWeightedEdge firstToSecondEdge = new DirectedWeightedEdge(first, second, 100, true);
	DirectedWeightedEdge secondToThirdEdge = new DirectedWeightedEdge(second, third, 100, true);
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(firstToSecondEdge.toJson());
	JsonObject secondNodeJson = second.toJson();
	secondNodeJson.get("adjacencies").getAsJsonArray().add(secondToThirdEdge.toJson());
	JsonObject thirdNodeJson = third.toJson();
	secondNodeJson.get("adjacencies").getAsJsonArray().add(secondToThirdEdge.toJson());
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);
	graphJsonArray.add(thirdNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 3);
	Assert.assertTrue(graph.getEdges().size() == 2);
	
	Assert.assertTrue(graph.getEdge(first, second).equals(firstToSecondEdge));
	Assert.assertTrue(graph.getEdge(second, third).equals(secondToThirdEdge));
    }

    @Test
    public void testFromJsonValidGraphTwoNodesNoEdges() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");

	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(first.toJson());
	graphJsonArray.add(second.toJson());

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().isEmpty());
    }

    @Test
    public void testFromJsonValidGraphTwoNodesEdgeToInexistentNode() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject invalidEdge = new JsonObject();
	invalidEdge.addProperty("nodeFrom", "First");
	invalidEdge.addProperty("nodeTo", "Fourth");
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(invalidEdge);
	JsonObject secondNodeJson = second.toJson();
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 3);
	Assert.assertTrue(graph.getEdges().size() == 1);
    }

    @Test
    public void testFromJsonValidEdgeMissingCost() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject firstToSecondEdge = new JsonObject();
	firstToSecondEdge.addProperty("nodeFrom", "First");
	firstToSecondEdge.addProperty("nodeTo", "Second");
	JsonObject firstToSecondEdgeData = new JsonObject();
	firstToSecondEdgeData.addProperty("isDirected", true);
	firstToSecondEdge.add("data", firstToSecondEdgeData);
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(firstToSecondEdge);
	JsonObject secondNodeJson = second.toJson();
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().size() == 1);
	Assert.assertTrue(graph.getEdges().get(0).getCost() == 0);
	Assert.assertTrue(graph.getEdges().get(0).isDirected());
    }

    @Test
    public void testFromJsonValidEdgeMissingIsDirected() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject firstToSecondEdge = new JsonObject();
	firstToSecondEdge.addProperty("nodeFrom", "First");
	firstToSecondEdge.addProperty("nodeTo", "Second");
	JsonObject firstToSecondEdgeData = new JsonObject();
	firstToSecondEdgeData.addProperty("cost", 15);
	firstToSecondEdge.add("data", firstToSecondEdgeData);
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(firstToSecondEdge);
	JsonObject secondNodeJson = second.toJson();
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().size() == 1);
	Assert.assertTrue(graph.getEdges().get(0).getCost() == 15);
	Assert.assertTrue(graph.getEdges().get(0).isDirected());
    }

    @Test
    public void testFromJsonValidEdgeEmptyData() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject firstToSecondEdge = new JsonObject();
	firstToSecondEdge.addProperty("nodeFrom", "First");
	firstToSecondEdge.addProperty("nodeTo", "Second");
	JsonObject firstToSecondEdgeData = new JsonObject();
	firstToSecondEdge.add("data", firstToSecondEdgeData);
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(firstToSecondEdge);
	JsonObject secondNodeJson = second.toJson();
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().size() == 1);
	Assert.assertTrue(graph.getEdges().get(0).getCost() == 0);
	Assert.assertTrue(graph.getEdges().get(0).isDirected());
    }

    @Test
    public void testFromJsonValidEdgeMissingData() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject firstToSecondEdge = new JsonObject();
	firstToSecondEdge.addProperty("nodeFrom", "First");
	firstToSecondEdge.addProperty("nodeTo", "Second");
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(firstToSecondEdge);
	JsonObject secondNodeJson = second.toJson();
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().size() == 1);
	Assert.assertTrue(graph.getEdges().get(0).getCost() == 0);
	Assert.assertTrue(graph.getEdges().get(0).isDirected());
    }

    @Test
    public void testFromJsonInvalidEdgeMissingNodeFrom() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject invalidEdge = new JsonObject();
	invalidEdge.addProperty("nodeTo", "Second");
	invalidEdge.addProperty("cost", 154);
	invalidEdge.addProperty("isDirected", true);
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(invalidEdge);
	JsonObject secondNodeJson = second.toJson();
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().isEmpty());
    }

    @Test
    public void testFromJsonInvalidEdgeMissingNodeTo() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject invalidEdge = new JsonObject();
	invalidEdge.addProperty("nodeFrom", "First");
	invalidEdge.addProperty("cost", 154);
	invalidEdge.addProperty("isDirected", true);
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(invalidEdge);
	JsonObject secondNodeJson = second.toJson();
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().isEmpty());
    }

    @Test
    public void testFromJsonInvalidEdgeMissingNodeFromNodeTo() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject invalidEdge = new JsonObject();
	invalidEdge.addProperty("cost", 154);
	invalidEdge.addProperty("isDirected", true);
	
	JsonObject firstNodeJson = first.toJson();
	firstNodeJson.get("adjacencies").getAsJsonArray().add(invalidEdge);
	JsonObject secondNodeJson = second.toJson();
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);

	DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonArray);
	
	Assert.assertTrue(graph.getNodes().size() == 2);
	Assert.assertTrue(graph.getEdges().isEmpty());
    }

    @Test
    public void testFromJsonInvalidGraphEmpty() throws Exception {
	JsonArray graphEmptyJson = new JsonArray();

	try {
	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphEmptyJson);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Cannot import empty graph"));
	}
    }

    @Test
    public void testFromJsonInvalidGraphNull() throws Exception {
	JsonArray graphEmptyJson = null;

	try {
	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphEmptyJson);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Cannot import empty graph"));
	}
    }
}
