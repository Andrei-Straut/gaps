package com.andreistraut.gaps.datamodel.graph;

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
	if (!(o1 instanceof GraphPath) || !(o2 instanceof GraphPath)) {
	    return 0;
	}

	GraphPath first = (GraphPath) o1;
	GraphPath second = (GraphPath) o2;

	if (first.getEdgeList().isEmpty()) {
	    if (second.getEdgeList().isEmpty()) {
		return 0;
	    }
	    
	    return Integer.MAX_VALUE;
	} else {
	    if (second.getEdgeList().isEmpty()) {
		return Integer.MIN_VALUE;
	    }	    
	}

	int firstCost = 0;
	int secondCost = 0;

	for (Object objEdge : first.getEdgeList()) {
	    DirectedWeightedEdge edge = (DirectedWeightedEdge) objEdge;
	    firstCost += edge.getCost();
	}

	for (Object objEdge : second.getEdgeList()) {
	    DirectedWeightedEdge edge = (DirectedWeightedEdge) objEdge;
	    secondCost += edge.getCost();
	}

	return firstCost - secondCost;
    }

}
