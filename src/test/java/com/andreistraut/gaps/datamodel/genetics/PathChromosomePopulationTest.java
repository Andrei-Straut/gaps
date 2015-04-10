package com.andreistraut.gaps.datamodel.genetics;

import java.util.ArrayList;
import java.util.List;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author straut
 */
public class PathChromosomePopulationTest {
    
    public PathChromosomePopulationTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getGenePool method, of class PathChromosomePopulation.
     */
    @Test
    public void testGetGenePool() {
	System.out.println("getGenePool");
	PathChromosomePopulation instance = null;
	EdgeGenePool expResult = null;
	EdgeGenePool result = instance.getGenePool();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of setGenePool method, of class PathChromosomePopulation.
     */
    @Test
    public void testSetGenePool() {
	System.out.println("setGenePool");
	EdgeGenePool genePool = null;
	PathChromosomePopulation instance = null;
	instance.setGenePool(genePool);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getChromosome method, of class PathChromosomePopulation.
     */
    @Test
    public void testGetChromosome() {
	System.out.println("getChromosome");
	int index = 0;
	PathChromosomePopulation instance = null;
	PathChromosome expResult = null;
	PathChromosome result = instance.getChromosome(index);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getChromosomes method, of class PathChromosomePopulation.
     */
    @Test
    public void testGetChromosomes() {
	System.out.println("getChromosomes");
	PathChromosomePopulation instance = null;
	List<IChromosome> expResult = null;
	List<IChromosome> result = instance.getChromosomes();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of setChromosome method, of class PathChromosomePopulation.
     */
    @Test
    public void testSetChromosome() {
	System.out.println("setChromosome");
	int index = 0;
	IChromosome chromosome = null;
	PathChromosomePopulation instance = null;
	instance.setChromosome(index, chromosome);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of setChromosomes method, of class PathChromosomePopulation.
     */
    @Test
    public void testSetChromosomes() {
	System.out.println("setChromosomes");
	List chromosomes = null;
	PathChromosomePopulation instance = null;
	instance.setChromosomes(chromosomes);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of addChromosome method, of class PathChromosomePopulation.
     */
    @Test
    public void testAddChromosome() {
	System.out.println("addChromosome");
	IChromosome toAdd = null;
	PathChromosomePopulation instance = null;
	instance.addChromosome(toAdd);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of addChromosomes method, of class PathChromosomePopulation.
     */
    @Test
    public void testAddChromosomes_Population() {
	System.out.println("addChromosomes");
	Population population = null;
	PathChromosomePopulation instance = null;
	instance.addChromosomes(population);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of addChromosomes method, of class PathChromosomePopulation.
     */
    @Test
    public void testAddChromosomes_ArrayList() {
	System.out.println("addChromosomes");
	ArrayList<PathChromosome> chromosomes = null;
	PathChromosomePopulation instance = null;
	instance.addChromosomes(chromosomes);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of determineFittestChromosome method, of class PathChromosomePopulation.
     */
    @Test
    public void testDetermineFittestChromosome_0args() {
	System.out.println("determineFittestChromosome");
	PathChromosomePopulation instance = null;
	PathChromosome expResult = null;
	PathChromosome result = instance.determineFittestChromosome();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of determineFittestChromosome method, of class PathChromosomePopulation.
     */
    @Test
    public void testDetermineFittestChromosome_int_int() {
	System.out.println("determineFittestChromosome");
	int startIndex = 0;
	int endIndex = 0;
	PathChromosomePopulation instance = null;
	PathChromosome expResult = null;
	PathChromosome result = instance.determineFittestChromosome(startIndex, endIndex);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of determineFittestChromosomes method, of class PathChromosomePopulation.
     */
    @Test
    public void testDetermineFittestChromosomes() {
	System.out.println("determineFittestChromosomes");
	int numberOfChromosomes = 0;
	PathChromosomePopulation instance = null;
	List expResult = null;
	List result = instance.determineFittestChromosomes(numberOfChromosomes);
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of size method, of class PathChromosomePopulation.
     */
    @Test
    public void testSize() {
	System.out.println("size");
	PathChromosomePopulation instance = null;
	int expResult = 0;
	int result = instance.size();
	assertEquals(expResult, result);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }
    
}
