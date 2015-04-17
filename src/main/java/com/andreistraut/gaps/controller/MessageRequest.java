
package com.andreistraut.gaps.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MessageRequest {

    private int callbackId;
    private MessageType type;
    private JsonObject data;

    public MessageRequest(String request) throws JsonSyntaxException {
	JsonObject requestJson = (new JsonParser()).parse(request).getAsJsonObject();
	this.fromJson(requestJson);
    }

    public MessageRequest(JsonObject requestJson) {
	this.fromJson(requestJson);
    }

    private void fromJson(JsonObject json) {
	this.callbackId = json.get("callback_id").getAsInt();
	this.type = MessageType.valueOf(json.get("type").getAsString());
	this.data = json.get("data").getAsJsonObject();
    }

    public int getCallbackId() {
	return callbackId;
    }

    public MessageType getType() {
	return this.type;
    }

    public JsonObject getData() {
	return data;
    }

    public String toJsonString() {
	return this.toJson().toString();
    }

    public JsonObject toJson() {
	JsonObject request = new JsonObject();

	request.addProperty("callback_id", this.callbackId);
	request.addProperty("messageType", this.type.toString());
	request.add("data", this.data);

	return request;
    }
}