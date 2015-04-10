package com.andreistraut.gaps.datamodel.graph;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphPathComparatorTest {

    private Node first;
    private Node second;
    private Node third;
    private Node fourth;
    private DirectedWeightedEdge firstToSecond;
    private DirectedWeightedEdge secondToThird;
    private DirectedWeightedEdge firstToFourth;
    private DirectedWeightedEdge fourthToThird;
    private final int firstToSecondCost = 1;
    private final int secondToThirdCost = 2;
    private final int firstToFourthCost = 2;
    private final int fourthToThirdCost = 2;
    private DirectedWeightedGraph graph;

    public GraphPathComparatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(GraphPathComparatorTest.class.getName()).log(Level.INFO,
		GraphPathComparatorTest.class.toString() + " TEST: Graph Path Comparator");    
    }

    @Before
    public void setUp() {
	first = new Node("1", "1");
	second = new Node("2", "2");
	third = new Node("3", "3");
	fourth = new Node("4", "4");

	firstToSecond = new DirectedWeightedEdge(first, second, firstToSecondCost, true);
	secondToThird = new DirectedWeightedEdge(second, third, secondToThirdCost, true);
	firstToFourth = new DirectedWeightedEdge(first, fourth, firstToFourthCost, true);
	fourthToThird = new DirectedWeightedEdge(fourth, third, fourthToThirdCost, true);

	graph = new DirectedWeightedGraph(3, 2);
	graph.addVertex(first);
	graph.addVertex(second);
	graph.addVertex(third);
	graph.addVertex(fourth);
	graph.addEdge(first, second, firstToSecond);
	graph.addEdge(second, third, secondToThird);
	graph.addEdge(first, fourth, firstToFourth);
	graph.addEdge(fourth, third, fourthToThird);
    }

    @Test
    public void testCompareLowerThan() {
	ArrayList<DirectedWeightedEdge> edgeList1 = new ArrayList<DirectedWeightedEdge>();
	edgeList1.add(firstToSecond);
	edgeList1.add(secondToThird);
	DirectedWeightedGraphPath path1 = new DirectedWeightedGraphPath(graph, edgeList1);

	ArrayList<DirectedWeightedEdge> edgeList2 = new ArrayList<DirectedWeightedEdge>();
	edgeList2.add(firstToFourth);
	edgeList2.add(fourthToThird);
	DirectedWeightedGraphPath path2 = new DirectedWeightedGraphPath(graph, edgeList2);

	Assert.assertTrue(new GraphPathComparator().compare(path1, path2) < 0);
    }

    @Test
    public void testCompareLargerThan() {
	ArrayList<DirectedWeightedEdge> edgeList1 = new ArrayList<DirectedWeightedEdge>();
	edgeList1.add(firstToSecond);
	edgeList1.add(secondToThird);
	DirectedWeightedGraphPath path1 = new DirectedWeightedGraphPath(graph, edgeList1);

	ArrayList<DirectedWeightedEdge> edgeList2 = new ArrayList<DirectedWeightedEdge>();
	edgeList2.add(firstToFourth);
	edgeList2.add(fourthToThird);
	DirectedWeightedGraphPath path2 = new DirectedWeightedGraphPath(graph, edgeList2);

	Assert.assertTrue(new GraphPathComparator().compare(path2, path1) > 0);
    }

    @Test
    public void testCompareEquality() {
	ArrayList<DirectedWeightedEdge> edgeList1 = new ArrayList<DirectedWeightedEdge>();
	edgeList1.add(firstToSecond);
	edgeList1.add(firstToSecond);
	edgeList1.add(secondToThird);
	DirectedWeightedGraphPath path1 = new DirectedWeightedGraphPath(graph, edgeList1);

	ArrayList<DirectedWeightedEdge> edgeList2 = new ArrayList<DirectedWeightedEdge>();
	edgeList2.add(firstToFourth);
	edgeList2.add(fourthToThird);
	DirectedWeightedGraphPath path2 = new DirectedWeightedGraphPath(graph, edgeList2);

	Assert.assertTrue(new GraphPathComparator().compare(path2, path1) == 0);
    }

    @Test
    public void testCompareEmptyPathLowerThan() {
	ArrayList<DirectedWeightedEdge> edgeList1 = new ArrayList<DirectedWeightedEdge>();
	edgeList1.add(firstToSecond);
	edgeList1.add(secondToThird);
	DirectedWeightedGraphPath path1 = new DirectedWeightedGraphPath(graph, edgeList1);

	ArrayList<DirectedWeightedEdge> edgeList2 = new ArrayList<DirectedWeightedEdge>();
	DirectedWeightedGraphPath path2 = new DirectedWeightedGraphPath(graph, edgeList2);

	Assert.assertTrue(new GraphPathComparator().compare(path1, path2) == Integer.MIN_VALUE);
    }

    @Test
    public void testCompareEmptyPathLargerThan() {
	ArrayList<DirectedWeightedEdge> edgeList1 = new ArrayList<DirectedWeightedEdge>();
	DirectedWeightedGraphPath path1 = new DirectedWeightedGraphPath(graph, edgeList1);

	ArrayList<DirectedWeightedEdge> edgeList2 = new ArrayList<DirectedWeightedEdge>();
	edgeList2.add(firstToSecond);
	edgeList2.add(secondToThird);
	DirectedWeightedGraphPath path2 = new DirectedWeightedGraphPath(graph, edgeList2);

	Assert.assertTrue(new GraphPathComparator().compare(path1, path2) == Integer.MAX_VALUE);
    }

    @Test
    public void testCompareEmptyPaths() {
	ArrayList<DirectedWeightedEdge> edgeList1 = new ArrayList<DirectedWeightedEdge>();
	DirectedWeightedGraphPath path1 = new DirectedWeightedGraphPath(graph, edgeList1);

	ArrayList<DirectedWeightedEdge> edgeList2 = new ArrayList<DirectedWeightedEdge>();
	DirectedWeightedGraphPath path2 = new DirectedWeightedGraphPath(graph, edgeList2);

	Assert.assertTrue(new GraphPathComparator().compare(path1, path2) == 0);
    }

    @Test
    public void testCompareDifferentClass() {
	ArrayList<DirectedWeightedEdge> edgeList1 = new ArrayList<DirectedWeightedEdge>();
	DirectedWeightedGraphPath path1 = new DirectedWeightedGraphPath(graph, edgeList1);

	Object secondPath = new Object();

	Assert.assertTrue(new GraphPathComparator().compare(path1, secondPath) == 0);
	Assert.assertTrue(new GraphPathComparator().compare(secondPath, path1) == 0);
    }
}
