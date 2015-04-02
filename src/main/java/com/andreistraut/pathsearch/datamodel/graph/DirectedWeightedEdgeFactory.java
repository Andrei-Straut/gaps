package com.andreistraut.pathsearch.datamodel.graph;

public class DirectedWeightedEdgeFactory implements org.jgrapht.EdgeFactory<Node, DirectedWeightedEdge> {

    @Override
    public DirectedWeightedEdge createEdge(Node sourceVertex, Node targetVertex) {
	DirectedWeightedEdge edge = new DirectedWeightedEdge(sourceVertex, targetVertex);
	return edge;
    }

    public DirectedWeightedEdge createEdge(Node sourceVertex, Node targetVertex, int cost) {
	DirectedWeightedEdge edge = this.createEdge(sourceVertex, targetVertex);
	edge.setCost(cost);
	return edge;
    }
}
