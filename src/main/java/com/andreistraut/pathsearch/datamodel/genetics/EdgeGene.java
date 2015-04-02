package com.andreistraut.pathsearch.datamodel.genetics;

import com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.BaseGene;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;

/**
 * Wrapper around {@link com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge}
 * providing genetic-algorithm specific methods.
 * 
 * All genetic-algorithm specific operations that apply to edges should be 
 * directed via this class
 */
public class EdgeGene extends BaseGene {
    private DirectedWeightedEdge allele;
    private final GeneticConfiguration configuration;

    public EdgeGene(DirectedWeightedEdge allele, GeneticConfiguration configuration) 
	    throws InvalidConfigurationException {
	
	super(configuration);
	this.allele = allele;
	this.configuration = configuration;
    }
    
    public EdgeGene(EdgeGene gene) throws InvalidConfigurationException {
	super(gene.getConfiguration());
	this.allele = gene.getAllele();
	this.configuration = gene.getConfiguration();
    }
    
    /**
     * Copies this gene (edge) instance to a new one
     * 
     * @return A copy of this gene (edge)
     * @throws InvalidConfigurationException 
     */
    public EdgeGene copy() throws InvalidConfigurationException {
	return new EdgeGene(this.allele, this.configuration);
    }

    /**
     * Retrieves the allele value represented by this gene (edge)
     * 
     * @return The allele value of this gene (edge)
     */
    @Override
    public DirectedWeightedEdge getAllele() {
	return allele;
    }

    /**
     * Sets the value of this gene (edge) to the new given value. Allele must
     * be an instance of 
     * {@link com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge}
     * 
     * @param allele The new value of this Gene instance. Must be an instance of 
     * {@link com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge}
     */
    @Override
    public void setAllele(Object allele) {
	if(!(allele instanceof DirectedWeightedEdge)) {
	    throw new IllegalArgumentException("Allele must be a GenericDirectedEdge");
	}
	
	this.allele = (DirectedWeightedEdge)allele;
    }
    
    /**
     * Gets the configuration used for this gene (edge)
     * 
     * @return The configuration used for this gene (edge)
     */
    @Override
    public GeneticConfiguration getConfiguration() {
	return this.configuration;
    }

    /**
     * Each Gene implementation holds its own value object keeping the allele value
     * {@link com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge}
     * 
     * @return The {@link com.andreistraut.pathsearch.datamodel.graph.DirectedWeightedEdge}
     * wrapped in this gene (edge)
     */
    @Override
    protected DirectedWeightedEdge getInternalValue() {
	return allele;
    }

    /**
     * Returns a new gene (edge) instance. The returned instance will have the
     * same allele (edge value) as this current instance;
     * 
     * @return A new gene (edge) instance with the same allele (edge value) as 
     * this current instance
     */
    @Override
    public Gene newGene() {
	try {
	    EdgeGene gene = new EdgeGene(allele, configuration);
	    return gene;	    
	} catch (InvalidConfigurationException ex) {
	    Logger.getLogger(EdgeGene.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	return null;
    }

    /**
     * Returns a new gene (edge) instance. The returned instance will have the
     * same allele (edge value) as this current instance;
     * 
     * @return A new gene (edge) instance with the same allele (edge value) as 
     * this current instance
     */
    @Override
    protected Gene newGeneInternal() {
	return this.newGene();
    }
    
    /**
     * Retrieves a string representation of the value of this Gene (edge) instance 
     * that includes any information required to reconstruct it at a later time, 
     * such as its value and internal state
     * 
     * @return String representation of this Gene's (edge) current state
     */
    @Override
    public String getPersistentRepresentation() throws UnsupportedOperationException {
	//TODO: Implement toJson on edges
	return this.toString();
    }

    /**
     * Sets the value and internal state of this Gene (edge) from the string 
     * representation returned by a previous invocation of the 
     * getPersistentRepresentation() method
     * 
     * @param representation The string representation retrieved from a prior 
     * call to the getPersistentRepresentation() method
     * 
     * @throws UnsupportedOperationException
     * @throws UnsupportedRepresentationException 
     */
    @Override
    public void setValueFromPersistentRepresentation(String representation) 
	    throws UnsupportedOperationException, UnsupportedRepresentationException {
	
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Sets the value of this Gene's (edge) cost to a random legal value for the 
     * implementation
     * 
     * @param numberGenerator The random number generator that should be used 
     * to create any random values. Generated values will be used to set gene
     * (edge) costs
     */
    @Override
    public void setToRandomValue(RandomGenerator numberGenerator) {
	this.getAllele().setCost(numberGenerator.nextInt());
    }

    /**
     * Mutates the allele of this gene, setting its cost to a random number between 
     * 0 (inclusive) and index given as parameter (exclusive), with the probability 
     * of this happening being percentage given as parameter. For example, for 
     * an index 10 and a percentage 50, in 50% of the cases the mutation will 
     * set the value to a random number between 0 and 9
     * 
     * @param index The maximum upper cost to set in the eventuality of a mutation
     * @param percentage The probability of the mutation happening. Can be given
     * as a number between (0 and 1), or [1 and 100]. If 0, method is no-op.
     */
    @Override
    public void applyMutation(int index, double percentage) {
	if(percentage <= 0) {
	    return;
	}
	
	if(percentage > 0 && percentage < 1)  {
	    percentage = percentage * 100;
	}
	
	Random generator = new Random();
	int next = generator.nextInt(99);
	
	//If we get lucky and score less or equal to the chance, we have a mutation
	if(next <= (percentage)) {
	    this.getAllele().setCost(generator.nextInt(index));
	}
    }

    /*
     * Computes the cost difference between this gene (edge) and the one given as
     * parameter
     *
     * @param o The gene (edge) to compute difference to
    
     * @return The difference in cost between this gene (edge) and the one given
     * as parameter
     */
    @Override
    public int compareTo(Object o) {
	if(o == null || !(o instanceof EdgeGene)) {
	    return this.allele.getCost();
	} else {
	    DirectedWeightedEdge otherAllele = (DirectedWeightedEdge)((EdgeGene) o).getAllele();
	    
	    if(otherAllele == null) {
		return this.allele.getCost();
	    }
	    
	    return this.allele.getCost() - otherAllele.getCost();
	}
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder
		.append("EdgeGene ")
		.append(this.hashCode())		
		.append(" [")
		.append(this.allele.toString())
		.append("]");
	
	return builder.toString();
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 53 * hash + (this.allele != null ? this.allele.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final EdgeGene other = (EdgeGene) obj;
	if (this.allele != other.allele && (this.allele == null || !this.allele.equals(other.allele))) {
	    return false;
	}
	return true;
    }
    
}
