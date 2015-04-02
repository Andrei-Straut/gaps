
package com.andreistraut.pathsearch.datamodel.translator;

import com.andreistraut.pathsearch.datamodel.genetics.EdgeGene;
import com.andreistraut.pathsearch.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.pathsearch.datamodel.genetics.PathChromosome;
import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.pathsearch.datamodel.graph.Node;
import java.util.ArrayList;
import org.jgap.InvalidConfigurationException;
import org.jgrapht.GraphPath;

public class GraphGeneticsTranslator {
    
    public static PathChromosome toPathChromosome(
	    GraphPath<Node, DirectedWeightedEdge> path, 
	    GeneticConfiguration configuration) throws InvalidConfigurationException {	
	
	ArrayList<EdgeGene> genes = new ArrayList<EdgeGene>();
	for(DirectedWeightedEdge edge : path.getEdgeList()) {
	    EdgeGene gene = new EdgeGene(edge, configuration);
	    genes.add(gene);
	}
	
	return new PathChromosome(configuration, genes, 
		path.getStartVertex(), path.getEndVertex());
    }
    
    public static ArrayList<EdgeGene> toEdgeGeneList(GraphPath<Node,DirectedWeightedEdge> edgeList, 
	    GeneticConfiguration configuration) throws InvalidConfigurationException {
	
	ArrayList<EdgeGene> geneList = new ArrayList<EdgeGene>();
	
	for(DirectedWeightedEdge edge : edgeList.getEdgeList()) {
	    geneList.add(new EdgeGene(edge, configuration));
	}
	
	return geneList;
    }
}
