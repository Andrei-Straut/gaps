package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import org.jgap.RandomGenerator;
import org.jgap.impl.StockRandomGenerator;

/**
 * Checks whether two PathChromosomes given as parameter can perform a valid
 * crossover
 * 
 * Conditions for crossover are:
 * - chance check must pass (depending on how high crossover rate is)
 * - both PathChromosomes must be legal
 */
class PathChromosomeCrossoverValidator {
    private boolean canCrossover;
    private final double matingChancePercentage;
    private final PathChromosome male;
    private final PathChromosome female;
    private final RandomGenerator generator;
    
    public PathChromosomeCrossoverValidator(
	    PathChromosome male, PathChromosome female, double matingChancePercentage) {
	
	this.canCrossover = true;
	this.male = male;
	this.female = female;
	
	if(matingChancePercentage > 1) {
	    matingChancePercentage = 1;
	}
	this.matingChancePercentage = matingChancePercentage;
	this.generator = new StockRandomGenerator();
    }
    
    public boolean canCrossover() {
	if(generator.nextDouble() > matingChancePercentage) {
	    this.canCrossover = false;
	    return this.canCrossover;
	}
	
	if(!male.isLegal() || !female.isLegal()) {
	    this.canCrossover = false;
	    return this.canCrossover;
	}
	
	return this.canCrossover;
    }
    
    public PathChromosome getMale() {
	return this.male;
    }
    
    public PathChromosome getFemale() {
	return this.female;
    }
}
