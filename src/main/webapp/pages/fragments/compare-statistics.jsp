<div id="compare-statistics" ng-show="getStatistics().getCompareStatisticsLoaded();">
    <div class="row">
        <a name="compare-statistics"></a>
        <div class="col-md-9 col-sm-12 col-xs-12">
            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <label class="checkbox-inline">
                                <input id="compare-statistics-viewer-toggle" type="checkbox" checked data-toggle="toggle" data-on="Visible" data-off="Hidden">
                            </label>
                            Results Comparison (KShortestPath)
                        </div>

                        <div class="panel-body" ng-show="compareStatisticsDisplayed">
                            <div id="morris-bar-compare-chart" style="height: 250px;"></div> 
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-3 col-sm-12 col-xs-12" ng-show="compareStatisticsDisplayed">
            <div class="panel panel-primary text-center no-boder bg-color-green">
                <div class="panel-footer back-footer-green">
                    Results Comparison: KShortestPath Values
                </div>
                <div class="panel-body">
                    <i class="fa fa-bar-chart-o fa-5x"></i>
                    <h3>Best Path Cost: {{(getStatistics().getCompareStatistics()).chromosomes[0].cost}}</h3>
                    <h3>Best Path Fitness: {{(getStatistics().getCompareStatistics()).chromosomes[0].fitness}}</h3>
                    <h3 
                        ng-if="(getStatistics().getCompareStatistics()).chromosomes[0].cost < (getStatistics().getGeneticStatistics()).bestPathCost">
                        Winner: KShortest</h3>
                    <h3 
                        ng-if="(getStatistics().getCompareStatistics()).chromosomes[0].cost >= (getStatistics().getGeneticStatistics()).bestPathCost">
                        Winner: GAPS</h3>
                    
                    <h3>Search Started: {{convertTime((getStatistics().getCompareStatistics()).startTimestamp)}}</h3>
                    <h3>Search Finished: {{convertTime((getStatistics().getCompareStatistics()).endTimestamp)}}</h3>
                    <h3>Total Time: {{convertTime((getStatistics().getCompareStatistics()).compareDiffTimestamp)}}</h3>
                </div>
            </div>                      
        </div>
    </div>

    <hr />
</div>