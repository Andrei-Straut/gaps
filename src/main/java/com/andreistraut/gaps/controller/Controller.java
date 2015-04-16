package com.andreistraut.gaps.controller;

import com.andreistraut.gaps.controller.dispatchers.GetGraphMessageDispatcher;
import com.andreistraut.gaps.controller.dispatchers.CalculatePathMessageDispatcher;
import com.andreistraut.gaps.controller.dispatchers.EvolveMessageDispatcher;
import com.andreistraut.gaps.datamodel.genetics.GenerationStatistic;
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
		    e.printStackTrace();
		    
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
		response = validateCompareRequest(session, request);
		if (response.getStatus() != HttpServletResponse.SC_OK) {
		    respond(session, response);
		    return;
		}

		Node source = this.graph.getNodes().get(request.getData().get("sourceNode").getAsInt());
		Node target = this.graph.getNodes().get(request.getData().get("destinationNode").getAsInt());
		List<GraphPath<Node, DirectedWeightedEdge>> kShortestPaths
			= this.graph.getKShortestPaths(
				source, target, request.getData().get("comparePaths").getAsInt());
		List<DirectedWeightedGraphPath> directedPaths = new ArrayList<DirectedWeightedGraphPath>();

		for (GraphPath<Node, DirectedWeightedEdge> kshortestPath : kShortestPaths) {
		    DirectedWeightedGraphPath path = new DirectedWeightedGraphPath(graph, kshortestPath.getEdgeList());
		    directedPaths.add(path);
		}

		GeneticEvolver evolver = new GeneticEvolver(
			0,
			0,
			this.graph, directedPaths);

		try {
		    List<IChromosome> chromosomes = evolver.init().getChromosomes();
		    for (IChromosome chromosome : chromosomes) {
			response
				.setStatus(HttpServletResponse.SC_OK)
				.setIsEnded(false)
				.setData(((PathChromosome) chromosome).toJson())
				.setDescription("Ok");
			respond(session, response);
		    }

		    response
			    .setStatus(HttpServletResponse.SC_OK)
			    .setIsEnded(true)
			    .setData(null)
			    .setDescription("Ok");
		    respond(session, response);
		} catch (InvalidConfigurationException ex) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
		}

		Logger.getLogger(Controller.class.getName()).log(Level.INFO,
			"Request from {0} with callback ID {1} processed",
			new Object[]{session.getId(), request.getCallbackId()});

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

    private ArrayList<DirectedWeightedGraphPath> getPaths(
	    int sourceNodeId,
	    int destinationNodeId,
	    int numberOfPaths) {

	Node source = this.graph.getNodes().get(sourceNodeId);
	Node target = this.graph.getNodes().get(destinationNodeId);

	return this.graph.getKPathsDepthFirst(source, target, numberOfPaths);
    }

    private MessageResponse validatePathRequest(Session session, MessageRequest request) {
	MessageResponse response = new MessageResponse(request.getCallbackId());

	if (!request.getData().has("sourceNode")
		|| !request.getData().has("destinationNode")
		|| !request.getData().has("numberOfPaths")) {

	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Request from {0} invalid. Path request malformed, missing parameters",
		    session.getId());
	    response
		    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
		    .setIsEnded(true)
		    .setDescription("Path request malformed, missing parameters");
	    return response;
	}

	if (this.graph == null) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Request from {0}: Could not find computed graph. Cannot continue",
		    session.getId());
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
		    .setIsEnded(true)
		    .setDescription("Could not find computed graph. Cannot continue");
	    return response;
	}

	int sourceNode = request.getData().get("sourceNode").getAsInt();
	int destinationNode = request.getData().get("destinationNode").getAsInt();

	if (sourceNode == destinationNode) {
	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
		    .setIsEnded(true)
		    .setDescription("Source and destination nodes must be different");
	    return response;
	}

	if (this.graph.getNodes().size() <= sourceNode
		|| this.graph.getNodes().size() <= destinationNode) {

	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Request from {0}: Source or destination node not found in graph",
		    session.getId());

	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
		    .setIsEnded(true)
		    .setDescription("Source or destination node not found in graph");
	    respond(session, response);
	    return response;
	}

	response
		.setStatus(HttpServletResponse.SC_OK)
		.setIsEnded(true)
		.setDescription("Ok");
	return response;
    }

    private MessageResponse validateGeneticRequest(Session session, MessageRequest request) {
	MessageResponse response = new MessageResponse(request.getCallbackId());

	if (!request.getData().has("numberOfEvolutions")
		|| !request.getData().has("stopConditionPercent")) {

	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Request from {0} invalid: Genetic request malformed, missing parameters",
		    session.getId());
	    response
		    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
		    .setIsEnded(true)
		    .setDescription("Genetic request malformed, missing parameters");
	    return response;
	}

	if (this.graph == null) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Request from {0}: Could not find computed graph. Cannot continue",
		    session.getId());
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
		    .setIsEnded(true)
		    .setDescription("Could not find computed graph. Cannot continue");
	    return response;
	}

	if (this.paths == null || this.paths.isEmpty()) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Request from {0}: could not find a path from source to destination. Cannot continue",
		    session.getId());
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
		    .setIsEnded(true)
		    .setDescription("Could not find a path from source to destination. Cannot continue");
	    return response;
	}

	response
		.setStatus(HttpServletResponse.SC_OK)
		.setIsEnded(true)
		.setDescription("Ok");
	return response;
    }

    private MessageResponse validateCompareRequest(Session session, MessageRequest request) {
	MessageResponse response = new MessageResponse(request.getCallbackId());

	if (!request.getData().has("sourceNode")
		|| !request.getData().has("destinationNode")
		|| !request.getData().has("numberOfPaths")) {

	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Request from {0} invalid: Genetic request malformed, missing parameters",
		    session.getId());
	    response
		    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
		    .setIsEnded(true)
		    .setDescription("Genetic request malformed, missing parameters");
	    return response;
	}

	if (this.graph == null) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Request from {0}: Could not find computed graph. Cannot continue",
		    session.getId());
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
		    .setIsEnded(true)
		    .setDescription("Could not find computed graph. Cannot continue");
	    return response;
	}

	response
		.setStatus(HttpServletResponse.SC_OK)
		.setIsEnded(true)
		.setDescription("Ok");
	return response;
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
