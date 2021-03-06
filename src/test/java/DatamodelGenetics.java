

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    com.andreistraut.gaps.datamodel.genetics.EdgeGeneTest.class, 
    com.andreistraut.gaps.datamodel.genetics.EdgeGeneConstraintCheckerTest.class, 
    com.andreistraut.gaps.datamodel.genetics.PathChromosomeTest.class,
    com.andreistraut.gaps.datamodel.genetics.PathChromosomeFitnessTest.class,
    com.andreistraut.gaps.datamodel.genetics.PathChromosomeFitnessComparatorTest.class,
    com.andreistraut.gaps.datamodel.genetics.PathChromosomePopulationTest.class
    })

public class DatamodelGenetics {    
}
