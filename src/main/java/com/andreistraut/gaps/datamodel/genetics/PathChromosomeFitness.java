package com.andreistraut.gaps.datamodel.genetics;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class PathChromosomeFitness extends FitnessFunction {
    
    private double lastComputedFitnessValue;

    public PathChromosomeFitness() {
    }
    
    @Override
    public double getLastComputedFitnessValue() {
	return this.lastComputedFitnessValue;
    }

    @Override
    public double getFitnessValue(IChromosome subject) {
	this.lastComputedFitnessValue = this.evaluate(subject);
	return this.lastComputedFitnessValue;
    }

    @Override
    protected double evaluate(IChromosome subject) {
	if(!(subject instanceof PathChromosome)) {
	    return (double) Integer.MIN_VALUE;
	}
	
	PathChromosome chromosome = (PathChromosome) subject;
	
	if (!chromosome.isLegal()) {
	    return (double) Integer.MIN_VALUE;
	}
	
	this.lastComputedFitnessValue = chromosome.getFitnessValue();
	return this.lastComputedFitnessValue;
    }
}
