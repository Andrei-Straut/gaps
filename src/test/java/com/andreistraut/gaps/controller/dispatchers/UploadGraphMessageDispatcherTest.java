package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphImported;
import com.andreistraut.gaps.datamodel.graph.Node;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges;
import com.andreistraut.gaps.datamodel.mock.MessageRequestMock;
import com.andreistraut.gaps.datamodel.mock.SessionMock;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class UploadGraphMessageDispatcherTest {

    private Controller controller;
    private Session session;
    private MessageRequestMock messageRequestMock;
    private final int RUN_LIMIT = 100;

    public UploadGraphMessageDispatcherTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(UploadGraphMessageDispatcherTest.class.getName()).log(Level.INFO,
		"{0} TEST: UploadGraphMessageDispatcher",
		UploadGraphMessageDispatcherTest.class.toString());
    }

    @Before
    public void setUp() {
	this.controller = new Controller();
	this.session = new SessionMock().getSession();
	this.messageRequestMock = new MessageRequestMock();
    }

    @Test
    public void testSetRequestValid() throws Exception {
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();

	graphDispatcher.setRequest(uploadGraphRequest);

	Assert.assertTrue(uploadGraphRequest.getData().get("graph") != null);
	Assert.assertTrue(uploadGraphRequest.getData().get("graph") instanceof JsonArray);
	Assert.assertTrue(uploadGraphRequest.getData().get("graph").equals(
		new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges()
		.getGraph().toJson().get("graph").getAsJsonArray()));
    }

    @Test
    public void testSetRequestInvalidDifferentRequest() throws Exception {
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();

	try {
	    graphDispatcher.setRequest(uploadGraphRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
	}
    }

    @Test
    public void testSetRequestInvalidEmptyRequest() throws Exception {
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", new JsonArray());

	try {
	    graphDispatcher.setRequest(uploadGraphRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
	}
    }

    @Test
    public void testSetRequestInvalidNullRequest() throws Exception {
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", null);

	try {
	    graphDispatcher.setRequest(uploadGraphRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
	}
    }

    @Test
    public void testSetRequestInvalidJsonObjectRequest() throws Exception {
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", new JsonObject());

	try {
	    graphDispatcher.setRequest(uploadGraphRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("not a JSON Array"));
	}
    }

    @Test
    public void testProcessValidGraphThreeNodesThreeEdges() throws Exception {
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	DirectedWeightedGraph expected = new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges().getGraph();

	graphDispatcher.setRequest(uploadGraphRequest);
	graphDispatcher.setParameters(new ArrayList<>());
	graphDispatcher.process();

	Assert.assertTrue(graphDispatcher.getGraph() != null);
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfNodes()
		== expected.getNumberOfNodes());
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfEdges()
		== expected.getNumberOfEdges());
    }

    @Test
    public void testProcessValidGraphThreeNodesTwoEdgesLinear() throws Exception {
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
	
	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(firstNodeJson);
	graphJsonArray.add(secondNodeJson);
	graphJsonArray.add(thirdNodeJson);
	
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", graphJsonArray);
	
	graphDispatcher.setRequest(uploadGraphRequest);
	graphDispatcher.setParameters(new ArrayList<>());
	graphDispatcher.process();
	
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfNodes() == 3);
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfEdges() == 2);
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("First"));
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("Second"));
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("Third"));
    }

    @Test
    public void testProcessValidGraphTwoNodesNoEdges() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");

	JsonArray graphJsonArray = new JsonArray();
	graphJsonArray.add(first.toJson());
	graphJsonArray.add(second.toJson());
	
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", graphJsonArray);
	
	graphDispatcher.setRequest(uploadGraphRequest);
	graphDispatcher.setParameters(new ArrayList<>());
	graphDispatcher.process();
	
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfNodes() == 2);
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfEdges() == 0);
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("First"));
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("Second"));
    }

    @Test
    public void testProcessValidGraphTwoNodesEdgeToInexistentNode() throws Exception {
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
	
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", graphJsonArray);
	
	graphDispatcher.setRequest(uploadGraphRequest);
	graphDispatcher.setParameters(new ArrayList<>());
	graphDispatcher.process();
	
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfNodes() == 3);
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfEdges() == 1);
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("First"));
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("Second"));
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("Fourth"));
    }

}
