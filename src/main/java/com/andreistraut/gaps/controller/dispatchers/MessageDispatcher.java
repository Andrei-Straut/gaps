
package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import java.util.ArrayList;
import javax.websocket.Session;

public abstract class MessageDispatcher {
    private final Controller controller;
    private final Session session;
    private final MessageType type;
    protected MessageRequest request;
    protected ArrayList<Object> parameters;
    
     public MessageDispatcher(Controller controller, Session session, MessageType type) {
	 this.controller = controller;
	 this.session = session;
	 this.type = type;
     }
     
    /**
     * Validates the MessageRequest given to this class for processing, and sets
     * it as a property if valid
     * 
     * @param request The MessageRequest to process
     * 
     * @return True if request is valid, false otherwise, Exception for malformed
     * request
     * @throws java.lang.Exception
     */
    public abstract boolean setRequest(MessageRequest request) throws Exception;
    
    /**
     * If there are additional parameters the class needs to do its job, init them here
     * 
     * @param parameters Additional parameters / preconditions the dispatcher may need 
     * @throws java.lang.Exception 
     */
    public abstract void setParameters(ArrayList<Object> parameters) throws Exception;
    public abstract boolean process() throws Exception;
    protected abstract void updateProgress(MessageResponse response);
}
