package com.andreistraut.gaps.datamodel.graph;

import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class DirectedWeightedGraphImportedTest {
    
    public DirectedWeightedGraphImportedTest() {
    }
    
    @Before
    public void setUp() {
    }

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger(DirectedWeightedGraphImportedTest.class.getName()).log(Level.INFO,
                "{0} TEST: Graph JSON Import",
                DirectedWeightedGraphImportedTest.class.toString());
    }

    @Test
    public void testFromJson() throws Exception {
    }
    
}
