package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.Controller;
import com.andreistraut.gaps.controller.MessageType;
import com.andreistraut.gaps.datamodel.mock.SessionMock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.BeforeClass;

public class MessageDispatcherFactoryTest {
    
    public MessageDispatcherFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(MessageDispatcherFactoryTest.class.getName()).log(Level.INFO, 
		"{0} TEST: MessageDispatcherFactory", 
		MessageDispatcherFactoryTest.class.toString());    
    }

    @Test
    public void testGetDispatcherGetGraph() throws Exception {
	Controller controller = new Controller();
	Session session = new SessionMock().getSession();
	
	MessageDispatcherFactory factory = new MessageDispatcherFactory(controller, session);
	MessageDispatcher graphDispatcher = factory.getDispatcher(MessageType.GetGraph);
	
	Assert.assertTrue(graphDispatcher instanceof GetGraphMessageDispatcher);
    }    
}
