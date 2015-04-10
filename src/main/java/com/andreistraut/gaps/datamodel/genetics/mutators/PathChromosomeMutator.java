package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGenePool;
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
    protected double mutationRatePercentage;
    protected boolean allowIllegalMutations;
    protected PathChromosomeOperationMode mutationMode;
    protected boolean printMutationStatistics;

    protected final double DEFAULT_MUTATION_RATE = 0.01;
    protected final String NEW_LINE = System.getProperty("line.separator");

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomeMutator(GeneticConfiguration configuration)
	    throws InvalidConfigurationException {

	super(configuration);
	this.mutationRatePercentage = DEFAULT_MUTATION_RATE;
	this.allowIllegalMutations = false;
	this.mutationMode = PathChromosomeOperationMode.RANDOM;
	this.printMutationStatistics = false;

	this.configuration = configuration;
    }

    public PathChromosomeMutator(GeneticConfiguration configuration,
	    EdgeGenePool genePool, double desiredMutationRate)
	    throws InvalidConfigurationException {

	this(configuration);
	this.mutationRatePercentage = desiredMutationRate;
    }

    public PathChromosomeMutator(GeneticConfiguration configuration,
	    double desiredMutationRate, boolean allowIllegalMutations,
	    PathChromosomeOperationMode mutationMode, boolean printStatistics)
	    throws InvalidConfigurationException {

	this(configuration);
	this.mutationRatePercentage = desiredMutationRate;
	this.allowIllegalMutations = allowIllegalMutations;
	this.mutationMode = mutationMode;
	this.printMutationStatistics = printStatistics;
    }

    private PathChromosomeMutator(GeneticConfiguration configuration,
	    IUniversalRateCalculator mutationRateCalculator) throws InvalidConfigurationException {

	this.configuration = configuration;
    }

    private PathChromosomeMutator(GeneticConfiguration configuration,
	    int desiredMutationRate) throws InvalidConfigurationException {

	this.configuration = configuration;
	this.mutationRatePercentage = convertMutationRate(desiredMutationRate);
    }
    //</editor-fold>

    @Override
    public int getMutationRate() {
	return (int) (this.mutationRatePercentage * 100);
    }

    @Override
    public void setMutationRate(int mutationRate) {
	this.mutationRatePercentage = convertMutationRate(mutationRate);
    }

    public double getMutationRatePercentage() {
	return mutationRatePercentage;
    }

    public void setMutationRatePercentage(double mutationRatePercentage) {
	this.mutationRatePercentage = mutationRatePercentage;
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
	    PathChromosome toMutate = (PathChromosome) candidateChromosomes.get(0);

	    try {
		doMutation(toMutate, candidateChromosomes, configuration.getRandomGenerator());
	    } catch (InvalidConfigurationException ex) {
		Logger.getLogger(PathChromosomeSingleGeneMutator.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

	int len = population.size();
	for (int i = 0; i < len; i++) {
	    candidateChromosomes.add(((PathChromosome) population.getChromosome(i)).clone());
	}
    }

    protected double convertMutationRate(int rate) {
	if (rate > 10000) {
	    rate = 10000;
	    return ((double) rate) / 10000;
	}

	if (rate > 1000) {
	    rate = 1000;
	    return ((double) rate) / 1000;
	}

	if (rate > 100) {
	    rate = 100;
	    return ((double) rate) / 100;
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

    abstract void doMutation(PathChromosome chromosome,
	    List candidateChromosomes, RandomGenerator generator) throws InvalidConfigurationException;
}
