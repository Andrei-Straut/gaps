package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphSemiRandom;

public class GeneticConfiguration extends org.jgap.Configuration {

    private DirectedWeightedGraphSemiRandom graph;

    public GeneticConfiguration(String name) {
	super(name);
    }

    public GeneticConfiguration(String name, DirectedWeightedGraphSemiRandom graph) {
	this(name);
	this.graph = graph;
    }

    public DirectedWeightedGraphSemiRandom getGraph() {
	return graph;
    }

    public void setGraph(DirectedWeightedGraphSemiRandom graph) {
	this.graph = graph;
    }
}
