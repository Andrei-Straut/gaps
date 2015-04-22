package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.gaps.datamodel.mock.MessageRequestMock;
import com.andreistraut.gaps.datamodel.mock.SessionMock;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComputePathMessageDispatcherTest {

    private Controller controller;
    private Session session;
    private MessageRequestMock messageRequestMock;
    private GetGraphMessageDispatcher graphDispatcher;
    private DirectedWeightedGraph graph;

    public ComputePathMessageDispatcherTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger(ComputePathMessageDispatcherTest.class.getName()).log(Level.INFO,
                "{0} TEST: ComputePathMessageDispatcher",
                ComputePathMessageDispatcherTest.class.toString());
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
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathDispatcher.setRequest(pathRequest);

        Assert.assertTrue(pathDispatcher.request.equals(pathRequest));
    }

    @Test
    public void testSetRequestInvalidMissingSourceNode() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathRequest.getData().remove("sourceNode");

        try {
            pathDispatcher.setRequest(pathRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Path request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidSourceNodeEqualDestinationNode() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathRequest.getData().addProperty("sourceNode", 1);
        pathRequest.getData().addProperty("destinationNode", 1);

        try {
            pathDispatcher.setRequest(pathRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Source and destination nodes must be different"));
        }
    }

    @Test
    public void testSetRequestInvalidMissingDestinationNode() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathRequest.getData().remove("destinationNode");

        try {
            pathDispatcher.setRequest(pathRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Path request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidMissingNumberOfPaths() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathRequest.getData().remove("numberOfPaths");

        try {
            pathDispatcher.setRequest(pathRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Path request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidMissingParameters() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathRequest.getData().remove("sourceNode");
        pathRequest.getData().remove("destinationNode");
        pathRequest.getData().remove("numberOfPaths");

        try {
            pathDispatcher.setRequest(pathRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Path request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidDifferentRequest() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();

        try {
            pathDispatcher.setRequest(getGraphRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Path request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidNullRequest() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = null;

        try {
            graphDispatcher.setRequest(pathRequest);
        } catch (NullPointerException e) {
            Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
        }
    }

    @Test
    public void testSetParametersValid() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathDispatcher.setRequest(pathRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);

        pathDispatcher.setParameters(parameters);
        Assert.assertTrue(((DirectedWeightedGraph) pathDispatcher.parameters.get(0))
                .equals(this.graph));
    }

    @Test
    public void testSetParametersInvalidSourceNodeInexistent() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathRequest.getData().addProperty("sourceNode", 1000);
        pathDispatcher.setRequest(pathRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);

        try {
            pathDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Source or destination node not found in graph"));
        }
    }

    @Test
    public void testSetParametersInvalidDestinationNodeInexistent() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathRequest.getData().addProperty("destinationNode", 1000);
        pathDispatcher.setRequest(pathRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);

        try {
            pathDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Source or destination node not found in graph"));
        }
    }

    @Test
    public void testSetParametersInvalidSourceAndDestinationNodeInexistent() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathRequest.getData().addProperty("sourceNode", 1000);
        pathRequest.getData().addProperty("destinationNode", 1001);
        pathDispatcher.setRequest(pathRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);

        try {
            pathDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Source or destination node not found in graph"));
        }
    }

    @Test
    public void testSetParametersInvalidEmpty() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathDispatcher.setRequest(pathRequest);

        ArrayList<Object> parameters = new ArrayList<Object>();

        try {
            pathDispatcher.setParameters(parameters);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains(
                    "First parameter must be a DirectedWeightedGraph"));
        }
    }

    @Test
    public void testSetParametersInvalidNull() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathDispatcher.setRequest(pathRequest);

        ArrayList<Object> parameters = null;

        try {
            pathDispatcher.setParameters(parameters);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains(
                    "First parameter must be a DirectedWeightedGraph"));
        }
    }

    @Test
    public void testSetParametersInvalidObject() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        pathDispatcher.setRequest(pathRequest);

        ArrayList<Object> parameters = new ArrayList<Object>();
        parameters.add(new Object());

        try {
            pathDispatcher.setParameters(parameters);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains(
                    "First parameter must be a DirectedWeightedGraph"));
        }
    }

    @Test
    public void testProcess() throws Exception {
        ComputePathMessageDispatcher pathDispatcher = new ComputePathMessageDispatcher(
                controller, session, MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);
        
        pathDispatcher.setRequest(pathRequest);
        pathDispatcher.setParameters(parameters);
        pathDispatcher.process();
        
        Assert.assertTrue(pathDispatcher.getPaths() != null && !pathDispatcher.getPaths().isEmpty());
    }

}
