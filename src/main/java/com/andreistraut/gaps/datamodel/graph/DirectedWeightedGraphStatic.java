package com.andreistraut.gaps.datamodel.graph;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectedWeightedGraphStatic extends DirectedWeightedGraph {

    public DirectedWeightedGraphStatic(int numberOfNodes, int numberOfEdges) {
	super(numberOfNodes, numberOfEdges);
    }

    public DirectedWeightedGraphStatic(GraphSettings settings) {
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

	Logger.getLogger(DirectedWeightedGraphStatic.class.getName()).log(
		Level.FINE, "Generating graph: Finished creating nodes");
	return nodesList;
    }

    @Override
    public ArrayList<DirectedWeightedEdge> initEdges() {
	ArrayList<Node> nodesList = this.getNodes();
	ArrayList<DirectedWeightedEdge> edgeList = new ArrayList<DirectedWeightedEdge>();

	for (int i = 0; i < this.numberOfNodes - 1; i++) {
	    Node source = nodesList.get(i);
	    Node destination = nodesList.get(i + 1);
	    int cost = this.maximumEdgeWeight - i;
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

	int numberOfCreatedEdges = this.edgeSet().size();
	int edgeDifference = 2;
	int edgesLeft = this.numberOfEdges - numberOfCreatedEdges;
	int sourceIndex = 0;
	int destinationIndex = sourceIndex + edgeDifference;

	while (edgesLeft > 0) {
	    if (destinationIndex > this.vertexSet().size() - 1) {
		sourceIndex = 0;
	    }
	    destinationIndex = sourceIndex + edgeDifference;

	    if (destinationIndex > this.vertexSet().size() - 1) {
		edgeDifference++;
		continue;
	    }

	    Node source = nodesList.get(sourceIndex);
	    Node destination = nodesList.get(destinationIndex);

	    int cost = this.maximumEdgeWeight - sourceIndex;
	    if (cost < this.minimumEdgeWeight) {
		cost = this.minimumEdgeWeight;
	    }
	    if (cost > this.maximumEdgeWeight) {
		cost = this.maximumEdgeWeight;
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

	    sourceIndex++;
	    edgesLeft--;
	}

	Logger.getLogger(DirectedWeightedGraphStatic.class.getName()).log(
		Level.FINE, "Generating graph: Finished creating edges");
	return edgeList;
    }

}
