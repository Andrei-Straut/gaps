package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeFitnessComparator;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeOperationMode;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.IUniversalRateCalculator;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.impl.MutationOperator;

public abstract class PathChromosomeMutator extends MutationOperator {

    protected GeneticConfiguration configuration;
    protected double mutationRate;
    protected boolean allowIllegalMutations;
    protected PathChromosomeOperationMode mutationMode;
    protected boolean printMutationStatistics;

    protected final double DEFAULT_MUTATION_RATE = 0.01;
    protected final String NEW_LINE = System.getProperty("line.separator");

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomeMutator(GeneticConfiguration configuration)
	    throws InvalidConfigurationException {

	super(configuration);
	this.mutationRate = DEFAULT_MUTATION_RATE;
	this.allowIllegalMutations = false;
	this.mutationMode = PathChromosomeOperationMode.RANDOM;
	this.printMutationStatistics = false;

	this.configuration = configuration;
    }

    public PathChromosomeMutator(GeneticConfiguration configuration, 
	    double desiredMutationRatePercentage)
	    throws InvalidConfigurationException {

	this(configuration);
	this.mutationRate = desiredMutationRatePercentage;
    }

    public PathChromosomeMutator(GeneticConfiguration configuration,
	    double desiredMutationRatePercentage, boolean allowIllegalMutations,
	    PathChromosomeOperationMode mutationMode, boolean printStatistics)
	    throws InvalidConfigurationException {

	this(configuration);
	this.mutationRate = desiredMutationRatePercentage;
	this.allowIllegalMutations = allowIllegalMutations;
	this.mutationMode = mutationMode;
	this.printMutationStatistics = printStatistics;
    }
    //</editor-fold>

    @Override
    public int getMutationRate() {
	return (int) (this.mutationRate * 100);
    }

    @Override
    public void setMutationRate(int mutationRatePercentage) {
	this.mutationRate = convertMutationRate(mutationRatePercentage);
    }

    public double getMutationRatePercentage() {
	return mutationRate;
    }

    public void setMutationRatePercentage(double mutationRatePercentage) {
	this.mutationRate = mutationRatePercentage;
    }

    public boolean isAllowIllegalMutations() {
	return allowIllegalMutations;
    }

    public void setAllowIllegalMutations(boolean allowIllegalMutations) {
	this.allowIllegalMutations = allowIllegalMutations;
    }

    public PathChromosomeOperationMode getMutationMode() {
	return mutationMode;
    }

    public void setMutationMode(PathChromosomeOperationMode mutationMode) {
	this.mutationMode = mutationMode;
    }

    public boolean isPrintMutationStatistics() {
	return printMutationStatistics;
    }

    public void setPrintMutationStatistics(boolean printMutationStatistics) {
	this.printMutationStatistics = printMutationStatistics;
    }

    @Override
    public void operate(Population population, List candidateChromosomes) {
	Collections.sort(candidateChromosomes, new PathChromosomeFitnessComparator());

	if (candidateChromosomes.size() > 0) {
	    PathChromosome toMutate = ((PathChromosome) candidateChromosomes.get(0)).clone();

	    try {
		doMutation(toMutate, candidateChromosomes, configuration.getRandomGenerator());

		/**
		 * Check mutation results, and if valid, add the mutated
		 * chromosome to candidates
		 */
		if ((toMutate.isLegal() || this.allowIllegalMutations)) {
		    toMutate.setIsSelectedForNextGeneration(true);
		    candidateChromosomes.add(toMutate);

		    if (this.configuration.isKeepPopulationSizeConstant()) {
			Collections.sort(candidateChromosomes, new PathChromosomeFitnessComparator());
			candidateChromosomes.remove(candidateChromosomes.size() - 1);
		    }
		}
	    } catch (InvalidConfigurationException ex) {
		Logger.getLogger(PathChromosomeSingleGeneMutator.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    protected double convertMutationRate(int rate) {
	if (rate >= 100) {
	    return 1;
	}
	
	if(rate <= 0) {
	    return 0;
	}

	return ((double) rate) / 100;
    }

    protected void printStatistics(StringBuffer log) {
	if (this.printMutationStatistics && log.length() != 0) {
	    System.out.println(log.toString());
	}
	log.delete(0, log.length());
    }

    abstract int getMutationPosition(RandomGenerator generator, PathChromosome chromosome);

    /**
     * Mutates an individual chromosome, depending on subclass. Only operates on
     * chromosome level, and does not change size of list of candidate
     * chromosomes, or population. Recommended passing a clone(), in order to
     * keep list contents intact
     *
     * @param chromosome Chromosome to mutate
     * @param candidateChromosomes List of candidate chromosomes
     * @param generator Random generator (used in determining mutation chance)
     * @throws InvalidConfigurationException
     */
    abstract void doMutation(PathChromosome chromosome,
	    List candidateChromosomes, RandomGenerator generator) throws InvalidConfigurationException;
}
