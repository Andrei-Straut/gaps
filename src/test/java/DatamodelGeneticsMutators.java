
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeCrossoverValidatorTest.class,
    com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeMutationValidatorTest.class,
    com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeMutatorTest.class,
    com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeCycleRemoveMutatorTest.class,
    com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeSingleGeneMutatorTest.class,
    com.andreistraut.gaps.datamodel.genetics.mutators.PathChromosomeOnePointCrossoverTest.class
    })

public class DatamodelGeneticsMutators {
}
