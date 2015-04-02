package com.andreistraut.pathsearch.datamodel.genetics.mutators;

import com.andreistraut.pathsearch.datamodel.genetics.EdgeGene;
import com.andreistraut.pathsearch.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.pathsearch.datamodel.genetics.PathChromosome;
import com.andreistraut.pathsearch.datamodel.genetics.PathChromosomeOperationMode;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;

/**
 * <p>
 * One-point crossover genetic operator for Chromosomes (paths)</p>
 *
 * <p>
 * Works if and only if there are multiple edges between the same nodes<p/>
 *
 * <p>
 * Mode of operation is as follows:<br/>
 * - For the first chromosome, depending on strategy, pick one gene (random, or
 * highest cost)<br/>
 * - In the second chromosome, check if there is a gene with the same source and
 * destination<br/>
 * - If a matching gene is found, swap them</p>
 *
 */
public class PathChromosomeOnePointCrossover extends PathChromosomeCrossover {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomeOnePointCrossover(GeneticConfiguration configuration)
	    throws InvalidConfigurationException {

	super(configuration);
	this.configuration = configuration;

	this.allowIllegalCrossovers = false;
	this.allowFullCrossover = false;
	this.allowCrossOverNewAge = false;
	this.printCrossoverStatistics = false;
	this.crossoverRatePercentage = DEFAULT_CROSSOVER_RATE;
	this.crossoverMode = PathChromosomeOperationMode.RANDOM;
    }

    public PathChromosomeOnePointCrossover(GeneticConfiguration configuration, double desiredCrossoverRate)
	    throws InvalidConfigurationException {

	super(configuration, desiredCrossoverRate);
	this.configuration = configuration;
	this.crossoverRatePercentage = desiredCrossoverRate;

	this.allowIllegalCrossovers = false;
	this.allowFullCrossover = false;
	this.allowCrossOverNewAge = false;
	this.printCrossoverStatistics = false;
	this.crossoverMode = PathChromosomeOperationMode.RANDOM;
    }
    // </editor-fold>

    @Override
    protected void doCrossover(IChromosome firstMate, IChromosome secondMate,
	    List candidateChromosomes, RandomGenerator generator) {

	PathChromosomeCrossoverValidator breeder = new PathChromosomeCrossoverValidator(
		(PathChromosome) firstMate,
		(PathChromosome) secondMate,
		this.crossoverRatePercentage);

	if (!breeder.canCrossover()) {
	    return;
	}

	PathChromosome first = breeder.getMale().clone();
	PathChromosome second = breeder.getFemale().clone();
	ImmutablePair<Integer, Integer> positions = this.getCrossoverPositions(generator, first, second);
	
	if(positions == null) {
	    return;
	}
	
	MutablePair<PathChromosome, PathChromosome> offspring = cross(first, positions.left, second, positions.right);

	if (offspring == null) {
	    return;
	}
	StringBuffer log = new StringBuffer();
	if ((offspring.left.isLegal() || this.allowIllegalCrossovers)
		&& offspring.left.getFitnessValue()
		>= ((PathChromosome) (candidateChromosomes.get(candidateChromosomes.size() - 1))).getFitnessValue()) {
	    first.setIsSelectedForNextGeneration(true);
	    candidateChromosomes.add(0, first);
	    log.append("Crossover offspring legal").append(NEW_LINE);
	} else {
	    log.append("Crossover offspring not legal").append(NEW_LINE);
	    first.setIsSelectedForNextGeneration(false);
	}

	if ((offspring.right.isLegal() || this.allowIllegalCrossovers)
		&& offspring.right.getFitnessValue()
		>= ((PathChromosome) (candidateChromosomes.get(candidateChromosomes.size() - 2))).getFitnessValue()) {
	    second.setIsSelectedForNextGeneration(true);
	    candidateChromosomes.add(0, second);
	    log.append("Crossover offspring legal").append(NEW_LINE);
	} else {
	    log.append("Crossover offspring not legal").append(NEW_LINE);
	    second.setIsSelectedForNextGeneration(false);
	}

	printStatistics(log);
    }

    private MutablePair<PathChromosome, PathChromosome> cross(
	    PathChromosome male, int maleCrossIndex,
	    PathChromosome female, int femaleCrossIndex) {

	EdgeGene replaceInMale = male.getGene(maleCrossIndex);
	EdgeGene replaceInFemale = female.getGene(femaleCrossIndex);

	if (replaceInMale.equals(replaceInFemale)) {
	    return null;
	}

	StringBuffer log = new StringBuffer();
	log.append("==================== Crossing over ====================").append(NEW_LINE);
	log.append(male.toString()).append(NEW_LINE);
	log.append("==================== with ====================").append(NEW_LINE);
	log.append(female.toString()).append(NEW_LINE);

	male.setGene(maleCrossIndex, replaceInFemale);
	female.setGene(femaleCrossIndex, replaceInMale);

	log.append("==================== results ====================").append(NEW_LINE);
	log.append(male.toString()).append(NEW_LINE);
	log.append(female.toString()).append(NEW_LINE);

	printStatistics(log);

	return new MutablePair<PathChromosome, PathChromosome>(male, female);
    }

    private ImmutablePair<Integer, Integer> getCrossoverPositions(
	    RandomGenerator generator, PathChromosome male, PathChromosome female) {

	int replacePositionInMale;
	int replacePositionInFemale;

	switch (this.crossoverMode) {
	    case RANDOM: {
		replacePositionInMale = generator.nextInt(male.size() - 1);
		break;
	    }
	    case MINIMIZE_COST: {
		replacePositionInMale = male.getHighestCostGeneIndex();
		break;
	    }
	    default: {
		replacePositionInMale = generator.nextInt(male.size() - 1);
	    }
	}
	
	replacePositionInFemale = getFemaleReplacePosition(male, replacePositionInMale, female);

	if (replacePositionInFemale == -1) {
	    return null;
	}

	return new ImmutablePair<Integer, Integer>(replacePositionInMale, replacePositionInFemale);
    }

    private int getFemaleReplacePosition(
	    PathChromosome male, Integer replacePositionInMale,
	    PathChromosome female) {

	for (int i = 0; i < female.size(); i++) {
	    boolean sourceMatch = male.getGene(replacePositionInMale).getAllele().getSource().equals(female.getGene(i).getAllele().getSource());
	    boolean destinationMatch = male.getGene(replacePositionInMale).getAllele().getDestination().equals(female.getGene(i).getAllele().getDestination());

	    if (sourceMatch && destinationMatch) {
		return i;
	    }
	}

	return -1;
    }
}
