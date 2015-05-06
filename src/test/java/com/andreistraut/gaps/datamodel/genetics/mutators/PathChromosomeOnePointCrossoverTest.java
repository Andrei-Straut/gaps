package com.andreistraut.gaps.datamodel.genetics.mutators;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.IChromosome;
import org.jgap.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class PathChromosomeOnePointCrossoverTest {
    
    public PathChromosomeOnePointCrossoverTest() {
    }
    
    @Before
    public void setUp() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeOnePointCrossoverTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosomeOnePointCrossoverTest",
		PathChromosomeOnePointCrossoverTest.class.toString());
    }    
}
