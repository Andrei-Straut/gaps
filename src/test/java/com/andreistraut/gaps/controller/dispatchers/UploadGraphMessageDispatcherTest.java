package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
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
import org.junit.Before;
import org.junit.Test;
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
	Assert.assertTrue(uploadGraphRequest.getData().get("graph") instanceof JsonObject);
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
	uploadGraphRequest.getData().add("graph", new JsonObject());

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
	JsonObject secondNodeJson = second.toJson();
	JsonObject thirdNodeJson = third.toJson();
	
	JsonArray nodeJsonArray = new JsonArray();
	nodeJsonArray.add(firstNodeJson);
	nodeJsonArray.add(secondNodeJson);
	nodeJsonArray.add(thirdNodeJson);
	
	JsonArray edgeJsonArray = new JsonArray();
	edgeJsonArray.add(firstToSecondEdge.toJson());
	edgeJsonArray.add(secondToThirdEdge.toJson());
	
	JsonObject graphObject = new JsonObject();
	graphObject.add("nodes", nodeJsonArray);
	graphObject.add("edges", edgeJsonArray);
	
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", graphObject);
	
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
    public void testProcessValidGraphTwoNodesEdgeToInexistentNode() throws Exception {
	Node first = new Node("First", "First");
	Node second = new Node("Second", "Second");
	
	JsonObject edge = new JsonObject();
	edge.addProperty("from", "First");
	edge.addProperty("to", "Fourth");
	JsonArray edgeJsonArray = new JsonArray();
	edgeJsonArray.add(edge);
	
	JsonObject firstNodeJson = first.toJson();
	JsonObject secondNodeJson = second.toJson();	
	JsonArray nodeJsonArray = new JsonArray();
	nodeJsonArray.add(firstNodeJson);
	nodeJsonArray.add(secondNodeJson);
	
	JsonObject graphObject = new JsonObject();
	graphObject.add("nodes", nodeJsonArray);
	graphObject.add("edges", edgeJsonArray);
	
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", graphObject);
	
	graphDispatcher.setRequest(uploadGraphRequest);
	graphDispatcher.setParameters(new ArrayList<>());
	graphDispatcher.process();
	
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfNodes() == 3);
	Assert.assertTrue(graphDispatcher.getGraph().getNumberOfEdges() == 1);
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("First"));
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("Second"));
	Assert.assertTrue(graphDispatcher.getGraph().hasNode("Fourth"));
    }

    @Test
    public void testProcessInvalidGraphNoRequestSet() throws Exception {
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	
	try {
	    graphDispatcher.process();
	} catch(Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
	}
    }

    @Test
    public void testProcessInvalidGraphInvalidJSONArray() throws Exception {
	UploadGraphMessageDispatcher graphDispatcher = new UploadGraphMessageDispatcher(
		this.controller, this.session, MessageType.UPLOADGRAPH);
	graphDispatcher.setSendUpdates(false);
	
	JsonObject invalidRequest = new JsonObject();
	invalidRequest.add("nodes", new JsonArray());
	invalidRequest.add("edges", new JsonArray());
	
	MessageRequest uploadGraphRequest = this.messageRequestMock.getUploadGraphRequest();
	uploadGraphRequest.getData().add("graph", invalidRequest);
	graphDispatcher.setRequest(uploadGraphRequest);
	graphDispatcher.setParameters(new ArrayList<>());
	
	boolean returnValue = graphDispatcher.process();
	Assert.assertFalse(returnValue);
    }
}
