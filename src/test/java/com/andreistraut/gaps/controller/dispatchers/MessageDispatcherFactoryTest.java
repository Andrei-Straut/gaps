package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.mock.MessageRequestMock;
import com.andreistraut.gaps.datamodel.mock.SessionMock;
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
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequestMock();

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
}
