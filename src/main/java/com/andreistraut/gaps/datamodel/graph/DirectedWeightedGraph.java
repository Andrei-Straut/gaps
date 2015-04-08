package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.InvalidConfigurationException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DirectedPseudograph;

/**
 * Class encapsulating a directed weighted jsonNodesWithAdjacencies, exposing some utility methods
 helping with dealing with jsonNodesWithAdjacencies operations
 */
public class DirectedWeightedGraph extends DirectedPseudograph<Node, DirectedWeightedEdge> {

    private int numberOfNodes;
    private int numberOfEdges;
    private DirectedWeightedEdgeFactory factory;
    private int maximumEdgeWeight = 1000;
    private int minimumEdgeWeight = 1;
    private ArrayList<DirectedWeightedGraphPath> depthFirstSearchResults = new ArrayList<DirectedWeightedGraphPath>();
    
    /**
     * Statistics
     * Initialize actual weights with opposite values, so when computing real 
     * values we start with opposite comparisons
     */
    private int actualMinimumEdgeCost = maximumEdgeWeight;
    private int actualMaximumEdgeCost = minimumEdgeWeight;
    private int totalEdgeCost = 0;
    private double averageEdgeCost = 0.0;
    private double averageEdgesPerNode = 0.0;

    private DirectedWeightedGraph(Class<? extends DirectedWeightedEdge> type) {
	super(type);
    }

    public DirectedWeightedGraph(int numberOfNodes, int numberOfEdges) {
	super(DirectedWeightedEdge.class);

	this.numberOfNodes = numberOfNodes;
	this.numberOfEdges = numberOfEdges;
	
	this.factory = new DirectedWeightedEdgeFactory();
    }
    
    public DirectedWeightedGraph(GraphSettings settings) {
	this(settings.getNumberOfNodes(), settings.getNumberOfEdges());
	this.minimumEdgeWeight = settings.getMinimumEdgeWeight();
	this.maximumEdgeWeight = settings.getMaximumEdgeWeight();
	
	this.factory = new DirectedWeightedEdgeFactory();
    }

    public int getNumberOfNodes() {
	return numberOfNodes;
    }

    public int getNumberOfEdges() {
	return numberOfEdges;
    }

    public ArrayList<Node> getNodes() {
	ArrayList<Node> nodesList = new ArrayList<Node>();
	nodesList.addAll(this.vertexSet());

	return nodesList;
    }

    public ArrayList<DirectedWeightedEdge> getEdges() {
	ArrayList<DirectedWeightedEdge> edgesList = new ArrayList<DirectedWeightedEdge>();
	edgesList.addAll(this.edgeSet());

	return edgesList;
    }

    @Override
    public DirectedWeightedEdgeFactory getEdgeFactory() {
	return this.factory;
    }

    public ArrayList<Node> initNodes() {
	for (int i = 0; i < this.numberOfNodes; i++) {
	    Node node = new Node(Integer.toString(i), Integer.toString(i));
	    this.addVertex(node);
	}

	ArrayList<Node> nodesList = new ArrayList<Node>();
	nodesList.addAll(this.vertexSet());

	return nodesList;
    }

    public ArrayList<DirectedWeightedEdge> initEdges() {
	ArrayList<Node> nodesList = this.getNodes();

	ArrayList<DirectedWeightedEdge> edgeList = new ArrayList<DirectedWeightedEdge>();
	Random generator = new Random();

	for (int i = 0; i < this.numberOfNodes - 1; i++) {
	    Node source = nodesList.get(i);
	    Node destination = nodesList.get(i + 1);
	    int cost = generator.nextInt(this.maximumEdgeWeight);
	    if (cost < this.minimumEdgeWeight) {
		cost = this.minimumEdgeWeight;
	    }

	    DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination, cost);

	    this.addEdge(source, destination, edge);
	    edgeList.add(edge);
	    
	    /**
	     * Statistics
	     */
	    if(cost < this.actualMinimumEdgeCost) {
		this.actualMinimumEdgeCost = cost;
	    }	    
	    if(cost > this.actualMaximumEdgeCost) {
		this.actualMaximumEdgeCost = cost;
	    }
	    this.totalEdgeCost += cost;
	}

	for (int i = this.numberOfNodes; i <= this.numberOfEdges; i++) {
	    int sourceIndex = generator.nextInt(nodesList.size() - 1);
	    int destinationIndex = generator.nextInt(nodesList.size() - 1);
	    
	    /**
	     * Do not allow nodes to link to themselves
	     */
	    if(sourceIndex == destinationIndex && this.numberOfNodes > 1) {
		if(destinationIndex == 0) {
		    destinationIndex = 1;
		} else {
		    destinationIndex = destinationIndex - 1;
		}
	    }
	    
	    Node source = nodesList.get(sourceIndex);
	    Node destination = nodesList.get(destinationIndex);
	    
	    while(source.equals(destination) && this.numberOfNodes > 1) {
		destination = nodesList.get(generator.nextInt(nodesList.size() - 1));
	    }
	    
	    int cost = generator.nextInt(this.maximumEdgeWeight);
	    if (cost < this.minimumEdgeWeight) {
		cost = this.minimumEdgeWeight;
	    }

	    DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination, cost);

	    this.addEdge(source, destination, edge);
	    edgeList.add(edge);
	    
	    /**
	     * Statistics
	     */
	    if(cost < this.actualMinimumEdgeCost) {
		this.actualMinimumEdgeCost = cost;
	    }	    
	    if(cost > this.actualMaximumEdgeCost) {
		this.actualMaximumEdgeCost = cost;
	    }	    
	    this.totalEdgeCost += cost;
	}

	return edgeList;
    }

    public List<GraphPath<Node, DirectedWeightedEdge>> getKShortestPaths(Node source,
	    Node destination, int numberOfPaths) {

	KShortestPaths<Node, DirectedWeightedEdge> kPaths = new KShortestPaths<Node, DirectedWeightedEdge>(
		this,
		source,
		numberOfPaths);

	List<GraphPath<Node, DirectedWeightedEdge>> paths = kPaths.getPaths(destination);
	Collections.sort(paths, new GraphPathComparator());

	return paths;
    }

    public ArrayList<DirectedWeightedGraphPath> getKPathsDepthFirst(Node source,
	    Node destination, int numberOfPaths) {

	depthFirstSearchResults.clear();
	LinkedList<Node> visited = new LinkedList<Node>();
	visited.add(source);

	/**
	 * Kinda reeks of bad engineering, side-effects on depthFirstSearchResults :(
	 */
	depthFirstSearch(visited, destination, numberOfPaths);
	return depthFirstSearchResults;
    }

    public DirectedWeightedEdge getLowestCostEdge() {
	ArrayList<DirectedWeightedEdge> edges = this.getEdges();

	if (edges == null || edges.isEmpty()) {
	    throw new NullPointerException("Edge list is empty");
	}

	if (edges.size() == 1) {
	    return edges.get(0);
	}

	DirectedWeightedEdge lowestCost = edges.get(0);

	for (DirectedWeightedEdge edge : edges) {
	    if (edge.getCost() < lowestCost.getCost()) {
		lowestCost = edge;
	    }
	}

	return lowestCost;
    }

    public DirectedWeightedEdge getHighestCostEdge() {
	ArrayList<DirectedWeightedEdge> edges = this.getEdges();

	if (edges == null || edges.isEmpty()) {
	    throw new NullPointerException("Edge list is empty");
	}

	if (edges.size() == 1) {
	    return edges.get(0);
	}

	DirectedWeightedEdge highestCost = edges.get(0);

	for (DirectedWeightedEdge edge : edges) {
	    if (edge.getCost() > highestCost.getCost()) {
		highestCost = edge;
	    }
	}

	return highestCost;
    }

    private void depthFirstSearch(LinkedList<Node> visited, Node destination, int maxIterations) {

	if (this.depthFirstSearchResults.size() >= maxIterations) {
	    return;
	}

	LinkedList<Node> outgoingNodes = new LinkedList<Node>();

	Set<DirectedWeightedEdge> edges = this.outgoingEdgesOf(visited.getLast());
	for (DirectedWeightedEdge edge : edges) {
	    outgoingNodes.add(edge.getDestination());
	}

	/**
	 * examine adjacent nodes
	 */
	for (Node node : outgoingNodes) {
	    if (visited.contains(node)) {
		continue;
	    }
	    if (node.equals(destination)) {
		visited.add(node);

		/**
		 * Save path
		 */
		DirectedWeightedGraphPath path = new DirectedWeightedGraphPath(this);

		for (int i = 0; i < visited.size() - 1; i++) {
		    Node current = visited.get(i);
		    Node next = visited.get(i + 1);

		    path.addEdgeToList(this.getEdge(current, next));
		}

		this.depthFirstSearchResults.add(path);

		/**
		 * Remove last visited node and move one
		 */
		visited.removeLast();
		break;
	    }
	}
	
	/**
	 * in depth-first, recursion needs to come after visiting adjacent nodes
	 */
	for (Node node : outgoingNodes) {
	    if (visited.contains(node) || node.equals(destination)) {
		continue;
	    }
	    visited.addLast(node);
	    depthFirstSearch(visited, destination, maxIterations);
	    visited.removeLast();
	}
    }
    
    public JsonObject toJson() {
	JsonObject graph = new JsonObject();
	
	JsonArray jsonNodesWithAdjacencies = this.getJsonNodesWithAdjacencies();	
	JsonArray jsonEdges = this.getJsonEdges();
	JsonObject jsonStatistics = this.getJsonStatistics();
		
	graph.add("graph", jsonNodesWithAdjacencies);
	graph.add("edges", jsonEdges);
	graph.add("statistics", jsonStatistics);
	
	return graph;
    }
    
    private JsonArray getJsonNodesWithAdjacencies() {
	JsonArray jsonNodesWithAdjacencies = new JsonArray();	
	for(Node node : this.getNodes()) {
	    JsonObject jsonNode = node.toJson();
	    JsonArray jsonAdjacencies = jsonNode.getAsJsonArray("adjacencies");
	    
	    for(DirectedWeightedEdge edge : this.outgoingEdgesOf(node)) {
		jsonAdjacencies.add(edge.toJson());
	    }
	    
	    jsonNodesWithAdjacencies.add(jsonNode);
	}
	
	return jsonNodesWithAdjacencies;
    }
    
    private JsonArray getJsonEdges() {
	JsonArray jsonEdges = new JsonArray();	
	for(DirectedWeightedEdge edge : this.getEdges()) {
	    JsonObject jsonEdge = edge.toJson();
	    jsonEdges.add(jsonEdge);
	}
	
	return jsonEdges;
    }
    
    private JsonObject getJsonStatistics() {
	JsonObject statistics = new JsonObject();
	
	this.averageEdgeCost = this.totalEdgeCost / this.edgeSet().size();
	try {
	    this.averageEdgesPerNode = NumberFormat.getInstance()
		    .parse(
			    String.format("%.2f", (double) this.edgeSet().size() / this.vertexSet().size()))
		    .doubleValue();
	} catch (ParseException ex) {
	    Logger.getLogger(DirectedWeightedGraph.class.getName()).log(Level.WARNING, 
		    "Failed to convert average edges per node value for value: " 
			    + (double) this.edgeSet().size() / this.vertexSet().size());
	    this.averageEdgesPerNode = (double) this.edgeSet().size() / this.vertexSet().size();
	}
	
	statistics.addProperty("numberOfNodes", this.vertexSet().size());
	statistics.addProperty("numberOfEdges", this.edgeSet().size());
	statistics.addProperty("minimumEdgeCost", this.actualMinimumEdgeCost);
	statistics.addProperty("maximumEdgeCost", this.actualMaximumEdgeCost);
	statistics.addProperty("totalEdgeCost", this.totalEdgeCost);
	statistics.addProperty("averageEdgeCost", this.averageEdgeCost);
	statistics.addProperty("averageEdgesPerNode", this.averageEdgesPerNode);
	
	return statistics;
    }
}