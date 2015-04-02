<div id="path-statistics" ng-show="load.pathStatisticsLoaded">
    <div class="row">
        <a name="path-statistics"></a>
        <div class="col-md-9 col-sm-12 col-xs-12">
            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <label class="checkbox-inline">
                                <input id="path-statistics-viewer-toggle" type="checkbox" checked data-toggle="toggle">
                            </label>
                            Path Statistics&nbsp;&nbsp;
                            (<span class="label label-default">Neutral Edge Cost</span>
                            <span class="label label-success">Low Edge Cost</span>
                            <span class="label label-warning">Medium Edge Cost</span>
                            <span class="label label-danger">High Edge Cost</span>)
                        </div>

                        <div class="panel-body" ng-model="pathStatistics" ng-show="load.pathStatisticsDisplayed">
                            Cheapest (Total Cost {{pathStatistics.cheapestPathCost}})
                            <div class="panel-body">
                                <span 
                                    class="label"
                                    ng-repeat="edge in pathStatistics.cheapestPath"
                                    ng-class="getEdgeClass(edge.data.cost)">
                                    {{edge.nodeFrom}} - {{edge.nodeTo}} ({{edge.data.cost}})
                                </span>
                            </div>
                            <br/>
                            Most Expensive (Total Cost {{pathStatistics.mostExpensivePathCost}})
                            <div class="panel-body">
                                <span 
                                    class="label"
                                    ng-repeat="edge in pathStatistics.mostExpensivePath"
                                    ng-class="getEdgeClass(edge.data.cost)">
                                    {{edge.nodeFrom}} - {{edge.nodeTo}} ({{edge.data.cost}})
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row" ng-show="load.pathStatisticsDisplayed">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Path Statistics: Paths
                        </div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table id="graph-paths" class="table table-striped table-bordered table-hover">
                                    <thead>
                                        <tr>
                                            <th>Id</th>
                                            <th>Number of Edges</th>
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
            </div>
        </div>

        <div class="col-md-3 col-sm-12 col-xs-12" ng-show="load.pathStatisticsLoaded && load.pathStatisticsDisplayed">
            <div class="panel panel-primary text-center no-boder bg-color-green">
                <div class="panel-footer back-footer-green">
                    Path Statistics: Path Values
                </div>
                <div class="panel-body">
                    <i class="fa fa-bar-chart-o fa-5x"></i>
                    <h3>Number of paths: {{pathStatistics.paths.length}}</h3>
                    <h3>Total path cost: {{pathStatistics.totalPathCost}}</h3>
                    <h3>Most expensive path cost: {{pathStatistics.mostExpensivePathCost}}</h3>
                    <h3>Most expensive path length: {{pathStatistics.mostExpensivePath.length}}</h3>
                    <h3>Cheapest path cost: {{pathStatistics.cheapestPathCost}}</h3>
                    <h3>Cheapest path length: {{pathStatistics.cheapestPath.length}}</h3>
                    <h3>Average path cost: {{pathStatistics.averagePathCost}}</h3>
                </div>
            </div>                      
        </div>
    </div>

    <hr />
</div>