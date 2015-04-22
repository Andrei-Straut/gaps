/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andreistraut.gaps.controller.dispatchers;

import com.andreistraut.gaps.controller.MessageRequest;
import com.andreistraut.gaps.controller.MessageResponse;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPath;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrei
 */
public class ComputePathMessageDispatcherTest {
    
    public ComputePathMessageDispatcherTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setRequest method, of class ComputePathMessageDispatcher.
     */
    @Test
    public void testSetRequest() throws Exception {
        System.out.println("setRequest");
        MessageRequest request = null;
        ComputePathMessageDispatcher instance = null;
        boolean expResult = false;
        boolean result = instance.setRequest(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameters method, of class ComputePathMessageDispatcher.
     */
    @Test
    public void testSetParameters() throws Exception {
        System.out.println("setParameters");
        ArrayList<Object> parameters = null;
        ComputePathMessageDispatcher instance = null;
        instance.setParameters(parameters);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of process method, of class ComputePathMessageDispatcher.
     */
    @Test
    public void testProcess() throws Exception {
        System.out.println("process");
        ComputePathMessageDispatcher instance = null;
        boolean expResult = false;
        boolean result = instance.process();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProgress method, of class ComputePathMessageDispatcher.
     */
    @Test
    public void testUpdateProgress() {
        System.out.println("updateProgress");
        MessageResponse response = null;
        ComputePathMessageDispatcher instance = null;
        instance.updateProgress(response);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPaths method, of class ComputePathMessageDispatcher.
     */
    @Test
    public void testGetPaths() {
        System.out.println("getPaths");
        ComputePathMessageDispatcher instance = null;
        ArrayList<DirectedWeightedGraphPath> expResult = null;
        ArrayList<DirectedWeightedGraphPath> result = instance.getPaths();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
