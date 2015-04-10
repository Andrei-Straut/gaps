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
	firstNode = new Node("1", "1");
	secondNode = new Node("2", "2");
	thirdNode = new Node("3", "3");

	firstToSecondEdge = new DirectedWeightedEdge(firstNode, secondNode, firstToSecondEdgeCost, true);
	secondToThirdEdge = new DirectedWeightedEdge(secondNode, thirdNode, secondToThirdEdgeCost, true);

	graph = new DirectedWeightedGraph(3, 2);
	graph.addVertex(firstNode);
	graph.addVertex(secondNode);
	graph.addVertex(thirdNode);
	graph.addEdge(firstNode, secondNode, firstToSecondEdge);
	graph.addEdge(secondNode, thirdNode, secondToThirdEdge);
    }

    public Node getFirstNode() {
	return firstNode;
    }

    public Node getSecondNode() {
	return secondNode;
    }

    public Node getThirdNode() {
	return thirdNode;
    }

    public int getFirstToSecondEdgeCost() {
	return firstToSecondEdgeCost;
    }

    public int getSecondToThirdEdgeCost() {
	return secondToThirdEdgeCost;
    }

    public DirectedWeightedEdge getFirstToSecondEdge() {
	return firstToSecondEdge;
    }

    public DirectedWeightedEdge getSecondToThirdEdge() {
	return secondToThirdEdge;
    }

    public DirectedWeightedGraph getGraph() {
	return graph;
    }
}
