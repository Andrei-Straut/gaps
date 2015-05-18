package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphImported;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

public class UploadGraphMessageDispatcher extends MessageDispatcher {

    private final Controller controller;
    private final Session session;
    private final MessageType type;

    private DirectedWeightedGraph graph;
    private JsonObject graphJson;

    public UploadGraphMessageDispatcher(Controller controller, Session session, MessageType type) {
	super(controller, session, type);
	this.controller = controller;
	this.session = session;
	this.type = type;
    }

    @Override
    boolean setRequest(MessageRequest request) throws Exception {
	if (request == null 
		|| request.getData() == null 
		|| request.getData().isJsonNull()
		|| !request.getData().has("graph")
		|| request.getData().get("graph").isJsonNull()) {
	    throw new Exception("Request invalid, missing data");
	}

	this.request = request;
	this.graphJson = this.request.getData().get("graph").getAsJsonObject();

	return true;
    }

    @Override
    void setParameters(ArrayList<Object> parameters) throws Exception {
	this.parameters = parameters;
    }

    @Override
    boolean process() throws Exception {
	if (request == null || request.getData() == null) {
	    throw new Exception("Request invalid, missing data");
	}
	
	MessageResponse response = new MessageResponse(this.request.getCallbackId());

	try {
	    this.graph = new DirectedWeightedGraphImported(this.graphJson);

	    response
		    .setStatus(HttpServletResponse.SC_OK)
		    .setIsEnded(true)
		    .setDescription("Ok")
		    .setData(graph.toJson());
	    updateProgress(response);

	} catch (Exception e) {
	    response.setStatus(HttpServletResponse.SC_BAD_REQUEST).setDescription(e.getMessage());
	    updateProgress(response);

	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "There was an error importing the graph: " + e.getMessage(), e);
	    return false;
	}

	return true;
    }

    public DirectedWeightedGraph getGraph() {
	return this.graph;
    }

    @Override
    void updateProgress(MessageResponse response) {
	if (this.sendUpdates) {
	    this.controller.respond(this.session, response);
	}
    }
}
