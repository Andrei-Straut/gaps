
package com.andreistraut.gaps.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpServletResponse;

public class MessageResponse {

	private int callbackId;
	private int status;
	private String description;
	private boolean isEnded;
	private JsonElement data;

	public MessageResponse(int callbackId) {
	    this.callbackId = callbackId;
	    this.status = HttpServletResponse.SC_OK;
	    this.isEnded = true;
	    this.description = "Ok";
	}

	public MessageResponse(int callbackId, boolean isEnded, JsonElement data) {
	    this(callbackId);
	    this.status = HttpServletResponse.SC_OK;
	    this.isEnded = isEnded;
	    this.description = "Ok";
	    this.data = data;
	}

	public MessageResponse(int callbackId, int status, boolean isEnded, String description) {
	    this(callbackId);
	    this.status = status;
	    this.isEnded = isEnded;
	    this.description = description;
	}

	public MessageResponse(int callbackId, int status, boolean isEnded, String description, JsonElement data) {
	    this(callbackId);
	    this.status = status;
	    this.isEnded = isEnded;
	    this.description = description;
	    this.data = data;
	}

	public int getStatus() {
	    return this.status;
	}

	public MessageResponse setStatus(int status) {
	    this.status = status;
	    return this;
	}

	public MessageResponse setDescription(String description) {
	    this.description = description;
	    return this;
	}

	public MessageResponse setData(JsonElement data) {
	    this.data = data;
	    return this;
	}

	public boolean isEnded() {
	    return isEnded;
	}

	public MessageResponse setIsEnded(boolean isEnded) {
	    this.isEnded = isEnded;
	    return this;
	}

	public String toJsonString() {
	    return this.toJson().toString();
	}

	public JsonObject toJson() {
	    JsonObject response = new JsonObject();

	    response.addProperty("callback_id", this.callbackId);
	    response.addProperty("status", this.status);
	    response.addProperty("isEnded", this.isEnded);
	    response.addProperty("description", this.description);
	    response.add("data", this.data);

	    return response;
	}
    }