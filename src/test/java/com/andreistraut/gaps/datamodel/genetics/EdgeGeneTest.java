package com.andreistraut.gaps.datamodel.genetics;

import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import com.andreistraut.gaps.datamodel.mock.DirectedWeightedGraphMockThreeNodeThreeEdges;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.jgap.InvalidConfigurationException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EdgeGeneTest {

    private DirectedWeightedGraphMockThreeNodeThreeEdges graphMock;
    DirectedWeightedEdge secondToFirstEdge;
    private GeneticConfiguration conf;
    
    private final int RUN_LIMIT = 10;

    public EdgeGeneTest() {
    }

    @BeforeClass
    public static void setUpClass() {
	Logger.getLogger(EdgeGeneTest.class.getName()).log(Level.INFO,
		"{0} TEST: Edge Gene",
		EdgeGeneTest.class.toString());
    }

    @Before
    public void setUp() throws InvalidConfigurationException {
	graphMock = new DirectedWeightedGraphMockThreeNodeThreeEdges();

	secondToFirstEdge = new DirectedWeightedEdge(
		graphMock.getSecondNode(), graphMock.getFirstNode(),
		graphMock.getFirstToSecondEdgeCost() + graphMock.getSecondToThirdEdgeCost());

	conf = new GeneticConfiguration("EdgeGeneTest", graphMock.getGraph());
    }

    @Test
    public void testCopy() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene second = first.copy();

	Assert.assertFalse(first == second);
	Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testNewGene() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene second = (EdgeGene) first.newGene();

	Assert.assertFalse(first == second);
	Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testSetAlleleValidValue() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Assert.assertTrue(first.getAllele() == graphMock.getFirstToSecondEdge());

	DirectedWeightedEdge firstToThirdEdge = graphMock.getFirstToThirdEdge();
	first.setAllele(firstToThirdEdge);
	Assert.assertTrue(first.getAllele() == firstToThirdEdge);
    }

    @Test
    public void testSetAlleleNullValue() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Assert.assertTrue(first.getAllele() == graphMock.getFirstToSecondEdge());

	DirectedWeightedEdge firstToThirdEdge = null;
	try {
	    first.setAllele(firstToThirdEdge);
	} catch (IllegalArgumentException e) {
	    Assert.assertTrue(e.getMessage().equals("Allele must be a GenericDirectedEdge"));
	}
    }

    @Test
    public void testSetAlleleInvalidValue() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Assert.assertTrue(first.getAllele() == graphMock.getFirstToSecondEdge());

	Object firstToThirdEdge = new Object();
	try {
	    first.setAllele(firstToThirdEdge);
	} catch (IllegalArgumentException e) {
	    Assert.assertTrue(e.getMessage().equals("Allele must be a GenericDirectedEdge"));
	}
    }

    @Test
    public void testNewGeneInternal() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene second = (EdgeGene) first.newGene();

	Assert.assertFalse(first == second);
	Assert.assertTrue(first.equals(second));
    }

    @Test
    public void testApplyMutationGuaranteed() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	int originalCost = first.getAllele().getCost();

	/*
	 * Mutation in this case is guaranteed. Values should be different 
	 */
	first.applyMutation(originalCost, 100);
	int newCost = first.getAllele().getCost();
	Assert.assertFalse(originalCost == newCost);
    }

    @Test
    public void testApplyMutationNoOp() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	int originalCost = first.getAllele().getCost();

	first.applyMutation(originalCost, 0);
	Assert.assertTrue(originalCost == first.getAllele().getCost());
    }

    @Test
    public void testApplyMutationPercentageSmallerThan0() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	int originalCost = first.getAllele().getCost();

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    first.applyMutation(originalCost, -1);
	    Assert.assertTrue(originalCost == first.getAllele().getCost());

	    first.applyMutation(originalCost, -2);
	    Assert.assertTrue(originalCost == first.getAllele().getCost());
	}
    }

    @Test
    public void testApplyMutationPercentageLargerThan100() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	int originalCost = first.getAllele().getCost();

	for (int i = 0; i < this.RUN_LIMIT; i++) {
	    first.applyMutation(originalCost, 101);
	    Assert.assertTrue(originalCost != first.getAllele().getCost());
	    
	    first.applyMutation(originalCost, 1010);
	    Assert.assertTrue(originalCost != first.getAllele().getCost());
	}
    }

    @Test
    public void testCompareToSmallerThan() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene second = new EdgeGene(secondToFirstEdge, conf);
	Assert.assertTrue(
		first.compareTo(second) == first.getAllele().getCost() - second.getAllele().getCost()
		&& first.compareTo(second) < 0);
    }

    @Test
    public void testCompareToLargerThan() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(secondToFirstEdge, conf);
	EdgeGene second = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Assert.assertTrue(
		first.compareTo(second) == first.getAllele().getCost() - second.getAllele().getCost()
		&& first.compareTo(second) > 0);
    }

    @Test
    public void testCompareToEquals() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene second = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Assert.assertTrue(first.compareTo(second) == 0);
    }

    @Test
    public void testCompareToAgainstNullAllele() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene second = new EdgeGene(null, conf);
	Assert.assertTrue(
		first.compareTo(second) == first.getAllele().getCost()
		&& first.compareTo(second) > 0);
    }

    @Test
    public void testCompareToAgainstNull() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(secondToFirstEdge, conf);
	EdgeGene second = null;
	Assert.assertTrue(
		first.compareTo(second) == first.getAllele().getCost()
		&& first.compareTo(second) > 0);
    }

    @Test
    public void testCompareToAgainstObject() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(secondToFirstEdge, conf);
	Object secondObject = new Object();
	Assert.assertTrue(
		first.compareTo(secondObject) == first.getAllele().getCost()
		&& first.compareTo(secondObject) > 0);
    }

    @Test
    public void testCopyConstructor() throws InvalidConfigurationException {
	EdgeGene first = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene second = new EdgeGene(first);
	Assert.assertTrue(first.equals(second));
	Assert.assertTrue(first.getAllele().equals(second.getAllele()));
	Assert.assertFalse(first == second);
    }

    @Test
    public void testToStringEquality() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Assert.assertTrue(firstGene.toString().equals(secondGene.toString()));
    }

    @Test
    public void testToStringDifference() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondGene = new EdgeGene(secondToFirstEdge, conf);
	Assert.assertFalse(firstGene.toString().equals(secondGene.toString()));
    }

    @Test
    public void testHashCodeEquality() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Assert.assertTrue(firstGene.hashCode() == secondGene.hashCode());
    }

    @Test
    public void testHashCodeDifference() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondGene = new EdgeGene(secondToFirstEdge, conf);
	Assert.assertFalse(firstGene.hashCode() == secondGene.hashCode());
    }

    @Test
    public void testEqualsEquality() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Assert.assertTrue(firstGene.equals(secondGene));
    }

    @Test
    public void testEqualsDifference() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondGene = new EdgeGene(secondToFirstEdge, conf);
	Assert.assertFalse(firstGene.equals(secondGene));
    }

    @Test
    public void testEqualsNull() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondGene = null;
	Assert.assertFalse(firstGene.equals(secondGene));
    }

    @Test
    public void testEqualsDifferentClass() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	Object secondGene = new Object();
	Assert.assertFalse(firstGene.equals(secondGene));
    }

    @Test
    public void testClone() throws InvalidConfigurationException {
	EdgeGene firstGene = new EdgeGene(graphMock.getFirstToSecondEdge(), conf);
	EdgeGene secondGene = firstGene.clone();

	Assert.assertFalse(firstGene == secondGene);
	Assert.assertTrue(firstGene.equals(secondGene));
    }

}
