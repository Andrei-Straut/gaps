
package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.mock.MessageRequestMock;
import com.andreistraut.gaps.datamodel.mock.SessionMock;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GetGraphMessageDispatcherTest {
    private Controller controller;
    private Session session;
    private MessageRequestMock messageRequestMock;
    
    public GetGraphMessageDispatcherTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger(GetGraphMessageDispatcherTest.class.getName()).log(Level.INFO,
                "{0} TEST: GetGraphMessageDispatcher",
                GetGraphMessageDispatcherTest.class.toString());
    }
    
    @Before
    public void setUp() {
        this.controller = new Controller();
        this.session = new SessionMock().getSession();
        this.messageRequestMock = new MessageRequestMock();
    }

    @Test
    public void testSetRequestValid() throws Exception {
        GetGraphMessageDispatcher graphDispatcher = new GetGraphMessageDispatcher
            (this.controller, this.session, MessageType.GETGRAPH);
        graphDispatcher.setSendUpdates(false);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();

        Assert.assertTrue(graphDispatcher.getGraphSettings() == null);

        graphDispatcher.setRequest(getGraphRequest);

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
    public void testSetRequestInvalidDifferentRequest() throws Exception {
        GetGraphMessageDispatcher graphDispatcher = new GetGraphMessageDispatcher
            (this.controller, this.session, MessageType.GETGRAPH);
        graphDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        
        try {
            graphDispatcher.setRequest(pathRequest);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains(
                    "Number of nodes and number of edges must always be specified"));
        }
    }

    @Test
    public void testSetRequestInvalidNullRequest() throws Exception {
        GetGraphMessageDispatcher graphDispatcher = new GetGraphMessageDispatcher
            (this.controller, this.session, MessageType.GETGRAPH);
        graphDispatcher.setSendUpdates(false);
        MessageRequest graphRequest = null;
        
        try {
            graphDispatcher.setRequest(graphRequest);
        } catch(NullPointerException e) {
            Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
        }
    }

    @Test
    public void testProcess() throws Exception {
        GetGraphMessageDispatcher graphDispatcher = new GetGraphMessageDispatcher
            (this.controller, this.session, MessageType.GETGRAPH);
        graphDispatcher.setSendUpdates(false);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();

        Assert.assertTrue(graphDispatcher.getGraphSettings() == null);
        Assert.assertTrue(graphDispatcher.getGraph() == null);
        
        graphDispatcher.setRequest(getGraphRequest);
        graphDispatcher.setParameters(new ArrayList<>());
        graphDispatcher.process();
        
        Assert.assertTrue(graphDispatcher.getGraph() != null);
        Assert.assertTrue(graphDispatcher.getGraph().getNumberOfNodes()
                == getGraphRequest.getData().get("numberOfNodes").getAsInt());
        Assert.assertTrue(graphDispatcher.getGraph().getNumberOfEdges()
                == getGraphRequest.getData().get("numberOfEdges").getAsInt());
    }
    
}
