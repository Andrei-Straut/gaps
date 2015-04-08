
package com.andreistraut.gaps.datamodel.genetics;

import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenerationStatistic {
    private final int evolutionStage;
    private final Date timeStamp;
    
    private int startPopulationSize;
    private int startBestFitness;
    private double startAverageFitness;
    private int startBestCost;
    private double startAverageCost;
    
    private int endPopulationSize;
    private int endBestFitness;
    private double endAverageFitness;
    private double endAverageCost;
    private int endBestCost;
    
    private JsonObject endBestChromosome;
    private JsonObject endWorstChromosome;
    
    public GenerationStatistic(int evolutionStage, Date timeStamp) {
	this.evolutionStage = evolutionStage;
	this.timeStamp = timeStamp;
    }

    public int getEvolutionStage() {
	return evolutionStage;
    }

    public Date getTimeStamp() {
	return timeStamp;
    }

    public int getStartPopulationSize() {
	return startPopulationSize;
    }

    public GenerationStatistic setStartPopulationSize(int startPopulationSize) {
	this.startPopulationSize = startPopulationSize;
	return this;
    }

    public int getStartBestFitness() {
	return startBestFitness;
    }

    public GenerationStatistic setStartBestFitness(int startBestFitness) {
	this.startBestFitness = startBestFitness;
	return this;
    }

    public double getStartAverageFitness() {
	return startAverageFitness;
    }

    public GenerationStatistic setStartAverageFitness(double startAverageFitness) {
	this.startAverageFitness = startAverageFitness;
	return this;
    }    

    public int getStartBestCost() {
	return startBestCost;
    }

    public GenerationStatistic setStartBestCost(int startBestCost) {
	this.startBestCost = startBestCost;
	return this;
    }

    public double getStartAverageCost() {
	return startAverageCost;
    }

    public GenerationStatistic setStartAverageCost(double startAverageCost) {
	this.startAverageCost = startAverageCost;
	return this;
    }

    public int getEndPopulationSize() {
	return endPopulationSize;
    }

    public GenerationStatistic setEndPopulationSize(int endPopulationSize) {
	this.endPopulationSize = endPopulationSize;
	return this;
    }

    public int getEndBestFitness() {
	return endBestFitness;
    }

    public GenerationStatistic setEndBestFitness(int endBestFitness) {
	this.endBestFitness = endBestFitness;
	return this;
    }

    public double getEndAverageFitness() {
	return endAverageFitness;
    }

    public GenerationStatistic setEndAverageFitness(double endAverageFitness) {
	this.endAverageFitness = endAverageFitness;
	return this;
    }

    public int getEndBestCost() {
	return endBestCost;
    }

    public GenerationStatistic setEndBestCost(int endBestCost) {
	this.endBestCost = endBestCost;
	return this;
    }

    public double getEndAverageCost() {
	return endAverageCost;
    }

    public GenerationStatistic setEndAverageCost(double endAverageCost) {
	this.endAverageCost = endAverageCost;
	return this;
    }

    public JsonObject getEndBestChromosome() {
	return endBestChromosome;
    }

    public GenerationStatistic setEndBestChromosome(JsonObject bestChromosome) {
	this.endBestChromosome = bestChromosome;
	return this;
    }

    public JsonObject getEndWorstChromosome() {
	return endWorstChromosome;
    }

    public GenerationStatistic setEndWorstChromosome(JsonObject worstChromosome) {
	this.endWorstChromosome = worstChromosome;
	return this;
    }
    
    public JsonObject toJson() {
	JsonObject jsonStatistic = new JsonObject();
	DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
	
	jsonStatistic.addProperty("evolutionStage", this.evolutionStage);
	jsonStatistic.addProperty("timeStamp", formatter.format(this.timeStamp));
	jsonStatistic.addProperty("startPopulationSize", this.startPopulationSize);
	jsonStatistic.addProperty("endPopulationSize", this.endPopulationSize);
	
	jsonStatistic.addProperty("startBestFitness", this.startBestFitness);
	jsonStatistic.addProperty("startAverageFitness", this.startAverageFitness);
	jsonStatistic.addProperty("startBestCost", this.startBestCost);
	jsonStatistic.addProperty("startAverageCost", this.startAverageCost);
	
	jsonStatistic.addProperty("endBestFitness", this.endBestFitness);
	jsonStatistic.addProperty("endAverageFitness", this.endAverageFitness);
	jsonStatistic.addProperty("endBestCost", this.endBestCost);
	jsonStatistic.addProperty("endAverageCost", this.endAverageCost);
	
	jsonStatistic.add("bestChromosome", this.endBestChromosome != null 
		? this.endBestChromosome
		: new JsonObject());
	jsonStatistic.add("worstChromosome", this.endWorstChromosome != null
		? this.endWorstChromosome
		: new JsonObject());
	
	return jsonStatistic;
    }
}
