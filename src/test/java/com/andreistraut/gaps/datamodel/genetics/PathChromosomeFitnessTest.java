package com.andreistraut.gaps.datamodel.genetics;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.IChromosome;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class PathChromosomeFitnessTest {
    
    public PathChromosomeFitnessTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeFitnessTest.class.getName()).log(Level.INFO,
		PathChromosomeFitnessTest.class.toString() + " TEST: PathChromosome Fitness");
    }
    
    @Before
    public void setUp() {
    }
    
    @Test
    public void testGetLastComputedFitnessValue() {
	System.out.println("getLastComputedFitnessValue");
	PathChromosomeFitness instance = new PathChromosomeFitness();
	double expResult = 0.0;
	double result = instance.getLastComputedFitnessValue();
	assertEquals(expResult, result, 0.0);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getFitnessValue method, of class PathChromosomeFitness.
     */
    @Test
    public void testGetFitnessValue() {
	System.out.println("getFitnessValue");
	IChromosome subject = null;
	PathChromosomeFitness instance = new PathChromosomeFitness();
	double expResult = 0.0;
	double result = instance.getFitnessValue(subject);
	assertEquals(expResult, result, 0.0);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of evaluate method, of class PathChromosomeFitness.
     */
    @Test
    public void testEvaluate() {
	System.out.println("evaluate");
	IChromosome subject = null;
	PathChromosomeFitness instance = new PathChromosomeFitness();
	double expResult = 0.0;
	double result = instance.evaluate(subject);
	assertEquals(expResult, result, 0.0);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
