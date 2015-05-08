package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonObject;

public class GraphSettings {

    private int numberOfNodes;
    private int numberOfEdges;
    private int minimumEdgeWeight;
    private int maximumEdgeWeight;
    private boolean isStatic;

    public GraphSettings(int numberOfNodes, int numberOfEdges) {
	this.numberOfNodes = numberOfNodes;
	this.numberOfEdges = numberOfEdges;
	this.minimumEdgeWeight = 1;
	this.maximumEdgeWeight = 1;
	this.isStatic = true;
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

    public boolean isStatic() {
	return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
	this.isStatic = isStatic;
    }

    public JsonObject toJson() {
	JsonObject settings = new JsonObject();

	settings.addProperty("numberOfNodes", this.numberOfNodes);
	settings.addProperty("numberOfEdges", this.numberOfEdges);
	settings.addProperty("minimumEdgeWeight", this.minimumEdgeWeight);
	settings.addProperty("maximumEdgeWeight", this.maximumEdgeWeight);
	settings.addProperty("isStatic", this.isStatic);

	return settings;
    }

    public GraphSettings fromJson(JsonObject settings) throws Exception {
	if (!settings.has("numberOfNodes") || !settings.has("numberOfEdges")) {
	    throw new Exception("Number of nodes and number of edges must always be specified!");
	}

	this.numberOfNodes = settings.get("numberOfNodes").getAsInt();
	this.numberOfEdges = settings.get("numberOfEdges").getAsInt();

	this.minimumEdgeWeight = settings.has("minimumEdgeWeight")
		? settings.get("minimumEdgeWeight").getAsInt()
		: 1;
	
	this.maximumEdgeWeight = settings.has("maximumEdgeWeight") 
		? settings.get("maximumEdgeWeight").getAsInt() 
		: 1;

	this.isStatic = settings.has("isStatic") 
		? settings.get("isStatic").getAsBoolean()
		: true;

	return this;
    }
}
