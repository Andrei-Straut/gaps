package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.GraphSettings;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

public class GetGraphMessageDispatcher extends MessageDispatcher {
    private final Controller controller;
    private final Session session;
    private final MessageType type;

    private GraphSettings settings;
    private DirectedWeightedGraph graph;

    public GetGraphMessageDispatcher(Controller controller, Session session, MessageType type) {
	super(controller, session, type);
	this.controller = controller;
	this.session = session;
	this.type = type;
    }

    @Override
    boolean setRequest(MessageRequest request) throws Exception {
	this.request = request;
	this.settings = new GraphSettings(this.request.getData());
	
	return true;
    }

    @Override
    void setParameters(ArrayList<Object> parameters) {
	this.parameters = parameters;
    }

    @Override
    boolean process() {
	MessageResponse response = new MessageResponse(this.request.getCallbackId());

	try {
	    this.graph = new DirectedWeightedGraph(this.settings);

	    this.graph.initNodes();
	    this.graph.initEdges();

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
		    "There was an error generating the graph: " + e.getMessage(), e);
	    return false;
	}

	return true;
    }

    @Override
    protected void updateProgress(MessageResponse response) {
	this.controller.respond(this.session, response);
    }

    public DirectedWeightedGraph getGraph() {
	return graph;
    }
}
