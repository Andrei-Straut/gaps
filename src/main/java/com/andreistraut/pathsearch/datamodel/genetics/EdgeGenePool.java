package com.andreistraut.pathsearch.datamodel.genetics;

import java.util.ArrayList;
import java.util.Random;

public class EdgeGenePool {

    private ArrayList<EdgeGene> genePool;

    public EdgeGenePool() {
	this.genePool = new ArrayList<EdgeGene>();
    }

    public EdgeGenePool(ArrayList<EdgeGene> genePool) {
	this.genePool = genePool;
    }

    public ArrayList<EdgeGene> getGenePool() {
	return genePool;
    }

    public void addGeneToPool(EdgeGene gene) {
	if (this.genePool == null) {
	    this.genePool = new ArrayList<EdgeGene>();
	}

	this.genePool.add(gene);
    }

    public void removeGeneGeneFromPool(EdgeGene gene) {
	if (this.genePool == null || this.genePool.isEmpty()) {
	    return;
	}

	this.genePool.remove(gene);
    }

    public void setGenePool(ArrayList<EdgeGene> genePool) {
	this.genePool = genePool;
    }

    public EdgeGene getRandomGene() {
	if (this.genePool == null || this.genePool.isEmpty()) {
	    throw new NullPointerException("Gene Pool is empty");
	}

	if (this.genePool.size() == 1) {
	    return this.genePool.get(0);
	}

	Random random = new Random();
	return this.genePool.get(random.nextInt(this.genePool.size() - 1));
    }

    public EdgeGene getLowestCostGene() {
	if (this.genePool == null || this.genePool.isEmpty()) {
	    throw new NullPointerException("Gene Pool is empty");
	}

	if (this.genePool.size() == 1) {
	    return this.genePool.get(0);
	}

	EdgeGene lowestCost = this.genePool.get(0);

	for (EdgeGene gene : this.genePool) {
	    if (gene.getAllele().getCost() < lowestCost.getAllele().getCost()) {
		lowestCost = gene;
	    }
	}

	return lowestCost;
    }

    public EdgeGene getHighestCostGene() {
	if (this.genePool == null || this.genePool.isEmpty()) {
	    throw new NullPointerException("Gene Pool is empty");
	}

	if (this.genePool.size() == 1) {
	    return this.genePool.get(0);
	}

	EdgeGene highestCost = this.genePool.get(0);

	for (EdgeGene gene : this.genePool) {
	    if (gene.getAllele().getCost() > gene.getAllele().getCost()) {
		highestCost = gene;
	    }
	}

	return highestCost;
    }
}
