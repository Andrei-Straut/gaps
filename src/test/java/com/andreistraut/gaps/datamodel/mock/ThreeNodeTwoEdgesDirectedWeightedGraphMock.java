package com.andreistraut.gaps.datamodel.mock;

import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.Node;

public class ThreeNodeTwoEdgesDirectedWeightedGraphMock {

    private Node firstNode;
    private Node secondNode;
    private Node thirdNode;
    private final int firstToSecondEdgeCost = 1;
    private final int secondToThirdEdgeCost = 2;
    private DirectedWeightedEdge firstToSecondEdge;
    private DirectedWeightedEdge secondToThirdEdge;
    private DirectedWeightedGraph graph;

    public ThreeNodeTwoEdgesDirectedWeightedGraphMock() {
	this.reset();
    }

    public final void reset() {
	this.firstNode = new Node("1", "1");
	this.secondNode = new Node("2", "2");
	this.thirdNode = new Node("3", "3");

	this.firstToSecondEdge = new DirectedWeightedEdge(this.firstNode, this.secondNode, this.firstToSecondEdgeCost, true);
	this.secondToThirdEdge = new DirectedWeightedEdge(this.secondNode, this.thirdNode, this.secondToThirdEdgeCost, true);

	this.graph = new DirectedWeightedGraph(3, 2);
	this.graph.addVertex(this.firstNode);
	this.graph.addVertex(this.secondNode);
	this.graph.addVertex(this.thirdNode);
	this.graph.addEdge(this.firstNode, this.secondNode, this.firstToSecondEdge);
	this.graph.addEdge(this.secondNode, this.thirdNode, this.secondToThirdEdge);
    }

    public Node getFirstNode() {
	return this.firstNode;
    }

    public Node getSecondNode() {
	return this.secondNode;
    }

    public Node getThirdNode() {
	return this.thirdNode;
    }

    public int getFirstToSecondEdgeCost() {
	return this.firstToSecondEdgeCost;
    }

    public int getSecondToThirdEdgeCost() {
	return this.secondToThirdEdgeCost;
    }

    public DirectedWeightedEdge getFirstToSecondEdge() {
	return this.firstToSecondEdge;
    }

    public DirectedWeightedEdge getSecondToThirdEdge() {
	return this.secondToThirdEdge;
    }

    public DirectedWeightedGraph getGraph() {
	return this.graph;
    }
}
