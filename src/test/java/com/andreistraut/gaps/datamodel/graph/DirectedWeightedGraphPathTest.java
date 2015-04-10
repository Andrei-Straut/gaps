package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectedWeightedGraphPathTest {

    private Node first;
    private Node second;
    private Node third;
    private DirectedWeightedEdge firstToSecond;
    private DirectedWeightedEdge secondToThird;
    private final int firstToSecondCost = 1;
    private final int secondToThirdCost = 2;
    private DirectedWeightedGraph graph;
    private DirectedWeightedGraphPath path;

    public DirectedWeightedGraphPathTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(DirectedWeightedGraphPathTest.class.getName()).log(Level.INFO, 
		"{0} TEST: Graph Path", 
		DirectedWeightedGraphPathTest.class.toString());
    }

    @Before
    public void setUp() {
	first = new Node("1", "1");
	second = new Node("2", "2");
	third = new Node("3", "3");

	firstToSecond = new DirectedWeightedEdge(first, second, firstToSecondCost, true);
	secondToThird = new DirectedWeightedEdge(second, third, secondToThirdCost, true);

	graph = new DirectedWeightedGraph(3, 2);	
	graph.addVertex(first);
	graph.addVertex(second);
	graph.addVertex(third);
	graph.addEdge(first, second, firstToSecond);
	graph.addEdge(second, third, secondToThird);
	
	ArrayList<DirectedWeightedEdge> edgeList = new ArrayList<DirectedWeightedEdge>();
	edgeList.add(firstToSecond);
	edgeList.add(secondToThird);
	
	path = new DirectedWeightedGraphPath(graph, edgeList);
    }
    
    @Test
    public void testGetStartVertex() {
	Assert.assertTrue(path.getStartVertex().equals(first));
    }

    @Test
    public void testGetEndVertex() {
	Assert.assertTrue(path.getEndVertex().equals(third));
    }

    @Test
    public void testGetEdgeList() {
	Assert.assertTrue(path.getEdgeList().size() == 2);
	Assert.assertTrue(path.getEdgeList().contains(firstToSecond));
	Assert.assertTrue(path.getEdgeList().get(0).equals(firstToSecond));
	Assert.assertTrue(path.getEdgeList().contains(secondToThird));
	Assert.assertTrue(path.getEdgeList().get(1).equals(secondToThird));
    }

    @Test
    public void testAddEdgeToList() {
	Assert.assertTrue(path.getEdgeList().size() == 2);
	
	Node fourth = new Node("4", "4");
	DirectedWeightedEdge firstToFourth = new DirectedWeightedEdge(first, fourth, 3, true);	
	graph.addVertex(fourth);
	graph.addEdge(first, fourth, firstToFourth);
	path.addEdgeToList(firstToFourth);
	
	Assert.assertTrue(path.getEdgeList().size() == 3);
	Assert.assertTrue(path.getEdgeList().contains(firstToSecond));
	Assert.assertTrue(path.getEdgeList().get(0).equals(firstToSecond));
	Assert.assertTrue(path.getEdgeList().contains(secondToThird));
	Assert.assertTrue(path.getEdgeList().get(1).equals(secondToThird));
	Assert.assertTrue(path.getEdgeList().contains(firstToFourth));
	Assert.assertTrue(path.getEdgeList().get(2).equals(firstToFourth));
    }

    @Test
    public void testSetEdgeList() {
	Node fourth = new Node("4", "4");
	DirectedWeightedEdge firstToFourth = new DirectedWeightedEdge(first, fourth, 3, true);
	DirectedWeightedEdge fourthToThird = new DirectedWeightedEdge(fourth, third, 4, true);
	graph.addVertex(fourth);
	graph.addEdge(first, fourth, firstToFourth);
	graph.addEdge(fourth, third, fourthToThird);
	
	ArrayList<DirectedWeightedEdge> newPath = new ArrayList<DirectedWeightedEdge>();
	newPath.add(firstToFourth);
	newPath.add(fourthToThird);
	
	path.setEdgeList(newPath);
	
	Assert.assertTrue(path.getEdgeList().size() == 2);
	Assert.assertTrue(path.getEdgeList().contains(firstToFourth));
	Assert.assertTrue(path.getEdgeList().get(0).equals(firstToFourth));
	Assert.assertTrue(path.getEdgeList().contains(fourthToThird));
	Assert.assertTrue(path.getEdgeList().get(1).equals(fourthToThird));
    }

    @Test
    public void testGetWeight() {
	Assert.assertTrue(path.getWeight() == 3);
    }

    @Test
    public void testToJson() {
	JsonObject jsonPath = path.toJson();
	Assert.assertTrue(jsonPath.has("path"));
	Assert.assertTrue(jsonPath.get("path").getAsJsonArray().size() == 2);
    }

}
