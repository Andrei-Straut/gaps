package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

public class GraphSettingsTest {

    public GraphSettingsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(GraphSettingsTest.class.getName()).log(Level.INFO,
		"{0} TEST: GraphSettings",
		GraphSettingsTest.class.toString());
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testConstructorIntValues() {
	GraphSettings settings = new GraphSettings(10, 10);

	Assert.assertTrue(settings.getNumberOfNodes() == 10);
	Assert.assertTrue(settings.getNumberOfEdges() == 10);
	Assert.assertTrue(settings.getMinimumEdgeWeight() == 1);
	Assert.assertTrue(settings.getMaximumEdgeWeight() == 1);
	Assert.assertTrue(settings.isStatic());
    }

    @Test
    public void testConstructorJsonObjectValid() throws Exception {
	JsonObject settingsJson = new JsonObject();
	settingsJson.addProperty("numberOfNodes", 15);
	settingsJson.addProperty("numberOfEdges", 25);
	settingsJson.addProperty("minimumEdgeWeight", 10);
	settingsJson.addProperty("maximumEdgeWeight", 100);
	settingsJson.addProperty("isStatic", true);

	GraphSettings settings = new GraphSettings(settingsJson);

	Assert.assertTrue(settings.getNumberOfNodes() == 15);
	Assert.assertTrue(settings.getNumberOfEdges() == 25);
	Assert.assertTrue(settings.getMinimumEdgeWeight() == 10);
	Assert.assertTrue(settings.getMaximumEdgeWeight() == 100);
	Assert.assertTrue(settings.isStatic());
    }

    @Test
    public void testConstructorJsonObjectDefaultValueMinEdgeWeight() throws Exception {
	JsonObject settingsJson = new JsonObject();
	settingsJson.addProperty("numberOfNodes", 15);
	settingsJson.addProperty("numberOfEdges", 25);
	settingsJson.addProperty("maximumEdgeWeight", 100);
	settingsJson.addProperty("isStatic", true);

	GraphSettings settings = new GraphSettings(settingsJson);
	Assert.assertTrue(settings.getMinimumEdgeWeight() == 1);
    }

    @Test
    public void testConstructorJsonObjectDefaultValueMaxEdgeWeight() throws Exception {
	JsonObject settingsJson = new JsonObject();
	settingsJson.addProperty("numberOfNodes", 15);
	settingsJson.addProperty("numberOfEdges", 25);
	settingsJson.addProperty("minimumEdgeWeight", 100);
	settingsJson.addProperty("isStatic", true);

	GraphSettings settings = new GraphSettings(settingsJson);
	Assert.assertTrue(settings.getMaximumEdgeWeight() == 1);
    }

    @Test
    public void testConstructorJsonObjectDefaultValueIsStatic() throws Exception {
	JsonObject settingsJson = new JsonObject();
	settingsJson.addProperty("numberOfNodes", 15);
	settingsJson.addProperty("numberOfEdges", 25);
	settingsJson.addProperty("minimumEdgeWeight", 100);
	settingsJson.addProperty("maximumEdgeWeight", 100);

	GraphSettings settings = new GraphSettings(settingsJson);
	Assert.assertTrue(settings.isStatic());
    }

    @Test
    public void testConstructorJsonObjectDefaultValues() throws Exception {
	JsonObject settingsJson = new JsonObject();
	settingsJson.addProperty("numberOfNodes", 15);
	settingsJson.addProperty("numberOfEdges", 25);

	GraphSettings settings = new GraphSettings(settingsJson);
	Assert.assertTrue(settings.getMinimumEdgeWeight() == 1);
	Assert.assertTrue(settings.getMaximumEdgeWeight() == 1);
	Assert.assertTrue(settings.isStatic());
    }

    @Test
    public void testConstructorJsonObjectInvalidMissingNumberOfNodes() {
	JsonObject settingsJson = new JsonObject();
	settingsJson.addProperty("numberOfEdges", 25);
	settingsJson.addProperty("minimumEdgeWeight", 10);
	settingsJson.addProperty("maximumEdgeWeight", 100);
	settingsJson.addProperty("isStatic", true);

	try {
	    GraphSettings settings = new GraphSettings(settingsJson);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Number of nodes and number of edges must always be specified"));
	}
    }

    @Test
    public void testConstructorJsonObjectInvalidMissingNumberOfEdges() {
	JsonObject settingsJson = new JsonObject();
	settingsJson.addProperty("numberOfNodes", 25);
	settingsJson.addProperty("minimumEdgeWeight", 10);
	settingsJson.addProperty("maximumEdgeWeight", 100);
	settingsJson.addProperty("isStatic", true);

	try {
	    GraphSettings settings = new GraphSettings(settingsJson);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Number of nodes and number of edges must always be specified"));
	}
    }

    @Test
    public void testConstructorJsonObjectInvalidMissingNumberOfNodesNumberOfEdges() {
	JsonObject settingsJson = new JsonObject();
	settingsJson.addProperty("minimumEdgeWeight", 10);
	settingsJson.addProperty("maximumEdgeWeight", 100);
	settingsJson.addProperty("isStatic", true);

	try {
	    GraphSettings settings = new GraphSettings(settingsJson);
	} catch (Exception e) {
	    Assert.assertTrue(e.getMessage().contains("Number of nodes and number of edges must always be specified"));
	}
    }

    @Test
    public void testToJsonWithConstructorIntegerValues() {
	GraphSettings settings = new GraphSettings(10, 10);
	
	JsonObject settingsJson = settings.toJson();
	Assert.assertTrue(settingsJson.get("numberOfNodes").getAsInt() == 10);
	Assert.assertTrue(settingsJson.get("numberOfEdges").getAsInt() == 10);
	Assert.assertTrue(settingsJson.get("minimumEdgeWeight").getAsInt() == 1);
	Assert.assertTrue(settingsJson.get("maximumEdgeWeight").getAsInt() == 1);
	Assert.assertTrue(settingsJson.get("isStatic").getAsBoolean());
    }

    @Test
    public void testToJsonWithConstructorDefaultIntegerValues() {
	GraphSettings settings = new GraphSettings(10, 10);
	settings.setMinimumEdgeWeight(100);
	settings.setMaximumEdgeWeight(150);
	settings.setIsStatic(false);
	
	JsonObject settingsJson = settings.toJson();
	Assert.assertTrue(settingsJson.get("numberOfNodes").getAsInt() == 10);
	Assert.assertTrue(settingsJson.get("numberOfEdges").getAsInt() == 10);
	Assert.assertTrue(settingsJson.get("minimumEdgeWeight").getAsInt() == 100);
	Assert.assertTrue(settingsJson.get("maximumEdgeWeight").getAsInt() == 150);
	Assert.assertFalse(settingsJson.get("isStatic").getAsBoolean());
    }

    @Test
    public void testToJsonWithConstructorJsonObject() throws Exception {
	JsonObject settingsJsonInit = new JsonObject();
	settingsJsonInit.addProperty("numberOfNodes", 15);
	settingsJsonInit.addProperty("numberOfEdges", 25);
	settingsJsonInit.addProperty("minimumEdgeWeight", 10);
	settingsJsonInit.addProperty("maximumEdgeWeight", 100);
	settingsJsonInit.addProperty("isStatic", true);
	GraphSettings settings = new GraphSettings(settingsJsonInit);
	
	JsonObject settingsJsonToJson = settings.toJson();
	Assert.assertTrue(settingsJsonInit.equals(settingsJsonToJson));
    }
}
