package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeFitnessComparator;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeOperationMode;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.jgap.GeneticOperator;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.impl.CrossoverOperator;

public abstract class PathChromosomeCrossover
	extends CrossoverOperator implements GeneticOperator {

    protected GeneticConfiguration configuration;
    protected double crossoverRatePercent;
    protected boolean allowIllegalCrossovers;
    protected boolean allowFullCrossover;
    protected boolean allowCrossOverNewAge;
    protected boolean printCrossoverStatistics;
    protected PathChromosomeOperationMode crossoverMode;
    protected final double DEFAULT_CROSSOVER_RATE = 0.01;
    protected final String NEW_LINE = System.getProperty("line.separator");

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomeCrossover(GeneticConfiguration configuration) throws InvalidConfigurationException {
	super(configuration);
	this.configuration = configuration;

	this.allowIllegalCrossovers = false;
	this.allowFullCrossover = false;
	this.allowCrossOverNewAge = false;
	this.printCrossoverStatistics = false;
	this.crossoverRatePercent = DEFAULT_CROSSOVER_RATE;
	this.crossoverMode = PathChromosomeOperationMode.RANDOM;
    }

    public PathChromosomeCrossover(GeneticConfiguration configuration, double desiredCrossoverRate)
	    throws InvalidConfigurationException {

	super(configuration);
	this.configuration = configuration;
	this.crossoverRatePercent = desiredCrossoverRate;

	this.allowIllegalCrossovers = false;
	this.allowFullCrossover = false;
	this.allowCrossOverNewAge = false;
	this.printCrossoverStatistics = false;
	this.crossoverMode = PathChromosomeOperationMode.RANDOM;
    }
    // </editor-fold>

    @Override
    public boolean isXoverNewAge() {
	return this.allowCrossOverNewAge;
    }

    @Override
    public void setXoverNewAge(boolean xoverNewAge) {
	this.allowCrossOverNewAge = xoverNewAge;
    }

    @Override
    public double getCrossOverRatePercent() {
	return this.crossoverRatePercent;
    }

    @Override
    public int getCrossOverRate() {
	return (int) (this.getCrossOverRatePercent());
    }

    @Override
    public boolean isAllowFullCrossOver() {
	return this.allowFullCrossover;
    }

    @Override
    public void setAllowFullCrossOver(boolean allowFullXOver) {
	this.allowFullCrossover = allowFullXOver;
    }

    public boolean isAllowIllegalCrossovers() {
	return allowIllegalCrossovers;
    }

    public void setAllowIllegalCrossovers(boolean allowIllegalCrossovers) {
	this.allowIllegalCrossovers = allowIllegalCrossovers;
    }

    public PathChromosomeOperationMode getCrossoverMode() {
	return crossoverMode;
    }

    public void setCrossoverMode(PathChromosomeOperationMode crossoverMode) {
	this.crossoverMode = crossoverMode;
    }

    public boolean isPrintCrossoverStatistics() {
	return printCrossoverStatistics;
    }

    public void setPrintCrossoverStatistics(boolean printCrossoverStatistics) {
	this.printCrossoverStatistics = printCrossoverStatistics;
    }

    @Override
    public int compareTo(Object other) {
	return super.compareTo(other);
    }

    protected void printStatistics(StringBuffer log) {
	if (this.printCrossoverStatistics) {
	    System.out.println(log.toString());
	}
	log.delete(0, log.length());
    }

    @Override
    public void operate(Population population, List candidateChromosomes) {
	Collections.sort(candidateChromosomes, new PathChromosomeFitnessComparator());

	if (candidateChromosomes.size() > 1) {
	    PathChromosome first = ((PathChromosome) candidateChromosomes.get(0)).clone();
	    PathChromosome second = ((PathChromosome) candidateChromosomes.get(1)).clone();

	    if (first.equals(second)) {
		second = ((PathChromosome) candidateChromosomes.get(
			new Random().nextInt(candidateChromosomes.size() - 1))).clone();
	    }

	    doCrossover(first, second, candidateChromosomes, configuration.getRandomGenerator());

	    /**
	     * Check crossover results, and if valid, add the mutated chromosome
	     * to candidates
	     */
	    if ((first.isLegal())) {
		first.setIsSelectedForNextGeneration(true);
		candidateChromosomes.add(first);

		if (this.configuration.isKeepPopulationSizeConstant()) {
		    Collections.sort(candidateChromosomes, new PathChromosomeFitnessComparator());
		    candidateChromosomes.remove(candidateChromosomes.size() - 1);
		}
	    }

	    if ((second.isLegal())) {
		second.setIsSelectedForNextGeneration(true);
		candidateChromosomes.add(second);

		if (this.configuration.isKeepPopulationSizeConstant()) {
		    Collections.sort(candidateChromosomes, new PathChromosomeFitnessComparator());
		    candidateChromosomes.remove(candidateChromosomes.size() - 1);
		}
	    }
	} else {
	    return;
	}
    }

    @Override
    protected abstract void doCrossover(IChromosome firstMate, IChromosome secondMate,
	    List candidateChromosomes, RandomGenerator generator);
}
