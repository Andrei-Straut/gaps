package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.gaps.datamodel.graph.Node;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

public class ComputePathMessageDispatcher extends MessageDispatcher {

    private final Controller controller;
    private final Session session;
    private final MessageType type;

    private DirectedWeightedGraph graph;
    private Node sourceNode;
    private Node destinationNode;
    private int numberOfPaths;

    private ArrayList<DirectedWeightedGraphPath> paths;

    public ComputePathMessageDispatcher(Controller controller, Session session, MessageType type) {
	super(controller, session, type);
	this.controller = controller;
	this.session = session;
	this.type = type;
    }

    @Override
    boolean setRequest(MessageRequest request) throws Exception {
        if(request == null || request.getData() == null) {
            throw new NullPointerException("Request invalid, missing data");
        }
        
	if (!request.getData().has("sourceNode")
		|| !request.getData().has("destinationNode")
		|| !request.getData().has("numberOfPaths")) {

	    throw new Exception("Path request malformed, missing parameters");
	}

	int sourceNodeId = request.getData().get("sourceNode").getAsInt();
	int destinationNodeId = request.getData().get("destinationNode").getAsInt();

	if (sourceNodeId == destinationNodeId) {
	    throw new Exception("Source and destination nodes must be different");
	}

	this.request = request;

	return true;
    }

    @Override
    void setParameters(ArrayList<Object> parameters) throws Exception {
	if (parameters == null || parameters.isEmpty() || 
                !(parameters.get(0) instanceof DirectedWeightedGraph)) {
            
	    throw new Exception("First parameter must be a DirectedWeightedGraph");
	}

	this.graph = (DirectedWeightedGraph) parameters.get(0);
	
	int sourceNodeId = this.request.getData().get("sourceNode").getAsInt();
	int destinationNodeId = this.request.getData().get("destinationNode").getAsInt();

	if (graph.getNodes().size() <= sourceNodeId
		|| graph.getNodes().size() <= destinationNodeId) {
	    throw new Exception("Source or destination node not found in graph");
	}

	this.sourceNode = this.graph.getNodes().get(sourceNodeId);
	this.destinationNode = this.graph.getNodes().get(destinationNodeId);
	this.numberOfPaths = request.getData().get("numberOfPaths").getAsInt();
	this.parameters = parameters;
    }

    @Override
    boolean process() throws Exception {
	this.paths = this.graph.getKPathsDepthFirst(this.sourceNode, this.destinationNode, this.numberOfPaths);

	if (this.paths.isEmpty()) {
	    throw new Exception("Could not find any valid paths from source to destination");
	}

	Logger.getLogger(Controller.class.getName()).log(
		Level.INFO, "Request from {0} with callback ID {1}. Finished processing paths",
		new Object[]{session.getId(), request.getCallbackId()});

	for (DirectedWeightedGraphPath path : this.paths) {
	    updateProgress(new MessageResponse(request.getCallbackId(), HttpServletResponse.SC_OK, false, "Ok", path.toJson()));
	}

	updateProgress(new MessageResponse(request.getCallbackId(), HttpServletResponse.SC_OK, true, "Ok"));

	Logger.getLogger(Controller.class.getName()).log(
		Level.INFO, "Request from {0} with callback ID {1} processed",
		new Object[]{session.getId(), request.getCallbackId()});
	return true;
    }

    @Override
    void updateProgress(MessageResponse response) {
        if(this.sendUpdates) {
            this.controller.respond(this.session, response);
        }
    }

    public ArrayList<DirectedWeightedGraphPath> getPaths() {
	return paths;
    }
}
