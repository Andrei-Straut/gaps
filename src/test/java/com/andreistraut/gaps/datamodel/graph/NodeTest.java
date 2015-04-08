package com.andreistraut.gaps.datamodel.graph;

import com.andreistraut.gaps.datamodel.graph.Node;
import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

public class NodeTest {

    Node first;
    Node second;

    public NodeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(NodeTest.class.getName()).log(Level.INFO,
		NodeTest.class.toString() + " TEST: Node");
    }

    @Before
    public void setUp() {
	first = new Node("Node1", "Node1");
	second = new Node("Node2", "Node2");
    }

    @Test
    public void testHashCodeEquality() {
	first.setId("Node1");
	first.setName("Node1");
	second.setId("Node1");
	second.setName("Node1");
	assertEquals(second.hashCode(), first.hashCode());
    }

    @Test
    public void testHashCodeDifferentId() {
	first.setId("Node2");
	second.setId("Node1");
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());
    }

    @Test
    public void testHashCodeDifferentName() {
	first.setName("Node2");
	second.setName("Node1");
	Assert.assertFalse(first.hashCode() == second.hashCode());
	Assert.assertFalse(second.hashCode() == first.hashCode());
    }

    @Test
    public void testEqualsEquality() {
	first.setId("Node1");
	first.setName("Node1");
	second.setId("Node1");
	second.setName("Node1");
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsDifferentId() {
	first.setId("Node2");
	second.setId("Node1");
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));
    }

    @Test
    public void testEqualsDifferentName() {
	first.setName("Node1");
	second.setName("Node2");
	Assert.assertFalse(first.equals(second));
	Assert.assertFalse(second.equals(first));
    }

    @Test
    public void testToStringEquality() {
	first.setId("Node1");
	first.setName("Node1");
	second.setId("Node1");
	second.setName("Node1");
	assertEquals(second.toString(), first.toString());
	assertEquals(first.toString(), first.toString());
    }

    @Test
    public void testToStringDifferentId() {
	first.setId("Node2");
	first.setName("Node1");
	second.setId("Node1");
	second.setName("Node1");
	assertFalse(first.toString().equals(second.toString()));
	assertFalse(second.toString().equals(first.toString()));
    }

    @Test
    public void testToStringDifferentName() {
	first.setId("Node1");
	first.setName("Node2");
	second.setId("Node1");
	second.setName("Node1");
	assertFalse(first.toString().equals(second.toString()));
	assertFalse(second.toString().equals(first.toString()));
    }

    @Test
    public void testToJson() {
	first = new Node("Node1", "Node1");
	JsonObject nodeJson = first.toJson();

	Assert.assertTrue(nodeJson.has("id"));
	Assert.assertTrue(nodeJson.has("name"));
	Assert.assertTrue(nodeJson.has("data"));

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
