package com.andreistraut.gaps.controller;

import com.andreistraut.gaps.controller.dispatchers.GetGraphMessageDispatcher;
import com.andreistraut.gaps.controller.dispatchers.CalculatePathMessageDispatcher;
import com.andreistraut.gaps.controller.dispatchers.CompareStatisticsMessageDispatcher;
import com.andreistraut.gaps.controller.dispatchers.EvolveMessageDispatcher;
import com.andreistraut.gaps.datamodel.genetics.GeneticEvolver;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.gaps.datamodel.graph.Node;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgrapht.GraphPath;

/**
 * @ServerEndpoint handling the communication between the client and the server.
 *
 * Is the unique link and method of communication between the client interface
 * and the server
 */
@ServerEndpoint("/controller")
public class Controller {

    private DirectedWeightedGraph graph;
    private ArrayList<DirectedWeightedGraphPath> paths;

    /**
     * @param session
     * @OnOpen allows us to intercept the creation of a new session. The session
     * class allows us to send data to the user. In the method onOpen, we'll let
     * the user know that the handshake was successful.
     */
    @OnOpen
    @SuppressWarnings("LoggerStringConcat")
    public void onOpen(Session session) {
        MessageResponse response = new MessageResponse(0, HttpServletResponse.SC_OK, true, "Connection Established", null);

        try {
            session.getBasicRemote().sendText(response.toJsonString());
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
                    "Could not send message to client: " + ex.getMessage(), ex);
        }

        Logger.getLogger(Controller.class.getName()).log(Level.INFO, session.getId() + " has opened a connection");
    }

    /**
     * When a user sends a message to the server, this method will intercept the
     * message and allow us to react to it. For now the message is read as a
     * String.
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        Logger.getLogger(Controller.class.getName()).log(
                Level.INFO, "Message from {0}: {1}",
                new Object[]{session.getId(), message});

        MessageRequest request;
        MessageResponse response;
        try {
            request = new MessageRequest(message);
        } catch (JsonSyntaxException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.INFO,
                    "Error occurred processing message request: " + message, e);

            response = new MessageResponse(0);
            response
                    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                    .setIsEnded(true)
                    .setDescription("Error occurred processing message request: " + message);
            respond(session, response);
            return;
        }

        switch (request.getType()) {
            case GetGraph: {
                GetGraphMessageDispatcher dispatcher = new GetGraphMessageDispatcher(this, session, MessageType.GetGraph);

                try {
                    dispatcher.setRequest(request);
                } catch (Exception e) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
                            "[GetGraph] Request from {0}: {1}",
                            new Object[]{session.getId(), e.getMessage()});

                    response = new MessageResponse(request.getCallbackId());
                    response
                            .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                            .setIsEnded(true)
                            .setDescription(e.getMessage());
                    respond(session, response);
                }

                dispatcher.process();

                Logger.getLogger(Controller.class.getName()).log(
                        Level.INFO, "Request from {0} with callback ID {1}. Finished processing graph",
                        new Object[]{session.getId(), request.getCallbackId()});

                this.graph = dispatcher.getGraph();
                break;
            }
            case ComputePaths: {
                CalculatePathMessageDispatcher dispatcher = new CalculatePathMessageDispatcher(this, session, MessageType.ComputePaths);

                try {
                    dispatcher.setRequest(request);

                    ArrayList<Object> params = new ArrayList<Object>();
                    params.add(this.graph);
                    dispatcher.setParameters(params);

                    dispatcher.process();
                    this.paths = dispatcher.getPaths();

                } catch (Exception e) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
                            "[ComputePaths] Request from {0}: {1}",
                            new Object[]{session.getId(), e.getMessage()});

                    response = new MessageResponse(request.getCallbackId());
                    response
                            .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                            .setIsEnded(true)
                            .setDescription(e.getMessage());
                    respond(session, response);
                }
                break;
            }
            case Evolve: {
                EvolveMessageDispatcher dispatcher = new EvolveMessageDispatcher(this, session, MessageType.ComputePaths);
                try {
                    dispatcher.setRequest(request);

                    ArrayList<Object> params = new ArrayList<>();
                    params.add(this.graph);
                    params.add(this.paths);
                    dispatcher.setParameters(params);

                    dispatcher.process();

                } catch (Exception e) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
                            "[Evolve] Request from {0}: {1}",
                            new Object[]{session.getId(), e.getMessage()});

                    response = new MessageResponse(request.getCallbackId());
                    response
                            .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                            .setIsEnded(true)
                            .setDescription(e.getMessage());
                    respond(session, response);
                }

                break;
            }
            case Compare: {
                CompareStatisticsMessageDispatcher dispatcher = new CompareStatisticsMessageDispatcher(this, session, MessageType.Compare);

                try {
                    dispatcher.setRequest(request);

                    ArrayList<Object> params = new ArrayList<Object>();
                    params.add(this.graph);
                    dispatcher.setParameters(params);

                    dispatcher.process();

                } catch (Exception e) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
                            "[CompareStatistics] Request from {0}: {1}",
                            new Object[]{session.getId(), e.getMessage()});
                    
                    response = new MessageResponse(request.getCallbackId());
                    response
                            .setStatus(HttpServletResponse.SC_BAD_REQUEST)
                            .setIsEnded(true)
                            .setDescription(e.getMessage());
                    respond(session, response);
                }

                break;
            }
        }
    }

    /**
     * The user closes the connection.
     *
     * Note: you can't send messages to the client from this method
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        Logger.getLogger(Controller.class.getName()).log(Level.INFO,
                "Session {0} ended", session.getId());
    }

    public void respond(Session session, MessageResponse response) {
        try {
            session.getBasicRemote().sendText(response.toJsonString());

        } catch (IOException ex) {
            Logger.getLogger(Controller.class
                    .getName()).log(Level.INFO,
                            "Error occurred responding to request: " + ex.getMessage(), ex);
        }
    }
}
