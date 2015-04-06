package com.andreistraut.pathsearch.datamodel.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

public class DirectedWeightedGraphPath implements GraphPath<Node, DirectedWeightedEdge> {

    private final DirectedWeightedGraph graph;
    private List<DirectedWeightedEdge> edgeList;

    public DirectedWeightedGraphPath(DirectedWeightedGraph graph) {
	this.graph = graph;
    }

    public DirectedWeightedGraphPath(DirectedWeightedGraph graph, 
	    List<DirectedWeightedEdge> edgeList) {
	
	this(graph);
	this.edgeList = edgeList;
    }

    @Override
    public Graph<Node, DirectedWeightedEdge> getGraph() {
	return this.graph;
    }

    @Override
    public Node getStartVertex() {
	return this.edgeList.get(0).getSource();
    }

    @Override
    public Node getEndVertex() {
	return this.edgeList.get(this.edgeList.size() - 1).getDestination();
    }

    @Override
    public List<DirectedWeightedEdge> getEdgeList() {
	return this.edgeList;
    }

    public void addEdgeToList(DirectedWeightedEdge edge) {
	if (this.edgeList == null) {
	    this.edgeList = new ArrayList<DirectedWeightedEdge>();
	}

	this.edgeList.add(edge);
    }

    public void setEdgeList(ArrayList<DirectedWeightedEdge> edges) {
	this.edgeList = edges;
    }

    @Override
    public double getWeight() {
	int cost = 0;

	for (DirectedWeightedEdge edge : this.edgeList) {
	    cost += edge.getCost();
	}

	return cost;
    }

    public JsonObject toJson() {
	JsonObject jsonPath = new JsonObject();
	JsonArray edgesJson = new JsonArray();
	
	for(DirectedWeightedEdge edge : this.edgeList) {
	    edgesJson.add(edge.toJson());
	}
	
	jsonPath.add("path", edgesJson);
	return jsonPath;
    }
}
