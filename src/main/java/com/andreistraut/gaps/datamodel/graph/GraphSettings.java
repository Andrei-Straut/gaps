
package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonObject;

public class GraphSettings {
    private int numberOfNodes;
    private int numberOfEdges;
    private int minimumEdgeWeight;
    private int maximumEdgeWeight;
    
    public GraphSettings(int numberOfNodes, int numberOfEdges) {
	this.numberOfNodes = numberOfNodes;
	this.numberOfEdges = numberOfEdges;
	this.minimumEdgeWeight = 1;
	this.maximumEdgeWeight = 1;
    }
    
    public GraphSettings(JsonObject settings) throws Exception {
	this.fromJson(settings);
    }

    public int getNumberOfNodes() {
	return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
	this.numberOfNodes = numberOfNodes;
    }

    public int getNumberOfEdges() {
	return numberOfEdges;
    }

    public void setNumberOfEdges(int numberOfEdges) {
	this.numberOfEdges = numberOfEdges;
    }

    public int getMinimumEdgeWeight() {
	return minimumEdgeWeight;
    }

    public void setMinimumEdgeWeight(int minimumEdgeWeight) {
	this.minimumEdgeWeight = minimumEdgeWeight;
    }

    public int getMaximumEdgeWeight() {
	return maximumEdgeWeight;
    }

    public void setMaximumEdgeWeight(int maximumEdgeWeight) {
	this.maximumEdgeWeight = maximumEdgeWeight;
    }
    
    public JsonObject toJson() {
	JsonObject settings = new JsonObject();
	
	settings.addProperty("numberOfNodes", this.numberOfNodes);
	settings.addProperty("numberOfEdges", this.numberOfEdges);
	settings.addProperty("minimumEdgeWeight", this.minimumEdgeWeight);
	settings.addProperty("maximumEdgeWeight", this.maximumEdgeWeight);
	
	return settings;
    }
    
    public GraphSettings fromJson(JsonObject settings) throws Exception {
	if(!settings.has("numberOfNodes") || !settings.has("numberOfEdges")) {
	    throw new Exception("Number of nodes and number of edges must always be specified!");
	}
	
	this.numberOfNodes = settings.get("numberOfNodes").getAsInt();
	this.numberOfEdges = settings.get("numberOfEdges").getAsInt();
	
	if(settings.has("minimumEdgeWeight")) {
	    this.minimumEdgeWeight = settings.get("minimumEdgeWeight").getAsInt();
	}
	
	if(settings.has("maximumEdgeWeight")) {
	    this.maximumEdgeWeight = settings.get("maximumEdgeWeight").getAsInt();
	}
	
	return this;
    }
}
