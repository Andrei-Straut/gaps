package com.andreistraut.pathsearch.datamodel.genetics;

import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedGraph;

public class GeneticConfiguration extends org.jgap.Configuration {

    private DirectedWeightedGraph graph;
    private EdgeGenePool genePool;

    public GeneticConfiguration(String name) {
	super(name);
    }

    public GeneticConfiguration(String name, DirectedWeightedGraph graph) {
	this(name);
	this.graph = graph;
    }

    public GeneticConfiguration(String name, DirectedWeightedGraph graph, EdgeGenePool genePool) {
	this(name, graph);
	this.genePool = genePool;
    }

    public DirectedWeightedGraph getGraph() {
	return graph;
    }

    public void setGraph(DirectedWeightedGraph graph) {
	this.graph = graph;
    }

    public EdgeGenePool getGenePool() {
	return genePool;
    }

    public void setGenePool(EdgeGenePool genePool) {
	this.genePool = genePool;
    }
}
