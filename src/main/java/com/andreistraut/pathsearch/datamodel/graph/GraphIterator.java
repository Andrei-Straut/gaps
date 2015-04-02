
package com.andreistraut.pathsearch.datamodel.graph;

import org.jgrapht.Graph;
import org.jgrapht.traverse.BreadthFirstIterator;

public class GraphIterator extends BreadthFirstIterator<Node, DirectedWeightedEdge> {

    public GraphIterator(Graph<Node, DirectedWeightedEdge> graph) {
	super(graph);
    }

    public GraphIterator(Graph<Node, DirectedWeightedEdge> graph, Node start) {
	super(graph, start);
    }
}
