package com.andreistraut.gaps.datamodel;

import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.EdgeGeneConstraintChecker;
import com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeOnePointCrossover;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeOperationMode;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeFitness;
import com.andreistraut.gaps.datamodel.genetics.GenerationStatistic;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomeFitnessComparator;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomePopulation;
import com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeCycleRemoveMutator;
import com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeMultipleGeneMutator;
import com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeSingleGeneMutator;
import com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeTwoPointCrossover;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.graph.Node;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import com.andreistraut.gaps.datamodel.translator.GraphGeneticsTranslator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.event.EventManager;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.StockRandomGenerator;

/**
 * Test class to quickly run stuff without having to deploy and run web
 */
public class Main {

    private DirectedWeightedGraph dirGraph;
    private final int numberOfNodes = 200;
    private final int numberOfEdges = 2000;
    private final int numberOfPaths = 1000;
    private final int numberOfEvolutions = 100000;
    private final int stopCondition = numberOfEvolutions / 7;

    /**
     * @param args the command line arguments
     * @throws org.jgap.InvalidConfigurationException
     */
    public static void main(String[] args) throws InvalidConfigurationException {
	Main main = new Main();
	main.run();
    }

    public void run() throws InvalidConfigurationException {
	Date date = new Date(System.currentTimeMillis());
	DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
	System.out.println("========================= Execution started at " + formatter.format(date) + " =========================");

	//<editor-fold desc="Create and initialize graph">
	dirGraph = new DirectedWeightedGraph(numberOfNodes, numberOfEdges);
	ArrayList<Node> nodes = dirGraph.initNodes();
	ArrayList<DirectedWeightedEdge> edges = dirGraph.initEdges();

	Node source = dirGraph.getNodes().get(0);
	Node target = dirGraph.getNodes().get(dirGraph.getNodes().size() - 1);

	LinkedList<Node> visited = new LinkedList<Node>();
	visited.add(source);
	ArrayList<DirectedWeightedGraphPath> firstToLastPaths = dirGraph.getKPathsDepthFirst(source, target, numberOfPaths);
	/*System.out.println("========================= GRAPH PATHS: =========================");
	 for(DirectedWeightedGraphPath path : firstToLastPaths)  {
	 System.out.println("========================= PATH: =========================");
	 System.out.println(path);
	 for(DirectedWeightedEdge edge : path.getEdgeList()) {
	 System.out.print("[" + edge.getSource().getId() + " - " + edge.getDestination().getId() + "] ");		
	 }
	 }
	 System.out.println();*/
	//</editor-fold>

	//<editor-fold desc="Create and initialize genetic configuration">
	GeneticConfiguration configuration = new GeneticConfiguration("GraphConfig");
	configuration.setAlwaysCaculateFitness(true);
	configuration.setFitnessFunction(new PathChromosomeFitness());
	configuration.setFitnessEvaluator(new PathChromosomeFitnessComparator());
	configuration.setKeepPopulationSizeConstant(false);
	configuration.setPopulationSize(numberOfPaths);
	configuration.setPreservFittestIndividual(true);
	configuration.setRandomGenerator(new StockRandomGenerator());
	configuration.setEventManager(new EventManager());
	configuration.setGraph(dirGraph);
	//</editor-fold>

	//<editor-fold desc="Create and initialize Chromosomes, GenePool and Population">
	EdgeGeneConstraintChecker checker = new EdgeGeneConstraintChecker();

	ArrayList<PathChromosome> chromosomes = new ArrayList<PathChromosome>();
	for (GraphPath path : firstToLastPaths) {
	    PathChromosome chromosome = GraphGeneticsTranslator.toPathChromosome(path, configuration);
	    chromosome.setConstraintChecker(checker);

	    chromosomes.add(chromosome);
	}

	PathChromosomePopulation population = new PathChromosomePopulation(configuration);
	population.setChromosomes(chromosomes);
	//</editor-fold>

	//<editor-fold desc="Update Configuration">
	configuration.setSampleChromosome(population.getChromosome(0));
	configuration.setPopulationSize(population.size());
	//</editor-fold>

	//<editor-fold desc="Create and initialize genetic operators">
	PathChromosomeSingleGeneMutator singleMutatorOperator = new PathChromosomeSingleGeneMutator(configuration, 0.01);
	singleMutatorOperator.setAllowIllegalMutations(false);
	singleMutatorOperator.setMutationMode(PathChromosomeOperationMode.RANDOM);
	singleMutatorOperator.setPrintMutationStatistics(false);

	PathChromosomeMultipleGeneMutator multipleMutatorOperator = new PathChromosomeMultipleGeneMutator(configuration, 0.01);
	multipleMutatorOperator.setAllowIllegalMutations(false);
	multipleMutatorOperator.setMutationMode(PathChromosomeOperationMode.RANDOM);
	multipleMutatorOperator.setPrintMutationStatistics(false);

	PathChromosomeCycleRemoveMutator cycleOperator = new PathChromosomeCycleRemoveMutator(configuration, 0.01);
	cycleOperator.setAllowIllegalMutations(false);
	cycleOperator.setMutationMode(PathChromosomeOperationMode.RANDOM);
	cycleOperator.setPrintMutationStatistics(false);

	PathChromosomeOnePointCrossover crossoverOperator = new PathChromosomeOnePointCrossover(configuration, 0.01);
	crossoverOperator.setAllowIllegalCrossovers(false);
	crossoverOperator.setAllowFullCrossOver(false);
	crossoverOperator.setXoverNewAge(true);
	crossoverOperator.setCrossoverMode(PathChromosomeOperationMode.RANDOM);
	crossoverOperator.setPrintCrossoverStatistics(false);

	PathChromosomeTwoPointCrossover crossoverOperator2P = new PathChromosomeTwoPointCrossover(configuration, 0.01);
	crossoverOperator2P.setAllowIllegalCrossovers(false);
	crossoverOperator2P.setAllowFullCrossOver(false);
	crossoverOperator2P.setXoverNewAge(true);
	crossoverOperator2P.setCrossoverMode(PathChromosomeOperationMode.RANDOM);
	crossoverOperator2P.setPrintCrossoverStatistics(false);

	BestChromosomesSelector selector = new BestChromosomesSelector(configuration);
	selector.setDoubletteChromosomesAllowed(false);
	selector.setOriginalRate(0.1);
	//</editor-fold>

	//<editor-fold desc="Add genetic operators to configuration">
	//configuration.addGeneticOperator(singleMutatorOperator);
	configuration.addNaturalSelector(selector, true);
	configuration.addGeneticOperator(multipleMutatorOperator);
	configuration.addGeneticOperator(cycleOperator);
	configuration.addGeneticOperator(crossoverOperator2P);
	//configuration.addGeneticOperator(crossoverOperator);
	//</editor-fold>

	//<editor-fold desc="Create, initialize and evolve genotype">
	date = new Date(System.currentTimeMillis());
	System.out.println("========================= GENETIC EVOLUTION START: " + formatter.format(date) + " =========================");
	Genotype genotype = new Genotype(configuration, population);

	GenerationStatistic startStatistic = new GenerationStatistic(-1, date);

	genotype.evolve();

	System.out.println("========================= GENERATION " + -1 + ": =========================");

	double endAverageFitness = genotype.getPopulation().size() / genotype.getFittestChromosome().getFitnessValue();
	double endAverageCost = genotype.getPopulation().size() / ((PathChromosome) genotype.getFittestChromosome()).getCost();
	startStatistic
		.setEndPopulationSize(genotype.getPopulation().size())
		.setEndBestFitness((int) ((PathChromosome) genotype.getFittestChromosome()).getFitnessValue())
		.setEndAverageFitness(endAverageFitness)
		.setEndBestCost(((PathChromosome) genotype.getFittestChromosome()).getCost())
		.setEndAverageCost(endAverageCost);

	startStatistic
		.setEndBestChromosome(((PathChromosome) genotype.getFittestChromosome()).toJson());
	System.out.println(startStatistic.toJson());

	int evolved = 0;
	int previousBestFitness = 0;
	boolean stopConditionUsed = false;
	for (int i = 0; i < this.numberOfEvolutions; i++) {
	    date = new Date(System.currentTimeMillis());
	    GenerationStatistic statistic = new GenerationStatistic(i, date);

	    if (i % (numberOfEvolutions / 100) == 0) {
		System.out.println("========================= GENERATION " + i + ": =========================");

		double startAverageFitness = genotype.getPopulation().size() / genotype.getFittestChromosome().getFitnessValue();
		double startAverageCost = genotype.getPopulation().size() / ((PathChromosome) genotype.getFittestChromosome()).getCost();
		statistic
			.setStartPopulationSize(genotype.getPopulation().size())
			.setStartBestFitness((int) ((PathChromosome) genotype.getFittestChromosome()).getFitnessValue())
			.setStartAverageFitness(startAverageFitness)
			.setStartBestCost(((PathChromosome) genotype.getFittestChromosome()).getCost())
			.setStartAverageCost(startAverageCost);
		statistic.setEndBestChromosome(((PathChromosome) genotype.getFittestChromosome()).toJson());
	    }

	    genotype.evolve();
	    evolved++;

	    if (i % (numberOfEvolutions / 100) == 0) {
		
		endAverageFitness = genotype.getPopulation().size() / genotype.getFittestChromosome().getFitnessValue();
		endAverageCost = genotype.getPopulation().size() / ((PathChromosome) genotype.getFittestChromosome()).getCost();
		statistic
			.setEndPopulationSize(genotype.getPopulation().size())
			.setEndBestFitness((int) ((PathChromosome) genotype.getFittestChromosome()).getFitnessValue())
			.setEndAverageFitness(endAverageFitness)
			.setEndBestCost(((PathChromosome) genotype.getFittestChromosome()).getCost())
			.setEndAverageCost(endAverageCost);
		statistic.setEndBestChromosome(((PathChromosome) genotype.getFittestChromosome()).toJson());

		System.out.println(statistic.toJson());
	    }

	    /**
	     * Put a stop condition. If no evolution takes place for a certain
	     * number of generations, stop the evolution
	     */
	    if (evolved == this.stopCondition) {
		System.out.println("========================= GENERATION " + i + ": =========================");
		
		endAverageFitness = genotype.getPopulation().size() / genotype.getFittestChromosome().getFitnessValue();
		endAverageCost = genotype.getPopulation().size() / ((PathChromosome) genotype.getFittestChromosome()).getCost();
		statistic
			.setEndPopulationSize(genotype.getPopulation().size())
			.setEndBestFitness((int) ((PathChromosome) genotype.getFittestChromosome()).getFitnessValue())
			.setEndAverageFitness(endAverageFitness)
			.setEndBestCost(((PathChromosome) genotype.getFittestChromosome()).getCost())
			.setEndAverageCost(endAverageCost);
		statistic.setEndBestChromosome(((PathChromosome) genotype.getFittestChromosome()).toJson());

		System.out.println(statistic.toJson());
		stopConditionUsed = true;
		break;
	    }

	    /**
	     * If a change in best fitness value took place, reset the stop
	     * counter
	     */
	    if (((PathChromosome) genotype.getFittestChromosome()).getFitnessValue() > previousBestFitness) {
		previousBestFitness = (int) ((PathChromosome) genotype.getFittestChromosome()).getFitnessValue();
		evolved = 0;
	    }
	}
	if (!stopConditionUsed) {
	    System.out.println("Fittest Chromosome:");
	    System.out.println((PathChromosome) genotype.getFittestChromosome());
	}

	date = new Date(System.currentTimeMillis());
	System.out.println("========================= GENETIC EVOLUTION END: " + formatter.format(date) + " =========================");
	System.out.println();
	//</editor-fold>

	//<editor-fold desc="Compare results with kshortest">
	date = new Date(System.currentTimeMillis());
	System.out.println("========================= K SHORTEST START: " + formatter.format(date) + " =========================");
	List<GraphPath<Node, DirectedWeightedEdge>> kshortest = dirGraph.getKShortestPaths(source, target, 5);
	int i = 0;
	for (GraphPath<Node, DirectedWeightedEdge> path : kshortest) {
	    System.out.println("========================= Path: " + i + " =========================");

	    int cost = 0;
	    for (DirectedWeightedEdge edge : path.getEdgeList()) {
		System.out.println(edge.toString());
		cost += edge.getCost();
	    }
	    System.out.println("Cost: " + cost);
	    i++;
	}
	date = new Date(System.currentTimeMillis());
	System.out.println("========================= K SHORTEST END: " + formatter.format(date) + " =========================");
	System.out.println();
	//</editor-fold>

	date = new Date(System.currentTimeMillis());
	System.out.println("========================= Execution ended at " + formatter.format(date) + " =========================");
    }

    private void printGraph(ArrayList<Node> nodes,
	    ArrayList<EdgeGene> edges) {

	Iterator<Node> nodesIterator = nodes.iterator();
	System.out.println("NODES:");
	while (nodesIterator.hasNext()) {
	    System.out.print(nodesIterator.next().getId() + ", ");
	}
	System.out.println();
	System.out.println("=========================");
	System.out.println();

	Iterator<EdgeGene> edgesIterator = edges.iterator();
	System.out.println("EDGES:");
	while (edgesIterator.hasNext()) {
	    EdgeGene edge = edgesIterator.next();
	    System.out.print(
		    edge.getAllele().getSource().getId() + "-"
		    + edge.getAllele().getDestination().getId() + "("
		    + edge.getAllele().getCost() + "), ");
	}
	System.out.println();
	System.out.println("=========================");
	System.out.println();
    }

    private void printPaths(GeneticConfiguration conf, List<GraphPath<Node, EdgeGene>> paths) throws InvalidConfigurationException {
	System.out.println("PATHS:");
	for (GraphPath<Node, EdgeGene> path : paths) {
	    PathChromosome chromosome = new PathChromosome(
		    conf,
		    ((ArrayList) path.getEdgeList()),
		    path.getStartVertex(),
		    path.getEndVertex());

	    System.out.println(chromosome.toString());
	    System.out.println("+++++++++++++++++++++++++");
	}
	System.out.println("=========================");
	System.out.println();
    }

    private void printPath(LinkedList<Node> visited) {
	for (int i = 0; i < visited.size() - 1; i++) {
	    Node current = visited.get(i);
	    Node next = visited.get(i + 1);

	    System.out.println(dirGraph.getEdge(current, next));
	}
	System.out.println();
    }
}
