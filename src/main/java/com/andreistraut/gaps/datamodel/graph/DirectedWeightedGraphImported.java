package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectedWeightedGraphImported extends DirectedWeightedGraph {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public DirectedWeightedGraphImported(JsonObject graph) throws Exception {
	super(1, 1);
	this.fromJson(graph);
    }
    //</editor-fold>

    @Override
    public ArrayList<Node> initNodes() {
	return new ArrayList<Node>();
    }

    @Override
    public ArrayList<DirectedWeightedEdge> initEdges() {
	return new ArrayList<DirectedWeightedEdge>();
    }

    public DirectedWeightedGraphImported fromJson(JsonObject graph) throws Exception {
	if (graph == null || graph.isJsonNull() || !graph.has("nodes") || !graph.has("edges")) {
	    throw new Exception("Cannot import empty graph");
	}

	if (!graph.has("nodes") || (graph.get("nodes").getAsJsonArray()).size() == 0) {
	    throw new Exception("No nodes found in graph");
	}

	if (!graph.has("edges") || (graph.get("edges").getAsJsonArray()).size() == 0) {
	    throw new Exception("No edges found in graph");
	}

	//first, import all nodes
	JsonArray nodes = graph.get("nodes").getAsJsonArray();
	for (int i = 0; i < nodes.size(); i++) {
	    JsonObject nodeJson = nodes.get(i).getAsJsonObject();
	    Node node = new Node(nodeJson);
	    this.addVertex(node);
	}
	Logger.getLogger(DirectedWeightedGraphImported.class.getName()).log(
		Level.FINE, "Generating graph: Finished creating nodes");

	//then, import all edges
	JsonArray edges = graph.get("edges").getAsJsonArray();
	for (int i = 0; i < edges.size(); i++) {
	    JsonObject edgeJson = edges.get(i).getAsJsonObject();

	    if (!edgeJson.has("from") || !edgeJson.has("to")) {
		continue;
	    }

	    String sourceNodeId = edgeJson.get("from").getAsString();
	    String destinationNodeId = edgeJson.get("to").getAsString();

	    /*
	     TODO: 
	     Probably refactor this a bit, as it's a pretty paranoic case to check again for
	     a source node after having created it, unless we have a node that is storing edges to completely
	     unrelated nodes. 
	     In any case however, it has already been covered in unit tests
	     */
	    Node source = null;
	    if (this.hasNode(sourceNodeId)) {
		source = this.getNodeById(sourceNodeId);
	    } else {
		source = new Node(sourceNodeId);
		this.addVertex(source);
	    }

	    Node destination = null;
	    if (this.hasNode(destinationNodeId)) {
		destination = this.getNodeById(destinationNodeId);
	    } else {
		destination = new Node(destinationNodeId);
		this.addVertex(destination);
	    }

	    if (source == null || destination == null) {
		continue;
	    }

	    int cost = 1;
	    if (edgeJson.has("cost") && edgeJson.get("cost") != null && !edgeJson.get("cost").isJsonNull()) {
		cost = edgeJson.get("cost").getAsInt();
	    }

	    boolean isDirected = true;
	    if (edgeJson.has("isDirected")) {
		isDirected = edgeJson.get("isDirected").getAsBoolean();
	    }

	    DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination, cost, isDirected);
	    this.addEdge(source, destination, edge);
	}
	Logger.getLogger(DirectedWeightedGraphImported.class.getName()).log(
		Level.FINE, "Generating graph: Finished creating edges");

	this.numberOfNodes = this.vertexSet().size();
	this.numberOfEdges = this.vertexSet().size();

	return this;
    }
}
