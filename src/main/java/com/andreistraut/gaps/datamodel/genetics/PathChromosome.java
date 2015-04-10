package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.graph.Node;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.BaseChromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.IChromosome;
import org.jgap.IGeneConstraintChecker;
import org.jgap.InvalidConfigurationException;
import org.jgap.UnsupportedRepresentationException;

/**
 * Genetic-algorithm implementation of a path between two nodes (represented as
 * a series of edges)
 *
 * Fitness is represented as inverse of total edge cost (lower cost, higher
 * fitness), and calculated as [Integer.MAX_VALUE - (sum (edge cost))]
 *
 * All genetic-algorithm specific operations that apply to paths should be
 * directed via this class
 */
public class PathChromosome extends BaseChromosome implements IChromosome {

    private final long serialVersionUID = 1L;
    private final int MAX_FITNESS_VALUE = 100000;
    private final int MIN_FITNESS_VALUE = -100000;

    private ArrayList<EdgeGene> genes;
    private Node source;
    private Node destination;
    private GeneticConfiguration configuration;

    private EdgeGeneConstraintChecker checker;
    private int fitnessValue = MAX_FITNESS_VALUE;
    private int operatedOn = 0;
    private int age = 0;
    private boolean hasFitnessValueSetDirectly = false;
    private boolean isSelectedForNextGeneration = false;

    //<editor-fold desc="Constructors" defaultstate="collapsed">    
    public PathChromosome(GeneticConfiguration configuration)
	    throws InvalidConfigurationException {

	super(configuration);
	this.configuration = configuration;
    }

    public PathChromosome(GeneticConfiguration configuration,
	    ArrayList<EdgeGene> genes,
	    Node source, Node destination)
	    throws InvalidConfigurationException {

	super(configuration);

	this.genes = genes;
	this.source = source;
	this.destination = destination;
	this.configuration = configuration;
    }

    public PathChromosome(GeneticConfiguration configuration, Gene[] initialGenes,
	    Node source, Node destination)
	    throws InvalidConfigurationException {

	this(configuration);
	for (Gene initialGene : initialGenes) {
	    this.genes.add((EdgeGene) initialGene);
	}
	this.source = source;
	this.destination = destination;
    }

    public PathChromosome(GeneticConfiguration configuration,
	    Gene[] initialGenes, Node source, Node destination,
	    IGeneConstraintChecker constraintChecker)
	    throws InvalidConfigurationException {

	this(configuration, initialGenes, source, destination);
	this.checker = (EdgeGeneConstraintChecker) constraintChecker;
    }

    private PathChromosome(GeneticConfiguration configuration,
	    String persistentRepresentatuion)
	    throws InvalidConfigurationException, UnsupportedRepresentationException {

	this(configuration);
    }

    private PathChromosome(GeneticConfiguration configuration, int desiredSize)
	    throws InvalidConfigurationException {

	this(configuration);
    }

    private PathChromosome(GeneticConfiguration configuration,
	    Gene sampleGene, int desiredSize)
	    throws InvalidConfigurationException {

	this(configuration);
    }

    private PathChromosome(GeneticConfiguration configuration, Gene sampleGene,
	    int desiredSize, IGeneConstraintChecker constraintChecker)
	    throws InvalidConfigurationException {

	this(configuration);
	this.checker = (EdgeGeneConstraintChecker) constraintChecker;
    }
    // </editor-fold>

    @Override
    public String getUniqueID() {
	return Long.toString(this.serialVersionUID);
    }

    /**
     * Gets the configuration used for this PathChromosome
     *
     * @return The configuration used for this PathChromosome
     */
    @Override
    public Configuration getConfiguration() {
	return this.configuration;
    }

    /**
     * Returns the Gene
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) with
     * the lowest cost within the PathChromosome
     *
     * @return The Gene (DirectedWeightedEdge) with the lowest cost
     */
    public EdgeGene getLowestCostGene() {
	EdgeGene lowestCost = this.getGene(0);

	for (EdgeGene gene : this.getGenesList()) {
	    if (gene.getAllele().getCost() < lowestCost.getAllele().getCost()) {
		lowestCost = gene;
	    }
	}

	return lowestCost;
    }

    /**
     * Returns the Gene
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) with
     * the highest cost within the PathChromosome
     *
     * @return The Gene (DirectedWeightedEdge) with the highest cost
     */
    public EdgeGene getHighestCostGene() {
	EdgeGene highestCost = this.getGene(0);

	for (EdgeGene gene : this.getGenesList()) {
	    if (gene.getAllele().getCost() >= highestCost.getAllele().getCost()) {
		highestCost = gene;
	    }
	}

	return highestCost;
    }

    /**
     * Returns the index of the Gene
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) with
     * the lowest cost within the PathChromosome
     *
     * @return The index of the Gene (DirectedWeightedEdge) with the lowest cost
     */
    public int getLowestCostGeneIndex() {
	return this.getGenesList().indexOf(this.getLowestCostGene());
    }

    /**
     * Returns the index of the Gene
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) with
     * the highest cost within the PathChromosome
     *
     * @return The index of the Gene (DirectedWeightedEdge) with the highest
     * cost
     */
    public int getHighestCostGeneIndex() {
	return this.getGenesList().indexOf(this.getHighestCostGene());
    }

    /**
     * Returns the Gene
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) at
     * the given index (locus) within the PathChromosome
     *
     * @param desiredLocus
     * @return Gene at index specified as parameter
     */
    @Override
    public EdgeGene getGene(int desiredLocus) {
	if (this.genes.size() > desiredLocus) {
	    return this.genes.get(desiredLocus);
	}

	throw new ArrayIndexOutOfBoundsException(
		"Requested locus ("
		+ desiredLocus
		+ ") is bigger than this chromosome's number of genes ("
		+ this.genes.size()
		+ ")");
    }

    /**
     * Sets the Gene
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) at
     * the given index (locus) within the PathChromosome with the specified
     * value. Replaces previous existing gene at given position
     *
     * @param index index to set the value at
     * @param gene value to set
     */
    @Override
    public void setGene(int index, Gene gene) {
	this.setGene(index, gene, false);
    }

    /**
     * Sets the Gene
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) at
     * the given index (locus) within the PathChromosome with the specified
     * value. Replaces previous existing gene at given position. Optionally
     * check if the value is seemsValid (is referenced by previous and next
     * genes)
     *
     * @param index index to set the value at
     * @param gene value to set
     * @param checkValidity Check if value is legal, and only set it if it is
     */
    public void setGene(int index, Gene gene, boolean checkValidity) {
	if (this.genes.size() < index) {
	    throw new ArrayIndexOutOfBoundsException(
		    "Requested locus ("
		    + index
		    + ") is bigger than this chromosome's number of genes ("
		    + this.genes.size()
		    + ")");
	}

	EdgeGene geneToSet = (EdgeGene) gene;

	if (checkValidity) {
	    boolean previousGeneValidationNeeded = true;
	    boolean nextGeneValidationNeeded = true;

	    if (index == 0) {
		previousGeneValidationNeeded = false;
	    }

	    if (index == this.getGenesList().size() - 1) {
		nextGeneValidationNeeded = false;
	    }

	    if (previousGeneValidationNeeded) {
		EdgeGene previous = this.getGene(index - 1);
		if (!geneToSet.getAllele().getSource().equals(previous.getAllele().getDestination())) {
		    return;
		}
	    }

	    if (nextGeneValidationNeeded) {
		EdgeGene next = this.getGene(index + 1);
		if (!geneToSet.getAllele().getDestination().equals(next.getAllele().getSource())) {
		    return;
		}
	    }
	}

	this.genes.set(index, (EdgeGene) gene);
    }

    /**
     * Removes Gene at specified index. Does internal bounds check
     *
     * @param index The index of the Gene to remove
     */
    public void removeGene(int index) {
	if (this.genes.size() > index) {
	    this.genes.remove(index);
	}
    }

    /**
     * Return an array containing all the genes
     * (({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge})) in
     * the PathChromosome
     *
     * @return An array containing all the genes
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) in
     * the PathChromosome
     */
    @Override
    public Gene[] getGenes() {
	return (this.genes != null && this.genes.size() > 0)
		? this.genes.toArray(new Gene[this.genes.size()])
		: new Gene[0];
    }

    /**
     * Sets this PathChromosome's genes
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge})
     *
     * @param genes The genes (edges) to set to this PathChromosome
     * @throws org.jgap.InvalidConfigurationException Exception is never thrown
     * in corresponding method body
     */
    @Override
    public void setGenes(Gene[] genes) throws InvalidConfigurationException {
	this.genes.clear();

	for (int i = 0; i < genes.length; i++) {
	    this.genes.set(i, (EdgeGene) genes[i]);
	}
    }

    /**
     * Return A list containing all the genes
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) in
     * the PathChromosome
     *
     * @return A list containing all the genes
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) in
     * this PathChromosome
     */
    public ArrayList<EdgeGene> getGenesList() {
	return this.genes;
    }

    /**
     * Sets this PathChromosome's genes
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge})
     *
     * @param genesList The genes
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge}) to
     * set to this PathChromosome
     */
    public void setGenesList(ArrayList<EdgeGene> genesList) {
	this.genes = genesList;
    }

    /**
     * @see
     * com.andreistraut.pathsearch.datamodel.genetics.EdgeChromosome.getGenesList
     * @return
     */
    @Override
    public Object getApplicationData() {
	return this.getGenesList();
    }

    /**
     * @see
     * com.andreistraut.pathsearch.datamodel.genetics.EdgeChromosome.setGenesList
     *
     * @param newData
     */
    @Override
    public void setApplicationData(Object newData) {
	if (newData instanceof ArrayList) {
	    this.genes = (ArrayList<EdgeGene>) newData;
	}
    }

    /**
     * Retrieves the total cost of this PathChromosome. Lower values are better
     *
     * @return The total cost of this PathChromosome
     */
    public int getCost() {
	if (!isLegal()) {
	    return Integer.MAX_VALUE;
	}

	int cost = 0;

	for (EdgeGene gene : this.genes) {
	    cost += gene.getAllele().getCost();
	}

	return cost;
    }

    /**
     * Retrieves the cost of the genes in this PathChromosome within the indices
     * specified as parameters. Lower values are better
     *
     * @param startIndex Index of first gene
     * @param endIndex Index of last gene
     * @return The cost of the genes in this PathChromosome within the indices
     */
    public int getCost(int startIndex, int endIndex) {
	if (!isLegal()) {
	    return Integer.MAX_VALUE;
	}

	int cost = 0;

	if (startIndex < 0 || startIndex >= this.size()) {
	    startIndex = 0;
	}

	if (endIndex >= this.size()) {
	    endIndex = this.size() - 1;
	}

	for (int i = startIndex; i <= endIndex; i++) {
	    cost += this.genes.get(i).getAllele().getCost();
	}

	return cost;
    }

    /**
     * Retrieves the minimum possible fitness value of this PathChromosome
     *
     * @return The minimum possible fitness value of this PathChromosome
     */
    public int getMaxFitnessValue() {
	return MAX_FITNESS_VALUE;
    }

    public int getMinFitnessValue() {
	return MIN_FITNESS_VALUE;
    }

    /**
     * Retrieves the fitness value of this PathChromosome. Higher values are
     * better. Is calculated as the inverse of cost
     *
     * @return The fitness of this PathChromosome
     */
    @Override
    public double getFitnessValue() {
	if (!isLegal()) {
	    return MIN_FITNESS_VALUE;
	}

	return this.MAX_FITNESS_VALUE - this.getCost();
    }

    /**
     * Sets the fitness value of this PathChromosome. Higher values are better
     *
     * @param newFitnessValue A positive value representing the fitness of this
     * PathChromosome
     */
    @Override
    public void setFitnessValue(double newFitnessValue) {
	this.hasFitnessValueSetDirectly = true;
	this.fitnessValue = (int) newFitnessValue;
    }

    /**
     * @see
     * com.andreistraut.pathsearch.datamodel.genetics.EdgeChromosome.getFitnessValue
     *
     * @return
     */
    @Override
    public double getFitnessValueDirectly() {
	return this.getFitnessValue();
    }

    /**
     * @see
     * com.andreistraut.pathsearch.datamodel.genetics.EdgeChromosome.setFitnessValue
     * @param newFitnessValue
     */
    @Override
    public void setFitnessValueDirectly(double newFitnessValue) {
	this.setFitnessValue(newFitnessValue);
    }

    /**
     * Compares the fitness value of the current PathChromosome with the one of
     * the PathChromosome given as parameter (strict compare)
     *
     * @param chromosome PathChromosome to compare fitness value to
     * @return True if this PathChromosome is strictly fitter (no equality
     * included), false otherwise
     */
    public boolean isFitterThan(PathChromosome chromosome) {
	if (chromosome == null) {
	    return true;
	}

	return this.getFitnessValue() > chromosome.getFitnessValue();
    }

    /**
     * <p>
     * Checks if this PathChromosome contains cyclic paths, and if it does,
     * returns the sequence of genes (DirectedWeightedEdge) forming the
     * cycle.</p>
     *
     * <p>
     * NOTE: Use this method sparingly, as it will provide a bit of a slowdown
     * for genetic evolution speed</p>
     *
     * @return An array containing the cyclic path, or empty if no cyclic path
     * was found
     */
    public ArrayList<EdgeGene> getCyclicGeneSequence() {

	ArrayList<EdgeGene> cycle = new ArrayList<EdgeGene>();
	LinkedHashMap<String, EdgeGene> visitedGenes = new LinkedHashMap<String, EdgeGene>();

	for (EdgeGene gene : this.genes) {

	    /**
	     * Cycle found
	     */
	    if (visitedGenes.containsKey(gene.getAllele().getDestination().toString())) {
		/**
		 * Find the section containing the cycle
		 */
		EdgeGene firstGene = visitedGenes.get(gene.getAllele().getDestination().toString());
		int firstEdgeIndex = this.genes.indexOf(firstGene);
		int lastEdgeIndex = this.genes.lastIndexOf(gene);

		cycle = new ArrayList<EdgeGene>(this.genes.subList(firstEdgeIndex, lastEdgeIndex + 1));
		return cycle;
	    } else {
		visitedGenes.put(gene.getAllele().getSource().toString(), gene);
	    }
	}

	return cycle;
    }

    /**
     * Retrieves whether this PathChromosome has been selected by the natural
     * selector to continue to the next generation
     *
     * @return True if this PathChromosome has been selected, false otherwise
     */
    @Override
    public boolean isSelectedForNextGeneration() {
	return this.isSelectedForNextGeneration;
    }

    /**
     * Sets whether this PathChromosome has been selected by the natural
     * selector to continue to the next generation
     *
     * @param isSelected True if this PathChromosome has been selected, false
     * otherwise
     */
    @Override
    public void setIsSelectedForNextGeneration(boolean isSelected) {
	this.isSelectedForNextGeneration = isSelected;
    }

    /**
     * Gets the number of genetic operations performed on this PathChromosome in
     * current evolution round
     *
     * @return The number of genetic operations performed on PathChromosome in
     * current evolution round
     */
    @Override
    public int operatedOn() {
	return this.operatedOn;
    }

    /**
     * Increase information of number of genetic operations performed on this
     * PathChromosome in current evolution round.
     */
    @Override
    public void increaseOperatedOn() {
	this.operatedOn++;
    }

    /**
     * Resets the information of how many genetic operators have been performed
     * on the PathChromosome in the current round of evolution
     */
    @Override
    public void resetOperatedOn() {
	this.operatedOn = 0;
    }

    /**
     * 0: Chromosome newly created in this generation, or number of generations
     * this chromosome (path) has survived
     *
     * @return Number of generations this chromosome (path) has survived
     */
    @Override
    public int getAge() {
	return this.age;
    }

    /**
     * Sets the age of the PathChromosome
     *
     * @param age The age to set
     */
    @Override
    public void setAge(int age) {
	this.age = age;
    }

    /**
     * Increases the number of evolutionary rounds of this PathChromosome in
     * which it has not been changed by one
     */
    @Override
    public void increaseAge() {
	this.age++;
    }

    /**
     * Reset age of PathChromosome because it has been changed
     */
    @Override
    public void resetAge() {
	this.age = 0;
    }

    /**
     * Sets the constraint checker to be used for this PathChromosome whenever
     * method setAllele is called
     *
     * @param constraintChecker The constraint checker to set
     * @throws InvalidConfigurationException
     */
    @Override
    public void setConstraintChecker(IGeneConstraintChecker constraintChecker) throws InvalidConfigurationException {
	if (constraintChecker instanceof EdgeGeneConstraintChecker) {
	    this.checker = (EdgeGeneConstraintChecker) constraintChecker;
	}
    }

    /**
     * Returns the size of this PathChromosome - the number of genes it contains
     * ({@link com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge})
     *
     * @return Number of genes (edges) contained within this PathChromosome
     * instance
     */
    @Override
    public int size() {
	return this.genes.size();
    }

    /**
     * Returns whether this PathChromosome is a legal path from source to
     * destination
     *
     * @return True if this PathChromosome is a legal path, false otherwise
     */
    public boolean isLegal() {
	boolean seemsValid = (this.genes != null && this.genes.size() > 0);

	if (source == null || destination == null || !seemsValid) {
	    return false;
	}

	if (!this.source.equals(genes.get(0).getAllele().getSource())
		|| !this.destination.equals(genes.get(genes.size() - 1).getAllele().getDestination())) {
	    return false;
	}

	if (seemsValid) {
	    for (int i = 0; i < genes.size() - 1; i++) {
		/* 
		 Continuity check from source to destination:
		 For each edge, the destination must be equal to the source of the next edge, i.e.
		 if we have a genes 1-2-3-4, the edges should be modeled as pairs containing
		 1-2, 2-3, 3-4
		 */
		Node nextSource = this.genes.get(i).getAllele().getDestination();
		Node currentDestination = this.genes.get(i + 1).getAllele().getSource();

		if (currentDestination != nextSource) {
		    return false;
		}
	    }
	}

	return seemsValid;
    }

    /**
     * Computes the cost difference between this PathChromosome's genes and the
     * one given as parameter.
     *
     * @param o The genes to compute difference to
     *
     * @return The difference in fitness between this chromosome and the one
     * given as parameter
     */
    @Override
    public int compareTo(Object o) {
	if (!(o instanceof PathChromosome)) {
	    return Integer.MAX_VALUE;
	}

	if (this.equals(o)) {
	    return 0;
	}

	PathChromosome other = (PathChromosome) o;
	
	if(this.isLegal() && !other.isLegal()) {
	    return Integer.MAX_VALUE;
	}
	
	if(!this.isLegal() && other.isLegal()) {
	    return Integer.MIN_VALUE;
	}

	return (int) (this.getFitnessValue() - other.getFitnessValue());
    }

    @Override
    public void cleanup() {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUniqueIDTemplate(String a_templateID, int a_index) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUniqueIDTemplate(int a_index) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public JsonObject toJson() {
	JsonObject jsonChromosome = new JsonObject();

	jsonChromosome.add("nodeFrom", this.source.toJson());
	jsonChromosome.add("nodeTo", this.destination.toJson());
	jsonChromosome.addProperty("fitness", this.getFitnessValue());
	jsonChromosome.addProperty("cost", this.getCost());
	jsonChromosome.addProperty("isLegal", this.isLegal());
	jsonChromosome.addProperty("isSelectedForNextGeneration", this.isSelectedForNextGeneration);
	jsonChromosome.addProperty("age", this.age);

	JsonArray path = new JsonArray();
	for (EdgeGene gene : this.getGenesList()) {
	    path.add(gene.getAllele().toJson());
	}
	jsonChromosome.add("path", path);

	return jsonChromosome;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();

	builder
		.append("PathChromosome ")
		.append(this.hashCode())
		.append(System.getProperty("line.separator"));

	for (int i = 0; i < this.genes.size(); i++) {
	    builder.append(this.genes.get(i).toString());
	    if (i < this.genes.size() - 1) {
		builder.append(", ")
			.append(System.getProperty("line.separator"));
	    }
	}

	builder.append(" (Fitness: [").append(this.getFitnessValue()).append("], Cost: [").append(this.getCost()).append("]) ");
	builder.append(" (Legal: ").append(this.isLegal()).append(")");
	return builder.toString();
    }

    /**
     * Deep-compares this PathChromosome's genes with another (edge-level
     * comparing)
     *
     * @param obj The PathChromosome to compare to
     * @return True if the two paths are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}

	final PathChromosome other = (PathChromosome) obj;

	if (other.getGenesList() == null || other.getGenesList().size() != this.genes.size()) {
	    return false;
	}

	final ArrayList<EdgeGene> otherGenes = other.getGenesList();

	for (int i = 0; i < this.genes.size(); i++) {
	    if (!otherGenes.get(i).equals(this.genes.get(i))) {
		return false;
	    }
	}

	return true;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 71 * hash + (this.genes != null ? this.genes.hashCode() : 0);
	return hash;
    }

    @Override
    public PathChromosome clone() {
	try {
	    PathChromosome cloned = new PathChromosome(configuration,
		    new ArrayList<EdgeGene>(genes),
		    (Node) this.source.clone(),
		    (Node) this.destination.clone());
	    return cloned;
	} catch (ClassCastException ex) {
	    Logger.getLogger(PathChromosome.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
	    return null;
	} catch (InvalidConfigurationException ex) {
	    Logger.getLogger(PathChromosome.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
	}

	return null;
    }

    @Override
    public boolean isHandlerFor(Object a_obj, Class a_class) {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object perform(Object a_obj, Class a_class, Object a_params) throws Exception {
	throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
