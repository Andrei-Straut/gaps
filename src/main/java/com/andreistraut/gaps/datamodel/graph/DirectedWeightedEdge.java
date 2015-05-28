package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonObject;
import java.util.UUID;
import org.jgrapht.graph.DefaultWeightedEdge;

public class DirectedWeightedEdge extends DefaultWeightedEdge {

    private Node source;
    private Node destination;
    private int cost;
    private boolean isDirected;
    private String id;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public DirectedWeightedEdge(Node source, Node destination) {
	this.source = source;
	this.destination = destination;
	this.isDirected = true;
	
	this.id = UUID.randomUUID().toString();
    }

    public DirectedWeightedEdge(Node source, Node destination, int cost) {
	this(source, destination);
	this.cost = cost;
    }

    public DirectedWeightedEdge(Node source, Node destination, int cost, boolean isDirected) {
	this(source, destination, cost);
	this.isDirected = isDirected;
    }
    //</editor-fold>

    public Node getSource() {
	return source;
    }

    public void setSource(Node source) {
	this.source = source;
    }

    public Node getDestination() {
	return destination;
    }

    public void setDestination(Node destination) {
	this.destination = destination;
    }

    public boolean isDirected() {
	return isDirected;
    }

    public void setIsDirected(boolean isDirected) {
	this.isDirected = isDirected;
    }

    public int getCost() {
	return cost;
    }

    public void setCost(int cost) {
	this.cost = cost;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}

	if (getClass() != obj.getClass()) {
	    return false;
	}

	final DirectedWeightedEdge other = (DirectedWeightedEdge) obj;

	if (this.source == null || !this.source.equals(other.source)) {
	    return false;
	}

	if (this.destination == null || !this.destination.equals(other.destination)) {
	    return false;
	}

	if (this.cost != other.cost) {
	    return false;
	}

	if (this.isDirected != other.isDirected) {
	    return false;
	}

	return true;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	hash = 43 * hash + (this.isDirected ? 1 : 0);
	hash = 43 * hash + (this.source != null ? this.source.hashCode() : 0);
	hash = 43 * hash + (this.destination != null ? this.destination.hashCode() : 0);
	hash = (int) ((32 >>> this.cost) ^ this.cost) + 43 * hash;
	return hash;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("DirectedWeightedEdge ")
		.append(this.hashCode()).append(" [From: ")
		.append(this.source.getId())
		.append(", To: ")
		.append(this.destination.getId())
		.append("]").append(" (Cost: ")
		.append(this.getCost()).append(")");

	return builder.toString();
    }

    public JsonObject toJson() {
	JsonObject edgeJson = new JsonObject();

	edgeJson.addProperty("from", this.source.getId());
	edgeJson.addProperty("to", this.destination.getId());
	edgeJson.addProperty("id", this.id);
	edgeJson.addProperty("cost", this.cost);
	edgeJson.addProperty("weight", this.cost);
	edgeJson.addProperty("label", this.cost);
	edgeJson.addProperty("title", "From: " + this.getSource().getId() + "<br/>To: " + this.getDestination().getId() + "<br/>Cost: " + this.getCost());
	edgeJson.addProperty("isDirected", this.isDirected);

	return edgeJson;
    }
}
