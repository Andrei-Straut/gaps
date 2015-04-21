package com.andreistraut.gaps.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageRequestTest {

    public MessageRequestTest() {
    }

    @Before
    public void setUp() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(MessageRequestTest.class.getName()).log(Level.INFO,
		"{0} TEST: MessageRequest",
		MessageRequestTest.class.toString());
    }

    @Test
    public void testNewRequestValidString() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", 0);
	requestJson.addProperty("type", "GetGraph");
	requestJson.add("data", new JsonObject());
	
	String requestString = requestJson.toString();

	MessageRequest request = new MessageRequest(requestString);
	Assert.assertTrue(request.getCallbackId() == 0);
	Assert.assertTrue(request.getType() == MessageType.GETGRAPH);
	Assert.assertTrue(request.getData().isJsonObject());
    }

    @Test
    public void testNewRequestValidJsonCallbackIdInt() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", 0);
	requestJson.addProperty("type", "GetGraph");
	requestJson.add("data", new JsonObject());

	MessageRequest request = new MessageRequest(requestJson);
	Assert.assertTrue(request.getCallbackId() == 0);
	Assert.assertTrue(request.getType() == MessageType.GETGRAPH);
	Assert.assertTrue(request.getData().isJsonObject());
    }

    @Test
    public void testNewRequestValidJsonCallbackIdString() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", "3");
	requestJson.addProperty("type", "GetGraph");
	requestJson.add("data", new JsonObject());

	MessageRequest request = new MessageRequest(requestJson);
	Assert.assertTrue(request.getCallbackId() == 3);
	Assert.assertTrue(request.getType() == MessageType.GETGRAPH);
	Assert.assertTrue(request.getData().isJsonObject());
    }

    @Test
    public void testNewRequestInvalidCallbackIdString() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", "asdf");
	requestJson.addProperty("type", "GetGraph");
	requestJson.add("data", new JsonObject());

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (NumberFormatException e) {
	    Assert.assertTrue(true);
	}
    }

    @Test
    public void testNewRequestInvalidCallbackIdMissing() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("type", "GetGraph");
	requestJson.add("data", new JsonObject());

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().contains("Request must contain a callback_id"));
	}
    }

    @Test
    public void testNewRequestInvalidMessageTypeNonExistent() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", 0);
	requestJson.addProperty("type", "asdf");
	requestJson.add("data", new JsonObject());

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (IllegalArgumentException e) {
	    Assert.assertTrue(true);
	}
    }

    @Test
    public void testNewRequestInvalidMessageTypeMissing() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", 0);
	requestJson.add("data", new JsonObject());

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().contains("Request must contain a message type"));
	}
    }

    @Test
    public void testNewRequestInvalidDataJsonArray() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", 0);
	requestJson.addProperty("type", "GetGraph");
	requestJson.add("data", new JsonArray());

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (IllegalStateException e) {
	    Assert.assertTrue(true);
	}
    }

    @Test
    public void testNewRequestInvalidDataNull() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", 0);
	requestJson.addProperty("type", "GetGraph");
	requestJson.add("data", null);

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (IllegalStateException e) {
	    Assert.assertTrue(true);
	}
    }

    @Test
    public void testNewRequestInvalidDataMissing() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", 0);
	requestJson.addProperty("type", "GetGraph");

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().contains("Request must contain a request data (even if empty)"));
	}
    }

    @Test
    public void testNewRequestInvalidEmptyJson() {
	JsonObject requestJson = new JsonObject();

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().equals("Request must contain a callback_id"));
	}
    }

    @Test
    public void testNewRequestInvalidNull() {
	JsonObject requestJson = null;

	try {
	    MessageRequest request = new MessageRequest(requestJson);
	} catch (NullPointerException e) {
	    Assert.assertTrue(e.getMessage().equals("Request cannot be empty"));
	}
    }

    @Test
    public void testNewRequestInvalidString() {
	String requestString = "REQUEST_TEST";

	try {
	    MessageRequest request = new MessageRequest(requestString);
	} catch (IllegalStateException e) {
	    Assert.assertTrue(true);
	}
    }

    @Test
    public void testToJson() {
	JsonObject requestJson = new JsonObject();
	requestJson.addProperty("callback_id", 0);
	requestJson.addProperty("type", "GetGraph");
	requestJson.add("data", new JsonObject());

	MessageRequest request = new MessageRequest(requestJson);
	
	JsonObject toJson = request.toJson();
	Assert.assertTrue(toJson.get("callback_id").getAsInt() == 0);
	Assert.assertTrue(toJson.get("type").getAsString().equals(MessageType.GETGRAPH.toString()));
	Assert.assertTrue(toJson.has("data"));
    }
}
