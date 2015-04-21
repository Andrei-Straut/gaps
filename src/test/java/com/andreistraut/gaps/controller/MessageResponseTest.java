package com.andreistraut.gaps.controller;

import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageResponseTest {

    public MessageResponseTest() {
    }

    @Before
    public void setUp() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(MessageResponseTest.class.getName()).log(Level.INFO,
		"{0} TEST: MessageResponse",
		MessageResponseTest.class.toString());
    }

    @Test
    public void testNewMessageResponseCallbackId() {
	MessageResponse response = new MessageResponse(1);

	Assert.assertTrue(response.getCallbackId() == 1);
	Assert.assertTrue(response.getStatus() == HttpServletResponse.SC_OK);
	Assert.assertTrue(response.getDescription().equals("Ok"));
	Assert.assertTrue(response.isEnded());
	Assert.assertTrue(response.getData().isJsonObject());
    }

    @Test
    public void testNewMessageResponseCallbackIdIsEndedData() {
	MessageResponse response = new MessageResponse(1, true, new JsonObject());

	Assert.assertTrue(response.getCallbackId() == 1);
	Assert.assertTrue(response.getStatus() == HttpServletResponse.SC_OK);
	Assert.assertTrue(response.getDescription().equals("Ok"));
	Assert.assertTrue(response.isEnded());
	Assert.assertTrue(response.getData().isJsonObject());
    }

    @Test
    public void testNewMessageResponseCallbackIdIsEndedDescription() {
	MessageResponse response = new MessageResponse(1, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, true, "Error");

	Assert.assertTrue(response.getCallbackId() == 1);
	Assert.assertTrue(response.getStatus() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	Assert.assertTrue(response.getDescription().equals("Error"));
	Assert.assertTrue(response.isEnded());
	Assert.assertTrue(response.getData().isJsonObject());
    }

    @Test
    public void testNewMessageResponseCallbackIdIsEndedDescriptionData() {
	JsonObject dataJson = new JsonObject();
	dataJson.addProperty("dataTestField", "DataTestMessage");
	MessageResponse response = new MessageResponse(1, HttpServletResponse.SC_OK, true, "Ok", dataJson);

	Assert.assertTrue(response.getCallbackId() == 1);
	Assert.assertTrue(response.getStatus() == HttpServletResponse.SC_OK);
	Assert.assertTrue(response.getDescription().equals("Ok"));
	Assert.assertTrue(response.isEnded());
	Assert.assertTrue(response.getData().isJsonObject());

	Assert.assertTrue(response.getData().getAsJsonObject().has("dataTestField"));
	Assert.assertTrue(response.getData().getAsJsonObject().get("dataTestField").getAsString().equals("DataTestMessage"));
    }

    @Test
    public void testNewMessageResponseSetter() {
	JsonObject dataJson = new JsonObject();
	dataJson.addProperty("dataTestField", "DataTestMessage");
	MessageResponse response = new MessageResponse(1)
		.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
		.setDescription("Error")
		.setIsEnded(false)
		.setData(dataJson);

	Assert.assertTrue(response.getCallbackId() == 1);
	Assert.assertTrue(response.getStatus() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	Assert.assertTrue(response.getDescription().equals("Error"));
	Assert.assertTrue(response.isEnded() == false);
	Assert.assertTrue(response.getData().isJsonObject());

	Assert.assertTrue(response.getData().getAsJsonObject().has("dataTestField"));
	Assert.assertTrue(response.getData().getAsJsonObject().get("dataTestField").getAsString().equals("DataTestMessage"));
    }

    @Test
    public void testToJsonValidResponse() {
	JsonObject dataJson = new JsonObject();
	dataJson.addProperty("dataTestField", "DataTestMessage");
	MessageResponse response = new MessageResponse(1, HttpServletResponse.SC_OK, false, "Ok", dataJson);
	
	JsonObject responseJson = response.toJson();

	Assert.assertTrue(responseJson.has("callback_id"));
	Assert.assertTrue(responseJson.has("status"));
	Assert.assertTrue(responseJson.has("isEnded"));
	Assert.assertTrue(responseJson.has("description"));
	Assert.assertTrue(responseJson.has("data"));	
	
	Assert.assertTrue(responseJson.get("callback_id").getAsInt() == 1);
	Assert.assertTrue(responseJson.get("status").getAsInt() == HttpServletResponse.SC_OK);
	Assert.assertTrue(responseJson.get("isEnded").getAsBoolean() == false);
	Assert.assertTrue(responseJson.get("description").getAsString().equals("Ok"));
	Assert.assertTrue(responseJson.get("data").getAsJsonObject().get("dataTestField").getAsString().equals("DataTestMessage"));
    }
}
