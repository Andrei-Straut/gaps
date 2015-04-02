<div id="graph-statistics" ng-show="load.graphStatisticsLoaded">
    <div class="row">
        <a name="graph-statistics"></a>
        <div class="col-md-9 col-sm-12 col-xs-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="checkbox-inline">
                        <input id="graph-statistics-viewer-toggle" type="checkbox" checked data-toggle="toggle">
                    </label>
                    Graph Statistics: Direct Edges
                </div>
                <div class="panel-body" ng-show="load.graphStatisticsDisplayed">
                    <div class="table-responsive">
                        <table id="graph-direct-edges" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>From</th>
                                    <th>To</th>
                                    <th>Cost</th>
                                </tr>
                            </thead>
                            <tbody>                                
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3 col-sm-12 col-xs-12" ng-show="load.graphStatisticsLoaded && load.graphStatisticsDisplayed">
            <div class="panel panel-primary text-center no-boder bg-color-green">
                <div class="panel-footer back-footer-green">
                    Graph Statistics: Edge Values
                </div>
                <div class="panel-body">
                    <i class="fa fa-bar-chart-o fa-5x"></i>
                    <h3>Number of edges: {{graphStatistics.numberOfEdges}}</h3>
                    <h3>Total edge cost: {{graphStatistics.totalEdgeCost}}</h3>
                    <h3>Most expensive edge: {{graphStatistics.maximumEdgeCost}}</h3>
                    <h3>Average edge cost: {{graphStatistics.averageEdgeCost}}</h3>
                    <h3>Average node connectivity: {{graphStatistics.averageEdgesPerNode}}</h3>
                </div>
            </div>                      
        </div>
    </div>

    <hr/>
</div>