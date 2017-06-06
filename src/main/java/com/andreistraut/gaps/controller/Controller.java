package com.andreistraut.gaps.controller;

import com.andreistraut.gaps.controller.dispatchers.MessageDispatcher;
import com.andreistraut.gaps.controller.dispatchers.MessageDispatcherFactory;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @ServerEndpoint handling the communication between the client and the server.
 *
 * Is the unique link and method of communication between the client interface
 * and the server
 */
@ServerEndpoint("/controller")
public class Controller {

    private MessageDispatcherFactory factory;

    /**
     * @param session
     * @OnOpen allows us to intercept the creation of a new session. The session
     * class allows us to send data to the user. In the method onOpen, we'll let
     * the user know that the handshake was successful.
     */
    @OnOpen
    public void onOpen(Session session) {
	MessageResponse response = new MessageResponse(0, HttpServletResponse.SC_OK, true, "Connection Established", null);

	try {
	    session.getBasicRemote().sendText(response.toJsonString());

	    Logger.getLogger(Controller.class.getName()).log(
		    Level.INFO, "{0}: {1}",
		    new Object[]{session.getId(), "Connection opened"});

	    this.factory = new MessageDispatcherFactory(this, session);

	    Logger.getLogger(Controller.class.getName()).log(
		    Level.INFO, "{0}: {1}",
		    new Object[]{session.getId(), "MessageDispatcherFactory initialized"});

	} catch (IOException e) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "{0}: Could not send message to client: ", new Object[]{session.getId(), e});
	    
	    HashMap<String, String> telemetryProperties = new HashMap<String, String>();
	    telemetryProperties.put("Session", session.getId());
	}
    }

    /**
     * When a user sends a message to the server, this method will intercept the
     * message and allow us to react to it. For now the message is read as a
     * String.
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
	Logger.getLogger(Controller.class.getName()).log(
		Level.INFO, "{0}: {1}",
		new Object[]{session.getId(), message});

	HashMap<String, String> telemetryProperties = new HashMap<String, String>();
	telemetryProperties.put("Session", session.getId());
	telemetryProperties.put("Request", message);
	

	MessageRequest request;
	MessageResponse response;
	try {
	    request = new MessageRequest(message);
	} catch (JsonSyntaxException e) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "{0}: Could not parse JSON request: {1}", new Object[]{session.getId(), e});

	    response = new MessageResponse(0);
	    response
		    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
		    .setIsEnded(true)
		    .setDescription("Error occurred processing message request: " + message);
	    respond(session, response);
	    return;
	}

	if (request.getType() == MessageType.UNKNOWN) {
	    Logger.getLogger(Controller.class.getName()).log(Level.WARNING,
		    "{0}: Message type unknown: {1}", new Object[]{session.getId(), message});

	    response = new MessageResponse(request.getCallbackId());
	    response
		    .setStatus(HttpServletResponse.SC_BAD_REQUEST)
		    .setIsEnded(true)
		    .setDescription("Message type unknown");
	    respond(session, response);
	    return;
	}

	MessageDispatcher messageDispatcher;
	try {
	    messageDispatcher = factory.getDispatcher(request.getType());
	    factory.initDispatcherRequest(messageDispatcher, request);
	    factory.initDispatcherParams(messageDispatcher);
	} catch (Exception e) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "{0}: Error initiating MessageDispatcher for session {1}",
		    new Object[]{session.getId(), e});

	    response = new MessageResponse(request.getCallbackId());
	    response
		    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
		    .setIsEnded(true)
		    .setDescription(e.getMessage());
	    respond(session, response);
	    return;
	}

	try {
	    factory.process(messageDispatcher);
	    Logger.getLogger(Controller.class.getName()).log(
		    Level.FINE, "{0}: Message processed: {1}", new Object[]{session.getId(), message});
	} catch (Exception e) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "{0}: Error processing MessageDispatcher: ",
		    new Object[]{session.getId(), e});

	    response = new MessageResponse(request.getCallbackId());
	    response
		    .setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
		    .setIsEnded(true)
		    .setDescription(e.getMessage());
	    respond(session, response);
	}

	Logger.getLogger(Controller.class.getName()).log(
		Level.INFO, "{0}: Idling", new Object[]{session.getId()});
    }

    /**
     * The user closes the connection.
     *
     * Note: you can't send messages to the client from this method
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
	if (this.factory != null) {
	    this.factory.release();
	}

	Logger.getLogger(Controller.class.getName()).log(Level.INFO,
		"{0}: session ended", session.getId());
    }

    public void respond(Session session, MessageResponse response) {
	try {
	    session.getBasicRemote().sendText(response.toJsonString());

	} catch (IOException e) {
	    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,
		    "{0}: Error occurred responding to request: {1}",
		    new Object[]{session.getId(), e});
	}
    }
}
