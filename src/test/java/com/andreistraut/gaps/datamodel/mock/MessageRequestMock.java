
package com.andreistraut.gaps.datamodel.mock;

import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.google.gson.JsonObject;

public class MessageRequestMock {
    private MessageRequest getGraphRequestMock;
    private MessageRequest uploadGraphRequestMock;
    private MessageRequest computePathsRequestMock;
    private MessageRequest evolveRequestMock;
    private MessageRequest compareRequestMock;
    
    private final JsonObject computeEvolveCompareRequestJsonData;
    
    public MessageRequestMock() {
	this.computeEvolveCompareRequestJsonData = new JsonObject();
	this.computeEvolveCompareRequestJsonData.addProperty("comparePaths", 5);
	this.computeEvolveCompareRequestJsonData.addProperty("sourceNode", 0);
	this.computeEvolveCompareRequestJsonData.addProperty("destinationNode", 29);
	this.computeEvolveCompareRequestJsonData.addProperty("numberOfPaths", 100);
	this.computeEvolveCompareRequestJsonData.addProperty("numberOfEvolutions", 1000);
	this.computeEvolveCompareRequestJsonData.addProperty("stopConditionPercent", 100);
	
	this.initGetGraphMock();
	this.initUploadGraphRequestMock();
	this.initComputePathsRequestMock();
	this.initEvolveRequestMock();
	this.initCompareRequestMock();
    }
    
    private void initGetGraphMock() {
	JsonObject getGraphRequest = new JsonObject();
        
        JsonObject getGraphRequestData = new JsonObject();
        getGraphRequestData.addProperty("numberOfNodes", 30);
        getGraphRequestData.addProperty("numberOfEdges", 100);
        getGraphRequestData.addProperty("minimumEdgeWeight", 1);
        getGraphRequestData.addProperty("maximumEdgeWeight", 100);
        getGraphRequestData.addProperty("isStatic", true);
        
	getGraphRequest.addProperty("callback_id", 1);
	getGraphRequest.addProperty("type", "GetGraph");
	getGraphRequest.add("data", getGraphRequestData);
	
	this.getGraphRequestMock = new MessageRequest(getGraphRequest);
    }
    
    private void initUploadGraphRequestMock() {
	JsonObject uploadGraphRequest = new JsonObject();
	JsonObject graphJson = new JsonObject();
        
	DirectedWeightedGraph graph = new DirectedWeightedGraphMockSemiRandomThreeNodeThreeEdges().getGraph();
	graphJson.add("graph", graph.toJson().get("graph").getAsJsonObject());
	
	uploadGraphRequest.addProperty("callback_id", 1);
	uploadGraphRequest.addProperty("type", "UploadGraph");
	uploadGraphRequest.add("data", graphJson);
	this.uploadGraphRequestMock = new MessageRequest(uploadGraphRequest);
    }
    
    private void initComputePathsRequestMock() {
	JsonObject computePathsRequest = new JsonObject();
	
	computePathsRequest.addProperty("callback_id", 2);
	computePathsRequest.addProperty("type", "ComputePaths");
	computePathsRequest.add("data", this.computeEvolveCompareRequestJsonData);
	
	this.computePathsRequestMock = new MessageRequest(computePathsRequest);
    }
    
    private void initEvolveRequestMock() {
	JsonObject evolveRequest = new JsonObject();	
	
	evolveRequest.addProperty("callback_id", 3);
	evolveRequest.addProperty("type", "Evolve");
	evolveRequest.add("data", this.computeEvolveCompareRequestJsonData);
	
	this.evolveRequestMock = new MessageRequest(evolveRequest);
    }
    
    private void initCompareRequestMock() {
	JsonObject compareRequest = new JsonObject();	
	
	compareRequest.addProperty("callback_id", 4);
	compareRequest.addProperty("type", "Compare");
	compareRequest.add("data", this.computeEvolveCompareRequestJsonData);
	
	this.compareRequestMock = new MessageRequest(compareRequest);
    }

    public MessageRequest getGetGraphRequest() {
	return getGraphRequestMock;
    }

    public MessageRequest getUploadGraphRequest() {
	return uploadGraphRequestMock;
    }

    public MessageRequest getComputePathsRequest() {
	return computePathsRequestMock;
    }

    public MessageRequest getEvolveRequest() {
	return evolveRequestMock;
    }

    public MessageRequest getCompareRequest() {
	return compareRequestMock;
    }
}
