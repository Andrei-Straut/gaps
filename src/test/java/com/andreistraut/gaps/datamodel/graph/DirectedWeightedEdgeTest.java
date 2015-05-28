package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	Logger.getLogger(DirectedWeightedEdgeTest.class.getName()).log(Level.INFO, 
		"{0} TEST: DirectedWeightedEdge", 
		DirectedWeightedEdgeTest.class.toString());
    }

    @Before
    public void setUp() {
	source = new Node("1", "1");
	destination = new Node("2", "2");
    }

    @Test
    public void testEqualsTwoParamConstructor() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsThreeParamConstructor() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 50);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination, 50);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsFourParamConstructor() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 50, true);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination, 50, true);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsChangeViaConstructors() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 100, true);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, source, 100, true);
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
    public void testEqualsSourceChanged() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setSource(destination);
	second.setSource(destination);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsDestinationChanged() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setDestination(source);
	second.setDestination(source);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsCostChanged() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setCost(100);
	second.setCost(100);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsDirectedChanged() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setIsDirected(true);
	second.setIsDirected(true);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(second.equals(first));
    }

    @Test
    public void testEqualsNull() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = null;

	first.setIsDirected(true);
	Assert.assertFalse(first.equals(second));
    }

    @Test
    public void testEqualsDifferentObject() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	Object second = new Object();

	first.setIsDirected(true);
	Assert.assertFalse(first.equals(second));
    }

    @Test
    public void testHashCodeTwoParamConstructor() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);
	Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void testHashCodeThreeParamConstructor() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 50);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination, 50);
	Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void testHashCodeFourParamConstructor() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 50, true);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination, 50, true);
	Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void testHashCodeChangeViaConstructors() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 100, true);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, source, 100, true);
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
    public void testHashCodeChangeSource() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setSource(destination);
	second.setSource(destination);
	Assert.assertTrue(first.hashCode() == second.hashCode());
	Assert.assertTrue(second.hashCode() == first.hashCode());
    }

    @Test
    public void testHashCodeChangeDestination() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setDestination(source);
	second.setDestination(source);
	Assert.assertTrue(first.hashCode() == second.hashCode());
	Assert.assertTrue(second.hashCode() == first.hashCode());
    }

    @Test
    public void testHashCodeChangeCost() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setCost(100);
	second.setCost(100);
	Assert.assertTrue(first.hashCode() == second.hashCode());
	Assert.assertTrue(second.hashCode() == first.hashCode());
    }

    @Test
    public void testHashCodeChangeDirected() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setIsDirected(true);
	second.setIsDirected(true);
	Assert.assertTrue(first.hashCode() == second.hashCode());
	Assert.assertTrue(second.hashCode() == first.hashCode());
    }

    @Test
    public void testToStringTwoParamConstructor() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);
	Assert.assertEquals(first.toString(), second.toString());
    }

    @Test
    public void testToStringConstructors() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 50);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination, 50);
	Assert.assertEquals(first.toString(), second.toString());
    }

    @Test
    public void testToStringFourParamConstructor() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 50, true);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination, 50, true);
	Assert.assertEquals(first.toString(), second.toString());
    }

    @Test
    public void testToStringChangeViaConstructors() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination, 100, true);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, source, 100, true);
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
    public void testToStringSourceChanged() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setSource(destination);
	second.setSource(destination);
	Assert.assertEquals(first.toString(), second.toString());
    }

    @Test
    public void testToStringDestinationChanged() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setDestination(source);
	second.setDestination(source);
	Assert.assertTrue(first.toString().equals(second.toString()));
	Assert.assertTrue(second.toString().equals(first.toString()));
    }

    @Test
    public void testToStringCostChanged() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setCost(100);
	second.setCost(100);
	Assert.assertTrue(first.toString().equals(second.toString()));
	Assert.assertTrue(second.toString().equals(first.toString()));
    }

    @Test
    public void testToStringDirectedChanged() {
	DirectedWeightedEdge first = new DirectedWeightedEdge(source, destination);
	DirectedWeightedEdge second = new DirectedWeightedEdge(source, destination);

	first.setIsDirected(true);
	second.setIsDirected(true);
	Assert.assertTrue(first.toString().equals(second.toString()));
	Assert.assertTrue(second.toString().equals(first.toString()));
    }

    @Test
    public void testToJson() {
	DirectedWeightedEdge first;
	DirectedWeightedEdge second;
	JsonObject firstJson;
	JsonObject secondJson;

	first = new DirectedWeightedEdge(source, destination, 100, true);
	firstJson = first.toJson();

	Assert.assertTrue(firstJson.has("from"));
	Assert.assertTrue(firstJson.has("to"));

	Assert.assertTrue(source.getId().equals(firstJson.get("from").getAsString()));
	Assert.assertTrue(destination.getId().equals(firstJson.get("to").getAsString()));
	Assert.assertTrue(first.getCost() == firstJson.get("cost").getAsInt());
	Assert.assertTrue(first.isDirected() == firstJson.get("isDirected").getAsBoolean());

	first = new DirectedWeightedEdge(source, destination);
	second = new DirectedWeightedEdge(source, destination);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertEquals(firstJson.get("from").getAsString(), secondJson.get("from").getAsString());
	Assert.assertEquals(firstJson.get("to").getAsString(), secondJson.get("to").getAsString());
	Assert.assertEquals(firstJson.get("cost").getAsInt(), secondJson.get("cost").getAsInt());
	Assert.assertEquals(firstJson.get("isDirected").getAsBoolean(), secondJson.get("isDirected").getAsBoolean());

	first = new DirectedWeightedEdge(source, destination, 100);
	second = new DirectedWeightedEdge(source, destination, 100);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertEquals(firstJson.get("from").getAsString(), secondJson.get("from").getAsString());
	Assert.assertEquals(firstJson.get("to").getAsString(), secondJson.get("to").getAsString());
	Assert.assertEquals(firstJson.get("cost").getAsInt(), secondJson.get("cost").getAsInt());
	Assert.assertEquals(firstJson.get("isDirected").getAsBoolean(), secondJson.get("isDirected").getAsBoolean());

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, destination, 100, true);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertEquals(firstJson.get("from").getAsString(), secondJson.get("from").getAsString());
	Assert.assertEquals(firstJson.get("to").getAsString(), secondJson.get("to").getAsString());
	Assert.assertEquals(firstJson.get("cost").getAsInt(), secondJson.get("cost").getAsInt());
	Assert.assertEquals(firstJson.get("isDirected").getAsBoolean(), secondJson.get("isDirected").getAsBoolean());

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(destination, source, 100, true);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertFalse(firstJson.get("from").getAsString().equals(secondJson.get("from").getAsString()));
	Assert.assertFalse(firstJson.get("to").getAsString().equals(secondJson.get("to").getAsString()));
	Assert.assertTrue(firstJson.get("cost").getAsString().equals(secondJson.get("cost").getAsString()));
	Assert.assertTrue(firstJson.get("isDirected").getAsString().equals(secondJson.get("isDirected").getAsString()));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 100, true);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertTrue(firstJson.get("from").getAsString().equals(secondJson.get("from").getAsString()));
	Assert.assertFalse(firstJson.get("to").getAsString().equals(secondJson.get("to").getAsString()));
	Assert.assertTrue(firstJson.get("cost").getAsString().equals(secondJson.get("cost").getAsString()));
	Assert.assertTrue(firstJson.get("isDirected").getAsString().equals(secondJson.get("isDirected").getAsString()));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, true);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertTrue(firstJson.get("from").getAsString().equals(secondJson.get("from").getAsString()));
	Assert.assertFalse(firstJson.get("to").getAsString().equals(secondJson.get("to").getAsString()));
	Assert.assertFalse(firstJson.get("cost").getAsString().equals(secondJson.get("cost").getAsString()));
	Assert.assertTrue(firstJson.get("isDirected").getAsString().equals(secondJson.get("isDirected").getAsString()));

	first = new DirectedWeightedEdge(source, destination, 100, true);
	second = new DirectedWeightedEdge(source, source, 101, false);
	firstJson = first.toJson();
	secondJson = second.toJson();
	Assert.assertTrue(firstJson.get("from").getAsString().equals(secondJson.get("from").getAsString()));
	Assert.assertFalse(firstJson.get("to").getAsString().equals(secondJson.get("to").getAsString()));
	Assert.assertFalse(firstJson.get("cost").getAsString().equals(secondJson.get("cost").getAsString()));
	Assert.assertFalse(firstJson.get("isDirected").getAsString().equals(secondJson.get("isDirected").getAsString()));
    }
}
