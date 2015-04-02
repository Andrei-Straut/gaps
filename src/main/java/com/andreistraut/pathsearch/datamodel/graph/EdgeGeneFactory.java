/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andreistraut.pathsearch.datamodel.graph;

import com.andreistraut.pathsearch.datamodel.genetics.EdgeGene;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.InvalidConfigurationException;

/**
 *
 * @author Andrei
 */
public class EdgeGeneFactory implements org.jgrapht.EdgeFactory<Node, EdgeGene> {

    @Override
    public EdgeGene createEdge(Node sourceVertex, Node targetVertex) {
	try {
	    DirectedWeightedEdge edge = new DirectedWeightedEdge(sourceVertex, targetVertex);
	    EdgeGene gene = new EdgeGene(edge, null);
	    return gene;
	} catch (InvalidConfigurationException ex) {
	    Logger.getLogger(EdgeGeneFactory.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	return null;
    }
}
