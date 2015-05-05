package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeOperationMode;
import com.andreistraut.gaps.datamodel.graph.Node;
import com.andreistraut.gaps.datamodel.translator.GraphGeneticsTranslator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;

public class PathChromosomeTwoPointCrossover extends PathChromosomeCrossover {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomeTwoPointCrossover(GeneticConfiguration configuration, double desiredCrossoverRate)
	    throws InvalidConfigurationException {

	super(configuration, desiredCrossoverRate);
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
    protected void doCrossover(IChromosome firstMate, IChromosome secondMate,
	    List candidateChromosomes, RandomGenerator generator) {

	PathChromosomeCrossoverValidator breeder = new PathChromosomeCrossoverValidator(
		(PathChromosome) firstMate,
		(PathChromosome) secondMate,
		this.crossoverRatePercent);

	if (!breeder.canCrossover()) {
	    return;
	}

	PathChromosome first = breeder.getMale();
	PathChromosome second = breeder.getFemale();

	ImmutablePair<ImmutablePair<Integer, Integer>, ImmutablePair<Integer, Integer>> crossoverPositions =
		this.getCrossoverPositions(generator, first, second);

	if (crossoverPositions == null) {
	    return;
	}

	MutablePair<PathChromosome, PathChromosome> offspring;
	try {
	    offspring = cross(first, second, crossoverPositions);
	} catch (InvalidConfigurationException ex) {
	    Logger.getLogger(PathChromosomeTwoPointCrossover.class.getName()).log(Level.SEVERE, null, ex);
	    return;
	}

	if (offspring == null) {
	    return;
	}
	
	firstMate = offspring.getLeft();
	secondMate = offspring.getRight();
    }

    private MutablePair<PathChromosome, PathChromosome> cross(PathChromosome male, PathChromosome female,
	    ImmutablePair<ImmutablePair<Integer, Integer>, ImmutablePair<Integer, Integer>> crossoverPositions) throws InvalidConfigurationException {

	StringBuffer log = new StringBuffer();
	log.append("==================== Crossing over ====================").append(NEW_LINE);
	log.append(male.toString()).append(NEW_LINE);
	log.append("==================== with ====================").append(NEW_LINE);
	log.append(female.toString()).append(NEW_LINE);

	ArrayList<EdgeGene> replaceInMale = new ArrayList(male.getGenesList().subList(crossoverPositions.left.left, crossoverPositions.right.left));
	ArrayList<EdgeGene> replaceInFemale = new ArrayList(female.getGenesList().subList(crossoverPositions.left.right, crossoverPositions.right.right));
	
	male.getGenesList().removeAll(replaceInMale);
	female.getGenesList().removeAll(replaceInFemale);	
		
	if(replaceInMale.equals(replaceInFemale)) {
	    Node source = replaceInMale.get(0).getAllele().getSource();
	    Node destination = replaceInMale.get(replaceInMale.size() - 1).getAllele().getDestination();
	    replaceInMale = GraphGeneticsTranslator.toEdgeGeneList(
		this.configuration.getGraph().getKShortestPaths(
			source,
			destination, 1).get(0), configuration);
	}
	
	log.append("==================== Crossing sections: ====================").append(NEW_LINE);
	log.append(replaceInMale.toString()).append(NEW_LINE);
	log.append("==================== And: ====================").append(NEW_LINE);
	log.append(replaceInFemale.toString()).append(NEW_LINE);

	male.getGenesList().addAll(crossoverPositions.left.left, replaceInFemale);
	female.getGenesList().addAll(crossoverPositions.left.right, replaceInMale);

	log.append("==================== results ====================").append(NEW_LINE);
	log.append(male.toString()).append(NEW_LINE);
	log.append(female.toString()).append(NEW_LINE);

	printStatistics(log);
	return new MutablePair<PathChromosome, PathChromosome>(male, female);
    }

    private ImmutablePair getCrossoverPositions(RandomGenerator generator, PathChromosome male, PathChromosome female) {

	int startPositionInMale;
	int startPositionInFemale;

	switch (this.crossoverMode) {
	    case RANDOM: {
		startPositionInMale = (male.size() > 1) ? generator.nextInt(male.size()) : 0;
		break;
	    }
	    case MINIMIZE_COST: {
		startPositionInMale = male.getHighestCostGeneIndex();
		break;
	    }
	    default: {
		startPositionInMale = (male.size() > 1) ? generator.nextInt(male.size()) : 0;
	    }
	}

	startPositionInFemale = getFemaleReplaceStartPosition(male, startPositionInMale, female);
	ImmutablePair<Integer, Integer> startPositions =
		new ImmutablePair<Integer, Integer>(startPositionInMale, startPositionInFemale);
	ImmutablePair<Integer, Integer> endPositions =
		this.getReplaceEndPositions(male, startPositionInMale, female, startPositionInFemale);

	if (startPositionInFemale == -1 || endPositions == null) {
	    return null;
	}

	return new ImmutablePair(startPositions, endPositions);
    }

    private int getFemaleReplaceStartPosition(
	    PathChromosome male, Integer maleStartPosition,
	    PathChromosome female) {

	for (int i = 0; i < female.size(); i++) {
	    boolean sourceMatch = male.getGene(maleStartPosition).getAllele().getSource().equals(female.getGene(i).getAllele().getSource());

	    if (sourceMatch) {
		return i;
	    }
	}

	return -1;
    }

    private ImmutablePair<Integer, Integer> getReplaceEndPositions(PathChromosome male,
	    Integer startPositionInMale, PathChromosome female, Integer startPositionInFemale) {
	
	for (int i = startPositionInMale + 2; i < male.size(); i++) {
	    for (int j = startPositionInFemale + 2; j < female.size(); j++) {
		boolean destinationMatch = male.getGene(i).getAllele().getDestination().equals(female.getGene(j).getAllele().getDestination());
		if (destinationMatch) {
		    return new ImmutablePair<Integer, Integer>(i, j);
		}
	    }
	}

	return null;
    }
}
