package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.gaps.datamodel.mock.MessageRequestMock;
import com.andreistraut.gaps.datamodel.mock.SessionMock;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageDispatcherFactoryTest {

    private Controller controller;
    private Session session;
    private MessageRequestMock messageRequestMock;
    private MessageDispatcherFactory factory;

    public MessageDispatcherFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger(MessageDispatcherFactoryTest.class.getName()).log(Level.INFO,
                "{0} TEST: MessageDispatcherFactory",
                MessageDispatcherFactoryTest.class.toString());
    }

    @Before
    public void setUp() {
        this.controller = new Controller();
        this.session = new SessionMock().getSession();
        this.messageRequestMock = new MessageRequestMock();
        this.factory = new MessageDispatcherFactory(controller, session);
    }

    @Test
    public void testGetDispatcherGetGraph() throws Exception {
        MessageDispatcher graphDispatcher = this.factory.getDispatcher(MessageType.GETGRAPH);
        Assert.assertTrue(graphDispatcher instanceof GetGraphMessageDispatcher);
    }

    @Test
    public void testGetDispatcherUploadGraph() throws Exception {
        MessageDispatcher graphDispatcher = this.factory.getDispatcher(MessageType.UPLOADGRAPH);
        Assert.assertTrue(graphDispatcher instanceof UploadGraphMessageDispatcher);
    }

    @Test
    public void testGetDispatcherComputePaths() throws Exception {
        MessageDispatcher graphDispatcher = this.factory.getDispatcher(MessageType.COMPUTEPATHS);
        Assert.assertTrue(graphDispatcher instanceof ComputePathMessageDispatcher);
    }

    @Test
    public void testGetDispatcherEvolve() throws Exception {
        MessageDispatcher graphDispatcher = this.factory.getDispatcher(MessageType.EVOLVE);
        Assert.assertTrue(graphDispatcher instanceof EvolveMessageDispatcher);
    }

    @Test
    public void testGetDispatcherCompare() throws Exception {
        MessageDispatcher graphDispatcher = this.factory.getDispatcher(MessageType.COMPARE);
        Assert.assertTrue(graphDispatcher instanceof CompareStatisticsMessageDispatcher);
    }

    @Test
    public void testGetDispatcherUnknown() throws Exception {
        try {
            MessageDispatcher graphDispatcher = factory.getDispatcher(MessageType.UNKNOWN);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Message Type Unknown"));
        }
    }

    @Test
    public void testGetDispatcherNull() throws Exception {
        try {
            MessageDispatcher graphDispatcher = factory.getDispatcher(null);
        } catch (NullPointerException e) {
            Assert.assertTrue(e.getMessage().contains("Message Type cannot be null"));
        }
    }

    @Test
    public void testInitGetGraphDispatcherRequest() throws Exception {
        GetGraphMessageDispatcher graphDispatcher = (GetGraphMessageDispatcher) this.factory.getDispatcher(MessageType.GETGRAPH);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();

        Assert.assertTrue(graphDispatcher.getGraphSettings() == null);

        this.factory.initDispatcherRequest(graphDispatcher, getGraphRequest);

        Assert.assertTrue(graphDispatcher.getGraphSettings() != null);
        Assert.assertTrue(graphDispatcher.getGraphSettings().getNumberOfNodes()
                == getGraphRequest.getData().get("numberOfNodes").getAsInt());
        Assert.assertTrue(graphDispatcher.getGraphSettings().getNumberOfEdges()
                == getGraphRequest.getData().get("numberOfEdges").getAsInt());
        Assert.assertTrue(graphDispatcher.getGraphSettings().getMinimumEdgeWeight()
                == getGraphRequest.getData().get("minimumEdgeWeight").getAsInt());
        Assert.assertTrue(graphDispatcher.getGraphSettings().getMaximumEdgeWeight()
                == getGraphRequest.getData().get("maximumEdgeWeight").getAsInt());
    }

    @Test
    public void testInitUploadGraphDispatcherRequest() throws Exception {
        UploadGraphMessageDispatcher graphDispatcher = (UploadGraphMessageDispatcher) this.factory.getDispatcher(MessageType.UPLOADGRAPH);
        MessageRequest getGraphRequest = this.messageRequestMock.getUploadGraphRequest();

        Assert.assertTrue(graphDispatcher.getGraph() == null);
        Assert.assertTrue(graphDispatcher.request == null);

        this.factory.initDispatcherRequest(graphDispatcher, getGraphRequest);

        Assert.assertTrue(graphDispatcher.request != null);
        Assert.assertTrue(graphDispatcher.request.getData().has("graph"));
        Assert.assertTrue(graphDispatcher.request.getData().get("graph") instanceof JsonArray);
    }

    @Test
    public void testInitComputePathsDispatcherParameters() throws Exception {
        //Initialize graph
        GetGraphMessageDispatcher graphDispatcher = (GetGraphMessageDispatcher) this.factory.getDispatcher(MessageType.GETGRAPH);
        graphDispatcher.setSendUpdates(false);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();
        this.factory.initDispatcherRequest(graphDispatcher, getGraphRequest);
        this.factory.initDispatcherParams(graphDispatcher);
        this.factory.process(graphDispatcher);
        Assert.assertTrue(graphDispatcher.getGraph() != null);
        
        //Compute paths
        ComputePathMessageDispatcher pathDispatcher = (ComputePathMessageDispatcher) this.factory.getDispatcher(MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();

        Assert.assertTrue(pathDispatcher.getPaths() == null || pathDispatcher.getPaths() == null);

        this.factory.initDispatcherRequest(pathDispatcher, pathRequest);
        this.factory.initDispatcherParams(pathDispatcher);
        
        Assert.assertTrue(pathDispatcher.parameters.get(0) != null);
        DirectedWeightedGraph graph = (DirectedWeightedGraph) pathDispatcher.parameters.get(0);
        Assert.assertTrue(graph.equals(graphDispatcher.getGraph()));
    }
    
    @Test
    public void testInitEvolveDispatcherParameters() throws Exception {
        //Initialize graph
        GetGraphMessageDispatcher graphDispatcher = (GetGraphMessageDispatcher) this.factory.getDispatcher(MessageType.GETGRAPH);
        graphDispatcher.setSendUpdates(false);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();
        this.factory.initDispatcherRequest(graphDispatcher, getGraphRequest);
        this.factory.initDispatcherParams(graphDispatcher);
        this.factory.process(graphDispatcher);
        Assert.assertTrue(graphDispatcher.getGraph() != null);
        
        //Compute paths
        ComputePathMessageDispatcher pathDispatcher = (ComputePathMessageDispatcher) this.factory.getDispatcher(MessageType.COMPUTEPATHS);
        pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        Assert.assertTrue(pathDispatcher.getPaths() == null || pathDispatcher.getPaths() == null);
        this.factory.initDispatcherRequest(pathDispatcher, pathRequest);
        this.factory.initDispatcherParams(pathDispatcher);
        this.factory.process(pathDispatcher);
        
        //Evolve
        EvolveMessageDispatcher evolveDispatcher = (EvolveMessageDispatcher) this.factory.getDispatcher(MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        this.factory.initDispatcherRequest(evolveDispatcher, evolveRequest);
        this.factory.initDispatcherParams(evolveDispatcher);
        
        Assert.assertTrue(evolveDispatcher.parameters.get(0) != null);
        DirectedWeightedGraph graph = (DirectedWeightedGraph) evolveDispatcher.parameters.get(0);
        Assert.assertTrue(graph.equals(graphDispatcher.getGraph()));
        
        Assert.assertTrue(evolveDispatcher.parameters.get(1) != null);
        ArrayList<DirectedWeightedGraphPath> paths = (ArrayList) evolveDispatcher.parameters.get(1);
        Assert.assertTrue(paths.equals(pathDispatcher.getPaths()));
    }
    
    @Test
    public void testInitCompareDispatcherParameters() throws Exception {
        //Initialize graph
        GetGraphMessageDispatcher graphDispatcher = (GetGraphMessageDispatcher) this.factory.getDispatcher(MessageType.GETGRAPH);
        graphDispatcher.setSendUpdates(false);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();
        this.factory.initDispatcherRequest(graphDispatcher, getGraphRequest);
        this.factory.initDispatcherParams(graphDispatcher);
        this.factory.process(graphDispatcher);
        Assert.assertTrue(graphDispatcher.getGraph() != null);
        
        //Compare
        CompareStatisticsMessageDispatcher compareDispatcher = (CompareStatisticsMessageDispatcher) this.factory.getDispatcher(MessageType.COMPARE);
        compareDispatcher.setSendUpdates(false);
        MessageRequest compareRequest = this.messageRequestMock.getCompareRequest();
        this.factory.initDispatcherRequest(compareDispatcher, compareRequest);
        this.factory.initDispatcherParams(compareDispatcher);
        
        Assert.assertTrue(compareDispatcher.parameters.get(0) != null);
        DirectedWeightedGraph graph = (DirectedWeightedGraph) compareDispatcher.parameters.get(0);
        Assert.assertTrue(graph.equals(graphDispatcher.getGraph()));
    }
}
