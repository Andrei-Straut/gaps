package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphSemiRandom;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
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

public class EvolveMessageDispatcherTest {

    private Controller controller;
    private Session session;
    private MessageRequestMock messageRequestMock;
    private GetGraphMessageDispatcher graphDispatcher;
    private ComputePathMessageDispatcher pathDispatcher;
    private DirectedWeightedGraph graph;
    private ArrayList<DirectedWeightedGraphPath> paths;

    public EvolveMessageDispatcherTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger(EvolveMessageDispatcherTest.class.getName()).log(Level.INFO,
                "{0} TEST: EvolveMessageDispatcher",
                EvolveMessageDispatcherTest.class.toString());
    }

    @Before
    public void setUp() throws Exception {
        this.controller = new Controller();
        this.session = new SessionMock().getSession();
        this.messageRequestMock = new MessageRequestMock();

        this.graphDispatcher = new GetGraphMessageDispatcher(this.controller, this.session, MessageType.GETGRAPH);
        this.graphDispatcher.setSendUpdates(false);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();
        this.graphDispatcher.setRequest(getGraphRequest);
        this.graphDispatcher.setParameters(new ArrayList<>());
        this.graphDispatcher.process();

        this.graph = graphDispatcher.getGraph();

        this.pathDispatcher = new ComputePathMessageDispatcher(this.controller, this.session, MessageType.COMPUTEPATHS);
        this.pathDispatcher.setSendUpdates(false);
        MessageRequest pathRequest = this.messageRequestMock.getComputePathsRequest();
        this.pathDispatcher.setRequest(pathRequest);
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);
        this.pathDispatcher.setParameters(parameters);
        this.pathDispatcher.process();

        this.paths = pathDispatcher.getPaths();
    }

    @Test
    public void testSetRequestValid() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        Assert.assertTrue(evolveDispatcher.request.equals(evolveRequest));
    }

    @Test
    public void testSetRequestInvalidMissingNumberOfEvolutions() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveRequest.getData().remove("numberOfEvolutions");

        try {
            evolveDispatcher.setRequest(evolveRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Genetic request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidMissingStopConditionPercent() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveRequest.getData().remove("stopConditionPercent");

        try {
            evolveDispatcher.setRequest(evolveRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Genetic request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidMissingParameters() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveRequest.getData().remove("numberOfEvolutions");
        evolveRequest.getData().remove("stopConditionPercent");

        try {
            evolveDispatcher.setRequest(evolveRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Genetic request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidDifferentRequest() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest getGraphRequest = this.messageRequestMock.getGetGraphRequest();

        try {
            evolveDispatcher.setRequest(getGraphRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Genetic request malformed, missing parameters"));
        }
    }

    @Test
    public void testSetRequestInvalidNullRequest() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = null;

        try {
            evolveDispatcher.setRequest(evolveRequest);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Request invalid, missing data"));
        }
    }

    @Test
    public void testSetParametersValid() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);
        parameters.add(this.paths);

        evolveDispatcher.setParameters(parameters);
        Assert.assertTrue(((DirectedWeightedGraph) evolveDispatcher.parameters.get(0))
                .equals(this.graph));
        Assert.assertTrue(((ArrayList) evolveDispatcher.parameters.get(1))
                .equals(this.paths));
    }

    @Test
    public void testSetParametersInvalidGraphDifferentObject() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(new Object());
        parameters.add(this.paths);

        try {
            evolveDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Could not find computed graph. Cannot continue"));
        }
    }

    @Test
    public void testSetParametersInvalidGraphNull() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(null);
        parameters.add(this.paths);

        try {
            evolveDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Could not find computed graph. Cannot continue"));
        }
    }

    @Test
    public void testSetParametersInvalidPathsDifferentObject() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);
        parameters.add(new Object());

        try {
            evolveDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Could not find computed paths. Cannot continue"));
        }
    }

    @Test
    public void testSetParametersInvalidPathsEmpty() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);
        parameters.add(new ArrayList<Object>());

        try {
            evolveDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Could not find computed paths. Cannot continue"));
        }
    }

    @Test
    public void testSetParametersInvalidEmpty() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = new ArrayList<>();

        try {
            evolveDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Parameters cannot be empty"));
        }
    }

    @Test
    public void testSetParametersInvalidNull() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = null;

        try {
            evolveDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Parameters cannot be empty"));
        }
    }

    @Test
    public void testSetParametersInvalidPathsNull() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);
        parameters.add(null);

        try {
            evolveDispatcher.setParameters(parameters);
        } catch(Exception e) {
            Assert.assertTrue(e.getMessage().contains("Could not find computed paths. Cannot continue"));
        }
    }

    @Test
    public void testProcess() throws Exception {
        EvolveMessageDispatcher evolveDispatcher = new EvolveMessageDispatcher(
                controller, session, MessageType.EVOLVE);
        evolveDispatcher.setSendUpdates(false);
        MessageRequest evolveRequest = this.messageRequestMock.getEvolveRequest();
        evolveDispatcher.setRequest(evolveRequest);

        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(this.graph);
        parameters.add(this.paths);

        evolveDispatcher.setParameters(parameters);
        
        boolean evolveResult = evolveDispatcher.process();
        Assert.assertTrue(evolveResult);
    }
}
