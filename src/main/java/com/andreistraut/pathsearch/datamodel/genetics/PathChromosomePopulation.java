package com.andreistraut.pathsearch.datamodel.genetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jgap.Configuration;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;

public class PathChromosomePopulation extends Population {

    private GeneticConfiguration configuration;
    private ArrayList<PathChromosome> chromosomes;
    private EdgeGenePool genePool;
    private PathChromosome fittestChromosome;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public PathChromosomePopulation(Configuration configuration) throws InvalidConfigurationException {
	super(configuration);
	this.configuration = (GeneticConfiguration) configuration;
    }

    public PathChromosomePopulation(Configuration configuration,
	    IChromosome[] chromosomes) throws InvalidConfigurationException {

	this(configuration);
	if (chromosomes.length > 0) {
	    this.chromosomes = new ArrayList<PathChromosome>();

	    for (IChromosome chromosome : chromosomes) {
		this.chromosomes.add((PathChromosome) chromosome);
	    }
	}
    }

    public PathChromosomePopulation(Configuration configuration,
	    ArrayList<PathChromosome> chromosomes) throws InvalidConfigurationException {

	this(configuration);
	this.chromosomes = chromosomes;
    }

    public PathChromosomePopulation(Configuration configuration,
	    ArrayList<PathChromosome> chromosomes, EdgeGenePool genePool)
	    throws InvalidConfigurationException {

	this(configuration);
	this.chromosomes = chromosomes;
	this.genePool = genePool;
    }

    private PathChromosomePopulation(Configuration configuration, IChromosome chromosome) throws InvalidConfigurationException {
	this(configuration, new ArrayList<PathChromosome>());
	this.chromosomes.add((PathChromosome) chromosome);
    }

    private PathChromosomePopulation(Configuration configuration, int size) throws InvalidConfigurationException {
	super(configuration, size);
    }

    private PathChromosomePopulation() throws InvalidConfigurationException {
    }
    //</editor-fold>

    public EdgeGenePool getGenePool() {
	return genePool;
    }

    public void setGenePool(EdgeGenePool genePool) {
	this.genePool = genePool;
    }

    /**
     * Returns a PathChromosome at given index in the Population
     *
     * @param index The index of the PathChromosome to be returned
     * @return PathChromosome at given index in the Population
     */
    @Override
    public PathChromosome getChromosome(int index) {
	return this.chromosomes.get(index);
    }

    /**
     * @return A copy of the list of PathChromosomes in the Population
     */
    @Override
    public List<IChromosome> getChromosomes() {
	return new ArrayList<IChromosome>(this.chromosomes);
    }

    /**
     * Sets the given PathChromosome on the given index in the list of chromosomes
     *
     * @param index The index to set the PathChromosome in
     * @param chromosome The PathChromosome to be set in
     */
    @Override
    public void setChromosome(int index, IChromosome chromosome) {
	if (this.chromosomes == null) {
	    this.chromosomes = new ArrayList<PathChromosome>();
	}

	this.chromosomes.add(index, (PathChromosome) chromosome);
    }

    /**
     * Sets the given PathChromosomes as the list of chromosomes
     *
     * @param chromosomes The PathChromosomes to set
     */
    @Override
    public void setChromosomes(List chromosomes) {
	this.chromosomes = new ArrayList<PathChromosome>(chromosomes);
    }

    @Override
    public void addChromosome(IChromosome toAdd) {
	if (this.chromosomes == null) {
	    this.chromosomes = new ArrayList<PathChromosome>();
	}

	this.chromosomes.add((PathChromosome) toAdd);
    }

    /**
     * Adds all the PathChromosomes in the given Population
     *
     * @param population The Population to add
     */
    @Override
    public void addChromosomes(Population population) {
	if (population != null && population.getChromosomes().size() > 0) {
	    for (IChromosome chromosome : population.getChromosomes()) {
		this.chromosomes.add((PathChromosome) chromosome);
	    }
	}
    }

    /**
     * Adds all the PathChromosomes to the Population
     *
     * @param chromosomes The Chromosomes to add
     */
    public void addChromosomes(ArrayList<PathChromosome> chromosomes) {
	if (this.chromosomes == null) {
	    this.chromosomes = new ArrayList<PathChromosome>();
	}

	this.chromosomes.addAll(chromosomes);
    }

    /**
     * Determines the fittest PathChromosomes (the one with the highest fitness 
     * value) and memorizes it
     * 
     * @return The fittest PathChromosome of the population
     */
    @Override
    public PathChromosome determineFittestChromosome() {
	this.fittestChromosome = 
		this.determineFittestChromosome(0, this.chromosomes.size() - 1);
	
	return this.fittestChromosome;
    }

    /**
     * Determines the fittest PathChromosome (the one with the highest fitness 
     * value) within the given indices and memorizes it
     * 
     * @param startIndex Index to begin the evaluation with
     * @param endIndex Index to end the evaluation with
     * @return The fittest PathChromosome of the population within the given 
     * indices
     */
    @Override
    public PathChromosome determineFittestChromosome(int startIndex, int endIndex) {
	if(this.chromosomes != null && this.chromosomes.size() > startIndex 
		&& this.chromosomes.size() < endIndex) {
	    
	    PathChromosome fittest = this.chromosomes.get(startIndex);
	    for(int i = startIndex; i < endIndex; i++) {
		if(this.chromosomes.get(i).isFitterThan(fittest)) {
		    fittest = this.chromosomes.get(i);
		    this.fittestChromosome = fittest;
		}
	    }
	    
	    return fittest;
	}
	
	return null;
    }

    /**
     * Sorts the PathChromosome list and returns the fittest n PathChromosomes 
     * in the population.
     * 
     * @param numberOfChromosomes Number of top performer chromosomes to be returned
     * @return List of the fittest n Chromosomes of the population, or the 
     * fittest x Chromosomes with x = number of chromosomes in case n > x.
     */
    @Override
    public List determineFittestChromosomes(int numberOfChromosomes) {
	Collections.sort(this.chromosomes, new PathChromosomeFitnessEvaluator());
	
	return this.chromosomes.subList(0, numberOfChromosomes - 1);
    }

    @Override
    public int size() {
	return this.chromosomes.size();
    }
}
