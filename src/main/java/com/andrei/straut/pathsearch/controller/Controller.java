package com.andrei.straut.pathsearch.controller;

import com.andreistraut.pathsearch.datamodel.genetics.GenerationStatistic;
import com.andreistraut.pathsearch.datamodel.genetics.GeneticEvolver;
import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.pathsearch.datamodel.graph.GraphSettings;
import com.andreistraut.pathsearch.datamodel.graph.Node;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.jgap.InvalidConfigurationException;

/**
 * @ServerEndpoint handling the communication between the client and the server.
 *
 * Is the unique link and method of communication between the client interface
 * and the server
 */
@ServerEndpoint("/controller")
public class Controller {

    private enum MessageType {

	GetGraph,
	ComputePaths,
	Evolve,
	Unknown
    }

    private DirectedWeightedGraph graph;
    private ArrayList<DirectedWeightedGraphPath> paths;

    /**
     * @param session
     * @OnOpen allows us to intercept the creation of a new session. The session
     * class allows us to send data to the user. In the method onOpen, we'll let
     * the user know that the handshake was successful.
     */
    @OnOpen
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
	Logger.getLogger(Controller.class.getName()).log(Level.INFO, "Message from " + session.getId() + ": " + message);

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
		response = this.getGraph(request);
		respond(session, response);
		Logger.getLogger(Controller.class.getName()).log(Level.INFO,
			"Request from " + session.getId() + " with callback ID " + request.getCallbackId() + " processed");
		break;
	    }
	    case ComputePaths: {
		response = new MessageResponse(request.getCallbackId());

		if (!this.validatePathRequest(request)) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
			    "Request from " + session.getId() + " invalid. Path request malformed, missing parameters");
		    response
			    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
			    .setIsEnded(true)
			    .setDescription("Path request malformed, missing parameters");
		    respond(session, response);
		    return;
		}

		if (this.graph == null) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
			    "Request from " + session.getId() + ": Could not find computed graph. Cannot continue");
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
			    .setIsEnded(true)
			    .setDescription("Could not find computed graph. Cannot continue");
		    respond(session, response);
		    return;
		}

		int sourceNode = request.getData().get("sourceNode").getAsInt();
		int destinationNode = request.getData().get("destinationNode").getAsInt();

		if (sourceNode == destinationNode) {
		    response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
			    .setIsEnded(true)
			    .setDescription("Source and destination nodes must be different");
		    respond(session, response);
		    return;
		}

		if (this.graph.getNodes().size() < sourceNode
			|| this.graph.getNodes().size() < destinationNode) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
			    "Request from " + session.getId() + ": Source or destination node not found in graph");
		    
		    response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
			    .setIsEnded(true)
			    .setDescription("Source or destination node not found in graph");
		    respond(session, response);
		    return;
		}

		this.paths = this.getPaths(
			request.getData().get("sourceNode").getAsInt(),
			request.getData().get("destinationNode").getAsInt(),
			request.getData().get("numberOfPaths").getAsInt());

		for (DirectedWeightedGraphPath path : this.paths) {
		    respond(session, new MessageResponse(request.getCallbackId(), 200, false, "Ok", path.toJson()));
		}

		respond(session, new MessageResponse(request.getCallbackId(), 200, true, "Ok"));
		Logger.getLogger(Controller.class.getName()).log(Level.INFO,
			"Request from " + session.getId() + " with callback ID " + request.getCallbackId() + " processed");

		break;
	    }
	    case Evolve: {
		response = new MessageResponse(request.getCallbackId());

		if (!this.validatePathRequest(request)) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
			    "Request from " + session.getId() + " invalid: Path request malformed or source or destination node id do not exist");
		    response
			    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
			    .setIsEnded(true)
			    .setDescription("Path request malformed or source or destination node id do not exist");
		    respond(session, response);
		    return;
		}

		if (this.graph == null) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
			    "Request from " + session.getId() + ": Could not find computed graph. Cannot continue");
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
			    .setIsEnded(true)
			    .setDescription("Could not find computed graph. Cannot continue");
		    respond(session, response);
		    return;
		}

		if (this.paths == null || this.paths.isEmpty()) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
			    "Request from " + session.getId() + ": could not find computed paths. Cannot continue");
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
			    .setIsEnded(true)
			    .setDescription("Could not find computed paths. Cannot continue");
		    respond(session, response);
		    return;
		}

		try {
		    int numberOfEvolutions = request.getData().get("numberOfEvolutions").getAsInt();
		    GeneticEvolver evolver = new GeneticEvolver(
			    request.getData().get("numberOfPaths").getAsInt(),
			    numberOfEvolutions,
			    request.getData().get("stopConditionPercent").getAsInt(),
			    this.graph, this.paths);

		    evolver.init();

		    while (!evolver.hasFinished()) {
			//Do not ouput all generations, generates TMI
			if (evolver.hasEvolved()) {

			    GenerationStatistic statistic = evolver.evolveAndGetStatistics();
			    response
				    .setStatus(HttpServletResponse.SC_OK)
				    .setIsEnded(false)
				    .setDescription("Ok").setData(statistic.toJson());
			    respond(session, response);
			} else {
			    evolver.evolveAndGetStatistics();
			}
		    }

		    GenerationStatistic lastStatistic = evolver.getLastStatistic();
		    response
			    .setStatus(HttpServletResponse.SC_OK)
			    .setIsEnded(true)
			    .setData(lastStatistic.toJson())
			    .setDescription("Ok");
		    respond(session, response);

		} catch (InvalidConfigurationException ex) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
			    .setIsEnded(true)
			    .setDescription("Error preparing configuration for genetic algorithm. Cannot continue");
		    return;
		} catch (Exception e) {
		    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
			    .setIsEnded(true)
			    .setDescription("Error running genetic algorithm " + e.getMessage());
		    return;
		}

		Logger.getLogger(Controller.class.getName()).log(Level.INFO,
			"Request from " + session.getId() + " with callback ID " + request.getCallbackId() + " processed");

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
	Logger.getLogger(Controller.class.getName()).log(
		Level.INFO, "Session " + session.getId() + " ended");
    }

    private ArrayList<DirectedWeightedGraphPath> getPaths(
	    int sourceNodeId,
	    int destinationNodeId,
	    int numberOfPaths) {

	Node source = this.graph.getNodes().get(sourceNodeId);
	Node target = this.graph.getNodes().get(destinationNodeId);

	LinkedList<Node> visited = new LinkedList<Node>();
	visited.add(source);

	return this.graph.getKPathsDepthFirst(source, target, numberOfPaths);
    }

    private MessageResponse getGraph(MessageRequest request) {
	MessageResponse response = new MessageResponse(request.getCallbackId());

	try {
	    GraphSettings settings = new GraphSettings(request.getData());
	    this.graph = new DirectedWeightedGraph(settings);

	    this.graph.initNodes();
	    this.graph.initEdges();

	    response
		    .setStatus(HttpServletResponse.SC_OK)
		    .setIsEnded(true)
		    .setDescription("Ok")
		    .setData(graph.toJson());
	} catch (InvalidConfigurationException ex) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "Configuration error generating the graph: " + ex.getMessage(), ex);

	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).setDescription(ex.getMessage());
	} catch (IllegalArgumentException e) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "There was an error generating the graph: " + e.getMessage(), e);

	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST).setDescription(e.getMessage());
	}

	Logger.getLogger(Controller.class.getName()).log(Level.INFO,
		"Returning message: " + response.toJson().toString());

	return response;
    }

    private boolean validatePathRequest(MessageRequest pathRequest) {
	if (pathRequest.getData().has("sourceNode")
		&& pathRequest.getData().has("destinationNode")
		&& pathRequest.getData().has("numberOfPaths")
		&& pathRequest.getData().has("numberOfEvolutions")
		&& pathRequest.getData().has("stopConditionPercent")) {

	    return true;
	}

	return false;
    }

    private void respond(Session session, MessageResponse response) {
	try {
	    session.getBasicRemote().sendText(response.toJsonString());
	} catch (IOException ex) {
	    Logger.getLogger(Controller.class.getName()).log(Level.INFO,
		    "Error occurred responding to graph request: " + ex.getMessage(), ex);
	}
    }

    private class MessageRequest {

	private int callbackId;
	private MessageType type;
	private JsonObject data;

	public MessageRequest(String request) throws JsonSyntaxException {
	    JsonObject requestJson = (new JsonParser()).parse(request).getAsJsonObject();
	    this.fromJson(requestJson);
	}

	public MessageRequest(JsonObject requestJson) {
	    this.fromJson(requestJson);
	}

	private void fromJson(JsonObject json) {
	    this.callbackId = json.get("callback_id").getAsInt();
	    this.type = MessageType.valueOf(json.get("type").getAsString());
	    this.data = json.get("data").getAsJsonObject();
	}

	public int getCallbackId() {
	    return callbackId;
	}

	public MessageType getType() {
	    return this.type;
	}

	public JsonObject getData() {
	    return data;
	}

	public String toJsonString() {
	    return this.toJson().toString();
	}

	public JsonObject toJson() {
	    JsonObject request = new JsonObject();

	    request.addProperty("callback_id", this.callbackId);
	    request.addProperty("messageType", this.type.toString());
	    request.add("data", this.data);

	    return request;
	}
    }

    private class MessageResponse {

	private int callbackId;
	private int status;
	private String description;
	private boolean isEnded;
	private JsonElement data;

	public MessageResponse(int callbackId) {
	    this.callbackId = callbackId;
	    this.status = HttpServletResponse.SC_OK;
	    this.isEnded = true;
	    this.description = "Ok";
	}

	public MessageResponse(int callbackId, boolean isEnded, JsonElement data) {
	    this(callbackId);
	    this.status = HttpServletResponse.SC_OK;
	    this.isEnded = isEnded;
	    this.description = "Ok";
	    this.data = data;
	}

	public MessageResponse(int callbackId, int status, boolean isEnded, String description) {
	    this(callbackId);
	    this.status = status;
	    this.isEnded = isEnded;
	    this.description = description;
	}

	public MessageResponse(int callbackId, int status, boolean isEnded, String description, JsonElement data) {
	    this(callbackId);
	    this.status = status;
	    this.isEnded = isEnded;
	    this.description = description;
	    this.data = data;
	}

	public MessageResponse setStatus(int status) {
	    this.status = status;
	    return this;
	}

	public MessageResponse setDescription(String description) {
	    this.description = description;
	    return this;
	}

	public MessageResponse setData(JsonElement data) {
	    this.data = data;
	    return this;
	}

	public boolean isEnded() {
	    return isEnded;
	}

	public MessageResponse setIsEnded(boolean isEnded) {
	    this.isEnded = isEnded;
	    return this;
	}

	public String toJsonString() {
	    return this.toJson().toString();
	}

	public JsonObject toJson() {
	    JsonObject response = new JsonObject();

	    response.addProperty("callback_id", this.callbackId);
	    response.addProperty("status", this.status);
	    response.addProperty("isEnded", this.isEnded);
	    response.addProperty("description", this.description);
	    response.add("data", this.data);

	    return response;
	}
    }
}
