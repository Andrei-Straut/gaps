
package com.andreistraut.gaps.datamodel.translator;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.Node;
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
