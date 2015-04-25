

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    com.andreistraut.gaps.datamodel.graph.NodeTest.class,
    com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdgeTest.class, 
    com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdgeFactoryTest.class,
    com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphTest.class, 
    com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphStaticTest.class, 
    com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphSemiRandomTest.class, 
    com.andreistraut.gaps.datamodel.graph.DirectedWeightedGraphPathTest.class, 
    com.andreistraut.gaps.datamodel.graph.GraphPathComparatorTest.class})

public class DatamodelGraph {
    
}
