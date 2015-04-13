package com.andreistraut.gaps.datamodel.genetics;

import java.util.Comparator;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.IChromosome;

/**
 * Class comparing fitnesses of different chromosomes (paths). Higher values are
 * better
 */
public class PathChromosomeFitnessComparator extends DefaultFitnessEvaluator
	implements Comparator {

    /**
     * Evaluate if first chromosome (path) given as parameter is fitter than the
     * second
     *
     * @param first First chromosome to evaluate
     * @param second Second chromosome to evaluate
     * @return True if first chromosome (path) has a higher fitness (lower cost)
     * than the second, false otherwise
     */
    @Override
    public boolean isFitter(IChromosome first, IChromosome second) {
	if (!(first instanceof PathChromosome) && !(second instanceof PathChromosome)) {
	    return true;
	}

	if ((first instanceof PathChromosome) && !(second instanceof PathChromosome)) {
	    return true;
	}

	if (!(first instanceof PathChromosome) && (second instanceof PathChromosome)) {
	    return false;
	}

	return ((PathChromosome) first).getFitnessValue() > ((PathChromosome) second).getFitnessValue();
    }

    /**
     * Evaluate if first fitness value given as parameter is better than the
     * second
     *
     * @param first First fitness value to evaluate
     * @param second Second fitness value to evaluate
     * @return True if first has higher fitness (lower cost) than the second,
     * false otherwise
     */
    @Override
    public boolean isFitter(double first, double second) {
	return first > second;
    }

    @Override
    public int compare(Object first, Object second) {
	if(first instanceof PathChromosome 
		&& (second == null || !(second instanceof PathChromosome))) {
	    return Integer.MAX_VALUE;
	}
	
	if((first == null || !(first instanceof PathChromosome)) 
		&& (second instanceof PathChromosome)) {
	    return Integer.MIN_VALUE;
	}
	
	if((first == null || !(first instanceof PathChromosome) ) 
		&& (second == null || !(second instanceof PathChromosome))) {
	    return 0;
	}
	
	if (first instanceof PathChromosome && second instanceof PathChromosome) {
	    return ((PathChromosome) first).compareTo((PathChromosome) second);	    
	}

	return 0;
    }
}
