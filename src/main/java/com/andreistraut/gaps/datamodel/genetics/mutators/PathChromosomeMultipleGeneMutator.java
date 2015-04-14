package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeOperationMode;
import com.andreistraut.gaps.datamodel.graph.Node;
import com.andreistraut.gaps.datamodel.translator.GraphGeneticsTranslator;
import java.util.ArrayList;
import java.util.List;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;

/**
 * <p>Mutates a section of genes in the chromosome, replacing it with a gene or
 * sequence of gene that have overall lower cost, increasing overall fitness</p>
 *
 * <p>Mode of operation is as follows:<br/>
 * - A random array of genes is chosen<br/>
 * - For the chosen array, a path search is performed, searching for a lower cost
 * alternative between the array's source and destination<br/>
 * - If a cheaper alternative is found (be it single gene or an array of genes),
 * the gene is replaced with the alternative</p>
 */
public class PathChromosomeMultipleGeneMutator extends PathChromosomeMutator {

    /**
     * Determines the number of genes (section size) that will be mutated. For
     * instance, if a value of 2 is set, every time this mutator is called, it
     * will try to mutate a section of 3 genes in sequence
     */
    private final int MUTATION_OFFSET = 3;
    private final boolean useRandomOffset = false;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomeMultipleGeneMutator(GeneticConfiguration configuration)
	    throws InvalidConfigurationException {

	super(configuration);
	this.mutationRatePercentage = DEFAULT_MUTATION_RATE;
	this.allowIllegalMutations = false;
	this.mutationMode = PathChromosomeOperationMode.RANDOM;
	this.printMutationStatistics = false;

	this.configuration = configuration;
    }

    public PathChromosomeMultipleGeneMutator(GeneticConfiguration configuration,
	    double desiredMutationRate) throws InvalidConfigurationException {

	this(configuration);
	this.mutationRatePercentage = desiredMutationRate;
    }
    //</editor-fold>

    @Override
    void doMutation(PathChromosome chromosome, List candidateChromosomes,
	    RandomGenerator generator) throws InvalidConfigurationException {

	/**
	 * Check mutation preconditions and exit early if not met
	 */
	PathChromosomeMutationValidator breeder = new PathChromosomeMutationValidator(
		(PathChromosome) chromosome,
		this.mutationRatePercentage);

	if (!breeder.canMutate()) {
	    return;
	}

	/**
	 * If mutation preconditions passed, get the chromosome and gene that
	 * will be mutated
	 */
	PathChromosome toMutate = breeder.getToMutate();
	int startPosition = this.getMutationPosition(generator, toMutate);
	int endPosition = this.getMutationEndPosition(generator, toMutate, startPosition);

	/**
	 * Get the start and end genes of the mutation section
	 */
	EdgeGene first = toMutate.getGene(startPosition);
	EdgeGene last = toMutate.getGene(endPosition);

	/**
	 * Get the path source and destination
	 */
	Node source = first.getAllele().getSource();
	Node destination = last.getAllele().getDestination();

	if (source.equals(destination)) {
	    return;
	}

	int originalCost = toMutate.getCost(startPosition, endPosition);

	StringBuffer log = new StringBuffer();
	log.append("==================== [PathChromosomeMultipleGeneMutator] Mutating ====================").append(NEW_LINE);
	log.append(toMutate.toString()).append(NEW_LINE);

	/**
	 * Get alternative section
	 */
	ArrayList<EdgeGene> alternative = GraphGeneticsTranslator.toEdgeGeneList(
		this.configuration.getGraph().getKShortestPaths(
			source,
			destination, 1).get(0), configuration);

	/**
	 * Calculate alternative section cost
	 */
	int alternativeCost = 0;
	for (EdgeGene gene : alternative) {
	    alternativeCost += gene.getAllele().getCost();
	}

	/**
	 * If alternative cost is better, mutate (replace the section)
	 */
	if (originalCost > alternativeCost && isAlternativeSectionValid(alternative)) {
	    // Remove original section
	    for (int i = startPosition; i <= endPosition; i++) {
		toMutate.getGenesList().remove(startPosition);
	    }

	    //Insert new section
	    toMutate.getGenesList().addAll(startPosition, alternative);

	    log.append("==================== results ====================").append(NEW_LINE);
	    log.append(toMutate.toString()).append(NEW_LINE);
	}
	printStatistics(log);
    }

    @Override
    int getMutationPosition(RandomGenerator generator, PathChromosome chromosome) {

	switch (this.mutationMode) {
	    case RANDOM: {
		return generator.nextInt(chromosome.size() - 1);
	    }
	    case MINIMIZE_COST: {
		return chromosome.getHighestCostGeneIndex();
	    }
	    default: {
		return generator.nextInt(chromosome.size() - 1);
	    }
	}
    }

    /**
     * Calculates the end position for a mutation section, such as the endpoint
     * of the mutation is not higher than the number of the chromosome's genes.
     * Ex: - Assume chromosomeSize = 6, startPosition = 4, mutationOffset = 2 -
     * If we try to mutate this as is, we will end up trying to mutate gene at
     * position 6 (which doesn't exist, index of last gene is 5) - In this case,
     * only the genes at indexes 4 and 5 will be mutated
     *
     * @param chromosome The chromosome to calculate end mutation position for
     * @param startPosition Start position of the mutation
     * @return End position of the mutation (may be equal to start position, if
     * start position is the last gene in the chromosome)
     */
    private int getMutationEndPosition(RandomGenerator generator, PathChromosome chromosome,
	    int startPosition) {

	int endPosition = (this.useRandomOffset) 
		? generator.nextInt(chromosome.size() - startPosition)
		: startPosition + this.MUTATION_OFFSET;

	if (endPosition >= chromosome.size() - 1) {
	    endPosition = (chromosome.size() - 1);
	}

	return endPosition;
    }

    private boolean isAlternativeSectionValid(ArrayList<EdgeGene> alternative) {
	return alternative != null && !alternative.isEmpty();
    }
}
