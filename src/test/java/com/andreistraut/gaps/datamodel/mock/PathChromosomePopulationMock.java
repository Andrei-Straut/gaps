package com.andreistraut.gaps.datamodel.mock;

import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.genetics.PathChromosomePopulation;
import java.util.ArrayList;
import org.jgap.InvalidConfigurationException;

/**
 * Wrapper nock for a Population with two PathChromosomes. Contains a
 * PathChromosomeMockOneGene, and a PathChromosomeMockTwoGene
 */
public class PathChromosomePopulationMock {

    private PathChromosome oneGene;
    private PathChromosome twoGene;
    private ArrayList<PathChromosome> chromosomes;
    private PathChromosomePopulation population;
    private final GeneticConfiguration conf;

    public PathChromosomePopulationMock(GeneticConfiguration conf) throws InvalidConfigurationException {
	this.conf = conf;
	this.reset();
    }

    public final void reset() throws InvalidConfigurationException {
	this.oneGene = new PathChromosomeMockOneGene(conf).getChromosome();
	this.twoGene = new PathChromosomeMockTwoGene(conf).getChromosome();
	
	this.chromosomes = new ArrayList<PathChromosome>();
	this.chromosomes.add(oneGene);
	this.chromosomes.add(twoGene);
	
	this.population = new PathChromosomePopulation(conf, this.chromosomes);
    }

    public PathChromosome getOneGene() {
	return oneGene;
    }

    public PathChromosome getTwoGene() {
	return twoGene;
    }

    public ArrayList<PathChromosome> getChromosomes() {
	return chromosomes;
    }

    public PathChromosomePopulation getPopulation() {
	return population;
    }
}
