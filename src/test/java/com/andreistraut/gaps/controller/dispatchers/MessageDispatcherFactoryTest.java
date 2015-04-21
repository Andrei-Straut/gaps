package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageType;
import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
    }

    @Test
    public void testInitDispatcherRequest() throws Exception {
    }

    @Test
    public void testInitDispatcherParams() throws Exception {
    }

    @Test
    public void testProcess() throws Exception {
    }
    
}
