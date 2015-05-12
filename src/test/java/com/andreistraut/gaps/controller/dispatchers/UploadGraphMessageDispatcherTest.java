package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraph;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class UploadGraphMessageDispatcherTest {
    
    public UploadGraphMessageDispatcherTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(UploadGraphMessageDispatcherTest.class.getName()).log(Level.INFO,
		"{0} TEST: UploadGraphMessageDispatcher",
		UploadGraphMessageDispatcherTest.class.toString());
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testSetRequest() throws Exception {
    }

    @Test
    public void testSetParameters() throws Exception {
    }

    @Test
    public void testProcess() throws Exception {
    }

    @Test
    public void testGetGraph() {
    }
    
}
