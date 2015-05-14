package com.andreistraut.gaps.datamodel.graph;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectedWeightedGraphSemiRandom extends DirectedWeightedGraph {

    public DirectedWeightedGraphSemiRandom(int numberOfNodes, int numberOfEdges) {
	super(numberOfNodes, numberOfEdges);
    }

    public DirectedWeightedGraphSemiRandom(GraphSettings settings) {
	super(settings);
    }
    
    @Override
    public ArrayList<Node> initNodes() {
	for (int i = 0; i < this.numberOfNodes; i++) {
	    Node node = new Node(Integer.toString(i), Integer.toString(i));
	    this.addVertex(node);
	    this.nodeIdMap.put(node.getId(), node);
	    this.nodeNameMap.put(node.getName(), node);
	}

	ArrayList<Node> nodesList = new ArrayList<Node>();
	nodesList.addAll(this.vertexSet());
	
	Logger.getLogger(DirectedWeightedGraphSemiRandom.class.getName()).log(
		Level.FINE, "Generating graph: Finished creating nodes");
	return nodesList;
    }

    @Override
    public ArrayList<DirectedWeightedEdge> initEdges() {
	ArrayList<Node> nodesList = this.getNodes();

	ArrayList<DirectedWeightedEdge> edgeList = new ArrayList<DirectedWeightedEdge>();
	Random generator = new Random();

	for (int i = 0; i < this.numberOfNodes - 1; i++) {
	    Node source = nodesList.get(i);
	    Node destination = nodesList.get(i + 1);
	    int cost = generator.nextInt(this.maximumEdgeWeight + 1);
	    if (cost < this.minimumEdgeWeight) {
		cost = this.minimumEdgeWeight;
	    }

	    DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination, cost);

	    this.addEdge(source, destination, edge);
	    edgeList.add(edge);

	    /**
	     * Statistics
	     */
	    if (cost < this.actualMinimumEdgeCost) {
		this.actualMinimumEdgeCost = cost;
	    }
	    if (cost > this.actualMaximumEdgeCost) {
		this.actualMaximumEdgeCost = cost;
	    }
	    this.totalEdgeCost += cost;
	}

	for (int i = this.numberOfNodes; i <= this.numberOfEdges; i++) {
	    int sourceIndex = generator.nextInt(nodesList.size() - 1);
	    int destinationIndex = generator.nextInt(nodesList.size() - 1);

	    /**
	     * Do not allow nodes to link to themselves
	     */
	    if (sourceIndex == destinationIndex && this.numberOfNodes > 1) {
		if (destinationIndex == 0) {
		    destinationIndex = 1;
		} else {
		    destinationIndex = destinationIndex - 1;
		}
	    }

	    Node source = nodesList.get(sourceIndex);
	    Node destination = nodesList.get(destinationIndex);

	    while (source.equals(destination) && this.numberOfNodes > 1) {
		destination = nodesList.get(generator.nextInt(nodesList.size() - 1));
	    }

	    int cost = generator.nextInt(this.maximumEdgeWeight);
	    if (cost < this.minimumEdgeWeight) {
		cost = this.minimumEdgeWeight;
	    }

	    DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination, cost);

	    this.addEdge(source, destination, edge);
	    edgeList.add(edge);

	    /**
	     * Statistics
	     */
	    if (cost < this.actualMinimumEdgeCost) {
		this.actualMinimumEdgeCost = cost;
	    }
	    if (cost > this.actualMaximumEdgeCost) {
		this.actualMaximumEdgeCost = cost;
	    }
	    this.totalEdgeCost += cost;
	}
	
	Logger.getLogger(DirectedWeightedGraphSemiRandom.class.getName()).log(
		Level.FINE, "Generating graph: Finished creating edges");
	return edgeList;
    }
}
