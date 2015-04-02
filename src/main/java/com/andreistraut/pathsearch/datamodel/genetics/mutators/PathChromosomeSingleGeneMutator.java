package com.andreistraut.pathsearch.datamodel.genetics.mutators;

import com.andreistraut.pathsearch.datamodel.genetics.EdgeGene;
import com.andreistraut.pathsearch.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.pathsearch.datamodel.genetics.PathChromosome;
import com.andreistraut.pathsearch.datamodel.genetics.PathChromosomeOperationMode;
import com.andreistraut.pathsearch.datamodel.translator.GraphGeneticsTranslator;
import java.util.ArrayList;
import java.util.List;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;

/**
 * <p>
 * Mutates a single gene in the chromosome, replacing it with a gene or sequence
 * of gene that have overall lower cost, increasing overall fitness<p/>
 * 
 * <p>Note that this mutator will never reduce chromosome (path) size, only 
 * increase it in terms of edge number, but will always decrease overall cost
 * (improving fitness)
 * </p>
 * 
 * <p>Works best for spans that have a lot of alternative paths (for instance,
 * if paths are mostly linear, for instance a ring graph, this will mutator
 * will have no effect)
 *
 * <p>
 * Mode of operation is as follows:<br/>
 * - Depending on strategy, a single gene is chosen (randomly, or gene with the
 * highest cost)<br/>
 * - For the chosen gene, a path search is performed, searching for a lower cost
 * alternative between the gene's source and destination<br/>
 * - If a cheaper alternative is found (be it single gene or an array of genes),
 * the gene is replaced with the alternative</p>
 */
public class PathChromosomeSingleGeneMutator extends PathChromosomeMutator {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomeSingleGeneMutator(GeneticConfiguration configuration)
	    throws InvalidConfigurationException {

	super(configuration);
	this.mutationRatePercentage = DEFAULT_MUTATION_RATE;
	this.allowIllegalMutations = false;
	this.mutationMode = PathChromosomeOperationMode.RANDOM;
	this.printMutationStatistics = false;

	this.configuration = configuration;
    }

    public PathChromosomeSingleGeneMutator(GeneticConfiguration configuration,
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
	PathChromosome toMutate = breeder.getToMutate().clone();
	int position = this.getMutationPosition(generator, toMutate);
	EdgeGene toReplace = toMutate.getGene(position);

	StringBuffer log = new StringBuffer();
	log.append("==================== [PathChromosomeSingleGeneMutator] Mutating ====================").append(NEW_LINE);
	log.append(toMutate.toString()).append(NEW_LINE);
	log.append("==================== gene ====================").append(NEW_LINE);
	log.append(toReplace.toString()).append(NEW_LINE);

	/**
	 * Get alternative section
	 */
	ArrayList<EdgeGene> alternative = GraphGeneticsTranslator.toEdgeGeneList(
		this.configuration.getGraph().getKShortestPaths(
			toReplace.getAllele().getSource(),
			toReplace.getAllele().getDestination(), 1).get(0), configuration);

	/**
	 * Check if found section is valid, i.e. has lower cost than the
	 * original gene to replace. If so, mutate the chromosome
	 */
	if (isAlternativeSectionValid(alternative, toReplace)) {
	    log.append("==================== replacing with ====================").append(NEW_LINE);

	    for (int i = 0; i < alternative.size(); i++) {
		toMutate.setGene(position + i, alternative.get(i));

		log.append("[")
			.append(alternative.get(i).getAllele().getSource().getId())
			.append(" - ")
			.append(alternative.get(i).getAllele().getDestination().getId())
			.append(", Cost: ")
			.append(alternative.get(i).getAllele().getCost())
			.append("]");
	    }
	    log.append(NEW_LINE);
	}

	log.append("==================== results ====================").append(NEW_LINE);
	log.append(toMutate.toString()).append(NEW_LINE);

	/**
	 * Check mutation results, and if valid, add the mutated chromosome to
	 * population
	 */
	if ((toMutate.isLegal() || this.allowIllegalMutations)
		&& toMutate.getFitnessValue()
		>= ((PathChromosome) (candidateChromosomes.get(candidateChromosomes.size() - 1))).getFitnessValue()) {
	    toMutate.setIsSelectedForNextGeneration(true);
	    candidateChromosomes.set(candidateChromosomes.size() - 1, toMutate);
	    log.append("Mutation offspring legal").append(NEW_LINE);
	} else {
	    log.append("Mutation offspring not legal. Fitness value check: ")
		    .append(toMutate.getFitnessValue() <= ((PathChromosome) (candidateChromosomes.get(candidateChromosomes.size() - 1))).getFitnessValue())
		    .append(", legality check: ").append(toMutate.isLegal())
		    .append(" allow illegals: ").append(this.allowIllegalMutations)
		    .append(NEW_LINE);
	    toMutate.setIsSelectedForNextGeneration(false);
	}

	printStatistics(log);
    }

    @Override
    int getMutationPosition(
	    RandomGenerator generator,
	    PathChromosome chromosome) {

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

    private boolean isAlternativeSectionValid(ArrayList<EdgeGene> alternative,
	    EdgeGene toReplace) {
	/**
	 * It makes no sense replace a gene with itself, if found path only has
	 * a single edge
	 */
	if (alternative != null && !alternative.isEmpty()) {
	    if ((alternative.size() == 1 && !alternative.get(0).equals(toReplace))
		    || alternative.size() > 1) {
		return true;
	    }
	}

	return false;
    }
}
