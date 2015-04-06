package com.andreistraut.pathsearch.datamodel.genetics;

import com.andreistraut.pathsearch.datamodel.genetics.mutators.PathChromosomeCycleRemoveMutator;
import com.andreistraut.pathsearch.datamodel.genetics.mutators.PathChromosomeMultipleGeneMutator;
import com.andreistraut.pathsearch.datamodel.genetics.mutators.PathChromosomeTwoPointCrossover;
import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.pathsearch.datamodel.translator.GraphGeneticsTranslator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jgap.Configuration;
import org.jgap.GeneticOperator;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.event.EventManager;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.StockRandomGenerator;
import org.jgrapht.GraphPath;

public class GeneticEvolver {

    private GeneticConfiguration configuration;
    private final DirectedWeightedGraph graph;
    private final List<DirectedWeightedGraphPath> paths;
    private boolean hasInited;
    private Genotype genotype;
    private PathChromosomePopulation population;
    private GenerationStatistic lastStatistic;

    private final int numberOfEvolutions;
    /**
     * Use this to stop when no improvement is made during X generations, where
     * X is a percent of the number of evolutions. i.e. for 1000 evolutions and
     * 27 stopCondition, if no improvement is made after 270 generations, stop
     * the algorithm
     */
    private final int stopConditionPercent;
    private int numberOfEvolutionsWithoutChange;
    /**
     * If stop condition has been reached
     */
    private boolean hasFinished;
    /**
     * If a change in top fitness took place in the last generation
     */
    private boolean hasEvolved = false;
    /**
     * How many steps have run
     */
    private int evolutions = 0;
    private int lastGenerationBestFitness = 0;

    public GeneticEvolver(int numberOfPaths, int numberOfEvolutions,
            int stopConditionPercent,
            DirectedWeightedGraph graph,
            List<DirectedWeightedGraphPath> paths) {

        this.numberOfEvolutions = numberOfEvolutions;
        this.stopConditionPercent = stopConditionPercent;
        this.graph = graph;
        this.paths = paths;
    }

    public boolean hasFinished() {
        return hasFinished;
    }

    public boolean hasEvolved() {
	return hasEvolved;
    }

    public int getCompletedSteps() {
        return this.evolutions;
    }

    public GenerationStatistic getLastStatistic() {
        return this.lastStatistic;
    }

    public GenerationStatistic evolveAndGetStatistics() throws InvalidConfigurationException {
        if (!hasInited) {
            throw new InvalidConfigurationException("Please init genetic evolver first");
        }

        if (this.hasFinished) {
            return null;
        }

        Date date = new Date(System.currentTimeMillis());
        GenerationStatistic statistic = new GenerationStatistic(this.evolutions, date);

        double startAverageFitness = 0;
        double startAverageCost = 0;
        double startBestFitness = 0;
        double startBestCost = 0;
        int populationSize = genotype.getPopulation().size();

        if (genotype.getFittestChromosome() != null) {
            PathChromosome fittest = (PathChromosome) genotype.getFittestChromosome();
            startAverageFitness = populationSize / fittest.getFitnessValue();
            startAverageCost = populationSize / fittest.getCost();
            startBestCost = fittest.getCost();
            startBestFitness = fittest.getFitnessValue();
        }

        statistic
                .setStartPopulationSize(genotype.getPopulation().size())
                .setStartBestFitness((int) startBestFitness)
                .setStartAverageFitness(startAverageFitness)
                .setStartBestCost((int) startBestCost)
                .setStartAverageCost(startAverageCost);

        genotype.evolve();
        evolutions++;

        double endAverageFitness = genotype.getFittestChromosome().getFitnessValue() / genotype.getPopulation().size();
        double endAverageCost = ((PathChromosome) genotype.getFittestChromosome()).getCost() / genotype.getPopulation().size();
        int endBestFitness = (int) ((PathChromosome) genotype.getFittestChromosome()).getFitnessValue();
        int endBestCost = ((PathChromosome) genotype.getFittestChromosome()).getCost();

        statistic
                .setEndPopulationSize(genotype.getPopulation().size())
                .setEndBestFitness(endBestFitness)
                .setEndAverageFitness(endAverageFitness)
                .setEndBestCost(endBestCost)
                .setEndAverageCost(endAverageCost);
        statistic.setEndBestChromosome(((PathChromosome) genotype.getFittestChromosome()).toJson());

        if (endBestFitness == this.lastGenerationBestFitness) {
            numberOfEvolutionsWithoutChange++;
	    this.hasEvolved = false;
        } else {
            numberOfEvolutionsWithoutChange = 0;
	    this.hasEvolved = true;
            this.lastGenerationBestFitness = endBestFitness;
        }

        if (this.evolutions == this.numberOfEvolutions
                || this.numberOfEvolutionsWithoutChange >= ((this.numberOfEvolutions * this.stopConditionPercent) / 100)) {
            this.hasFinished = true;
        }

	this.lastStatistic = statistic;
        return statistic;
    }

    public PathChromosomePopulation init() throws InvalidConfigurationException {

        this.configuration = this.initConfiguration(this.paths.size(), this.graph);
        ArrayList<PathChromosome> chromosomes = this.initChromosomes(this.configuration);
        EdgeGenePool genePool = this.initGenePool(this.configuration);
        this.population = this.initPopulation(this.configuration, chromosomes, genePool);

        this.configuration.setSampleChromosome(this.population.getChromosome(0));
        this.configuration.setPopulationSize(this.population.size());
        this.configuration.setGenePool(genePool);

        ArrayList<GeneticOperator> operators = this.initMutators(this.configuration);
        BestChromosomesSelector selector = new BestChromosomesSelector(this.configuration);
        selector.setDoubletteChromosomesAllowed(false);
        selector.setOriginalRate(0.1);

        this.configuration.addNaturalSelector(selector, true);
        for (GeneticOperator operator : operators) {
            this.configuration.addGeneticOperator(operator);
        }

        this.genotype = new Genotype(this.configuration, this.population);
        this.hasInited = true;
        return population;
    }

    private GeneticConfiguration initConfiguration(
            int populationSize, DirectedWeightedGraph graph)
            throws InvalidConfigurationException {

        GeneticConfiguration config = new GeneticConfiguration(
                new Date(System.currentTimeMillis()).toString());

        Configuration.reset();
        config.setAlwaysCaculateFitness(true);
        config.setFitnessFunction(new PathChromosomeFitness());
        config.setFitnessEvaluator(new PathChromosomeFitnessEvaluator());
        config.setKeepPopulationSizeConstant(false);
        config.setPopulationSize(populationSize);
        config.setPreservFittestIndividual(true);
        config.setRandomGenerator(new StockRandomGenerator());
        config.setEventManager(new EventManager());
        config.setGraph(graph);

        return config;
    }

    private ArrayList<PathChromosome> initChromosomes(
            GeneticConfiguration config)
            throws InvalidConfigurationException {

        ArrayList<PathChromosome> chromosomes = new ArrayList<PathChromosome>();
        for (GraphPath path : this.paths) {
            PathChromosome chromosome = GraphGeneticsTranslator.toPathChromosome(path, config);
            chromosome.setConstraintChecker(new EdgeGeneConstraintChecker());

            chromosomes.add(chromosome);
        }

        return chromosomes;
    }

    private EdgeGenePool initGenePool(GeneticConfiguration config)
            throws InvalidConfigurationException {
        EdgeGenePool genePool = new EdgeGenePool();
        for (DirectedWeightedEdge edge : this.graph.edgeSet()) {
            EdgeGene gene = new EdgeGene(edge, config);
            genePool.addGeneToPool(gene);
        }

        return genePool;
    }

    private PathChromosomePopulation initPopulation(
            GeneticConfiguration config,
            ArrayList<PathChromosome> chromosomes, EdgeGenePool genePool)
            throws InvalidConfigurationException {

        PathChromosomePopulation pop = new PathChromosomePopulation(config);
        pop.setChromosomes(chromosomes);
        pop.setGenePool(genePool);

        return pop;
    }

    private ArrayList<GeneticOperator> initMutators(GeneticConfiguration configuration) throws InvalidConfigurationException {
        ArrayList<GeneticOperator> mutators = new ArrayList<GeneticOperator>();

        PathChromosomeMultipleGeneMutator multipleMutatorOperator = new PathChromosomeMultipleGeneMutator(configuration, 0.01);
        multipleMutatorOperator.setAllowIllegalMutations(false);
        multipleMutatorOperator.setMutationMode(PathChromosomeOperationMode.RANDOM);
        multipleMutatorOperator.setPrintMutationStatistics(false);

        PathChromosomeCycleRemoveMutator cycleOperator = new PathChromosomeCycleRemoveMutator(configuration, 0.01);
        cycleOperator.setAllowIllegalMutations(false);
        cycleOperator.setMutationMode(PathChromosomeOperationMode.RANDOM);
        cycleOperator.setPrintMutationStatistics(false);

        PathChromosomeTwoPointCrossover crossoverOperator2P = new PathChromosomeTwoPointCrossover(configuration, 0.01);
        crossoverOperator2P.setAllowIllegalCrossovers(false);
        crossoverOperator2P.setAllowFullCrossOver(false);
        crossoverOperator2P.setXoverNewAge(true);
        crossoverOperator2P.setCrossoverMode(PathChromosomeOperationMode.RANDOM);
        crossoverOperator2P.setPrintCrossoverStatistics(false);

        mutators.add(multipleMutatorOperator);
        mutators.add(cycleOperator);
        mutators.add(crossoverOperator2P);

        return mutators;
    }
}
