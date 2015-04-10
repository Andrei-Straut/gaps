package com.andreistraut.gaps.datamodel.mock;

import com.andreistraut.gaps.datamodel.genetics.EdgeGene;
import com.andreistraut.gaps.datamodel.genetics.GeneticConfiguration;
import com.andreistraut.gaps.datamodel.genetics.PathChromosome;
import java.util.ArrayList;
import org.jgap.InvalidConfigurationException;

public class TwoGenePathChromosomeMock {

    private PathChromosome chromosome;
    private EdgeGene firstToSecondGene;
    private EdgeGene secondToThirdGene;
    private final GeneticConfiguration conf;
    
    private ThreeNodeTwoEdgesDirectedWeightedGraphMock graphMock;
    
    public TwoGenePathChromosomeMock(GeneticConfiguration conf) throws InvalidConfigurationException {
	this.conf = conf;
	this.reset();
    }

    public final void reset() throws InvalidConfigurationException {
	this.graphMock = new ThreeNodeTwoEdgesDirectedWeightedGraphMock();
	
	this.firstToSecondGene = new EdgeGene(this.graphMock.getFirstToSecondEdge(), this.conf);
	this.secondToThirdGene = new EdgeGene(this.graphMock.getSecondToThirdEdge(), this.conf);

	ArrayList<EdgeGene> genes = new ArrayList<EdgeGene>();
	genes.add(this.firstToSecondGene);
	genes.add(this.secondToThirdGene);
	
	this.chromosome = new PathChromosome(conf, genes, graphMock.getFirstNode(), graphMock.getThirdNode());
    }

    public PathChromosome getChromosome() {
	return chromosome;
    }

    public EdgeGene getFirstToSecondGene() {
	return firstToSecondGene;
    }

    public EdgeGene getSecondToThirdGene() {
	return secondToThirdGene;
    }
}
