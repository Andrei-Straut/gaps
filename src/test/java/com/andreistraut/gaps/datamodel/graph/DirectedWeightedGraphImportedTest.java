package com.andreistraut.gaps.datamodel.graph;

import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class DirectedWeightedGraphImportedTest {

    private final int RUN_LIMIT_SMALL = 10000;
    private final int NUMBER_OF_NODES_SMALL = 5;
    private final int NUMBER_OF_EDGES_SMALL = 10;

    private JsonObject graphJson;

    public DirectedWeightedGraphImportedTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(DirectedWeightedGraphImportedTest.class.getName()).log(Level.INFO,
		"{0} TEST: Graph JSON Import",
		DirectedWeightedGraphImportedTest.class.toString());
    }

    @Before
    public void setUp() {
	DirectedWeightedGraph graph = new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges().getGraph();
	this.graphJson = graph.toJson().get("graph").getAsJsonObject();
    }

    @Test
    public void testFromJsonValidGraph() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJson.getAsJsonObject());
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
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    DirectedWeightedEdge firstToSecondEdge = new DirectedWeightedEdge(first, second, 100, true);
	    DirectedWeightedEdge secondToFirstEdge = new DirectedWeightedEdge(second, first, 100, true);
	    
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(firstToSecondEdge.toJson());
	    edgesArray.add(secondToFirstEdge.toJson());

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 2);
	    Assert.assertTrue(graph.getEdges().size() == 2);

	    Assert.assertTrue(graph.getEdge(first, second).equals(firstToSecondEdge));
	    Assert.assertTrue(graph.getEdge(second, first).equals(secondToFirstEdge));
	}
    }

    @Test
    public void testFromJsonValidGraphThreeNodesTwoEdgesLinear() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");
	    Node third = new Node("Third", "Third");
	    
	    DirectedWeightedEdge firstToSecondEdge = new DirectedWeightedEdge(first, second, 100, true);
	    DirectedWeightedEdge secondToThirdEdge = new DirectedWeightedEdge(second, third, 100, true);
	    
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(firstToSecondEdge.toJson());
	    edgesArray.add(secondToThirdEdge.toJson());

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonObject thirdNodeJson = third.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);
	    nodesArray.add(thirdNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 3);
	    Assert.assertTrue(graph.getEdges().size() == 2);

	    Assert.assertTrue(graph.getEdge(first, second).equals(firstToSecondEdge));
	    Assert.assertTrue(graph.getEdge(second, third).equals(secondToThirdEdge));
	}
    }

    @Test
    public void testFromJsonValidGraphTwoNodesEdgeToInexistentNode() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("to", "Third");
	    edge.addProperty("from", "Second");
	    edge.addProperty("cost", 154);
	    edge.addProperty("isDirected", true);
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 3);
	    Assert.assertTrue(graph.getEdges().size() == 1);
	}
    }

    @Test
    public void testFromJsonValidGraphTwoNodesEdgeFromInexistentNode() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("from", "Third");
	    edge.addProperty("to", "Second");
	    edge.addProperty("cost", 154);
	    edge.addProperty("isDirected", true);
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 3);
	    Assert.assertTrue(graph.getEdges().size() == 1);
	}
    }

    @Test
    public void testFromJsonValidEdgeMissingCost() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("isDirected", true);
	    edge.addProperty("from", "First");
	    edge.addProperty("to", "Second");
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 2);
	    Assert.assertTrue(graph.getEdges().size() == 1);
	    Assert.assertTrue(graph.getEdges().get(0).getCost() == 1);
	    Assert.assertTrue(graph.getEdges().get(0).isDirected());
	}
    }

    @Test
    public void testFromJsonValidEdgeMissingIsDirected() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("cost", 154);
	    edge.addProperty("from", "First");
	    edge.addProperty("to", "Second");
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 2);
	    Assert.assertTrue(graph.getEdges().size() == 1);
	    Assert.assertTrue(graph.getEdges().get(0).getCost() == 154);
	    Assert.assertTrue(graph.getEdges().get(0).isDirected());
	}
    }

    @Test
    public void testFromJsonInvalidEdgeMissingNodeFrom() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("cost", 154);
	    edge.addProperty("isDirected", true);
	    edge.addProperty("to", "Second");
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 2);
	    Assert.assertTrue(graph.getEdges().isEmpty());
	}
    }

    @Test
    public void testFromJsonInvalidEdgeMissingNodeTo() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("cost", 154);
	    edge.addProperty("isDirected", true);
	    edge.addProperty("from", "First");
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 2);
	    Assert.assertTrue(graph.getEdges().isEmpty());
	}
    }

    @Test
    public void testFromJsonInvalidEdgeMissingNodeFromNodeTo() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("cost", 154);
	    edge.addProperty("isDirected", true);
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.getNodes().size() == 2);
	    Assert.assertTrue(graph.getEdges().isEmpty());
	}
    }

    @Test
    public void testFromJsonInvalidGraphTwoNodesNoEdges() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", new JsonArray());

	    try {
		DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);
	    } catch(Exception e) {
		Assert.assertTrue(e.getMessage().contains("No edges found in graph"));
	    }
	}
    }

    @Test
    public void testFromJsonInvalidGraphNoNodes() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", new JsonArray());
	    graphJsonObject.add("edges", new JsonArray());

	    try {
		DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);
	    } catch(Exception e) {
		Assert.assertTrue(e.getMessage().contains("No nodes found in graph"));
	    }
	}
    }

    @Test
    public void testFromJsonInvalidGraphEmpty() throws Exception {
	JsonObject graphEmptyJson = new JsonObject();

	try {
	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphEmptyJson);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Cannot import empty graph"));
	}
    }

    @Test
    public void testFromJsonInvalidGraphNull() throws Exception {
	JsonObject graphEmptyJson = null;

	try {
	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphEmptyJson);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Cannot import empty graph"));
	}
    }

    @Test
    public void testInitNodesNonEmptyGraph() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("cost", 154);
	    edge.addProperty("isDirected", true);
	    edge.addProperty("from", "First");
	    edge.addProperty("to", "Second");
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);
	    DirectedWeightedGraphImported copy = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.initNodes().isEmpty());
	    Assert.assertTrue(graph.equals(copy));
	    Assert.assertTrue(graph.getNodes().size() == 2);
	    Assert.assertTrue(graph.getEdges().size() == 1);
	}
    }

    @Test
    public void testEdgesNodesNonEmptyGraph() throws Exception {
	for (int i = 0; i < RUN_LIMIT_SMALL; i++) {
	    Node first = new Node("First", "First");
	    Node second = new Node("Second", "Second");

	    JsonObject edge = new JsonObject();
	    edge.addProperty("cost", 154);
	    edge.addProperty("isDirected", true);
	    edge.addProperty("from", "First");
	    edge.addProperty("to", "Second");
	    JsonArray edgesArray = new JsonArray();
	    edgesArray.add(edge);

	    JsonObject firstNodeJson = first.toJson();
	    JsonObject secondNodeJson = second.toJson();
	    JsonArray nodesArray = new JsonArray();
	    nodesArray.add(firstNodeJson);
	    nodesArray.add(secondNodeJson);

	    JsonObject graphJsonObject = new JsonObject();
	    graphJsonObject.add("nodes", nodesArray);
	    graphJsonObject.add("edges", edgesArray);

	    DirectedWeightedGraphImported graph = new DirectedWeightedGraphImported(graphJsonObject);
	    DirectedWeightedGraphImported copy = new DirectedWeightedGraphImported(graphJsonObject);

	    Assert.assertTrue(graph.initEdges().isEmpty());
	    Assert.assertTrue(graph.equals(copy));
	    Assert.assertTrue(graph.getNodes().size() == 2);
	    Assert.assertTrue(graph.getEdges().size() == 1);
	}
    }
}
