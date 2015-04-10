

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    com.andreistraut.gaps.datamodel.genetics.EdgeGeneTest.class, 
    com.andreistraut.gaps.datamodel.genetics.PathChromosomeTest.class,
    com.andreistraut.gaps.datamodel.genetics.PathChromosomeFitnessTest.class
    })

public class DatamodelGenetics {    
}
