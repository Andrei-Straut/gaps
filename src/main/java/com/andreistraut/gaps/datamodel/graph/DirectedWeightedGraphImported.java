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
	    for (int j = 0; j < edgesJson.size(); i++) {
		JsonObject edgeJson = edgesJson.get(i).getAsJsonObject();

		if (edgeJson.has("nodeFrom") && edgeJson.has("nodeTo")) {
		    Node source = this.hasNode(edgeJson.get("nodeFrom").getAsString())
			    ? this.getNodeById(edgeJson.get("nodeFrom").getAsString())
			    : new Node(edgeJson.get("nodeFrom").getAsString());

		    Node destination = this.hasNode(edgeJson.get("nodeTo").getAsString())
			    ? this.getNodeById(edgeJson.get("nodeTo").getAsString())
			    : new Node(edgeJson.get("nodeTo").getAsString());
		    
		    int cost = 1;
		    boolean isDirected = true;
		    if(edgeJson.has("data")) {
			JsonObject data = edgeJson.get("data").getAsJsonObject();
			
			if(data.has("cost")) {
			    cost = data.get("cost").getAsInt();
			}			
			
			if(data.has("isDirected")) {
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
