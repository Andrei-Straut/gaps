package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class GraphSettingsTest {
    
    public GraphSettingsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(GraphSettingsTest.class.getName()).log(Level.INFO, 
		"{0} TEST: GraphSettings", 
		GraphSettingsTest.class.toString());
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testToJson() {
    }

    @Test
    public void testFromJson() throws Exception {
    }
    
}
