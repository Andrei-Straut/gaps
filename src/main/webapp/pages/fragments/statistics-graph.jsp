
<div class="row">
    <div id="graph-statistics" ng-controller="graphstatisticscontroller" ng-show="statisticsLoaded">
        <a name="graph-statistics"></a>
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <label class="checkbox-inline">
                        <input id="graph-statistics-viewer-toggle" type="checkbox" checked data-toggle="toggle" data-on="Visible" data-off="Hidden">
                    </label>
                    Graph Statistics
                </div>
                <div class="panel-body" ng-show="statisticsDisplayed">
                    <div class="row">                        
                        
                        <div class="col-md-9 col-sm-12 col-xs-12">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Graph Statistics: Direct Edges
                                </div>
                                <div class="panel-body">
                                    <div class="table-responsive">
                                        <table id="graph-statistics-direct-edges" class="table table-striped table-bordered table-hover">
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
                        
                        <div info-card info-title="Graph Statistics: Edge Values" info-data="statisticsInfoCardValue"></div>
                    </div>
                </div>
            </div>
        </div>
        <hr/>
    </div>
</div>