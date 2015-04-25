
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<DirectedWeightedEdge> initEdges() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
