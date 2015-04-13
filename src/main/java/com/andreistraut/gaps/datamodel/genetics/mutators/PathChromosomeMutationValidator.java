package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import org.jgap.RandomGenerator;
import org.jgap.impl.StockRandomGenerator;

/**
 * Checks whether a PathChromosome given as parameter can perform a mutation
 *
 * Conditions for mutation are: - chance check must pass (depending on how high
 * mutation rate is) - PathChromosome must be legal
 */
class PathChromosomeMutationValidator {

    private boolean canMutate;
    private final double mutationChancePercentage;
    private final PathChromosome toMutate;
    private RandomGenerator generator;

    public PathChromosomeMutationValidator(
	    PathChromosome toMutate, double mutationChancePercentage) {

	this.canMutate = true;
	this.toMutate = toMutate;

	if (mutationChancePercentage > 1) {
	    mutationChancePercentage = 1;
	}
	this.mutationChancePercentage = mutationChancePercentage;
	this.generator = new StockRandomGenerator();
    }

    public boolean canMutate() {
	return this.canMutate(false);
    }

    public boolean canMutate(boolean skipChanceCheck) {
	if (!skipChanceCheck) {
	    if (generator.nextDouble() > mutationChancePercentage) {
		this.canMutate = false;
		return this.canMutate;
	    }
	}

	if (toMutate == null || !toMutate.isLegal()) {
	    this.canMutate = false;
	    return this.canMutate;
	}

	return this.canMutate;
    }

    public PathChromosome getToMutate() {
	return this.toMutate;
    }
}
