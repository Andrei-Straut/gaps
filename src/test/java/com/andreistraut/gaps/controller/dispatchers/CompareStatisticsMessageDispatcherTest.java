package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.mock.MessageRequestMock;
import com.andreistraut.gaps.datamodel.mock.SessionMock;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompareStatisticsMessageDispatcherTest {

    private Controller controller;
    private Session session;
    private MessageRequestMock messageRequestMock;
    private GetGraphMessageDispatcher graphDispatcher;
    private DirectedWeightedGraph graph;

    public CompareStatisticsMessageDispatcherTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(CompareStatisticsMessageDispatcherTest.class.getName()).log(Level.INFO,
		"{0} TEST: CompareStatisticsMessageDispatcher",
		CompareStatisticsMessageDispatcherTest.class.toString());
    }

    @Before
    public void setUp() throws Exception {
	this.controller = new Controller();
	this.session = new SessionMock().getSession();
	this.messageRequestMock = new MessageRequestMock();

	graphDispatcher = new GetGraphMessageDispatcher(this.controller, this.session, MessageType.GETGRAPH);
	graphDispatcher.setSendUpdates(false);
	MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();
	graphDispatcher.setRequest(getGraphRequest);
	graphDispatcher.setParameters(new ArrayList<>());
	graphDispatcher.process();

	this.graph = graphDispatcher.getGraph();
    }

    @Test
    public void testSetRequestValid() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareDispatcher.setRequest(compareRequest);

	Assert.assertTrue(compareDispatcher.request.equals(compareRequest));
    }

    @Test
    public void testSetRequestInvalidSourceNodeEqualDestinationNode() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareRequest.getData().addProperty("sourceNode", 1);
	compareRequest.getData().addProperty("destinationNode", 1);

	try {
	    compareDispatcher.setRequest(compareRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Source and destination nodes must be different"));
	}
    }

    @Test
    public void testSetRequestInvalidMissingSourceNode() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareRequest.getData().remove("sourceNode");

	try {
	    compareDispatcher.setRequest(compareRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Compare request malformed, missing parameters"));
	}
    }

    @Test
    public void testSetRequestInvalidMissingDestinationNode() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareRequest.getData().remove("destinationNode");

	try {
	    compareDispatcher.setRequest(compareRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Compare request malformed, missing parameters"));
	}
    }

    @Test
    public void testSetRequestInvalidMissingParameters() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareRequest.getData().remove("sourceNode");
	compareRequest.getData().remove("destinationNode");

	try {
	    compareDispatcher.setRequest(compareRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Compare request malformed, missing parameters"));
	}
    }

    @Test
    public void testSetRequestInvalidDifferentRequest() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();

	try {
	    compareDispatcher.setRequest(pathRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Path request malformed, missing parameters"));
	}
    }

    @Test
    public void testSetRequestInvalidNullRequest() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = null;

	try {
	    compareDispatcher.setRequest(compareRequest);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
	}
    }

    @Test
    public void testSetParametersValid() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareDispatcher.setRequest(compareRequest);

	ArrayList<Object> parameters = new ArrayList<>();
	parameters.add(this.graph);

	compareDispatcher.setParameters(parameters);
	Assert.assertTrue(((DirectedWeightedGraph) compareDispatcher.parameters.get(0))
		.equals(this.graph));
    }

    @Test
    public void testSetParametersInvalidSourceNodeInexistent() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareRequest.getData().addProperty("sourceNode", 1000);
	compareDispatcher.setRequest(compareRequest);

	ArrayList<Object> parameters = new ArrayList<>();
	parameters.add(this.graph);

	try {
	    compareDispatcher.setParameters(parameters);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Source or destination node not found in graph"));
	}
    }

    @Test
    public void testSetParametersInvalidDestinationNodeInexistent() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareRequest.getData().addProperty("destinationNode", 1000);
	compareDispatcher.setRequest(compareRequest);

	ArrayList<Object> parameters = new ArrayList<>();
	parameters.add(this.graph);

	try {
	    compareDispatcher.setParameters(parameters);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Source or destination node not found in graph"));
	}
    }

    @Test
    public void testSetParametersInvalidSourceAndDestinationNodeInexistent() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareRequest.getData().addProperty("sourceNode", 1000);
	compareRequest.getData().addProperty("destinationNode", 1001);
	compareDispatcher.setRequest(compareRequest);

	ArrayList<Object> parameters = new ArrayList<>();
	parameters.add(this.graph);

	try {
	    compareDispatcher.setParameters(parameters);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Source or destination node not found in graph"));
	}
    }

    @Test
    public void testSetParametersInvalidEmpty() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareDispatcher.setRequest(compareRequest);

	ArrayList<Object> parameters = new ArrayList<>();

	try {
	    compareDispatcher.setParameters(parameters);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("First parameter must be a DirectedWeightedGraph"));
	}
    }

    @Test
    public void testSetParametersInvalidObject() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareDispatcher.setRequest(compareRequest);

	ArrayList<Object> parameters = new ArrayList<>();
	parameters.add(new Object());

	try {
	    compareDispatcher.setParameters(parameters);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("First parameter must be a DirectedWeightedGraph"));
	}
    }

    @Test
    public void testSetParametersInvalidNull() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareDispatcher.setRequest(compareRequest);

	ArrayList<Object> parameters = null;

	try {
	    compareDispatcher.setParameters(parameters);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("First parameter must be a DirectedWeightedGraph"));
	}
    }

    @Test
    public void testProcess() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
	compareDispatcher.setRequest(compareRequest);

	ArrayList<Object> parameters = new ArrayList<>();
	parameters.add(this.graph);

	compareDispatcher.setParameters(parameters);
	boolean compareResult = compareDispatcher.process();
	Assert.assertTrue(compareResult);
    }

    @Test
    public void testProcessInvalidNoRequestSet() throws Exception {
	CompareStatisticsMessageDispatcher compareDispatcher = new CompareStatisticsMessageDispatcher(
		controller, session, MessageType.COMPARE);
	compareDispatcher.setSendUpdates(false);
	
	try {
	    boolean compareResult = compareDispatcher.process();
	    //This MUST throw exception, if we get here, it's an error
	    Assert.assertTrue(false);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
	}
    }
}
