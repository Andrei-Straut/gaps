
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
                        
                        <div info-card info-title="Path Statistics: Path Values" info-data="statisticsInfoCardValue"></div>
                    </div>
                </div>
            </div>
        </div>
        <hr />
    </div>
</div>