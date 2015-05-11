package com.andreistraut.gaps.datamodel.graph;

import java.util.ArrayList;

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
        int sourceIndex = 0;
        int destinationIndex = sourceIndex + edgeDifference;
        
	for (int i = numberOfCreatedEdges; i < this.numberOfEdges; i++) {
            
            if(destinationIndex > this.vertexSet().size() - 1) {
                
                edgeDifference++;
                sourceIndex = 0;
                destinationIndex = sourceIndex + edgeDifference;
                
                if(destinationIndex > this.vertexSet().size() - 1) {
                    destinationIndex = this.vertexSet().size() - 1;
                }
                
                if(sourceIndex == destinationIndex && sourceIndex > 0) {
                    sourceIndex = sourceIndex - 1;
                }
            }
            
	    Node source = nodesList.get(sourceIndex);
	    Node destination = nodesList.get(destinationIndex);

	    int cost = this.minimumEdgeWeight + i;
	    if (cost < this.minimumEdgeWeight) {
		cost = this.minimumEdgeWeight;
	    }

	    DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination, cost);

	    this.addEdge(source, destination, edge);
	    edgeList.add(edge);
            
            sourceIndex++;
            destinationIndex++;

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

	return edgeList;
    }

}
