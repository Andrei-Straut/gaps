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

	Assert.assertTrue(firstJson.has("nodeFrom"));
	Assert.assertTrue(firstJson.has("nodeTo"));
	Assert.assertTrue(firstJson.has("data"));

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
