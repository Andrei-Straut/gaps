package com.andreistraut.pathsearch.datamodel.graph;

import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectedWeightedEdgeTest {

    private Node source;
    private Node destination;

    public DirectedWeightedEdgeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	Logger.getLogger(DirectedWeightedGraph.class.getName()).log(Level.INFO,
		NodeTest.class.toString() + " TEST: Edge");

	source = new Node("1", "1");
	destination = new Node("2", "2");
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetSource() {
	DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination);
	Assert.assertEquals(source, edge.getSource());
    }

    @Test
    public void testSetSource() {
	DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination);
	edge.setSource(destination);
	Assert.assertEquals(destination, edge.getSource());
    }

    @Test
    public void testGetDestination() {
	DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination);
	Assert.assertEquals(destination, edge.getDestination());
    }

    @Test
    public void testSetDestination() {
	DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination);
	edge.setDestination(source);
	Assert.assertEquals(source, edge.getDestination());
    }

    @Test
    public void testIsDirected() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	Assert.assertEquals(true, first.isDirected());

	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination, 100, false);
	Assert.assertEquals(false, second.isDirected());
    }

    @Test
    public void testSetIsDirected() {
	DirectedWeightedEdge edge = new DirectedWeightedEdge(source, destination, 100, true);
	Assert.assertEquals(true, edge.isDirected());

	edge.setIsDirected(false);
	Assert.assertEquals(false, edge.isDirected());
    }

    @Test
    public void testSetCost() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 100);
	Assert.assertEquals(100, first.getCost());

	first.setCost(101);
	Assert.assertEquals(101, first.getCost());
    }

    @Test
    public void testEquals() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));

	first = new DirectedWeightedEdge(source, destination, 50);
	second = new DirectedWeightedEdge(source, destination, 50);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));

	first = new DirectedWeightedEdge(source, destination, 50, true);
	second = new DirectedWeightedEdge(source, destination, 50, true);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));

	first.setSource(destination);
	second.setSource(destination);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));

	first.setDestination(source);
	second.setDestination(source);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));

	first.setCost(100);
	second.setCost(100);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));

	first.setIsDirected(true);
	second.setIsDirected(true);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 100, true);
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, destination, 101, true);
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, destination, 100, false);
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, false);
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));

	first = new DirectedWeightedEdge(destination, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, false);
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));
    }

    @Test
    public void testHashCode() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);
	Assert.assertEquals(first.hashCode(), second.hashCode());

	first = new DirectedWeightedEdge(source, destination, 50);
	second = new DirectedWeightedEdge(source, destination, 50);
	Assert.assertEquals(first.hashCode(), second.hashCode());

	first = new DirectedWeightedEdge(source, destination, 50, true);
	second = new DirectedWeightedEdge(source, destination, 50, true);
	Assert.assertEquals(first.hashCode(), second.hashCode());

	first.setSource(destination);
	second.setSource(destination);
	Assert.assertTrue(first.hashCode() == second.hashCode());
	Assert.assertTrue(second.hashCode() == first.hashCode());

	first.setDestination(source);
	second.setDestination(source);
	Assert.assertTrue(first.hashCode() == second.hashCode());
	Assert.assertTrue(second.hashCode() == first.hashCode());

	first.setCost(100);
	second.setCost(100);
	Assert.assertTrue(first.hashCode() == second.hashCode());
	Assert.assertTrue(second.hashCode() == first.hashCode());

	first.setIsDirected(true);
	second.setIsDirected(true);
	Assert.assertTrue(first.hashCode() == second.hashCode());
	Assert.assertTrue(second.hashCode() == first.hashCode());

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 100, true);
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, destination, 101, true);
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, destination, 100, false);
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, false);
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());

	first = new DirectedWeightedEdge(destination, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, false);
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());
    }

    @Test
    public void testToString() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);
	Assert.assertEquals(first.toString(), second.toString());

	first = new DirectedWeightedEdge(source, destination, 50);
	second = new DirectedWeightedEdge(source, destination, 50);
	Assert.assertEquals(first.toString(), second.toString());

	first = new DirectedWeightedEdge(source, destination, 50, true);
	second = new DirectedWeightedEdge(source, destination, 50, true);
	Assert.assertEquals(first.toString(), second.toString());

	first.setSource(destination);
	second.setSource(destination);
	Assert.assertEquals(first.toString(), second.toString());

	first.setDestination(source);
	second.setDestination(source);
	Assert.assertTrue(first.toString().equals(second.toString()));
	Assert.assertTrue(second.toString().equals(first.toString()));

	first.setCost(100);
	second.setCost(100);
	Assert.assertTrue(first.toString().equals(second.toString()));
	Assert.assertTrue(second.toString().equals(first.toString()));

	first.setIsDirected(true);
	second.setIsDirected(true);
	Assert.assertTrue(first.toString().equals(second.toString()));
	Assert.assertTrue(second.toString().equals(first.toString()));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 100, true);
	Assert.assertFalse(first.toString().equals(second.toString()));
	Assert.assertFalse(second.toString().equals(first.toString()));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, destination, 101, true);
	Assert.assertFalse(first.toString().equals(second.toString()));
	Assert.assertFalse(second.toString().equals(first.toString()));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, destination, 100, false);
	Assert.assertFalse(first.toString().equals(second.toString()));
	Assert.assertFalse(second.toString().equals(first.toString()));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, false);
	Assert.assertFalse(first.toString().equals(second.toString()));
	Assert.assertFalse(second.toString().equals(first.toString()));

	first = new DirectedWeightedEdge(destination, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, false);
	Assert.assertFalse(first.toString().equals(second.toString()));
	Assert.assertFalse(second.toString().equals(first.toString()));
    }

    @Test
    public void testToJson() {
	DirectedWeightedEdge first;
	DirectedWeightedEdge second;
	JsonObject firstJson;
	JsonObject secondJson;
	
	first = new DirectedWeightedEdge(source, destination, 100, true);
	firstJson = first.toJson();
	Assert.assertTrue(source.getId().equals(firstJson.get("nodeFrom").getAsString()));
	Assert.assertTrue(destination.getId().equals(firstJson.get("nodeTo").getAsString()));
	Assert.assertTrue(first.hashCode() == firstJson.get("data").getAsJsonObject().get("id").getAsInt());
	Assert.assertTrue(firstJson.get("data").getAsJsonObject().get("cost").getAsInt() == 100);
	Assert.assertTrue(firstJson.get("data").getAsJsonObject().get("isDirected").getAsBoolean() == true);

	first = new DirectedWeightedEdge(source, destination);
	second = new DirectedWeightedEdge(source, destination);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertEquals(firstJson, secondJson);

	first = new DirectedWeightedEdge(source, destination, 100);
	second = new DirectedWeightedEdge(source, destination, 100);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertEquals(firstJson, secondJson);

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, destination, 100, true);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertEquals(firstJson, secondJson);

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(destination, source, 100, true);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertFalse(firstJson.equals(secondJson));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 100, true);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertFalse(firstJson.equals(secondJson));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, true);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertFalse(firstJson.equals(secondJson));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, false);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertFalse(firstJson.equals(secondJson));
    }

}
