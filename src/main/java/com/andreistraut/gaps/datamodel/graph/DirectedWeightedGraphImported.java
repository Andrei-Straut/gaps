package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;

public class DirectedWeightedGraphImported extends DirectedWeightedGraph {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public DirectedWeightedGraphImported(JsonArray graph) throws Exception {
	super(1, 1);
	this.fromJson(graph);
    }

    private DirectedWeightedGraphImported(int numberOfNodes, int numberOfEdges) {
	super(numberOfNodes, numberOfEdges);
    }

    private DirectedWeightedGraphImported(GraphSettings settings) {
	super(settings);
    }
    //</editor-fold>

    @Override
    public ArrayList<Node> initNodes() {
	return new ArrayList<>(this.vertexSet());
    }

    @Override
    public ArrayList<DirectedWeightedEdge> initEdges() {
	return new ArrayList<>(this.edgeSet());
    }

    public DirectedWeightedGraphImported fromJson(JsonArray graph) throws Exception {
	if(graph == null || graph.size() == 0) {
	    throw new Exception("Cannot import empty graph");
	}
	
	//first, import all nodes
	for (int i = 0; i < graph.size(); i++) {
	    JsonObject nodeJson = graph.get(i).getAsJsonObject();
	    Node node = new Node(nodeJson);
	    this.addVertex(node);
	}

	//then, import all edges
	for (int i = 0; i < graph.size(); i++) {
	    JsonObject nodeJson = graph.get(i).getAsJsonObject();

	    if (!nodeJson.has("adjacencies")
		    || !nodeJson.get("adjacencies").isJsonArray()
		    || ((JsonArray) nodeJson.get("adjacencies").getAsJsonArray()).size() == 0) {
		continue;
	    }

	    JsonArray edgesJson = nodeJson.get("adjacencies").getAsJsonArray();
	    for (int j = 0; j < edgesJson.size(); j++) {
		JsonObject edgeJson = edgesJson.get(j).getAsJsonObject();

		if (edgeJson.has("nodeFrom") && edgeJson.has("nodeTo")) {
		    Node source = null;		    
		    if(this.hasNode(edgeJson.get("nodeFrom").getAsString())) {
			source = this.getNodeById(edgeJson.get("nodeFrom").getAsString());
		    } else {
			source = new Node(edgeJson.get("nodeFrom").getAsString());
			this.addVertex(source);
		    }
		    
		    Node destination = null;
		    if(this.hasNode(edgeJson.get("nodeTo").getAsString())) {
			destination = this.getNodeById(edgeJson.get("nodeTo").getAsString());
		    } else {
			destination = new Node(edgeJson.get("nodeTo").getAsString());
			this.addVertex(destination);
		    }

		    if(source == null || destination == null) {
			continue;
		    }

		    int cost = 0;
		    boolean isDirected = true;
		    if (edgeJson.has("data")) {
			JsonObject data = edgeJson.get("data").getAsJsonObject();

			if (data.has("cost")) {
			    cost = data.get("cost").getAsInt();
			}

			if (data.has("isDirected")) {
			    isDirected = data.get("isDirected").getAsBoolean();
			}
		    }

		    DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination, cost, isDirected);		    
		    this.addEdge(source, destination, edge);
		}
	    }
	}

	this.numberOfNodes = this.vertexSet().size();
	this.numberOfEdges = this.vertexSet().size();

	return this;
    }
}
