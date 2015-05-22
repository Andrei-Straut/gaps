
<div class="row">
    <div id="genetic-statistics" ng-controller="geneticstatisticscontroller" ng-show="statisticsLoaded">
        <a name="genetic-statistics"></a>
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="checkbox-inline">
                        <input id="genetic-statistics-viewer-toggle" type="checkbox" checked data-toggle="toggle" data-on="Visible" data-off="Hidden">
                    </label>
                    Genetic Statistics
                </div>

                <div class="panel-body" ng-show="statisticsDisplayed">
                    <div class="row">
                        <div class="col-md-12 col-sm-12 col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Evolution steps
                                </div>
                                <div class="panel-body">
                                    <div id="evolution-timeline"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-12 col-sm-12 col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Evolution steps
                                </div>
                                <div class="panel-body">
                                    (Move slider to select generation or 
                                    <button id="genetic-statistics-path-reset" type="reset" class="btn btn-primary btn-sm"
                                            ng-click="resetPathSelection();">Reset Selection</button>)
                                    <br/>
                                    <input type="text" 
                                           id="generation-slider"
                                           class="span" 
                                           value="{{(getStatistics()).selectedGenerationIndex}}" 
                                           style="width: 100%"
                                           data-slider-min="0" 
                                           data-slider-max="{{(getStatistics()).generations.length - 1}}" 
                                           data-slider-step="1" 
                                           data-slider-value="{{(getStatistics()).selectedGenerationIndex}}" 
                                           data-slider-orientation="horizontal" 
                                           data-slider-selection="after"
                                           data-slider-tooltip="hide">
                                    <hr/>

                                    Generation <span class="label-info">{{(getStatistics()).selectedGeneration.evolutionStage}}</span><br/>
                                    Best Cost: <span class="label-info">{{(getStatistics()).selectedGeneration.bestChromosome.cost}}</span><br/>
                                    Average Cost: <span class="label-info">{{(getStatistics()).selectedGeneration.endAverageCost}}</span><br/>
                                    Best Fitness: <span class="label-info">{{(getStatistics()).selectedGeneration.bestChromosome.fitness}}</span><br/>
                                    Average Fitness: <span class="label-info">{{(getStatistics()).selectedGeneration.endAverageFitness}}</span><br/>
                                    <br/>
                                    Best Path:
                                    <div class="panel-body">
                                        <span 
                                            class="label"
                                            ng-repeat="gene in (getStatistics()).selectedGeneration.bestChromosome.path"
                                            ng-class="(getGraphStatistics()).getEdgeClass(gene.cost)">
                                            {{gene.from}} - {{gene.to}} ({{gene.cost}})
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-9 col-sm-12 col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Genetic Evolution (Costs)
                                </div>
                                <div class="panel-body">
                                    <div id="morris-bar-cost-chart" style="height: 250px;"></div> 
                                </div>
                            </div>                    
                        </div>

                        <div class="col-md-3 col-sm-12 col-xs-12">
                            <div class="panel panel-primary text-center no-boder bg-color-green">
                                <div class="panel-footer back-footer-green">
                                    Genetic Statistics: Best Path Values
                                </div>
                                <div class="panel-body">
                                    <i class="fa fa-bar-chart-o fa-5x"></i>
                                    <h3>Found in generation: {{(getStatistics()).evolutionStage}}</h3>
                                    <h3>Cost: {{(getStatistics()).bestPathCost}}</h3>
                                    <h3>Fitness: {{(getStatistics()).bestPathFitness}}</h3>
                                    <h3>Path Length: {{(getStatistics()).bestPathEdgeNumber}}</h3>
                                    <h3>Evolution Started: {{getEvolutionStartTime()}}</h3>
                                    <h3>Evolution Finished: {{getEvolutionEndTime()}}</h3>
                                    <h3>Total Time: {{getEvolutionDiffTime()}}</h3>
                                </div>
                            </div>                      
                        </div>
                    </div>
                </div>
            </div>        
        </div>

        <hr />
    </div>
</div>