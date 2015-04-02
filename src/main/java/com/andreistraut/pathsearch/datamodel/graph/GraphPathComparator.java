package com.andreistraut.pathsearch.datamodel.graph;

import java.util.Comparator;
import org.jgrapht.GraphPath;

public class GraphPathComparator implements Comparator {

    /**
     * Compare cost between two graph paths. Lower values are better
     * 
     * @param o1 First path
     * @param o2 Second path
     * @return A positive number if first path is better (lower cost), 0 if they
     * are equal, a negative number if second path is better (lower cost)
     */
    @Override
    public int compare(Object o1, Object o2) {
	if(!(o1 instanceof GraphPath) || !(o2 instanceof GraphPath)) {
	    return 0;
	}
	
	GraphPath first = (GraphPath) o1;
	GraphPath second = (GraphPath) o2;
	int firstCost = 0;
	int secondCost = 0;
	
	for(Object objEdge : first.getEdgeList()) {	    
	    DirectedWeightedEdge gene = (DirectedWeightedEdge) objEdge;
	    firstCost += gene.getCost();
	}
	
	for(Object objEdge : second.getEdgeList()) {
	    DirectedWeightedEdge gene = (DirectedWeightedEdge) objEdge;
	    secondCost += gene.getCost();
	}
	
	return firstCost - secondCost;
    }
    
}
