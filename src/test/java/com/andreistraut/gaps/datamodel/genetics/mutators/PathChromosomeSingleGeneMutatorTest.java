package com.andreistraut.gaps.datamodel.genetics.mutators;

import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class PathChromosomeSingleGeneMutatorTest {
    
    public PathChromosomeSingleGeneMutatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(PathChromosomeSingleGeneMutatorTest.class.getName()).log(Level.INFO,
		"{0} TEST: PathChromosomeSingleGeneMutator",
		PathChromosomeSingleGeneMutatorTest.class.toString());
    }

    @Test
    public void testDoMutation() throws Exception {
    }

    @Test
    public void testGetMutationPosition() {
    }
    
}
