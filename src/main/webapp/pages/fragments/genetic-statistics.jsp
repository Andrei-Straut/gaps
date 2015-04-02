<div id="genetic-statistics" ng-show="load.evolutionComputed">
    <div class="row">
        <a name="genetic-statistics"></a>
        <div class="col-md-9 col-sm-12 col-xs-12">
            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <label class="checkbox-inline">
                                <input id="genetic-statistics-viewer-toggle" type="checkbox" checked data-toggle="toggle">
                            </label>
                            Genetic Statistics
                        </div>

                        <div class="panel-body" ng-model="geneticStatistics" ng-show="load.evolutionComputed && load.geneticStatisticsDisplayed">
                            (Move slider to select generation or 
                            <button id="genetic-statistics-path-reset" type="reset" class="btn btn-primary btn-sm"
                                    ng-click="resetPathSelection();">Reset Selection</button>)
                            <br/>
                            <input type="text" 
                                   id="generation-slider"
                                   class="span" 
                                   value="0" 
                                   style="width: 100%"
                                   data-slider-min="0" 
                                   data-slider-max="{{geneticStatistics.generations.length - 1}}" 
                                   data-slider-step="1" 
                                   data-slider-value="0" 
                                   data-slider-orientation="horizontal" 
                                   data-slider-selection="after"
                                   data-slider-tooltip="hide">

                            <div class="panel-body">
                                Generation <span class="label-info">{{geneticStatistics.selectedGeneration.evolutionStage}}</span><br/>
                                Best Cost: <span class="label-info">{{geneticStatistics.selectedGeneration.bestChromosome.cost}}</span><br/>
                                Best Fitness: <span class="label-info">{{geneticStatistics.selectedGeneration.bestChromosome.fitness}}</span><br/>
                                Best Path:
                                <div class="panel-body">
                                    <span 
                                        class="label"
                                        ng-repeat="gene in geneticStatistics.selectedGeneration.bestChromosome.path"
                                        ng-class="getEdgeClass(gene.data.cost)">
                                        {{gene.nodeFrom}} - {{gene.nodeTo}} ({{gene.data.cost}})
                                    </span>
                                </div>
                            </div>                            
                        </div>
                    </div>
                </div>
            </div>

            <div class="row" ng-show="load.evolutionComputed && load.geneticStatisticsDisplayed">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Genetic Evolution (Costs)
                        </div>
                        <div class="panel-body">
                            <div id="morris-bar-cost-chart" style="height: 250px;"></div> 
                        </div>
                    </div>                    
                </div>
            </div>
        </div>

        <div class="col-md-3 col-sm-12 col-xs-12" ng-show="load.evolutionComputed && load.geneticStatisticsDisplayed">
            <div class="panel panel-primary text-center no-boder bg-color-green">
                <div class="panel-footer back-footer-green">
                    Genetic Statistics: Best Path Values
                </div>
                <div class="panel-body">
                    <i class="fa fa-bar-chart-o fa-5x"></i>
                    <h3>Found in generation: {{geneticStatistics.evolutionStage}}</h3>
                    <h3>Cost: {{geneticStatistics.bestPathCost}}</h3>
                    <h3>Fitness: {{geneticStatistics.bestPathFitness}}</h3>
                    <h3>Path Length: {{geneticStatistics.bestPathEdgeNumber}}</h3>
                    <h3>Evolution Started: {{convertTime(geneticStatistics.startTimestamp)}}</h3>
                    <h3>Evolution Started: {{convertTime(geneticStatistics.endTimestamp)}}</h3>
                    <h3>Total Time: {{convertTime(geneticStatistics.evolutionDiffTimestamp)}}</h3>
                </div>
            </div>                      
        </div>
    </div>

    <hr />
</div>