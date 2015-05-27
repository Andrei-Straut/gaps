
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
                                <div class="panel-body">
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
                                    (Move above slider to select generation)<br/>
                                    <button id="genetic-statistics-path-reset" type="reset" class="btn btn-primary btn-sm"
                                            ng-click="resetPathSelection();">Reset Selection</button>
                                    <button id="genetic-statistics-select-best-gaps" type="reset" class="btn btn-primary btn-sm"
                                            ng-click="selectGapsBest();">Show GAPS Best</button>
                                    <button id="genetic-statistics-select-best-gaps" type="reset" class="btn btn-primary btn-sm"
                                            ng-click="selectJgraphtBest();">Show JGraphT Best</button>
                                    <br/>
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
                        <div class="col-md-12 col-sm-12 col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Evolution timeline (Click on item to select generation)
                                </div>
                                <div class="panel-body">
                                    <div id="evolution-timeline"></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-9 col-sm-12 col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Genetic Evolution (Costs, lower values are better)
                                </div>
                                <div class="panel-body">
                                    <div id="costs-timeline"></div>
                                </div>
                            </div>
                        </div>
                        
                        <div info-card info-title="Genetic Statistics: Best Path Values" info-data="statisticsInfoCardValue"></div>
                    </div>
                </div>
            </div>        
        </div>

        <hr />
    </div>
</div>