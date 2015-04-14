package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeFitnessComparator;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeOperationMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;

/**
 * <p>
 * Searches for cyclical paths in a chromosome, and removes them<p/>
 *
 * <p>
 * Mode of operation is as follows:<br/>
 * - Does a gene scan, checking node sources<br/>
 * - If a source is found a second time, then we have a cycle<br/>
 * - Remove the cycle</p>
 */
public class PathChromosomeCycleRemoveMutator extends PathChromosomeMutator {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomeCycleRemoveMutator(GeneticConfiguration configuration)
	    throws InvalidConfigurationException {

	super(configuration);
	this.mutationRatePercentage = DEFAULT_MUTATION_RATE;
	this.allowIllegalMutations = false;
	this.mutationMode = PathChromosomeOperationMode.RANDOM;
	this.printMutationStatistics = false;

	this.configuration = configuration;
    }

    public PathChromosomeCycleRemoveMutator(GeneticConfiguration configuration,
	    double desiredMutationRate) throws InvalidConfigurationException {

	this(configuration);
	this.mutationRatePercentage = desiredMutationRate;
    }
    //</editor-fold>

    @Override
    void doMutation(PathChromosome chromosome, List candidateChromosomes, RandomGenerator generator) throws InvalidConfigurationException {
	PathChromosomeMutationValidator breeder = new PathChromosomeMutationValidator(
		(PathChromosome) chromosome,
		this.mutationRatePercentage);

	if (!breeder.canMutate(false)) {
	    return;
	}

	StringBuffer log = new StringBuffer();
	/**
	 * Within chromosome, check for cycles. A cycle is detected when a
	 * source node is found downstream again as a destination node. Use
	 * LinkedHashMap because gene order is important
	 */
	PathChromosome toMutate = breeder.getToMutate();
	ArrayList<EdgeGene> cycle = toMutate.getCyclicGeneSequence();
	
	if (!cycle.isEmpty()) {
	    log.append("==================== [PathChromosomeCycleRemoveMutator] Mutating ====================").append(NEW_LINE);
	    log.append(toMutate.toString()).append(NEW_LINE);

	    for (EdgeGene gene : cycle) {
		toMutate.getGenesList().remove(gene);
	    }

	    log.append("==================== Results:  ====================").append(NEW_LINE);
	    log.append(toMutate.toString()).append(NEW_LINE);
	}
	
	printStatistics(log);
    }

    @Override
    int getMutationPosition(RandomGenerator generator, PathChromosome chromosome) {
	return 0;
    }
}
