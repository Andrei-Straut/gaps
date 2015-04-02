package com.andreistraut.pathsearch.datamodel.genetics;

import com.andreistraut.pathsearch.datamodel.genetics.PathChromosome;
import java.util.Comparator;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.IChromosome;

/**
 * Class comparing fitnesses of different chromosomes (paths). Higher values are
 * better
 */
public class PathChromosomeFitnessEvaluator extends DefaultFitnessEvaluator 
    implements Comparator {

    /**
     * Evaluate if first chromosome (path) given as parameter is fitter than 
     * the second
     * 
     * @param first First chromosome to evaluate
     * @param second Second chromosome to evaluate
     * @return True if first chromosome (path) has a higher fitness (lower cost)
     * than the second, false otherwise
     */
    @Override
    public boolean isFitter(IChromosome first, IChromosome second) {
	if(!(first instanceof PathChromosome)) {
	    return false;
	}
	
	if(!(second instanceof PathChromosome)) {
	    return false;
	}
	
	return ((PathChromosome) first).getFitnessValue() > ((PathChromosome) second).getFitnessValue();
    }

    /**
     * Evaluate if first fitness value given as parameter is better than 
     * the second
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
    public int compare(Object o1, Object o2) {
	if(o1 instanceof PathChromosome && o2 instanceof PathChromosome) {
	    return ((PathChromosome) o1).compareTo((PathChromosome) o2);
	}
	
	return 0;
    }
    
}
