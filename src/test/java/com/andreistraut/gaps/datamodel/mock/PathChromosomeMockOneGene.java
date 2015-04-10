package com.andreistraut.gaps.datamodel.mock;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import com.andreistraut.gaps.datamodel.graph.DirectedWeightedEdge;
import java.util.ArrayList;
import org.jgap.InvalidConfigurationException;

/**
 * Wrapper nock for a PathChromosome with two Genes. 
 * Assumes 3 nodes and two edges in graph.
 * 
 * Nodes are 1, 2, 3
 * Edges are 1-2 (cost 1), 2-3 (cost 2), 1-3 (cost 1)
 * 
 * Chromosome source is 1, destination is 3
 * Total chromosome cost 1
 */
public class PathChromosomeMockOneGene {

    private PathChromosome chromosome;
    private EdgeGene firstToThirdGene;
    private final GeneticConfiguration conf;

    private DirectedWeightedGraphMockThreeNodeTwoEdges graphMock;

    public PathChromosomeMockOneGene(GeneticConfiguration conf) throws InvalidConfigurationException {
	this.conf = conf;
	this.reset();
    }

    public final void reset() throws InvalidConfigurationException {
	this.graphMock = new DirectedWeightedGraphMockThreeNodeTwoEdges();
	
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
