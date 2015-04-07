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
import static org.junit.Assert.*;

public class NodeTest {

    public NodeTest() {
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
		NodeTest.class.toString() + " TEST: Node");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetId() {
	Node node = new Node("Node1", "Node1");
	node.setId("Node2");
	assertEquals("Node2", node.getId());
    }

    @Test
    public void testSetName() {
	Node node = new Node("Node1", "Node1");
	node.setName("Node2");
	assertEquals("Node2", node.getName());
    }

    @Test
    public void testHashCode() {
	Node first = new Node("Node1", "Node1");
	Node second = new Node("Node1", "Node1");
	assertEquals(second.hashCode(), first.hashCode());

	first.setId("Node2");
	first.setName("Node2");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());

	first.setId("Node2");
	first.setName("Node1");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());

	first.setId("Node1");
	first.setName("Node2");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());
    }

    @Test
    public void testEquals() {
	Node first = new Node("Node1", "Node1");
	Node second = new Node("Node1", "Node1");
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));

	first.setId("Node2");
	first.setName("Node2");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));

	first.setId("Node2");
	first.setName("Node1");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));

	first.setId("Node1");
	first.setName("Node2");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));
    }

    @Test
    public void testToString() {
	Node first = new Node("Node1", "Node1");
	Node second = new Node("Node1", "Node1");
	assertEquals(second.toString(), first.toString());
	assertEquals(second.toString(), first.toString());

	first.setId("Node2");
	first.setName("Node2");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.toString().equals(second.toString()));
	Assert.assertFalse(second.toString().equals(first.toString()));

	first.setId("Node2");
	first.setName("Node1");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));

	first.setId("Node1");
	first.setName("Node2");
	second.setId("Node1");
	second.setId("Node1");
	Assert.assertFalse(first.toString().equals(second.toString()));
	Assert.assertFalse(second.toString().equals(first.toString()));
    }

    @Test
    public void testToJson() {
	Node first = new Node("Node1", "Node1");
	JsonObject nodeJson = first.toJson();
	assertEquals("Node1", nodeJson.get("id").getAsString());
	assertEquals("Node1", nodeJson.get("name").getAsString());

	first.setId("Node2");
	first.setName("Node2");
	nodeJson = first.toJson();
	assertEquals("Node2", nodeJson.get("id").getAsString());
	assertEquals("Node2", nodeJson.get("name").getAsString());

	first.setId("Node1");
	first.setName("Node1");
	nodeJson = first.toJson();
	Node second = new Node("Node1", "Node1");
	JsonObject secondJson = second.toJson();
	assertEquals(nodeJson, secondJson);

	first.setId("Node2");
	first.setName("Node2");
	nodeJson = first.toJson();
	second.setId("Node1");
	second.setName("Node1");
	secondJson = second.toJson();
	Assert.assertFalse(nodeJson.equals(secondJson));
	Assert.assertFalse(secondJson.equals(nodeJson));
    }

    @Test
    public void testClone() {
	Node node = new Node("Node1", "Node1");

	Node second = node.clone();
	Assert.assertFalse((node == second));
	Assert.assertTrue((node.equals(second)));
    }

}
