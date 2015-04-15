package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;

public class GeneticConfiguration extends org.jgap.Configuration {

    private DirectedWeightedGraph graph;

    public GeneticConfiguration(String name) {
	super(name);
    }

    public GeneticConfiguration(String name, DirectedWeightedGraph graph) {
	this(name);
	this.graph = graph;
    }

    public DirectedWeightedGraph getGraph() {
	return graph;
    }

    public void setGraph(DirectedWeightedGraph graph) {
	this.graph = graph;
    }
}
