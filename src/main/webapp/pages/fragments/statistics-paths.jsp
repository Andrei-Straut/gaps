
<div class="row">
    <div id="path-statistics" ng-controller="pathsstatisticscontroller" ng-show="statisticsLoaded">
        <a name="path-statistics"></a>
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="checkbox-inline">
                        <input id="path-statistics-viewer-toggle" type="checkbox" checked data-toggle="toggle" data-on="Visible" data-off="Hidden">
                    </label>
                    Path Statistics&nbsp;&nbsp;
                    (<span class="label label-default">Neutral Edge Cost</span>
                    <span class="label label-success">Low Edge Cost</span>
                    <span class="label label-warning">Medium Edge Cost</span>
                    <span class="label label-danger">High Edge Cost</span>)
                </div>

                <div class="panel-body" ng-show="statisticsDisplayed">
                    <div class="row">
                        <div class="col-md-12 col-sm-12 col-xs-12">
                            <div class="panel-body">
                                Cheapest (Total Cost {{(getStatistics()).cheapestPathCost}})
                                <div class="panel-body">
                                    <span 
                                        class="label"
                                        ng-repeat="edge in (getStatistics()).cheapestPath"
                                        ng-class="(getGraphStatistics()).getEdgeClass(edge.cost)">
                                        {{edge.from}} - {{edge.to}} ({{edge.cost}})
                                    </span>
                                </div>
                                <br/>
                                Most Expensive (Total Cost {{(getStatistics()).mostExpensivePathCost}})
                                <div class="panel-body">
                                    <span 
                                        class="label"
                                        ng-repeat="edge in (getStatistics()).mostExpensivePath"
                                        ng-class="(getGraphStatistics()).getEdgeClass(edge.cost)">
                                        {{edge.from}} - {{edge.to}} ({{edge.cost}})
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-9 col-sm-12 col-xs-12" ng-show="statisticsDisplayed">
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

                        <div class="col-md-3 col-sm-12 col-xs-12">
                            <div class="panel panel-primary text-center no-boder bg-color-green">
                                <div class="panel-footer back-footer-green">
                                    Path Statistics: Path Values
                                </div>
                                <div class="panel-body">
                                    <i class="fa fa-bar-chart-o fa-5x"></i>
                                    <h3>Number of paths: {{(getStatistics()).paths.length}}</h3>
                                    <h3>Total path cost: {{(getStatistics()).totalPathCost}}</h3>
                                    <h3>Most expensive path cost: {{(getStatistics()).mostExpensivePathCost}}</h3>
                                    <h3>Most expensive path length: {{(getStatistics()).mostExpensivePath.length}}</h3>
                                    <h3>Cheapest path cost: {{(getStatistics()).cheapestPathCost}}</h3>
                                    <h3>Cheapest path length: {{(getStatistics()).cheapestPath.length}}</h3>
                                    <h3>Average path cost: {{(getStatistics()).averagePathCost}}</h3>
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