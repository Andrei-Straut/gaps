package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.genetics.GeneticEvolver;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphSemiRandom;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.gaps.datamodel.graph.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import org.jgap.IChromosome;
import org.jgrapht.GraphPath;

public class CompareStatisticsMessageDispatcher extends MessageDispatcher {

    private final Controller controller;
    private final Session session;
    private final MessageType type;

    private DirectedWeightedGraphSemiRandom graph;
    private Node sourceNode;
    private Node destinationNode;

    public CompareStatisticsMessageDispatcher(Controller controller, Session session, MessageType type) {
        super(controller, session, type);
        this.controller = controller;
        this.session = session;
        this.type = type;
    }

    @Override
    boolean setRequest(MessageRequest request) throws Exception {
        if(request == null || request.getData() == null) {
            throw new Exception("Request invalid, missing data");
        }
        
        if (!request.getData().has("sourceNode")
                || !request.getData().has("destinationNode")) {

            throw new Exception("Compare request malformed, missing parameters");
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
                !(parameters.get(0) instanceof DirectedWeightedGraphSemiRandom)) {
            
	    throw new Exception("First parameter must be a DirectedWeightedGraph");
	}

        this.graph = (DirectedWeightedGraphSemiRandom) parameters.get(0);

        int sourceNodeId = request.getData().get("sourceNode").getAsInt();
        int destinationNodeId = request.getData().get("destinationNode").getAsInt();

        if (graph.getNodes().size() <= sourceNodeId
                || graph.getNodes().size() <= destinationNodeId) {
            throw new Exception("Source or destination node not found in graph");
        }

        this.sourceNode = this.graph.getNodes().get(sourceNodeId);
        this.destinationNode = this.graph.getNodes().get(destinationNodeId);
        this.parameters = parameters;
    }

    @Override
    boolean process() throws Exception {
        MessageResponse response = new MessageResponse(request.getCallbackId());

        List<GraphPath<Node, DirectedWeightedEdge>> kShortestPaths
                = this.graph.getKShortestPaths(this.sourceNode, this.destinationNode,
                        request.getData().get("comparePaths").getAsInt());
        List<DirectedWeightedGraphPath> directedPaths = new ArrayList<DirectedWeightedGraphPath>();

        for (GraphPath<Node, DirectedWeightedEdge> kshortestPath : kShortestPaths) {
            DirectedWeightedGraphPath path = new DirectedWeightedGraphPath(
                    graph, kshortestPath.getEdgeList());
            directedPaths.add(path);
        }

        GeneticEvolver evolver = new GeneticEvolver(
                0,
                0,
                this.graph, directedPaths);

        List<IChromosome> chromosomes = evolver.init().getChromosomes();
        for (IChromosome chromosome : chromosomes) {
            response
                    .setStatus(HttpServletResponse.SC_OK)
                    .setIsEnded(false)
                    .setData(((PathChromosome) chromosome).toJson())
                    .setDescription("Ok");
            updateProgress(response);
        }

        response
                .setStatus(HttpServletResponse.SC_OK)
                .setIsEnded(true)
                .setData(null)
                .setDescription("Ok");
        updateProgress(response);

        Logger.getLogger(Controller.class.getName()).log(Level.INFO,
                "Request from {0} with callback ID {1} processed",
                new Object[]{session.getId(), request.getCallbackId()});

        return true;
    }

    @Override
    protected void updateProgress(MessageResponse response) {
        if(this.sendUpdates) {
            this.controller.respond(this.session, response);
        }
    }
}
