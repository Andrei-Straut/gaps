package com.andreistraut.gaps.datamodel.mock;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import java.util.ArrayList;
import org.jgap.InvalidConfigurationException;

public class OneGenePathChromosomeMock {

    private PathChromosome chromosome;
    private EdgeGene firstToThirdGene;
    private final GeneticConfiguration conf;

    private ThreeNodeTwoEdgesDirectedWeightedGraphMock graphMock;

    public OneGenePathChromosomeMock(GeneticConfiguration conf) throws InvalidConfigurationException {
	this.conf = conf;
	this.reset();
    }

    public final void reset() throws InvalidConfigurationException {
	this.graphMock = new ThreeNodeTwoEdgesDirectedWeightedGraphMock();
	
	DirectedWeightedEdge firstToThirdEdge = new DirectedWeightedEdge(
		graphMock.getFirstNode(), 
		graphMock.getThirdNode(), 
		graphMock.getFirstToSecondEdgeCost());
	this.graphMock.getGraph().addEdge(graphMock.getFirstNode(), graphMock.getThirdNode(), firstToThirdEdge);
	
	this.firstToThirdGene = new EdgeGene(firstToThirdEdge, conf);

	ArrayList<EdgeGene> genes = new ArrayList<EdgeGene>();
	genes.add(firstToThirdGene);

	this.chromosome = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());
    }

    public PathChromosome getChromosome() {
	return chromosome;
    }

    public EdgeGene getFirstToThirdGene() {
	return firstToThirdGene;
    }
}
