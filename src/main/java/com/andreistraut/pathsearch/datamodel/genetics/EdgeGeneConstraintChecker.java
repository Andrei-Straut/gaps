package com.andreistraut.pathsearch.datamodel.genetics;

import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.IGeneConstraintChecker;

public class EdgeGeneConstraintChecker implements IGeneConstraintChecker {

    public EdgeGeneConstraintChecker() {
    }

    /**
     * Check if a given allele (edge) value is valid for the given gene
     * instance. This particular implementation will not check for gene passed
     * as parameter, but will check the gene (edge) contained at index geneIndex 
     * within the chromosome (path) given as parameter
     *
     * @param gene The gene the given allele is to be validated for. Not used
     * @param alleleValue The allele (edge) value to be validated
     * @param chromosome The chromosome (path) in which the gene (edge) is 
     * contained
     * @param geneIndex The index the gene (edge) is contained in the 
     * at (path), or -1 if unknown
     * 
     * @return True if allele may be set for gene, false otherwise
     */
    @Override
    public boolean verify(Gene gene, Object alleleValue, IChromosome chromosome, int geneIndex) {
	if (gene == null
		|| !(gene instanceof EdgeGene)
		|| ((EdgeGene) gene).getAllele() == null) {
	    return false;
	}

	/* If the chromosome (path) is not null, and requested gene index is a valid position, 
	 and the chromosome (path) is longer than gene (edge) at requested gene index */
	if (chromosome == null
		|| !(alleleValue instanceof DirectedWeightedEdge)
		|| chromosome.getGenes().length <= 0
		|| geneIndex <= 0
		|| chromosome.getGenes().length < geneIndex) {
	    return false;
	}

	/* If geneIndex is 0, it means it is the first edge in the path, so it wont't have a
	 previous edge. Conversely, if it last, it won't have a next*/
	DirectedWeightedEdge toVerify = (DirectedWeightedEdge) alleleValue;
	boolean previousGeneValidationNeeded = (geneIndex > 0);
	boolean nextGeneValidationNeeded = (geneIndex < chromosome.getGenes().length - 1);

	EdgeGene previousGene;
	EdgeGene nextGene;

	if (previousGeneValidationNeeded) {
	    previousGene = (EdgeGene) ((chromosome.getGenes())[geneIndex - 1]);

	    if (previousGene == null || previousGene.getAllele().getDestination() != toVerify.getSource()) {
		return false;
	    }
	}

	if (nextGeneValidationNeeded) {
	    nextGene = (EdgeGene) ((chromosome.getGenes())[geneIndex + 1]);

	    if (nextGene == null || nextGene.getAllele().getSource() != toVerify.getDestination()) {
		return false;
	    }
	}

	return true;
    }

}