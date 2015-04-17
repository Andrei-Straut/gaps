
package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.controller.MessageType;
import java.util.ArrayList;
import javax.websocket.Session;

public abstract class MessageDispatcher {
    protected MessageRequest request;
    protected ArrayList<Object> parameters;
    
     public MessageDispatcher(Controller controller, Session session, MessageType type) {}
     
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
    abstract boolean setRequest(MessageRequest request) throws Exception;
    
    /**
     * If there are additional parameters the class needs to do its job, init them here
     * 
     * @param parameters Additional parameters / preconditions the dispatcher may need 
     * @throws java.lang.Exception 
     */
    abstract void setParameters(ArrayList<Object> parameters) throws Exception;
    abstract boolean process() throws Exception;
    abstract void updateProgress(MessageResponse response);
}
