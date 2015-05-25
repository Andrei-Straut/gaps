

<div class="row">
    <div id="compare-statistics" ng-controller="comparestatisticscontroller" ng-show="statisticsLoaded">
        <a name="compare-statistics"></a>
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="checkbox-inline">
                        <input id="compare-statistics-viewer-toggle" type="checkbox" checked data-toggle="toggle" data-on="Visible" data-off="Hidden">
                    </label>
                    Results Comparison
                </div>
                <div class="panel-body" ng-show="statisticsDisplayed">
                    <div class="row">
                        <div class="col-md-9 col-sm-12 col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Best Values
                                </div>
                                <div class="panel-body">
                                    <div id="morris-bar-compare-chart" style="height: 250px;"></div> 
                                </div>
                            </div>
                        </div>

                        <div info-card info-title="Results Comparison: JGraphT Values" info-data="statisticsInfoCardValue"></div>
                    </div>            
                </div>
            </div>
        </div>
        <hr />
    </div>
</div>